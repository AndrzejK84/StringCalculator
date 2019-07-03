package stringcalculator;

import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.*;

public class StringCalculatorTest {

    @Test
    public void shouldReturn0WhenEmptyString() {
        // given

        // when
        int result = StringCalculator.add("");

        // then
        assertEquals(result, 0);
    }

    @Test
    public void shouldAddTwoNumbersSeperatedByComma() {
        // given

        // when
        int result = StringCalculator.add("1,2");

        // then
        assertEquals(result, 3);
    }

    @Test
    public void shouldAddTwoNumbersSeperatedByNewLine() {
        // given

        // when
        int result = StringCalculator.add("1\n2");

        // then
        assertEquals(result, 3);
    }

    @Test
    public void shouldAddMultipleNumbersSeperatedByNewLineandComma() {
        // given

        // when
        int result = StringCalculator.add("1\n2,3,4\n5");

        // then
        assertEquals(result, 15);
    }

    @Test
    public void shouldNotSkipNumbersThatAreEqualOrLowerThenLimit() {
        // given
        int limit = 1000;
        // when
        int result = StringCalculator.add("1\n2,3,4\n" + limit);

        // then
        assertEquals(result, 1010);
    }

    @Test
    public void shouldSkipNumbersThatAreGreaterThenLimit() {
        // given
        int limit = 1000;
        // when
        int result = StringCalculator.add("1\n2,3,4\n" + (limit + 1));

        // then
        assertEquals(result, 10);
    }

    @Test
    public void shouldSupportCustomDelimiters() {
        // given

        // when
        int delimiter1 = StringCalculator.add("//[**][;;;;;][%%%]\n4;;;;;8%%%333**5");
        int delimiter2 = StringCalculator.add("//[']\n4'8'333'5");
        int delimiter3 = StringCalculator.add("//[}][)][?][*][+][.][>][\\d][\t]\n1}2)3?4*5+6.7>8\\d9\t10");

        // then
        assertEquals(delimiter1, 350);
        assertEquals(delimiter2, 350);
        assertEquals(delimiter3, 55);
    }

    @Test
    public void shouldThrowExpectionRegardingNegativeNumber() {
        // given
        // when

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            StringCalculator.add("1\n2,-3,4\n-4");
        })
                .withMessage("negatives not allowed: [-3, -4]")
                .withNoCause();
    }

    @Test
    public void shouldThrowExpectionRegardingInvalidNumberFormat() {
        // given
        // when

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            StringCalculator.add("1\n2,3,4\n4l");
        })
                .withMessage("Wrong number format: 4l")
                .withNoCause();
    }

    @Test
    public void shouldThrowExpectionRegardingInvalidNumberFormatWhenWrongDelimiterUsed() {
        // given
        // when

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            StringCalculator.add("//[']\n4'8:333'5");
        })
                .withMessage("Wrong number format: 8:333")
                .withNoCause();
    }

    @Test
    public void shouldThrowExpectionRegardingDelimitersWhenNumberUsedInDelimiter() {
        // given
        // when

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            StringCalculator.add("//[4]\n4'8:333'5");
        })
                .withMessage("Delimiters are not correctly defined.")
                .withNoCause();
    }

    @Test
    public void shouldThrowExpectionRegardingDelimitersWhenEmptyDelimiter() {
        // given
        // when

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            StringCalculator.add("//[]\n4'8:333'5");
        })
                .withMessage("Delimiters are not correctly defined.")
                .withNoCause();
    }

    @Test
    public void shouldThrowExpectionRegardingDelimitersWhenWrongDelimiter() {
        // given
        // when

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            StringCalculator.add("//\n4[;8[;333;5");
        })
                .withMessage("Delimiters are not correctly defined.")
                .withNoCause();
    }

    @Test
    public void shouldThrowExpectionRegardingNumbersWhenNoNextLineUsedToDefineNumbers() {
        // given
        // when

        // then
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> {
            StringCalculator.add("//4[;8[;333;5");
        })
                .withMessage("Numbers are not correctly defined.")
                .withNoCause();
    }
}
