package org.example;

import java.util.stream.StreamSupport;
import java.util.stream.Collectors;
import java.util.Collections;

/**
 * Implements the optimization algorithm for the booking problem described
 * in the assignment (note: in real app it would be some reference to requirements).
 */
public class BookingOptimizer {
    /**
     * Amount of money in EUR currency, in canonical representation,
     * i.e. a number of full euros plus between 0 and 99 cents.
     */
    public static record EuroAmount(long euros, int cents) implements Comparable<EuroAmount> {
	public EuroAmount {
	    if (euros < 0) throw new IllegalArgumentException();
	    if (cents < 0) throw new IllegalArgumentException();
	    if (cents >= 100) throw new IllegalArgumentException();
	}
	public EuroAmount plus(EuroAmount other) {
	    long fullCents = this.fullAmountInCents()+ other.fullAmountInCents();
	    return new EuroAmount(fullCents / 100, (short)(fullCents % 100));
	}
	public long fullAmountInCents() {
	    return 100*this.euros + this.cents;
	}
	@Override
	public int compareTo(EuroAmount other) {
	    long thisFullCents = 100*this.euros+ this.cents;
	    long otherFullCents = 100*other.euros+100*other.cents;
	    if (thisFullCents < otherFullCents) {
		return -1;
	    } else if (thisFullCents == otherFullCents) {
		return 0;
	    } else {
		return 1;
	    }
	}
    };

    public static record OptimizationResult(OptimizationResultForPremium premium,
				     OptimizationResultForEconomy economy)
    {
	public OptimizationResult {
	    java.util.Objects.requireNonNull(premium);
	    java.util.Objects.requireNonNull(economy);
	}
    };
    public static record OptimizationResultForPremium(int bookedRooms, EuroAmount totalIncome) {
	public OptimizationResultForPremium {
	    java.util.Objects.requireNonNull(totalIncome);
	}
    };
    public static record OptimizationResultForEconomy(int bookedRooms, EuroAmount totalIncome) {
	public OptimizationResultForEconomy {
	    java.util.Objects.requireNonNull(totalIncome);
	}
    };

    public static OptimizationResult optimize(
        int freePremiumRooms, int freeEconomyRooms, Iterable<EuroAmount> clientOffers)
    {
	if (freePremiumRooms < 0) throw new IllegalArgumentException();
	if (freeEconomyRooms < 0) throw new IllegalArgumentException();

	var HighOfferLimit = new EuroAmount(100, 0);
	var separatedOffers = StreamSupport.stream(clientOffers.spliterator(), false)
	    .collect(Collectors.partitioningBy(offer -> offer.compareTo(HighOfferLimit) >= 0));
	var lowOffers = separatedOffers.get(false);
	Collections.sort(lowOffers, (a, b) -> b.compareTo(a));
	var highOffers = separatedOffers.get(true);
	Collections.sort(highOffers, (a, b) -> b.compareTo(a));

	var highOffersTaken = highOffers.subList(0, Math.min(highOffers.size(), freePremiumRooms));

	var remainingPremiumRooms = freePremiumRooms - highOffersTaken.size();
	var lowOfferCapacity = freeEconomyRooms + remainingPremiumRooms;
	var lowOffersTaken = lowOffers.subList(0, Math.min(lowOffers.size(), lowOfferCapacity));
	var upgradedOfferCount = Math.max(0, lowOffersTaken.size() - freeEconomyRooms);

	var truePremiumBookings = highOffersTaken;
	var upgradedEconomyBookings = lowOffersTaken.subList(0, upgradedOfferCount);
	var trueEconomyBookings = lowOffersTaken.subList(upgradedOfferCount, lowOffersTaken.size());

	return new OptimizationResult(
            new OptimizationResultForPremium(
                truePremiumBookings.size()+upgradedEconomyBookings.size(),
		truePremiumBookings.stream().reduce(new EuroAmount(0, 0),
						    EuroAmount::plus).plus(
		upgradedEconomyBookings.stream().reduce(new EuroAmount(0, 0),
							EuroAmount::plus))),
            new OptimizationResultForEconomy(
                trueEconomyBookings.size(),
		trueEconomyBookings.stream().reduce(new EuroAmount(0, 0),
						    EuroAmount::plus)));
    }
}
