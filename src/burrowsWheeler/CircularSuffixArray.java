package burrowsWheeler;

import java.util.Arrays;

public class CircularSuffixArray {

    private int n;
    private CircularSuffix[] circularSuffix;
    private String s;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        this.s = s;
        n = s.length();
        circularSuffix = new CircularSuffix[n];
        for (int i = 0; i < n; i++) {
            circularSuffix[i] = new CircularSuffix(i);
        }
        Arrays.sort(circularSuffix);
    }

    public int length() {
        return n;
    }

    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return circularSuffix[i].getI();
    }

    private class CircularSuffix implements Comparable<CircularSuffix> {
        private int i;

        private CircularSuffix(int i) {
            this.i = i;
        }

        private int getI() {
            return i;
        }

        @Override
        public int compareTo(CircularSuffix o) {
            int count = 0;
            int i = this.getI();
            int j = o.getI();
            if (s.charAt(i) != s.charAt(j)) return s.charAt(i) > s.charAt(j) ? 1 : -1;
            else {
                while (count < n) {
                    count++;
                    j++;
                    i++;
                    if (i == n) {
                        i = 0;
                    }
                    if (j == n) {
                        j = 0;
                    }
                    if (s.charAt(i) != s.charAt(j)) return s.charAt(i) > s.charAt(j) ? 1 : -1;
                }
            }
            return 0;
        }
    }

    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
    }

}
