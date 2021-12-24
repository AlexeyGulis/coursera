package kdTree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> point2DSet;

    public PointSET() {
        point2DSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return point2DSet.isEmpty();
    }

    public int size() {
        return point2DSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        point2DSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return point2DSet.contains(p);
    }

    public void draw() {
        for (Point2D p : point2DSet
        ) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> rangeSet = new TreeSet<>();
        for (Point2D p : point2DSet
        ) {
            if (rect.contains(p)) rangeSet.add(p);
        }
        return rangeSet;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Point2D nearest = null;
        for (Point2D c : point2DSet
        ) {
            if (nearest == null) {
                nearest = c;
            } else if (c.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = c;
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        //unit-test
    }
}
