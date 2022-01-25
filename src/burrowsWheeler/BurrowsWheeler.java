package burrowsWheeler;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

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
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        if (BinaryStdIn.isEmpty()) return;
        char[] t = BinaryStdIn.readString().toCharArray();
        int[] index = new int[t.length];
        char[] tOrder = new char[t.length];
        int[] count = new int[256 + 1];
        for (int i = 0; i < t.length; i++) {
            count[t[i] + 1]++;
        }
        for (int i = 0; i < 256; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < t.length; i++) {
            index[count[t[i]]] = i;
            tOrder[count[t[i]]++] = t[i];
        }
        int j = start;
        for (int i = 0; i < t.length; i++) {
            BinaryStdOut.write(tOrder[j]);
            j = index[j];
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
