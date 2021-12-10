package kdTree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class KdTree {

    private Node topNode;

    private class Node {
        public Node left;
        public Node right;
        public int size;
        public Point2D key;

        public Node(Node left, Node right, int size, Point2D key) {
            this.left = left;
            this.right = right;
            this.size = size;
            this.key = key;
        }
    }

    public KdTree() {
        topNode = null;
    }

    public boolean isEmpty() {
        return topNode == null;
    }

    public int size() {
        return topNode.size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        topNode = insert(topNode, p, true);
    }

    private Node insert(Node search, Point2D newPoint, boolean rotate) {
        if (search == null) return new Node(null, null, 1, newPoint);
        int cmp;
        int cmpAlt;
        if (rotate) {
            cmp = comparePoint(search.key.x(), newPoint.x());
            cmpAlt = comparePoint(search.key.y(), newPoint.y());
        } else {
            cmp = comparePoint(search.key.y(), newPoint.y());
            cmpAlt = comparePoint(search.key.x(), newPoint.x());
        }
        if (cmp > 0) {
            search.left = insert(search.left, newPoint, !rotate);
        } else if (cmp <= 0) {
            if (cmpAlt != 0) {
                search.right = insert(search.right, newPoint, !rotate);
            }
        }

        search.size = 1 + size(search.left) + size(search.right);
        return search;
    }

    private int comparePoint(Double p1, Double p2) {
        return p1.compareTo(p2);
    }

    private int size(Node t) {
        if (t == null) return 0;
        else return t.size;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        boolean result = false;
        boolean rotate = true;
        Node t = topNode;
        int cmp;
        int cmpAlt;
        while (t != null) {
            if (t.key.equals(p)) {
                return true;
            }
            if (rotate) {
                cmp = comparePoint(t.key.x(), p.x());
                cmpAlt = comparePoint(t.key.y(), p.y());
            } else {
                cmp = comparePoint(t.key.x(), p.x());
                cmpAlt = comparePoint(t.key.y(), p.y());
            }
            if (cmp > 0) {
                t = t.left;
                rotate = !rotate;
            } else if (cmp <= 0) {
                if (cmpAlt != 0) {
                    t = t.right;
                    rotate = !rotate;
                }
            }
        }
        return result;
    }


    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> rangeSet = new TreeSet<>();
        return rangeSet;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Point2D nearest = null;
        return nearest;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.7, 0.3));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        StdOut.println(kdTree.contains(new Point2D(0.5,0.2)));
    }
}
