package collinearPoints;

import java.util.Arrays;

public class FastCollinearPoints {
    private int numberSegments;
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
        numberSegments = 0;
        if (points == null) {
            throw new IllegalArgumentException();
        } else {
            for (int i = 0; i < points.length; i++) {
                if (points[i] == null) {
                    throw new IllegalArgumentException();
                }
            }
            lineSegments = new LineSegment[points.length * points.length];
            Point max;
            Point min;
            for (int i = 0; i < points.length - 3; i++) {
                Arrays.sort(points, i + 1, points.length, points[i].slopeOrder());
                max = points[i];
                min = points[i];
                int k = 0;
                for (int j = i + 1; j < points.length - 1; j++) {
                    if (points[i].compareTo(points[j]) == 0) {
                        throw new IllegalArgumentException();
                    }
                    if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[j + 1])) {
                        if (points[j].compareTo(max) == 1) {
                            max = points[j];
                        } else if (points[j].compareTo(min) == -1) {
                            min = points[j];
                        }
                        k++;
                        if (j + 1 == points.length - 1) {
                            if (k >= 2) {
                                if (points[j + 1].compareTo(max) == 1) {
                                    max = points[j + 1];
                                }
                                if (points[j + 1].compareTo(min) == -1) {
                                    min = points[j + 1];
                                }
                                boolean flag = true;
                                if(i >= 1){
                                    for (int l = i - 1; l >= 0; l--) {
                                        if(points[l].slopeTo(max) == min.slopeTo(max)){
                                            flag = false;
                                        }
                                    }
                                }
                                if(flag) lineSegments[numberSegments++] = new LineSegment(min, max);
                            }
                        }
                    } else {
                        if (k >= 2) {
                            boolean flag = true;
                            if(i >= 1){
                                for (int l = i - 1; l >= 0; l--) {
                                    if(points[l].slopeTo(max) == min.slopeTo(max)){
                                        flag = false;
                                    }
                                }
                            }
                            if(flag) lineSegments[numberSegments++] = new LineSegment(min, max);
                        }
                        max = points[i];
                        min = points[i];
                        k = 0;
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return numberSegments;
    }

    public LineSegment[] segments() {
        return lineSegments;
    }
}
