package converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Converter {
    int source ;
    int target;
    String integer;
    String fraction = "";

    public Converter(int source, int target) {
        this.source = source;
        this.target = target;
    }

    String convert() {
        String decimal;
        if (fraction.isBlank()) {
            decimal = toDecimal(integer, source);
        } else  {
            decimal = toDecimal(integer, fraction, source);
        }

        return toTarget(decimal, target);
    }


    // For whole number conversion
    private String toDecimal(String number, int base) {
        BigDecimal result = BigDecimal.ZERO;

        // i -> loops from last to first index
        // e.g. 1630, loop start from "0"

        // p -> regular increment loop for power of
        // e.g. Octal conversion: (i ∗ 8 pow p) + (i ∗ 8 pow p)
        for (int p = 0, i = number.length() - 1; p < number.length(); p++, i--) {

            // e.g. 1726 = 1∗512 + 7∗64 + 2∗8 + 6∗1 = 982 (octal to decimal)
            result = result.add(parseNumber(String.valueOf(number.charAt(i))).multiply(new BigDecimal(String.valueOf(Math.round(Math.pow(base, p))))));
        }

        return result.toString();
    }

    // For fractional number conversion
    private String toDecimal(String number, String fraction, int base) {
        BigDecimal result = BigDecimal.ZERO;

        // i -> loops from last to first index
        // e.g. 1630, loop start from "0"
        // p -> regular increment loop for power of
        // e.g. Octal conversion: (i ∗ 8 pow p) + (i ∗ 8 pow p)
        for (int p = 0, i = number.length() - 1; p < number.length(); p++, i--) {

            // e.g. 1726 = 1∗512 + 7∗64 + 2∗8 + 6∗1 = 982 (octal to decimal)
            result = result.add(parseNumber(String.valueOf(number.charAt(i))).multiply(new BigDecimal(String.valueOf(Math.round(Math.pow(base, p))))));
        }

        for (int p = -1, i = 0; i < fraction.length(); p--, i++) {

            // e.g. Binary to decimal
            // e.g. (0.011)₂ = (0 × 2⁻¹) + (1 × 2⁻²) + (1 × 2⁻³) = (0.375)₁₀
            result = result.add(parseNumber(String.valueOf(fraction.charAt(i))).multiply(new BigDecimal(String.valueOf(Math.pow(base, p)))));
        }

        return result.toString();
    }

    static String toTarget(String decimal, int base) {
        BigDecimal integer = new BigDecimal(decimal.split("\\.")[0]);
        BigDecimal fraction = new BigDecimal(decimal).subtract(integer);
        StringBuilder integerResult = new StringBuilder("");
        StringBuilder fractionResult = new StringBuilder("");

        // Integer part conversion
        int remainder = 0;
        while (integer.compareTo(BigDecimal.valueOf(base)) >= 0) {
            remainder = integer.remainder(BigDecimal.valueOf(base)).intValue();
            integer = integer.divide(BigDecimal.valueOf(base), 0, RoundingMode.DOWN);

            integerResult.insert(0, toHexFormat(remainder));
        }

        integerResult.insert(0, toHexFormat(integer.intValue()));

        // Fraction part conversion
        if (fraction.scale() > 0) integerResult.append(".");

        // Example conversion case: 0.75 (decimal to binary)
        while (fraction.scale() > 0 && fractionResult.length() < 5) { // 0.75
            fraction = fraction.multiply(BigDecimal.valueOf(base)); // 0.75 * 2
            fractionResult.append(toHexFormat(fraction.toBigInteger().intValue())); // 1.5 -> append "1"
            if (fraction.compareTo(BigDecimal.valueOf(1)) > 0) { // if fraction > 1 -> if 1.5 > 1
                BigDecimal whole = fraction.setScale(0, RoundingMode.DOWN); // 1.5 -> 1
                fraction = fraction.subtract(whole); // 1.5 - 1
            }
        }

        return integerResult.append(fractionResult).toString();
    }

    // return hexadecimal formatted number
    // e.g. 15 return F
    static String toHexFormat(int n) {
        // Conversion through ASCII characters sequence with '9' and 'A' as the logic point
        if (n >= 10) {
            int offset = n - 9;
            return Character.toString((char) (64 + offset)); // 64 -> ASCII decimal just before 'A'
        }
        return Integer.toString(n);
    }

    // return decimal number from Hex format, else return parsed Integer
    // e.g. F return 15
    static BigDecimal parseNumber(String n) {
        // Conversion through ASCII characters sequence with 'A' as the logic point
        if (n.toUpperCase().matches("[A-Z]")) {
            int offset = n.toUpperCase().charAt(0) - 64; // 64 -> decimal value of '@', behind 'A' of 65 decimal
            return new BigDecimal(String.valueOf(9 + offset));
        }
        return new BigDecimal(n);
    }

    // Getter and setter
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getInteger() {
        return integer;
    }

    public void setInteger(String n) {
        this.integer = n;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }
}
