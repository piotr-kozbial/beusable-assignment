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

	assertEquals(actualResult, expectedResult);
    }
}
