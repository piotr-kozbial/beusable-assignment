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
    List<EuroAmount> clientOffers = Arrays.asList();
    var actualResult = BookingOptimizer.optimize(5, 3, clientOffers);
    var expectedResult =
        new BookingOptimizer.OptimizationResult(
            new BookingOptimizer.OptimizationResultForPremium(0, new EuroAmount(0, 0)),
            new BookingOptimizer.OptimizationResultForEconomy(0, new EuroAmount(0, 0)));

    assertEquals(expectedResult, actualResult);
  }

  @ParameterizedTest
  @MethodSource("clientTestProvider")
  void clientTest(
      List<EuroAmount> clientOffers,
      int freePremiumRooms,
      int freeEconomyRooms,
      int expectedPremiumRoomsBooked,
      EuroAmount expectedPremiumRoomsIncome,
      int expectedEconomyRoomsBooked,
      EuroAmount expectedEconomyRoomsIncome) {
    var actualResult = BookingOptimizer.optimize(freePremiumRooms, freeEconomyRooms, clientOffers);
    var expectedResult =
        new BookingOptimizer.OptimizationResult(
            new BookingOptimizer.OptimizationResultForPremium(
                expectedPremiumRoomsBooked, expectedPremiumRoomsIncome),
            new BookingOptimizer.OptimizationResultForEconomy(
                expectedEconomyRoomsBooked, expectedEconomyRoomsIncome));
    assertEquals(expectedResult, actualResult);
  }

  static List<EuroAmount> clientTestOffers =
      Arrays.asList(
          new EuroAmount(23, 0),
          new EuroAmount(45, 0),
          new EuroAmount(155, 0),
          new EuroAmount(374, 0),
          new EuroAmount(22, 0),
          new EuroAmount(99, 99),
          new EuroAmount(100, 0),
          new EuroAmount(101, 0),
          new EuroAmount(115, 0),
          new EuroAmount(209, 0));

  static Stream<Arguments> clientTestProvider() {
    return Stream.of(
        Arguments.of(clientTestOffers, 3, 3, 3, new EuroAmount(738, 0), 3, new EuroAmount(167, 99)),
        Arguments.of(
            clientTestOffers, 7, 5, 6, new EuroAmount(1054, 0), 4, new EuroAmount(189, 99)),
        Arguments.of(clientTestOffers, 2, 7, 2, new EuroAmount(583, 0), 4, new EuroAmount(189, 99)),
        Arguments.of(
            clientTestOffers, 7, 1, 7, new EuroAmount(1153, 99), 1, new EuroAmount(45, 0)));
  }
}
