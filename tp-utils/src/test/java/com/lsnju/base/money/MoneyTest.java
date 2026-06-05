package com.lsnju.base.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Money}.
 */
class MoneyTest {

    @Test
    void constructor_andGetters_defaultCurrencyAndAmount() {
        Money money = new Money("12.34");
        Assertions.assertEquals("CNY", money.getCurrencyCode());
        Assertions.assertEquals(1234L, money.getCent());
        Assertions.assertEquals(new BigDecimal("12.34"), money.getAmount());
    }

    @Test
    void constructor_withRoundingMode_appliesRounding() {
        Money down = new Money(new BigDecimal("10.005"), Currency.getInstance("CNY"), RoundingMode.DOWN);
        Money up = new Money(new BigDecimal("10.005"), Currency.getInstance("CNY"), RoundingMode.UP);

        Assertions.assertEquals(1000L, down.getCent());
        Assertions.assertEquals(1001L, up.getCent());
    }

    @Test
    void setAmount_andSetCent_shouldUpdateValue() {
        Money money = new Money("1.11");
        money.setAmount(new BigDecimal("2.555"));
        Assertions.assertEquals(256L, money.getCent());
        Assertions.assertEquals(new BigDecimal("2.56"), money.getAmount());

        Money returned = money.setCent(999L);
        Assertions.assertSame(money, returned);
        Assertions.assertEquals(new BigDecimal("9.99"), money.getAmount());
    }

    @Test
    void setCurrencyCode_nullShouldFallbackToDefault() {
        Money money = new Money("1.00");
        money.setCurrencyCode(null);
        Assertions.assertEquals("CNY", money.getCurrencyCode());
    }

    @Test
    void compare_equals_hashCode_andGreaterThan() {
        Money m1 = new Money("8.88");
        Money m2 = new Money("8.88");
        Money m3 = new Money("9.00");

        Assertions.assertEquals(m1, m2);
        Assertions.assertEquals(0, m1.compareTo(m2));
        Assertions.assertTrue(m3.greaterThan(m1));
        Assertions.assertNotEquals(m1, m3);
        Assertions.assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    void addSubtract_multiplyDivide_nonMutatingAndMutating() {
        Money base = new Money("10.00");
        Money delta = new Money("2.50");

        Money added = base.add(delta);
        Assertions.assertEquals(1250L, added.getCent());
        Assertions.assertEquals(1000L, base.getCent());

        base.addTo(delta);
        Assertions.assertEquals(1250L, base.getCent());

        Money subtracted = base.subtract(new Money("0.50"));
        Assertions.assertEquals(1200L, subtracted.getCent());
        base.subtractFrom(new Money("0.50"));
        Assertions.assertEquals(1200L, base.getCent());

        Assertions.assertEquals(2400L, base.multiply(2).getCent());
        Assertions.assertEquals(600L, base.divide(2).getCent());

        base.multiplyBy(1.5d);
        Assertions.assertEquals(1800L, base.getCent());
        base.divideBy(3d);
        Assertions.assertEquals(600L, base.getCent());
    }

    @Test
    void bigDecimalMultiplyAndDivide_shouldRespectRoundingMode() {
        Money money = new Money("1.00");
        Assertions.assertEquals(33L, money.divide(new BigDecimal("3"), RoundingMode.DOWN).getCent());
        Assertions.assertEquals(34L, money.divide(new BigDecimal("3"), RoundingMode.UP).getCent());

        Money multiplied = money.multiply(new BigDecimal("1.255"), RoundingMode.HALF_UP);
        Assertions.assertEquals(126L, multiplied.getCent());
    }

    @Test
    void allocate_byTargets_shouldKeepTotalAndBalance() {
        Money money = new Money("1.00");
        Money[] parts = money.allocate(3);

        Assertions.assertEquals(3, parts.length);
        long sum = parts[0].getCent() + parts[1].getCent() + parts[2].getCent();
        Assertions.assertEquals(100L, sum);
        Assertions.assertTrue(parts[0].getCent() - parts[2].getCent() <= 1);
    }

    @Test
    void allocate_byRatios_shouldKeepTotal() {
        Money money = new Money("1.00");
        Money[] parts = money.allocate(new long[]{3, 7});

        Assertions.assertEquals(2, parts.length);
        Assertions.assertEquals(30L, parts[0].getCent());
        Assertions.assertEquals(70L, parts[1].getCent());
        Assertions.assertEquals(100L, parts[0].getCent() + parts[1].getCent());
    }

    @Test
    void currencyMismatch_shouldThrowOnMathAndCompare() {
        Money cny = new Money("1.00", Currency.getInstance("CNY"));
        Money usd = new Money("1.00", Currency.getInstance("USD"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> cny.add(usd));
        Assertions.assertThrows(IllegalArgumentException.class, () -> cny.compareTo(usd));
    }

    @Test
    void toString_dump_andCentFactor() {
        Money cny = new Money("8.88", Currency.getInstance("CNY"));
        Assertions.assertEquals("8.88", cny.toString());
        Assertions.assertEquals(100, cny.centFactor());
        Assertions.assertTrue(cny.dump().contains("cent = 888"));

        Money jpy = new Money(new BigDecimal("10"), Currency.getInstance("JPY"));
        Assertions.assertEquals(1, jpy.centFactor());
        Assertions.assertEquals(10L, jpy.getCent());
    }
}
