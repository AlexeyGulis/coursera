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

import java.io.File;

public class ResizeDemo {
    public static void main(String[] args) {
        if (args.length != 4) {
            StdOut.println("Usage:\njava ResizeDemo [image filename] [num cols to remove] [num rows to remove]");
            return;
        }
        String folder = args[0];
        String safeFolder = args[3];
        Stopwatch sw = new Stopwatch();
        for (int j = 0; j < 101; j++) {
            StringBuilder str = new StringBuilder();
            str.append("Img_0").append(String.format("%03d", j)).append(".tif");
            Picture inputImg = new Picture(folder + str);
            int removeColumns = (int) (inputImg.height() * .9);
            int removeRows = (int) (inputImg.width() * .9);
            SeamCarverOpt sc = new SeamCarverOpt(inputImg);
            int count = 0;
            int p = 0;
            int k = 0;
            while (count < removeColumns + removeRows) {
                if (p < removeRows) {
                    int[] horizontalSeam = sc.findHorizontalSeam();
                    sc.removeHorizontalSeam(horizontalSeam);
                    p++;
                }
                if (k < removeColumns) {
                    int[] verticalSeam = sc.findVerticalSeam();
                    sc.removeVerticalSeam(verticalSeam);
                    k++;
                }
                if ((((double) (inputImg.height() - p - 1) / (double) inputImg.height()) * 100) % 10 == 0) {
                    File dirSave = new File(safeFolder + (inputImg.height() - p - 1));
                    if (!dirSave.exists()) {
                        dirSave.mkdir();
                    }
                    Picture outputImg = sc.picture();
                    outputImg.save(dirSave + "\\" + str);
                }
                count++;
            }
            //StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());
        }
        StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
    }

}
