package collinearPoints;

import javax.sound.sampled.Line;
import java.util.Arrays;

public class BruteCollinearPoints {

    private int numberOfSegments;
    private LineSegment[] lineSegments;
    private Point[] minMax;

    public BruteCollinearPoints(Point[] points) {
        numberOfSegments = 0;
        boolean flag;
        minMax = new Point[4];

        if (points == null) {
            throw new IllegalArgumentException();
        } else {
            lineSegments = new LineSegment[points.length * points.length];
            for (int i = 0; i < points.length; i++) {
                if (points[i] == null) {
                    throw new IllegalArgumentException();
                }
                for (int j = i + 1; j < points.length; j++) {
                    if (points[i].compareTo(points[j]) == 0) {
                        throw new IllegalArgumentException();
                    }
                    for (int k = j + 1; k < points.length; k++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])) {
                            flag = true;
                            for (int l = k + 1; l < points.length; l++) {
                                if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[l])) {
                                    if(flag){
                                        flag = false;
                                        minMax[0]=points[i];
                                        minMax[1]=points[j];
                                        minMax[2]=points[k];
                                        minMax[3]=points[l];
                                        Arrays.sort(minMax);
                                    }else{
                                        if(points[l].compareTo(minMax[0]) == -1){
                                            minMax[0] = points[l];
                                        }
                                        if(points[l].compareTo(minMax[3]) == 1){
                                            minMax[3] = points[l];
                                        }
                                    }

                                }
                            }
                            if(!flag){
                                lineSegments[numberOfSegments++] = new LineSegment(minMax[0],minMax[3]);
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        return lineSegments;
    }

}
