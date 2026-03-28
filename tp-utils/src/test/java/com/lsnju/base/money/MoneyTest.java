package com.lsnju.base.money;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link Money}.
 *
 * @author ls
 */
class MoneyTest {

    private static final Currency CNY = Currency.getInstance(Money.DEFAULT_CURRENCY_CODE);
    private static final Currency USD = Currency.getInstance("USD");

    @Test
    void defaultConstructor_zeroCny() {
        Money m = new Money();
        assertEquals(0L, m.getCent());
        assertEquals(CNY, m.getCurrency());
        assertEquals(BigDecimal.ZERO.setScale(2), m.getAmount());
    }

    @Test
    void stringAmount_tenFive_equals_tenFiftyScale() {
        Money a = new Money("10.5");
        Money b = new Money("10.50");
        assertEquals(a, b);
        assertEquals(1050L, a.getCent());
    }

    @Test
    void bigDecimalConstructor_usesRoundingMode() {
        Money half = new Money(new BigDecimal("0.005"), CNY, RoundingMode.HALF_UP);
        assertEquals(1L, half.getCent());
        Money down = new Money(new BigDecimal("0.004"), CNY, RoundingMode.DOWN);
        assertEquals(0L, down.getCent());
    }

    @Test
    void yuanAndCent_cny_normalizesCentModHundred() {
        Money m = new Money(1L, 50);
        assertEquals(150L, m.getCent());
        Money m2 = new Money(1L, 150);
        assertEquals(150L, m2.getCent());
    }

    @Test
    void doubleConstructor_documentedExamples() {
        assertEquals(999L, new Money(9.995).getCent());
        assertEquals(1001L, new Money(10.005).getCent());
    }

    @Test
    void centFactor_cny_is100() {
        assertEquals(100, new Money("1").centFactor());
    }

    @Test
    void getSetCurrencyCode() {
        Money m = new Money("1");
        m.setCurrencyCode("USD");
        assertEquals("USD", m.getCurrencyCode());
        m.setCurrencyCode(null);
        assertEquals(Money.DEFAULT_CURRENCY_CODE, m.getCurrencyCode());
    }

    @Test
    void setAmount_updatesCent_halfEvenTwoDecimals() {
        Money m = new Money("0");
        m.setAmount(new BigDecimal("12.345"));
        assertEquals(1234L, m.getCent());
    }

    @Test
    void setAmount_null_noOp() {
        Money m = new Money("5");
        long before = m.getCent();
        m.setAmount(null);
        assertEquals(before, m.getCent());
    }

    @Test
    void setCent_fluent() {
        Money m = new Money("0").setCent(999L);
        assertEquals(999L, m.getCent());
    }

    @Test
    void equals_hashCode_compareTo_greaterThan() {
        Money x = new Money("10");
        Money y = new Money("20");
        Money x2 = new Money("10");
        assertEquals(x, x2);
        assertEquals(x.hashCode(), x2.hashCode());
        assertNotEquals(x, y);
        assertFalse(Objects.equals(x, null));
        assertFalse(x.equals("10"));
        assertTrue(x.compareTo(y) < 0);
        assertTrue(y.compareTo(x) > 0);
        assertEquals(0, x.compareTo(x2));
        assertTrue(y.greaterThan(x));
        assertFalse(x.greaterThan(y));
    }

    @Test
    void compareTo_differentCurrency_throws() {
        Money c = new Money("1", CNY);
        Money u = new Money("1", USD);
        assertThrows(IllegalArgumentException.class, () -> c.compareTo(u));
    }

    @Test
    void add_subtract_immutable() {
        Money a = new Money("10");
        Money b = new Money("3.5");
        Money sum = a.add(b);
        assertEquals(new Money("13.5"), sum);
        assertEquals(1000L, a.getCent());
        Money diff = a.subtract(b);
        assertEquals(new Money("6.5"), diff);
        assertEquals(1000L, a.getCent());
    }

    @Test
    void addTo_subtractFrom_mutate() {
        Money a = new Money("10");
        a.addTo(new Money("1"));
        assertEquals(new Money("11"), a);
        a.subtractFrom(new Money("4"));
        assertEquals(new Money("7"), a);
    }

    @Test
    void add_mismatchedCurrency_throws() {
        Money c = new Money("1", CNY);
        Money u = new Money("1", USD);
        assertThrows(IllegalArgumentException.class, () -> c.add(u));
    }

    @Test
    void multiply_long_and_double() {
        Money m = new Money("10");
        assertEquals(new Money("30"), m.multiply(3L));
        assertEquals(1000L, m.getCent());
        assertEquals(new Money("15"), m.multiply(1.5d));
    }

    @Test
    void multiplyBy_mutates() {
        Money m = new Money("4");
        m.multiplyBy(2L);
        assertEquals(new Money("8"), m);
        m.multiplyBy(0.5d);
        assertEquals(new Money("4"), m);
    }

    @Test
    void multiply_bigDecimal_rounding() {
        Money m = new Money("10");
        Money r = m.multiply(new BigDecimal("0.333"), RoundingMode.HALF_UP);
        assertEquals(333L, r.getCent());
    }

    @Test
    void divide_double_and_bigDecimal() {
        Money m = new Money("10");
        assertEquals(new Money("5"), m.divide(2d));
        assertEquals(new Money("3.33"), m.divide(new BigDecimal("3"), RoundingMode.HALF_UP));
    }

    @Test
    void divideBy_mutates() {
        Money m = new Money("1.00");
        m.divideBy(new BigDecimal("3"), RoundingMode.DOWN);
        assertEquals(33L, m.getCent());
    }

    @Test
    void allocate_equalParts() {
        Money m = new Money("1.00");
        Money[] parts = m.allocate(3);
        assertEquals(3, parts.length);
        long sum = 0;
        for (Money p : parts) {
            sum += p.getCent();
        }
        assertEquals(100L, sum);
        assertTrue(parts[0].getCent() >= parts[2].getCent());
    }

    @Test
    void allocate_ratios() {
        Money m = new Money("10.00");
        Money[] parts = m.allocate(new long[] {1, 1});
        assertEquals(2, parts.length);
        assertEquals(500L, parts[0].getCent());
        assertEquals(500L, parts[1].getCent());
    }

    @Test
    void toString_isAmountPlainString() {
        assertEquals("8.88", new Money("8.88").toString());
    }

    @Test
    void dump_containsCentAndCurrency() {
        String d = new Money("1.23").dump();
        assertTrue(d.contains("cent"));
        assertTrue(d.contains("currency"));
        assertTrue(d.contains("123"));
    }

    @Test
    void usd_centFactor_andAmount() {
        Money usd = new Money("12.34", USD);
        assertEquals(100, usd.centFactor());
        assertEquals(new BigDecimal("12.34").setScale(2), usd.getAmount());
        assertEquals(1234L, usd.getCent());
    }
}
