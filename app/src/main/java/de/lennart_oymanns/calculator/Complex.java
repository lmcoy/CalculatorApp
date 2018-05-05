package de.lennart_oymanns.calculator;

/**
 * Created by lo on 02.05.17.
 */

public class Complex {
    private double real = 0.0;
    private double imag = 0.0;

    public Complex(double re, double im) {
        real = re;
        imag = im;
    }

    public double real() {
        return real;
    }

    public double imag() {
        return imag;
    }

    private static Complex parseNumber(String s) {
        if (s.equals("i")) {
            return new Complex(0.0, 1.0);
        }
        if (s.charAt(s.length() - 1) == 'i') {
            double imag = Double.parseDouble(s.substring(0, s.length() - 1));
            return new Complex(0.0, imag);
        }
        double real = Double.parseDouble(s);
        return new Complex(real, 0.0);
    }

    public static Complex parseComplex(String s) {
        int index_minus = s.indexOf('-');
        int index_plus = s.indexOf('+');
        if (index_minus != -1 && index_plus != -1) {
            throw new NumberFormatException("multiple + or -");
        }
        if (index_minus == -1 && index_plus == -1) {
            return parseNumber(s.trim());
        }

        int index = (index_minus != -1) ? index_minus : index_plus;

        double real = 0.0;

        if (index == 0) {
            real = 0.0;
        } else {
            String realpart = s.substring(0, index).trim();
            if (!realpart.isEmpty()) {
                Complex c1 = parseNumber(realpart);
                if (c1.imag() != 0.0) {
                    throw new NumberFormatException("real part is imaginary");
                }
                real = c1.real();
            }
        }

        String imgpart = s.substring(index + 1).trim();
        Complex c = parseNumber(imgpart);
        if (c.real() != 0.0) {
            throw new NumberFormatException("imag part is real");
        }
        double imag = c.imag();
        if (index_minus >= 0) {
            imag *= -1;
        }

        return new Complex(real, imag);
    }

    @Override
    public String toString() {
        if (Math.abs(real) == 0.0 && Math.abs(imag) == 0.0) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        if (Math.abs(real) != 0.0) {
            builder.append(real);
        }
        if (Math.abs(imag) == 0.0) {
            return builder.toString();
        }
        if (real != 0.0 && imag > 0.0) {
            builder.append("+");
        }
        if (imag != 1.0) {
            builder.append(imag);
        }
        builder.append("i");

        return builder.toString();
    }
}
