package org.example;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Implements the optimization algorithm for the booking problem described in the assignment (note:
 * in real app it would be some reference to requirements).
 */
public class BookingOptimizer {
  public static record OptimizationResult(
      OptimizationResultForPremium premium, OptimizationResultForEconomy economy) {
    public OptimizationResult {
      java.util.Objects.requireNonNull(premium);
      java.util.Objects.requireNonNull(economy);
    }
  }
  ;

  public static record OptimizationResultForPremium(int bookedRooms, EuroAmount totalIncome) {
    public OptimizationResultForPremium {
      java.util.Objects.requireNonNull(totalIncome);
    }
  }
  ;

  public static record OptimizationResultForEconomy(int bookedRooms, EuroAmount totalIncome) {
    public OptimizationResultForEconomy {
      java.util.Objects.requireNonNull(totalIncome);
    }
  }
  ;

  static class ProblemInstance {
    public ProblemInstance(
        int freePremiumRooms, int freeEconomyRooms, List<EuroAmount> clientOffers) {
      this.freePremiumRooms = freePremiumRooms;
      this.freeEconomyRooms = freeEconomyRooms;
      this.clientOffers = clientOffers;
    }

    public int freePremiumRooms;
    public int freeEconomyRooms;
    public List<EuroAmount> clientOffers;

    public int totalPremiumRoomsBooked() {
      return truePremiumBookings().size() + upgradedEconomyBookings().size();
    }

    public EuroAmount totalPremiumRoomsIncome() {
      var truePremiumTotal = calculateTotal(truePremiumBookings());
      var upgradedEconomyTotal = calculateTotal(upgradedEconomyBookings());
      return truePremiumTotal.plus(upgradedEconomyTotal);
    }

    public int totalEconomyRoomsBooked() {
      return trueEconomyBookings().size();
    }

    public EuroAmount totalEconomyRoomsIncome() {
      return calculateTotal(trueEconomyBookings());
    }

    private static EuroAmount calculateTotal(List<EuroAmount> amounts) {
      return amounts.stream().reduce(EuroAmount.zero(), EuroAmount::plus);
    }

    public int upgradeCapacity() {
      return freePremiumRooms - truePremiumBookings().size();
    }

    public int lowOfferCapacity() {
      return freeEconomyRooms + upgradeCapacity();
    }

    public int acceptedLowOffersCount() {
      return Math.min(lowOffers().size(), lowOfferCapacity());
    }

    public int upgradedLowOffersCount() {
      return Math.max(0, acceptedLowOffersCount() - freeEconomyRooms);
    }

    public List<EuroAmount> truePremiumBookings() {
      if (truePremiumBookings.isEmpty()) {
        truePremiumBookings =
            Optional.of(highOffers().subList(0, Math.min(highOffers().size(), freePremiumRooms)));
      }
      return truePremiumBookings.get();
    }

    public List<EuroAmount> upgradedEconomyBookings() {
      if (upgradedEconomyBookings.isEmpty()) {
        upgradedEconomyBookings = Optional.of(lowOffers().subList(0, upgradedLowOffersCount()));
      }
      return upgradedEconomyBookings.get();
    }

    private Optional<List<EuroAmount>> upgradedEconomyBookings = Optional.empty();

    private Optional<List<EuroAmount>> truePremiumBookings = Optional.empty();

    public List<EuroAmount> trueEconomyBookings() {
      if (trueEconomyBookings.isEmpty()) {
        trueEconomyBookings =
            Optional.of(lowOffers().subList(upgradedLowOffersCount(), acceptedLowOffersCount()));
      }
      return trueEconomyBookings.get();
    }

    private Optional<List<EuroAmount>> trueEconomyBookings = Optional.empty();

    public List<EuroAmount> highOffers() {
      if (highOffers.isEmpty()) {
        separateAndSortOffers();
      }
      return highOffers.get();
    }

    public List<EuroAmount> lowOffers() {
      if (lowOffers.isEmpty()) {
        separateAndSortOffers();
      }
      return lowOffers.get();
    }

    private void separateAndSortOffers() {
      var separatedOffers =
          StreamSupport.stream(clientOffers.spliterator(), false)
              .collect(Collectors.partitioningBy(offer -> offer.compareTo(HIGH_OFFER_LIMIT) >= 0));

      lowOffers = Optional.of(separatedOffers.get(false));
      Collections.sort(lowOffers.get(), (a, b) -> b.compareTo(a));

      highOffers = Optional.of(separatedOffers.get(true));
      Collections.sort(highOffers.get(), (a, b) -> b.compareTo(a));
    }

    private Optional<List<EuroAmount>> lowOffers = Optional.empty();
    private Optional<List<EuroAmount>> highOffers = Optional.empty();
  }
  ;

  public static OptimizationResult optimize(
      int freePremiumRooms, int freeEconomyRooms, List<EuroAmount> clientOffers) {
    if (freePremiumRooms < 0) throw new IllegalArgumentException();
    if (freeEconomyRooms < 0) throw new IllegalArgumentException();

    var instance = new ProblemInstance(freePremiumRooms, freeEconomyRooms, clientOffers);

    return new OptimizationResult(
        new OptimizationResultForPremium(
            instance.totalPremiumRoomsBooked(), instance.totalPremiumRoomsIncome()),
        new OptimizationResultForEconomy(
            instance.totalEconomyRoomsBooked(), instance.totalEconomyRoomsIncome()));
  }

  static EuroAmount HIGH_OFFER_LIMIT = new EuroAmount(100, 0);
}
