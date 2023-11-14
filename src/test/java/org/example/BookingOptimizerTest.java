package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BookingOptimizerTest {
  @Test
  void noOffers() {
    List<BookingOptimizer.EuroAmount> clientOffers = Arrays.asList();
    var actualResult = BookingOptimizer.optimize(5, 3, clientOffers);
    var expectedResult =
        new BookingOptimizer.OptimizationResult(
            new BookingOptimizer.OptimizationResultForPremium(
                0, new BookingOptimizer.EuroAmount(0, 0)),
            new BookingOptimizer.OptimizationResultForEconomy(
                0, new BookingOptimizer.EuroAmount(0, 0)));

    assertEquals(expectedResult, actualResult);
  }

  @ParameterizedTest
  @MethodSource("clientTestProvider")
  void clientTest(
      List<BookingOptimizer.EuroAmount> clientOffers,
      int freePremiumRooms,
      int freeEconomyRooms,
      int expectedPremiumRoomsBooked,
      BookingOptimizer.EuroAmount expectedPremiumRoomsIncome,
      int expectedEconomyRoomsBooked,
      BookingOptimizer.EuroAmount expectedEconomyRoomsIncome) {
    var actualResult = BookingOptimizer.optimize(freePremiumRooms, freeEconomyRooms, clientOffers);
    var expectedResult =
        new BookingOptimizer.OptimizationResult(
            new BookingOptimizer.OptimizationResultForPremium(
                expectedPremiumRoomsBooked, expectedPremiumRoomsIncome),
            new BookingOptimizer.OptimizationResultForEconomy(
                expectedEconomyRoomsBooked, expectedEconomyRoomsIncome));
    assertEquals(expectedResult, actualResult);
  }

  static List<BookingOptimizer.EuroAmount> clientTestOffers =
      Arrays.asList(
          new BookingOptimizer.EuroAmount(23, 0),
          new BookingOptimizer.EuroAmount(45, 0),
          new BookingOptimizer.EuroAmount(155, 0),
          new BookingOptimizer.EuroAmount(374, 0),
          new BookingOptimizer.EuroAmount(22, 0),
          new BookingOptimizer.EuroAmount(99, 99),
          new BookingOptimizer.EuroAmount(100, 0),
          new BookingOptimizer.EuroAmount(101, 0),
          new BookingOptimizer.EuroAmount(115, 0),
          new BookingOptimizer.EuroAmount(209, 0));

  static Stream<Arguments> clientTestProvider() {
    return Stream.of(
        Arguments.of(
            clientTestOffers,
            3,
            3,
            3,
            new BookingOptimizer.EuroAmount(738, 0),
            3,
            new BookingOptimizer.EuroAmount(167, 99)),
        Arguments.of(
            clientTestOffers,
            7,
            5,
            6,
            new BookingOptimizer.EuroAmount(1054, 0),
            4,
            new BookingOptimizer.EuroAmount(189, 99)),
        Arguments.of(
            clientTestOffers,
            2,
            7,
            2,
            new BookingOptimizer.EuroAmount(583, 0),
            4,
            new BookingOptimizer.EuroAmount(189, 99)),
        Arguments.of(
            clientTestOffers,
            7,
            1,
            7,
            new BookingOptimizer.EuroAmount(1153, 99),
            1,
            new BookingOptimizer.EuroAmount(45, 0)));
  }
}
