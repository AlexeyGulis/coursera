package seamCarving;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pictureIn;
    private Picture pictureOut;
    private static boolean rotation;
    private static double[][] energy;
    private static double[][] distTo;
    private static int[][] edgeTo;


    public SeamCarver(Picture picture) {
        rotation = true;
        this.pictureIn = new Picture(picture);
        this.pictureOut = new Picture(picture);
        energy = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                if (rotation) {
                    energy[i][j] = energy(j, i);
                } else {
                    energy[i][j] = energy(i, j);
                }
            }
        }
    }

    public Picture picture() {
        return pictureOut;
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
        int rgb1 = picture().getRGB(x, y - 1);
        int rgb2 = picture().getRGB(x, y + 1);
        return getDelta(rgb1, rgb2);
    }

    private int deltaY(int x, int y) {
        int rgb1 = picture().getRGB(x - 1, y);
        int rgb2 = picture().getRGB(x + 1, y);
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
        energy = rotateEnergy(energy, energy.length, energy[0].length, 1);
        int[] result = findVerticalSeam();
        energy = rotateEnergy(energy, energy.length, energy[0].length, 0);
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
        if (!rotation) {
            for (int i = 0; i < result.length; i++) {
                result[i] = width - 1 - result[i];
            }
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
            energy[i][seam[i]] = energy(seam[i], i);
            if (seam[i] != 0) {
                energy[i][seam[i] - 1] = energy(seam[i] - 1, i);
            }
        }
    }

    private void revalueEnergyHorizontal(int[] seam) {
        for (int i = 0; i < width(); i++) {
            System.arraycopy(energy[i], seam[width() - i - 1] + 1, energy[i], seam[width() - i - 1], height() - seam[width() - i - 1]);
        }
        for (int i = 0; i < width(); i++) {
            energy[i][seam[width() - i - 1]] = energy(i, seam[i]);
            if (seam[i] != 0 && seam[width() - i - 1] != 0) {
                energy[i][seam[width() - i - 1] - 1] = energy(i, seam[i] - 1);
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
        pictureOut = temp;
        energy = rotateEnergy(energy, energy.length, energy[0].length, 0);
        revalueEnergyHorizontal(seam);
        energy = rotateEnergy(energy, energy.length, energy[0].length, 1);

    }

    public static void main(String[] args) {

    }
}
