package collinearPoints;

import java.util.Arrays;

public class FastCollinearPoints {
    private final int numberSegments;
    private final LineSegment[] lineSegments;
    private final Point[] clonePoints;
    private Point max;
    private Point min;

    public FastCollinearPoints(Point[] points) {
        int numberSegments1 = 0;
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException();
        }
        clonePoints = new Point[points.length];
        System.arraycopy(points, 0, clonePoints, 0, points.length);
        Arrays.sort(clonePoints);
        for (int i = 0; i < clonePoints.length - 1; i++) {
            if (clonePoints[i].compareTo(clonePoints[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
        lineSegments = new LineSegment[clonePoints.length * clonePoints.length];
        for (int i = 0; i < clonePoints.length - 3; i++) {
            Arrays.sort(clonePoints, i + 1, clonePoints.length, clonePoints[i].slopeOrder());
            if (i != 0) Arrays.sort(clonePoints, 0, i, clonePoints[i].slopeOrder());
            max = clonePoints[i];
            min = clonePoints[i];
            int k = 0;
            for (int j = i + 1; j < clonePoints.length - 1; j++) {
                if (clonePoints[i].slopeTo(clonePoints[j]) == clonePoints[i].slopeTo(clonePoints[j + 1])) {
                    minMax(j);
                    k++;
                    if (j + 1 == clonePoints.length - 1 && k >= 2) {
                        minMax(j + 1);
                        if (i != 0) {
                            if (this.binarySearch(0, i, min.slopeTo(max)) == -1) {
                                lineSegments[numberSegments1++] = new LineSegment(min, max);
                            }
                        } else {
                            lineSegments[numberSegments1++] = new LineSegment(min, max);
                        }
                    }
                } else {
                    if (k >= 2) {
                        minMax(j);
                        if (i != 0) {
                            if (this.binarySearch(0, i, min.slopeTo(max)) == -1) {
                                lineSegments[numberSegments1++] = new LineSegment(min, max);
                            }
                        } else {
                            lineSegments[numberSegments1++] = new LineSegment(min, max);
                        }
                    }
                    max = clonePoints[i];
                    min = clonePoints[i];
                    k = 0;
                }
            }
        }
        numberSegments = numberSegments1;
    }

    private void minMax(int i) {
        if (clonePoints[i].compareTo(max) > 0) {
            max = clonePoints[i];
        } else if (clonePoints[i].compareTo(min) < 0) {
            min = clonePoints[i];
        }
    }

    private int binarySearch(int first, int last, double key) {
        if (last >= first) {
            int mid = first + (last - first) / 2;
            if (clonePoints[mid].slopeTo(max) == key) {
                return mid;
            }
            if (clonePoints[mid].slopeTo(max) > key) {
                return binarySearch(first, mid - 1, key);
            } else {
                return binarySearch(mid + 1, last, key);
            }
        }
        return -1;
    }

    public int numberOfSegments() {
        return numberSegments;
    }

    public LineSegment[] segments() {
        LineSegment[] lineSegm = new LineSegment[numberSegments];
        System.arraycopy(lineSegments, 0, lineSegm, 0, numberSegments);
        return lineSegm;
    }
}
