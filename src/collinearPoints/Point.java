package collinearPoints;

/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        if (this.x == that.x && this.y == that.y) {
            return Double.NEGATIVE_INFINITY;
        }
        if (this.x == that.x) {
            return +0.0;
        }
        if (this.y == that.y) {
            return Double.POSITIVE_INFINITY;
        }
        return (double) (that.y - this.y) / (double) (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        if (this.y < that.y || (this.y == that.y && this.x < that.x)) {
            return -1;
        } else if ((this.y == that.y && this.x > that.x) || this.y > that.y) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        Comparator<Point> comparator = new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                if (Point.this.slopeTo(p1) < Point.this.slopeTo(p2)) {
                    return -1;
                } else if (Point.this.slopeTo(p1) > Point.this.slopeTo(p2)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        return comparator;
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        /*check compareTo
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        StdOut.println(p1.compareTo(p2));
        end check compareTo*/
        /*check slopeTo
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 1);
        StdOut.println(p1.slopeTo(p2));
        end check slopeTo*/
        /*check slopeOrder*/
        int size = StdIn.readInt();
        Point[] points = new Point[size];
        int x;
        int y;
        int i = 0;
        while(!StdIn.isEmpty()){
            x = StdIn.readInt();
            y = StdIn.readInt();
            points[i++] = new Point(x,y);
        }
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        StdOut.println(fastCollinearPoints.numberOfSegments());
        LineSegment[] lineSegments = fastCollinearPoints.segments();
        for (int j = 0; j < fastCollinearPoints.numberOfSegments(); j++) {
            lineSegments[j].draw();
        }
        StdDraw.show();
        /*end check slopeTo*/
        /*
        Point[] points = new Point[9];
        for (int i = 0; i < 5; i++) {
            points[i] = new Point(i, 0);
        }
        for (int i = 5; i < 8; i++) {
            points[i] = new Point(3, 4 - i);
        }
        points[8] = new Point(5, 6);
        //Arrays.sort(points);
        for (int i = 0; i < 9 - 1; i++) {
            Arrays.sort(points,i + 1, points.length, points[i].slopeOrder());
        }
        StdOut.println();
         */
    }
}