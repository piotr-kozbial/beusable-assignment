package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BookingOptimizerGenerativeTest {
    @ParameterizedTest
    @MethodSource("testProvider")
    void test(
		     List<BookingOptimizer.EuroAmount> clientOffers,
		     int freePremiumRooms,
		     int freeEconomyRooms,
		     int expectedPremiumRoomsBooked,
		     BookingOptimizer.EuroAmount expectedPremiumRoomsIncome,
		     int expectedEconomyRoomsBooked,
		     BookingOptimizer.EuroAmount expectedEconomyRoomsIncome)
    {
	var instance = new BookingOptimizer.ProblemInstance(freePremiumRooms, freeEconomyRooms, clientOffers);
	checkPremiumCapacityNotExceeded(instance);
	checkEconomyCapacityNotExceeded(instance);
	checkAllEconomyBookingsAreBelowThreshold(instance);
	checkAllPremiumBookingsAreAtLeastThresholdIfEconomyBelowCapacity(instance);
	checkPremiumBookingsAreTheTopOnes(instance);
	checkPremiumBookingsBelowThresholdAreTheTopOnes(instance);
	checkEconomyBookingsAreTopOnesBelowThresholdAfterPremium(instance);
    }

    private void checkPremiumCapacityNotExceeded(BookingOptimizer.ProblemInstance instance) {
	assertTrue(instance.totalPremiumRoomsBooked() <= instance.freePremiumRooms);
    }
    private void checkEconomyCapacityNotExceeded(BookingOptimizer.ProblemInstance instance) {
		assertTrue(instance.totalEconomyRoomsBooked() <= instance.freeEconomyRooms);
    }
    private void checkAllEconomyBookingsAreBelowThreshold(BookingOptimizer.ProblemInstance instance) {
	instance.trueEconomyBookings().forEach(booking ->
            assertTrue(booking.compareTo(BookingOptimizer.HIGH_OFFER_LIMIT) < 0));
    }
    private void checkAllPremiumBookingsAreAtLeastThresholdIfEconomyBelowCapacity(
        BookingOptimizer.ProblemInstance instance)
    {
    }
    private void checkPremiumBookingsAreTheTopOnes(BookingOptimizer.ProblemInstance instance)
    {
    }
    private void checkPremiumBookingsBelowThresholdAreTheTopOnes(BookingOptimizer.ProblemInstance instance)
    {
		// - premium bookings < 100 are the top of what we've seen so far
		// in < 100 range
    }
    private void checkEconomyBookingsAreTopOnesBelowThresholdAfterPremium(
	    BookingOptimizer.ProblemInstance instance)
    {
		// - economy bookings are the top of what we've seen so far
		// in < 100 range, except for the premium bookings
    }								  


    // TODO: random generation (with fixed seeds)
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
	
    static Stream<Arguments> testProvider() {
	return Stream.of(
            Arguments.of(
                clientTestOffers,
                3, 3,
                3, new BookingOptimizer.EuroAmount(738, 0),
                3, new BookingOptimizer.EuroAmount(167, 99)),
            Arguments.of(
                clientTestOffers,
                7, 5,
                6, new BookingOptimizer.EuroAmount(1054, 0),
                4, new BookingOptimizer.EuroAmount(189, 99)),
            Arguments.of(
                clientTestOffers,
                2, 7,
                2, new BookingOptimizer.EuroAmount(583, 0),
                4, new BookingOptimizer.EuroAmount(189, 99)),
            Arguments.of(
                clientTestOffers,
                7, 1,
                7, new BookingOptimizer.EuroAmount(1153, 99),
                1, new BookingOptimizer.EuroAmount(45, 0)));
    }
}
