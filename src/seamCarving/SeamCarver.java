package seamCarving;

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture pictureIn;
    private Picture pictureOut;
    private boolean rotation;
    private double[][] energy;
    private static double[][] distTo;
    private static int[][] edgeTo;


    public SeamCarver(Picture picture) {
        if(picture == null) throw new IllegalArgumentException();
        rotation = true;
        this.pictureIn = new Picture(picture);
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
        energy = rotateEnergy(energy, height(), width(), 1);
        int[] result = findVerticalSeam();
        energy = rotateEnergy(energy, width(), height(), 0);
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
        double[][] temp = new double[height()][width()];
        for (int i = 0; i < height(); i++) {
            System.arraycopy(energy[i], 0, temp[i], 0, seam[i]);
            System.arraycopy(energy[i], seam[i] + 1, temp[i], seam[i], width() - seam[i]);
        }
        energy = temp;
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energy[i][j] = energy(j,i);
            }
        }
    }

    private void revalueEnergyHorizontal(int[] seam) {
        double[][] temp = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            System.arraycopy(energy[i], 0, temp[i], 0, seam[width() - i - 1]);
            System.arraycopy(energy[i], seam[width() - i - 1] + 1, temp[i], seam[width() - i - 1], height() - seam[width() - i - 1]);
        }
        energy = temp;
        energy = rotateEnergy(energy, width(), height(), 1);
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                energy[i][j] = energy(j,i);
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
        energy = rotateEnergy(energy, height(), width(), 0);
        pictureOut = temp;
        revalueEnergyHorizontal(seam);
    }

    public static void main(String[] args) {
        int[][] temp2 = new int[6][6];
        String[] temp = {"#050908 #050407 #090100 #060008 #050402 #000201",
                "#030309 #060706 #090404 #060005 #020800 #010204",
                "#080907 #080103 #090105 #070702 #050208 #050305",
                "#020005 #070007 #090903 #060404 #020502 #010303",
                "#060000 #080407 #060305 #020707 #010808 #050603",
                "#070706 #040604 #080202 #070508 #080602 #020006"
        };
        for (int i = 0; i < temp.length; i++) {
            String[] temp1 = temp[i].split(" ");
            for (int j = 0; j < temp1.length; j++) {
                temp2[i][j] = Integer.parseInt(temp1[j].replace('#','F'),16);
            }
        }
        Picture pic = new Picture(6,6);
        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {
                pic.setRGB(j,i,temp2[i][j]);
            }
        }
        SeamCarver carver = new SeamCarver(pic);
        for (int i = 0; i < carver.width(); i++) {
            for (int j = 0; j < carver.height(); j++) {
                StdOut.printf("%7.2f ", carver.energy(j,i));
            }
            StdOut.println();
        }
        int[] r = {3, 4, 3, 2, 2, 1};
        carver.removeHorizontalSeam(r);
        carver.findHorizontalSeam();
        int[] horizontalSeam = carver.findHorizontalSeam();
        for (int y : horizontalSeam)
            StdOut.print(y + " ");
        StdOut.println("}");
        PrintSeams.printSeam(carver, horizontalSeam, true);
    }
}
