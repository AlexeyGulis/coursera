package collinearPoints;

import java.util.Arrays;

public class FastCollinearPoints {
    private int numberSegments;
    private LineSegment[] lineSegments;
    public FastCollinearPoints(Point[] points){
        if (points == null) {
            throw new IllegalArgumentException();
        } else {
            for (int i = 0; i < points.length; i++) {
                if(points[i] == null){
                    throw new IllegalArgumentException();
                }
            }
            lineSegments = new LineSegment[points.length];
            for (int i = 0; i < points.length - 3; i++) {
                Arrays.sort(points,i + 1,points.length,points[i].slopeOrder());
                int k = 0;
                for (int j = i + 1; j < points.length; j++) {

                }
            }
        }
    }
    public int numberOfSegments(){
        return numberSegments;
    }
    public LineSegment[] segments(){
        return lineSegments;
    }
}
