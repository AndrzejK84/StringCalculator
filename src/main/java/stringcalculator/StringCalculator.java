package stringcalculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * String calculator
 */
public class StringCalculator {

    private static final String[] DEFAULT_DELIMITERS = {",", "\n"};
    private static final String DELIMITER_PART_PATTERN = "^//\\[(\\D+)\\]\n.*";
    private static final String DELIMITER_PART_SPLITTER = "\\]\\[";
    private static final String NUMBERS_PART_PATTERN = "^//.*\n(.+)";
    private static final int MAX_NUMBER = 1000;


    /**
     * Adds numbers. Numbers can be separated by ',' or '\n' by default or by a defined set of delimiters.
     * Numbers greater than {@value #MAX_NUMBER} are ignored. In case of negative numbers an exception will be thrown.
     * Custom delimiters can be defined as follows:
     * The beginning of the string can contain a separate line starting with <code>//</code> which contains the list of delimiters.
     * Delimiters can be of any length and are defined within square brackets <code>[]</code>, next line ('\n') ends delimiters definition
     * and starts numbers definition
     * example: <code>//[*][%]\n1*2%3</code>
     * The following characters are not allowed as part of a delimiter: <code>[ ] - \n</code>
     *
     * @param numbers input string
     * @return the sum of all given numbers which are lower than {@value #MAX_NUMBER}
     */

    public static int add(String numbers) {
        List<Integer> parsedNumbers = filterOutNumberGreaterThenMax(parseInput(numbers));
        validateNegativeNumbers(parsedNumbers);
        int sum = parsedNumbers.stream().reduce(0, (x, y) -> {
            if (x.longValue() + y.longValue() > Integer.MAX_VALUE) {
                throw new RuntimeException("The sum is greater than allowed. Max sum is: " + Integer.MAX_VALUE);
            }
            return x + y;
        });
        return sum;
    }

    private static List<Integer> parseInput(String input) {
        if (input.startsWith("//")) {
            String numbers = getNumbersString(input);
            return parseStringsToNumbers(numbers.split(generateSplitString(getDelimiters(input))));
        } else {
            return parseStringsToNumbers(input.split(generateSplitString(DEFAULT_DELIMITERS)));
        }
    }

    private static String[] getDelimiters(String input) {
        Pattern delimiterPattern = Pattern.compile(DELIMITER_PART_PATTERN);
        Matcher matcher = delimiterPattern.matcher(input);
        List<String> delimiters = new ArrayList<String>();
        if (matcher.find()) {
            return (matcher.group(1).split(DELIMITER_PART_SPLITTER));
        } else {
            throw new RuntimeException("Delimiters are not correctly defined.");
        }
    }

    private static String getNumbersString(String input) {
        Pattern stringPattern = Pattern.compile(NUMBERS_PART_PATTERN);
        Matcher matcher = stringPattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new RuntimeException("Numbers are not correctly defined.");
        }
    }

    private static String generateSplitString(String[] splitStrings) {
        return Arrays.asList(splitStrings).stream().map(StringCalculator::escapeSpecialCharacters)
                .collect(Collectors.joining("|"));
    }

    private static String escapeSpecialCharacters(String str) {
        return str.replaceAll("[\\W]", "\\\\$0");
    }

    private static List<Integer> parseStringsToNumbers(String[] numbersString) {
        List<Integer> parsedNumbers = new ArrayList<Integer>();
        if (numbersString.length == 1 && numbersString[0].equals("")) {
            parsedNumbers.add(0);
        } else {
            for (String numberString : numbersString) {
                try {
                    parsedNumbers.add(Integer.parseInt(numberString));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Wrong number format: " + numberString);
                }
            }
        }
        return parsedNumbers;
    }

    private static List<Integer> filterOutNumberGreaterThenMax(List<Integer> numbers) {
        return numbers.stream().filter(integer -> integer <= MAX_NUMBER)
                .collect(Collectors.toList());
    }

    private static void validateNegativeNumbers(List<Integer> numbers) {
        List<Integer> negativeNumbers = numbers.stream().filter(number -> number < 0).collect(Collectors.toList());
        if (!negativeNumbers.isEmpty()) {
            throw new RuntimeException("negatives not allowed: " + negativeNumbers);
        }
    }
}
