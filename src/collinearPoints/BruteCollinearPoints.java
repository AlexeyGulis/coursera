package collinearPoints;

import java.util.Arrays;

public class BruteCollinearPoints {

    private final int numberSegments;
    private final LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {
        Point[] clonePoints;
        int numberSegments1 = 0;
        boolean flag;
        Point[] minMax = new Point[4];
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
        for (int i = 0; i < clonePoints.length; i++) {
            for (int j = i + 1; j < clonePoints.length; j++) {
                for (int k = j + 1; k < clonePoints.length; k++) {
                    if (clonePoints[i].slopeTo(clonePoints[j]) == clonePoints[i].slopeTo(clonePoints[k])) {
                        minMax[0] = clonePoints[i];
                        minMax[1] = clonePoints[j];
                        minMax[2] = clonePoints[k];
                        flag = true;
                        for (int p = k + 1; p < clonePoints.length; p++) {
                            if (clonePoints[i].slopeTo(clonePoints[j]) == clonePoints[i].slopeTo(clonePoints[p])) {
                                minMax[3] = clonePoints[p];
                                Arrays.sort(minMax);
                                if (clonePoints[p].compareTo(minMax[0]) < 0 && !flag) {
                                    minMax[0] = clonePoints[p];
                                }
                                if (clonePoints[p].compareTo(minMax[3]) > 0 && !flag) {
                                    minMax[3] = clonePoints[p];
                                }
                                flag = false;
                            }
                        }
                        if (!flag) {
                            lineSegments[numberSegments1++] = new LineSegment(minMax[0], minMax[3]);
                        }
                    }
                }
            }
        }
        numberSegments = numberSegments1;
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
