import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import seamCarving.PrintSeams;
import seamCarving.SeamCarver;
import sliderPuzzle.Board;

public class Test {
    public static void main(String[] args) {
        int[][] temp2 = new int[6][6];
        String[] temp = {"#080807 #050301 #080703 #020208 #010706 #090803",
                "#020606 #080305 #080304 #050106 #070708 #060008",
                "#090304 #010809 #030903 #000103 #070700 #050400",
                "#020604 #070504 #070606 #060309 #070800 #000508",
                "#030705 #040906 #030505 #090400 #020500 #040105",
                "#000609 #040600 #020307 #070502 #010206 #000401"
        };
        for (int i = 0; i < temp.length; i++) {
            String[] temp1 = temp[i].split(" ");
            for (int j = 0; j < temp1.length; j++) {
                temp2[i][j] = Integer.parseInt(temp1[j].replace('#', 'F'), 16);
            }
        }
        Picture pic = new Picture(6, 6);
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                pic.setRGB(j, i, temp2[i][j]);
            }
        }
        SeamCarver carver = new SeamCarver(pic);
        for (int i = 0; i < carver.width(); i++) {
            for (int j = 0; j < carver.height(); j++) {
                StdOut.printf("%7.2f ", carver.energy(j, i));
            }
            StdOut.println();
        }
        int[] r = {1, 2, 3, 3, 4, 3};
        carver.removeVerticalSeam(r);
        int[] r1 = {3, 3, 4, 4, 4, 4};
        carver.removeVerticalSeam(r1);
        carver.findHorizontalSeam();
        int[] horizontalSeam = carver.findHorizontalSeam();
        for (int y : horizontalSeam)
            StdOut.print(y + " ");
        StdOut.println("}");
        PrintSeams.printSeam(carver, horizontalSeam, true);
    }
}
