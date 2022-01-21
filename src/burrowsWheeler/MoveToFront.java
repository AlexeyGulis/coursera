package burrowsWheeler;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    public static void encode() {
        char[] alphabet = new char[256];
        for (int i = 0; i < alphabet.length; i++) {
            alphabet[i] = (char) i;
        }
        char[] input = BinaryStdIn.readString().toCharArray();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < alphabet.length; i++) {
                if (alphabet[j] == input[i]) {
                    BinaryStdOut.write((char) j, 8);
                    System.arraycopy(alphabet, 0, alphabet, 1, j);
                    alphabet[0] = input[i];
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
        char[] input = BinaryStdIn.readString().toCharArray();
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < alphabet.length; i++) {
                if (alphabet[j] == input[i]) {
                    BinaryStdOut.write(alphabet[j], 8);
                    System.arraycopy(alphabet, 0, alphabet, 1, j);
                    alphabet[0] = input[i];
                }
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if(args[0].equals("-")){
            encode();
        }else if(args[0].equals("+")){
            decode();
        }
    }
}
