package seamCarving;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverOpt {
    private int[] img;
    private boolean isTranspose;
    private final static boolean HORIZONTAL = true;
    private final static boolean VERTICAL = false;
    private int width;
    private int height;
    private int tempWidth;
    private double[] energyOpt;
    private static double[] distToOpt;
    private static int[] edgeToOpt;


    public SeamCarverOpt(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        width = picture.width();
        height = picture.height();
        tempWidth = width;
        img = new int[height() * width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                img[getIndex(i, j, HORIZONTAL)] = picture.getRGB(i, j);
            }
        }
        energyOpt = new double[height() * width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energyOpt[getIndex(i, j, VERTICAL)] = energy(j, i);
            }
        }
        isTranspose = VERTICAL;
    }

    public Picture picture() {
        Picture rePic = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                rePic.setRGB(i, j, img[getIndex(i, j, HORIZONTAL)]);
            }
        }
        return rePic;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    private int getIndex(int i, int j, boolean isTranspose) {
        return isTranspose ? j * tempWidth + i : i * tempWidth + j;
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
        int rgb1 = img[getIndex(x, y - 1, HORIZONTAL)];
        int rgb2 = img[getIndex(x, y + 1, HORIZONTAL)];
        return getDelta(rgb1, rgb2);
    }

    private int deltaY(int x, int y) {
        int rgb1 = img[getIndex(x - 1, y, HORIZONTAL)];
        int rgb2 = img[getIndex(x + 1, y, HORIZONTAL)];
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
        distToOpt = new double[height() * width()];
        edgeToOpt = new int[height() * width()];
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                if (i == 0) {
                    distToOpt[getIndex(i, j, isTranspose)] = 0;
                } else {
                    distToOpt[getIndex(i, j, isTranspose)] = Double.POSITIVE_INFINITY;
                }
            }
        }
        int[] result = new int[height()];
        for (int i = 0; i < height() - 1; i++) {
            for (int j = 0; j < width(); j++) {
                if (j != 0) {
                    relax(i, j - 1, j);
                }
                if (j != width() - 1) {
                    relax(i, j + 1, j);
                }
                relax(i, j, j);
            }
        }
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width(); i++) {
            if (min > distToOpt[getIndex(height() - 1, i, isTranspose)]) {
                min = distToOpt[getIndex(height() - 1, i, isTranspose)];
                result[height() - 1] = i;
            }
        }
        for (int i = height - 2; i >= 0; i--) {
            result[i] = edgeToOpt[getIndex(i + 1, result[i + 1], isTranspose)];
        }
        return result;
    }

    public int[] findHorizontalSeam() {
        isTranspose = HORIZONTAL;
        int temp = width;
        width = height;
        height = temp;
        int[] result = findVerticalSeam();
        height = width;
        width = temp;
        isTranspose = VERTICAL;
        return result;
    }

    private void relax(int i, int j, int k) {
        if (i == 0) {
            if (distToOpt[getIndex(i + 1, j, isTranspose)] >
                    energyOpt[getIndex(i, k, isTranspose)] + energyOpt[getIndex(i + 1, j, isTranspose)]) {
                distToOpt[getIndex(i + 1, j, isTranspose)] = energyOpt[getIndex(i, k, isTranspose)] + energyOpt[getIndex(i + 1, j, isTranspose)];
                edgeToOpt[getIndex(i + 1, j, isTranspose)] = k;
            }
        } else if (distToOpt[getIndex(i + 1, j, isTranspose)] >
                distToOpt[getIndex(i, k, isTranspose)] + energyOpt[getIndex(i + 1, j, isTranspose)]) {
            distToOpt[getIndex(i + 1, j, isTranspose)] = distToOpt[getIndex(i, k, isTranspose)] + energyOpt[getIndex(i + 1, j, isTranspose)];
            edgeToOpt[getIndex(i + 1, j, isTranspose)] = k;
        }
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
        int[] temp = new int[(width() - 1) * height()];
        for (int i = 0; i < height(); i++) {
            int j2 = 0;
            for (int j = 0; j < width(); j++) {
                if (seam[i] != j) {
                    if (!isTranspose) temp[getIndex(i, j2, isTranspose) - i] = img[getIndex(i, j, isTranspose)];
                    else temp[getIndex(i, j2, isTranspose)] = img[getIndex(i, j, isTranspose)];
                    j2++;
                }
            }
        }
        img = temp;
        revalueEnergy(seam);
    }

    public void removeHorizontalSeam(int[] seam) {
        isTranspose = HORIZONTAL;
        int temp = width;
        width = height;
        height = temp;
        removeVerticalSeam(seam);
        isTranspose = VERTICAL;
    }

    private void revalueEnergy(int[] seam) {
        for (int i = 0; i < seam.length; i++) {
            if (isTranspose) {
                for (int j = 0; j < width(); j++) {
                    if (j > seam[i]) {
                        energyOpt[getIndex(i, j - 1, isTranspose)] = energyOpt[getIndex(i, j, isTranspose)];
                    }
                }
            } else {
                System.arraycopy(energyOpt,
                        getIndex(i, seam[i], isTranspose) + 1 - i,
                        energyOpt,
                        getIndex(i, seam[i], isTranspose) - i,
                        energyOpt.length - (getIndex(i, seam[i], isTranspose) + 1 - i));
            }

        }
        width--;
        if (!isTranspose) {
            tempWidth = width;
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] != width()) {
                    energyOpt[getIndex(i, seam[i], isTranspose)] = energy(seam[i], i);
                }
                if (seam[i] != 0) {
                    energyOpt[getIndex(i, seam[i] - 1, isTranspose)] = energy(seam[i] - 1, i);
                }
            }
        } else {
            int temp = width;
            width = height;
            height = temp;
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] != height()) {
                    energyOpt[getIndex(i, seam[i], isTranspose)] = energy(i, seam[i]);
                }
                if (seam[i] != 0) {
                    energyOpt[getIndex(i, seam[i] - 1, isTranspose)] = energy(i, seam[i] - 1);
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}
