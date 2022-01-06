package seamCarving;

import edu.princeton.cs.algs4.Picture;

public class SeamCarverOpt {
    private int[] img;
    private boolean isTranspose;
    private final static boolean HORIZONTAL = true;
    private final static boolean VERTICAL = false;
    private int width;
    private int height;
    private double[] energyOpt;
    private static double[] distToOpt;
    private static int[] edgeToOpt;


    public SeamCarverOpt(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        width = picture.width();
        height = picture.height();
        img = new int[height() * width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                img[twoDtoOneD(i, j, HORIZONTAL)] = picture.getRGB(i, j);
            }
        }
        energyOpt = new double[height() * width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energyOpt[twoDtoOneD(i, j, VERTICAL)] = energy(i, j);
            }
        }
        isTranspose = VERTICAL;
    }

    public Picture picture() {
        Picture rePic = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                rePic.setRGB(i, j, img[twoDtoOneD(i, j, true)]);
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

    private int twoDtoOneD(int i, int j, boolean rotation) {
        return rotation ? i * height() + j : j * width() + i;
    }

    private int getIndex(int i, int j, boolean isTranspose) {
        return isTranspose ? j * width() + i : i * width() + j;
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
        int rgb1 = img[twoDtoOneD(x, y - 1, true)];
        int rgb2 = img[twoDtoOneD(x, y + 1, true)];
        return getDelta(rgb1, rgb2);
    }

    private int deltaY(int x, int y) {
        int rgb1 = img[twoDtoOneD(x - 1, y, true)];
        int rgb2 = img[twoDtoOneD(x + 1, y, true)];
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
        isTranspose = VERTICAL;
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
        distToOpt = new double[height() * width()];
        edgeToOpt = new int[height() * width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                if (i == 0) {
                    distToOpt[getIndex(i, j, isTranspose)] = 0;
                } else {
                    distToOpt[getIndex(i, j, isTranspose)] = Double.POSITIVE_INFINITY;
                }
            }
        }
        int[] result = new int[width()];
        for (int i = 0; i < width() - 1; i++) {
            for (int j = 0; j < height(); j++) {
                if (j != 0) {
                    relax(i, j - 1, j);
                }
                if (j != height() - 1) {
                    relax(i, j + 1, j);
                }
                relax(i, j, j);
            }
        }
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height(); i++) {
            if (min > distToOpt[getIndex(width() - 1, i, isTranspose)]) {
                min = distToOpt[getIndex(width() - 1, i, isTranspose)];
                result[width() - 1] = i;
            }
        }
        for (int i = width - 2; i >= 0; i--) {
            result[i] = edgeToOpt[getIndex(i + 1, result[i + 1], isTranspose)];
        }
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
                    temp[twoDtoOneD(j2, i, true)] = img[twoDtoOneD(j, i, true)];
                    j2++;
                }
            }
        }
        width--;
        img = temp;
        isTranspose = VERTICAL;
        revalueEnergy(seam);
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
        int[] temp = new int[width() * (height() - 1)];
        for (int i = 0; i < width(); i++) {
            int j2 = 0;
            for (int j = 0; j < height(); j++) {
                if (seam[i] != j) {
                    temp[twoDtoOneD(i, j2, true)] = img[twoDtoOneD(i, j, true)];
                    j2++;
                }
            }
        }
        img = temp;
        height--;
        isTranspose = HORIZONTAL;
        revalueEnergy(seam);
    }

    private void revalueEnergy(int[] seam) {
        for (int i = 0; i < seam.length; i++) {
            System.arraycopy(energyOpt, getIndex(i,seam[i],isTranspose) + 1 - i, energyOpt, getIndex(i,seam[i],isTranspose) - i, energyOpt.length - (getIndex(i,seam[i],isTranspose) + 1 - i));
        }
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energyOpt[twoDtoOneD(i, j, VERTICAL)] = energy(i, j);
            }
        }
    }



    public static void main(String[] args) {

    }
}
