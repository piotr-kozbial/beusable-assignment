package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class BookingOptimizerTest {
    @Test
    void noOffers() {
	List<BookingOptimizer.EuroAmount> clientOffers = Arrays.asList();
	var actualResult = BookingOptimizer.optimize(5, 3, clientOffers);
	var expectedResult = new BookingOptimizer.OptimizationResult(
            new BookingOptimizer.OptimizationResultForPremium(
                0, new BookingOptimizer.EuroAmount(0, 0)),
            new BookingOptimizer.OptimizationResultForEconomy(
		0, new BookingOptimizer.EuroAmount(0, 0)));

	assertEquals(expectedResult, actualResult);
    }

    @Test
    void clientTest1() {
	var clientOffers = Arrays.asList(
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
	var actualResult = BookingOptimizer.optimize(3, 3, clientOffers);
	var expectedResult = new BookingOptimizer.OptimizationResult(
            new BookingOptimizer.OptimizationResultForPremium(
                3, new BookingOptimizer.EuroAmount(738, 0)),
            new BookingOptimizer.OptimizationResultForEconomy(
		3, new BookingOptimizer.EuroAmount(167, 99)));
	assertEquals(expectedResult, actualResult);
    }
}
