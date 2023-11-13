package org.example;

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
	    long fullCents = this.cents + other.cents;
	    return new EuroAmount(fullCents / 100, (short)(fullCents % 100));
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
	var r = new OptimizationResult(
            new OptimizationResultForPremium(0, new EuroAmount(0, 0)),
            new OptimizationResultForEconomy(0, new EuroAmount(0, 0)));
	return r;
    }
}
