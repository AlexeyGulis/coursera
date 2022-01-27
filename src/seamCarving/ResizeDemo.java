package seamCarving;
/******************************************************************************
 *  Compilation:  javac ResizeDemo.java
 *  Execution:    java ResizeDemo input.png columnsToRemove rowsToRemove
 *  Dependencies: SeamCarver.java SCUtility.java
 *
 *
 *  Read image from file specified as command line argument. Use SeamCarver
 *  to remove number of rows and columns specified as command line arguments.
 *  Show the images and print time elapsed to screen.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class ResizeDemo {
    public static void main(String[] args) {
        if (args.length != 4) {
            StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
            return;
        }
        String folder = args[0];
        String safeFolder = args[3];
        Stopwatch sw = new Stopwatch();
        for (int i = 0; i < 101; i++) {
            StringBuilder str = new StringBuilder();
            str.append("Img_0").append(String.format("%03d", i)).append(".tif");
            Picture inputImg = new Picture(folder + str);
            int removeColumns = Integer.parseInt(args[1]);
            int removeRows = Integer.parseInt(args[2]);

            //StdOut.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
            SeamCarverOpt sc = new SeamCarverOpt(inputImg);

            for (int j = 0; j < removeRows; j++) {
                int[] horizontalSeam = sc.findHorizontalSeam();
                sc.removeHorizontalSeam(horizontalSeam);
            }

            for (int j = 0; j < removeColumns; j++) {
                int[] verticalSeam = sc.findVerticalSeam();
                sc.removeVerticalSeam(verticalSeam);
            }
            Picture outputImg = sc.picture();

            outputImg.save(safeFolder + str);

            //StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());


        }
        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
    }

}
