package burrowsWheeler;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    public static void encode() {
        char[] alphabet = new char[256];
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char temp = BinaryStdIn.readChar(8);
            for (int j = 0; j < alphabet.length; j++) {
                if (alphabet[j] == temp) {
                    BinaryStdOut.write((char) j, 8);
                    System.arraycopy(alphabet, 0, alphabet, 1, j);
                    alphabet[0] = temp;
                }
            }
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] alphabet = new char[256];
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            BinaryStdOut.write(alphabet[c], 8);
            char temp = alphabet[c];
            System.arraycopy(alphabet, 0, alphabet, 1, c);
            alphabet[0] = temp;
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}
