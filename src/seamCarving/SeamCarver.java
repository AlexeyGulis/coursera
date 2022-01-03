package seamCarving;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture pictureIn;
    private int[][] imgRGB;
    private Picture pictureOut;
    private static int width;
    private static int height;
    private static boolean rotation;
    private static double[][] energy;
    private static double[][] distTo;
    private static int[][] edgeTo;


    public SeamCarver(Picture picture) {
        rotation = true;
        this.pictureIn = new Picture(picture);
        width = picture.width();
        height = picture.height();
        this.pictureOut = new Picture(picture);
        imgRGB = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                imgRGB[i][j] = picture.getRGB(i, j);
            }
        }
        StdOut.println();
    }

    public Picture picture() {
        return pictureOut;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int x, int y) {
        if (x > width - 1 || x < 0 || y > height - 1 || y < 0) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
            return 1000.0;
        }
        return Math.sqrt(deltaX(x, y) + deltaY(x, y));
    }

    private int deltaX(int x, int y) {
        int rgb1 = imgRGB[x][y - 1];
        int rgb2 = imgRGB[x][y + 1];
        return getDelta(rgb1, rgb2);
    }

    private int deltaY(int x, int y) {
        int rgb1 = imgRGB[x - 1][y];
        int rgb2 = imgRGB[x + 1][y];
        return getDelta(rgb1, rgb2);
    }

    private int getDelta(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >> 8) & 0xFF;
        int b1 = (rgb1 >> 0) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >> 8) & 0xFF;
        int b2 = (rgb2 >> 0) & 0xFF;
        return (r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) + (b1 - b2) * (b1 - b2);
    }

    public int[] findVerticalSeam() {
        initialTopologicalSort();
        return find();
    }

    private void relax(int i, int j, int k) {
        if (i == 0) {
            if (distTo[i + 1][j] > energy[i][k] + energy[i + 1][j]) {
                distTo[i + 1][j] = energy[i][k] + energy[i + 1][j];
                edgeTo[i + 1][j] = k;
            }
        } else if (distTo[i + 1][j] > distTo[i][k] + energy[i + 1][j]) {
            distTo[i + 1][j] = distTo[i][k] + energy[i + 1][j];
            edgeTo[i + 1][j] = k;
        }
    }

    public int[] findHorizontalSeam() {
        rotation = false;
        int[] result = findVerticalSeam();
        rotation = true;
        return result;
    }

    private int[] find() {
        int height;
        int width;
        if (rotation) {
            height = height();
            width = width();
        } else {
            height = width();
            width = height();
        }
        int[] result = new int[height];
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                if (j != 0) {
                    relax(i, j - 1, j);
                }
                if (j != width - 1) {
                    relax(i, j + 1, j);
                }
                relax(i, j, j);
            }
        }
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width; i++) {
            if (min > distTo[height - 1][i]) {
                min = distTo[height - 1][i];
                result[height - 1] = i;
            }
        }
        for (int i = height - 2; i >= 0; i--) {
            result[i] = edgeTo[i + 1][result[i + 1]];
        }
        return result;
    }

    private void initialTopologicalSort() {
        int height;
        int width;
        if (rotation) {
            height = height();
            width = width();
        } else {
            height = width();
            width = height();
        }
        energy = new double[height][width];
        distTo = new double[height][width];
        edgeTo = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0) {
                    distTo[i][j] = 0;
                    edgeTo[i][j] = i;
                } else {
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(rotation){
                    energy[i][j] = energy(j, i);
                }else{
                    energy[i][j] = energy(i, j);
                }
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        for (int i = 0; i < height(); i++) {
            if (seam[i] >= height() || seam[i] < 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        for (int i = 0; i < width(); i++) {
            if (seam[i] >= width() || seam[i] < 0) {
                throw new IllegalArgumentException();
            }
        }

    }

    public static void main(String[] args) {

    }
}
