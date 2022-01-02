package seamCarving;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energy;
    private int width = -1;
    private int height = -1;
    private double[][] distTo;
    private int[][] edgeTo;


    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
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
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                energy[j][i] = energy(i, j);
            }
        }
    }

    public Picture picture() {
        return picture;
    }

    public int width() {
        if (width == -1) {
            width = picture.width();
            return width;
        } else {
            return width;
        }
    }

    public int height() {
        if (height == -1) {
            height = picture.height();
            return height;
        } else {
            return height;
        }
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
        int rgb1 = picture.getRGB(x, y - 1);
        int rgb2 = picture.getRGB(x, y + 1);
        return getDelta(rgb1, rgb2);
    }

    private int deltaY(int x, int y) {
        int rgb1 = picture.getRGB(x - 1, y);
        int rgb2 = picture.getRGB(x + 1, y);
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
        return null;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        for (int i = 0; i < height; i++) {
            if(seam[i] >= height || seam[i] < 0){
                throw new IllegalArgumentException();
            }
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        for (int i = 0; i < width; i++) {
            if(seam[i] >= width || seam[i] < 0){
                throw new IllegalArgumentException();
            }
        }

    }

    public static void main(String[] args) {

    }
}
