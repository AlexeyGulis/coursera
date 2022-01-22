package burrowsWheeler;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    public static void transform(){
        if(BinaryStdIn.isEmpty()) return;
        String str = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(str);
        StringBuilder stringBuilder = new StringBuilder();
        int number = 0;
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            if(circularSuffixArray.index(i) == 0){
                number = i;
                stringBuilder.append(str.charAt(str.length() - 1));
            }else{
                stringBuilder.append(str.charAt(circularSuffixArray.index(i) - 1));
            }
        }
        BinaryStdOut.write(number);
        BinaryStdOut.write(stringBuilder.toString());
    }
    public static void inverseTransform(){

    }

    public static void main(String[] args) {
        if(args[0].equals("-")){
            transform();
        }else if(args[0].equals("-")){
            inverseTransform();
        }
    }
}
