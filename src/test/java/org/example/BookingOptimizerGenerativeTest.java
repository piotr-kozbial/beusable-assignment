package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BookingOptimizerGenerativeTest {
  private static record TestArguments(
      int freePremiumRooms, int freeEconomyRooms, List<BookingOptimizer.EuroAmount> clientOffers) {}
  ;

  @ParameterizedTest
  @MethodSource("testProvider")
  void test(TestArguments arguments) {
    var instance =
        new BookingOptimizer.ProblemInstance(
            arguments.freePremiumRooms(), arguments.freeEconomyRooms(), arguments.clientOffers());
    checkPremiumCapacityNotExceeded(instance);
    checkEconomyCapacityNotExceeded(instance);
    checkAllEconomyBookingsAreBelowThreshold(instance);
    checkNoUpgradesIfEconomyBelowCapacity(instance);
    checkNoMoreBookedThanOffered(instance);
  }

  private void checkPremiumCapacityNotExceeded(BookingOptimizer.ProblemInstance instance) {
    assertTrue(instance.totalPremiumRoomsBooked() <= instance.freePremiumRooms);
  }

  private void checkEconomyCapacityNotExceeded(BookingOptimizer.ProblemInstance instance) {
    assertTrue(instance.totalEconomyRoomsBooked() <= instance.freeEconomyRooms);
  }

  private void checkNoMoreBookedThanOffered(BookingOptimizer.ProblemInstance instance) {
    int totalRoomsBooked = instance.totalPremiumRoomsBooked() + instance.totalEconomyRoomsBooked();
    assertTrue(totalRoomsBooked <= instance.clientOffers.size());
  }

  private void checkAllEconomyBookingsAreBelowThreshold(BookingOptimizer.ProblemInstance instance) {
    instance
        .trueEconomyBookings()
        .forEach(booking -> assertTrue(booking.compareTo(BookingOptimizer.HIGH_OFFER_LIMIT) < 0));
  }

  private void checkNoUpgradesIfEconomyBelowCapacity(BookingOptimizer.ProblemInstance instance) {
    if (instance.acceptedLowOffersCount() < instance.freeEconomyRooms) {
      assertEquals(instance.upgradedLowOffersCount(), 0);
    }
  }

  private static long RANDOM_SEED = 1416847872156649280l;
  private static int RANDOM_TEST_COUNT = 100;
  private static int MAX_FREE_PREMIUM_ROOMS = 30;
  private static int MAX_FREE_ECONOMY_ROOMS = 30;
  private static int MAX_OFFER_LIST_LENGTH = 100;
  private static int MAX_OFFER_IN_CENTS = 1000 * 100;

  private static Stream<Arguments> testProvider() {
    var random = new Random(RANDOM_SEED);
    var tests = new ArrayList<TestArguments>(RANDOM_TEST_COUNT);
    for (int i = 0; i < RANDOM_TEST_COUNT; i++) {
      int freePremiumRooms = random.nextInt(MAX_FREE_PREMIUM_ROOMS);
      int freeEconomyRooms = random.nextInt(MAX_FREE_ECONOMY_ROOMS);
      var clientOffers = randomClientOffers(random);
      tests.add(new TestArguments(freePremiumRooms, freeEconomyRooms, clientOffers));
    }
    return tests.stream().map(Arguments::of);
  }

  private static List<BookingOptimizer.EuroAmount> randomClientOffers(Random random) {
    int offerCount = random.nextInt(MAX_OFFER_LIST_LENGTH);
    var offers = new ArrayList<BookingOptimizer.EuroAmount>(offerCount);
    for (int i = 0; i < offerCount; i++) {
      offers.add(randomOffer(random));
    }
    return offers;
  }

  private static BookingOptimizer.EuroAmount randomOffer(Random random) {
    int cents = random.nextInt(MAX_OFFER_IN_CENTS);
    return new BookingOptimizer.EuroAmount(cents / 100, cents % 100);
  }
}
