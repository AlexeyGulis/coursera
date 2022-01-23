package burrowsWheeler;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
    private static char[] t;

    public static void transform() {
        if (BinaryStdIn.isEmpty()) return;
        String str = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);
        StringBuilder stringBuilder = new StringBuilder();
        int number = 0;
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                number = i;
                stringBuilder.append(str.charAt(str.length() - 1));
            } else {
                stringBuilder.append(str.charAt(circularSuffixArray.index(i) - 1));
            }
        }
        BinaryStdOut.write(number);
        BinaryStdOut.write(stringBuilder.toString(), 8);
    }

    public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        if (BinaryStdIn.isEmpty()) return;
        t = BinaryStdIn.readString().toCharArray();
        Custom[] customs = new Custom[t.length];
        for (int i = 0; i < t.length; i++) {
            customs[i] = new Custom(i, t[i]);
        }
        Arrays.sort(customs);
        int j = start;
        for (int i = 0; i < t.length; i++) {
            if (i == 0) j = start;
            BinaryStdOut.write(customs[j].getB());
            j = customs[j].getI();
        }
    }

    private static class Custom implements Comparable<Custom> {
        private int i;
        private char b;

        private Custom(int i, char b) {
            this.i = i;
            this.b = b;
        }

        private int getI() {
            return i;
        }

        private char getB() {
            return b;
        }

        @Override
        public int compareTo(Custom o) {
            return t[this.getI()] > t[o.getI()] ? 1 : t[this.getI()] < t[o.getI()] ? -1 : 0;
        }
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
