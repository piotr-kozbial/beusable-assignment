package org.example;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Amount of money in EUR currency, in canonical representation, i.e. a number of full euros plus
 * between 0 and 99 cents.
 */
public record EuroAmount(long euros, int cents) implements Comparable<EuroAmount> {
  public EuroAmount {
    if (euros < 0) throw new IllegalArgumentException();
    if (cents < 0) throw new IllegalArgumentException();
    if (cents >= 100) throw new IllegalArgumentException();
  }

  public static EuroAmount zero() {
    return new EuroAmount(0, 0);
  }

  public static EuroAmount ofDouble(double value) {
    var decimal = new BigDecimal(value);
    var euros = decimal.longValue();
    var cents =
        decimal.remainder(BigDecimal.ONE).round(MathContext.DECIMAL32).movePointRight(2).intValue();
    return new EuroAmount(euros, cents);
  }

  public EuroAmount plus(EuroAmount other) {
    long fullCents = this.fullAmountInCents() + other.fullAmountInCents();
    return new EuroAmount(fullCents / 100, (short) (fullCents % 100));
  }

  public long fullAmountInCents() {
    return 100 * this.euros + this.cents;
  }

  @Override
  public int compareTo(EuroAmount other) {
    long thisFullCents = 100 * this.euros + this.cents;
    long otherFullCents = 100 * other.euros + other.cents;
    if (thisFullCents < otherFullCents) {
      return -1;
    } else if (thisFullCents == otherFullCents) {
      return 0;
    } else {
      return 1;
    }
  }
}
