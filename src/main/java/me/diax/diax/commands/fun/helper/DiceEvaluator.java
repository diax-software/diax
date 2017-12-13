package me.diax.diax.commands.fun.helper;

import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class DiceEvaluator {
    private static final Pattern DICE = Pattern.compile("\\d*d\\d+");
    private static final int MAX_ROLLS = 50000;
    private static final int MAX_SIDES = 50000;
    private final Random random = new Random();
    private final String str;
    private int pos = -1, ch;

    public DiceEvaluator(String str) {
        this.str = str;
    }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();

        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    public double parse() {
        nextChar();
        double x = parseExpr();
        if (pos < str.length()) throw new RuntimeException("Unexpected: " + ((char) ch));
        return x;
    }

    private double parseConst(String key) {
        key = key.toLowerCase();

        if (key.equals("pi")) {
            return Math.PI;
        }

        if (key.equals("e")) {
            return Math.E;
        }

        if (DICE.matcher(key).matches()) {
            String[] parts = key.split("d");
            int amount = Integer.parseInt(parts[0]);
            int size = Integer.parseInt(parts[1]);

            return IntStream.range(0, amount)
                    .mapToLong(operand -> random.nextInt(size) + 1)
                    .sum();
        }

        throw new RuntimeException("Unknown constant: " + key);
    }

    private double parseExpr() {
        double x = parseTerm();
        for (; ; ) {
            if (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseFactor() {
        if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();

        double x;
        int startPos = this.pos;
        if (eat('(')) {
            x = parseExpr();
            eat(')');
        } else {
            x = parseNumberOrConst();
        }

        if (eat('^')) x = Math.pow(x, parseFactor());

        return x;
    }

    private double parseNumberOrConst() {
        boolean letters = false, numbers = false, period = false;

        double x;
        int startPos = this.pos;
        while ((ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || ch == '.') {
            if (!period && ch == '.') period = true;
            if (!numbers && (ch >= '0' && ch <= '9')) numbers = true;
            if (!letters && (ch >= 'a' && ch <= 'z')) letters = true;
            nextChar();
        }

        String token = str.substring(startPos, this.pos);

        if (numbers && !letters) {
            return Double.parseDouble(token);
        }

        if (letters && !period) {
            return parseConst(token);
        }

        throw new RuntimeException("Invalid token: " + token);
    }

    private double parseTerm() {
        double x = parseFactor();
        for (; ; ) {
            if (eat('*')) x *= parseFactor();
            else if (eat('/')) x /= parseFactor();
            else return x;
        }
    }
}
