package seamCarving;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pictureOut;
    private boolean rotation;
    private double[][] energy;
    private static double[][] distTo;
    private static int[][] edgeTo;


    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        rotation = true;
        this.pictureOut = new Picture(picture);
        energy = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energy[i][j] = energy(j, i);
            }
        }
    }

    public Picture picture() {
        Picture rePic = new Picture(pictureOut);
        return rePic;
    }

    public int width() {
        return pictureOut.width();
    }

    public int height() {
        return pictureOut.height();
    }

    public double energy(int x, int y) {
        if (x > width() - 1 || x < 0 || y > height() - 1 || y < 0) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000.0;
        }
        return Math.sqrt(deltaX(x, y) + deltaY(x, y));
    }

    private int deltaX(int x, int y) {
        int rgb1 = pictureOut.getRGB(x, y - 1);
        int rgb2 = pictureOut.getRGB(x, y + 1);
        return getDelta(rgb1, rgb2);
    }

    private int deltaY(int x, int y) {
        int rgb1 = pictureOut.getRGB(x - 1, y);
        int rgb2 = pictureOut.getRGB(x + 1, y);
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
        initialVertTopologicalSort();
        return findVert();
    }

    public int[] findHorizontalSeam() {
        initialHorzTopologicalSort();
        return findHorz();
    }


    private void initialVertTopologicalSort() {
        int height = height();
        int width = width();
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
    }

    private void initialHorzTopologicalSort() {
        int height = height();
        int width = width();
        distTo = new double[height][width];
        edgeTo = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (j == 0) {
                    distTo[i][j] = 0;
                    edgeTo[i][j] = j;
                } else {
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    private int[] findHorz() {
        int height = height();
        int width = width();
        int[] result = new int[width];
        for (int i = 0; i < width - 1; i++) {
            for (int j = 0; j < height; j++) {
                if (j != 0) {
                    relaxHorz(i, j - 1, j);
                }
                if (j != height - 1) {
                    relaxHorz(i, j + 1, j);
                }
                relaxHorz(i, j, j);
            }
        }
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height; i++) {
            if (min > distTo[i][width - 1]) {
                min = distTo[i][width - 1];
                result[width - 1] = i;
            }
        }
        for (int i = width - 2; i >= 0; i--) {
            result[i] = edgeTo[result[i + 1]][i + 1];
        }
        return result;
    }

    private int[] findVert() {
        int height = height();
        int width = width();
        int[] result = new int[height];
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                if (j != 0) {
                    relaxVert(i, j - 1, j);
                }
                if (j != width - 1) {
                    relaxVert(i, j + 1, j);
                }
                relaxVert(i, j, j);
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

    private void relaxVert(int i, int j, int k) {
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
    private void relaxHorz(int i, int j, int k) {
        if (i == 0) {
            if (distTo[j][i + 1] > energy[k][i] + energy[j][i + 1]) {
                distTo[j][i + 1] = energy[k][i] + energy[j][i + 1];
                edgeTo[j][i + 1] = k;
            }
        } else if (distTo[j][i + 1] > distTo[k][i] + energy[j][i + 1]) {
            distTo[j][i + 1] = distTo[k][i] + energy[j][i + 1];
            edgeTo[j][i + 1] = k;
        }
    }

    private static double[][] rotateEnergy
            (double[][] imageArr, int rows, int columns, int flag) {

        double rotatedImageArr[][] = new double[columns][rows];
        if (flag == 1) {  //90 degree rotation in right
            for (int i = 0; i < columns; i++) {
                for (int j = 0; j < rows; j++) {
                    rotatedImageArr[i][j] = imageArr[rows - 1 - j][i];
                }
            }
        }

        if (flag == 0) { //90 degree rotation in left
            for (int i = 0; i < columns; i++) {
                for (int j = 0; j < rows; j++) {
                    rotatedImageArr[i][j] = imageArr[j][columns - 1 - i];
                }
            }
        }
        return rotatedImageArr;
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null || seam.length != height() || width() <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < height(); i++) {
            if (seam[i] >= width() || seam[i] < 0) {
                throw new IllegalArgumentException();
            }
            if (i != 0) {
                if (Math.abs(seam[i - 1] - seam[i]) > 1 || Math.abs(seam[i - 1] - seam[i]) < 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        Picture temp = new Picture(width() - 1, height());
        for (int i = 0; i < height(); i++) {
            int j2 = 0;
            for (int j = 0; j < width(); j++) {
                if (seam[i] != j) {
                    temp.setRGB(j2, i, pictureOut.getRGB(j, i));
                    j2++;
                }
            }
        }
        pictureOut = temp;
        revalueEnergyVertical(seam);
    }

    private void revalueEnergyVertical(int[] seam) {
        for (int i = 0; i < height(); i++) {
            System.arraycopy(energy[i], seam[i] + 1, energy[i], seam[i], width() - seam[i]);
        }
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energy[i][j] = energy(j, i);
            }
        }
    }

    private void revalueEnergyHorizontal(int[] seam) {
        for (int i = 0; i < width(); i++) {
            System.arraycopy(energy[i], seam[width() - i - 1] + 1, energy[i], seam[width() - i - 1], height() - seam[width() - i - 1]);
        }
        energy = rotateEnergy(energy, energy.length, energy[0].length, 1);
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energy[i][j] = energy(j, i);
            }
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width() || height() <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < width(); i++) {
            if (seam[i] >= height() || seam[i] < 0) {
                throw new IllegalArgumentException();
            }
            if (i != 0) {
                if (Math.abs(seam[i - 1] - seam[i]) > 1 || Math.abs(seam[i - 1] - seam[i]) < 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        Picture temp = new Picture(width(), height() - 1);
        for (int i = 0; i < width(); i++) {
            int j2 = 0;
            for (int j = 0; j < height(); j++) {
                if (seam[i] != j) {
                    temp.setRGB(i, j2, pictureOut.getRGB(i, j));
                    j2++;
                }
            }
        }
        energy = rotateEnergy(energy, energy.length, energy[0].length, 0);
        pictureOut = temp;
        revalueEnergyHorizontal(seam);
    }

    public static void main(String[] args) {

    }
}
