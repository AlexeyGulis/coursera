package kdTree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class KdTree {

    private Node topNode;

    private class Node {
        public RectHV rectHV;
        public Node left;
        public Node right;
        public int size;
        public Point2D key;

        public Node(Node left, Node right, int size, Point2D key, RectHV rectHV) {
            this.left = left;
            this.right = right;
            this.size = size;
            this.key = key;
            this.rectHV = rectHV;
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
        topNode = insert(topNode, p, true, new RectHV(0, 0, 1, 1));
    }

    private Node insert(Node search, Point2D newPoint, boolean rotate, RectHV rect) {
        if (search == null) return new Node(null, null, 1, newPoint, rect);
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
            if (rotate) {
                search.left = insert(search.left, newPoint, !rotate, new RectHV(rect.xmin(), rect.ymin(), search.key.x(), rect.ymax()));
            } else {
                search.left = insert(search.left, newPoint, !rotate, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), search.key.y()));
            }
        } else if (cmp <= 0) {
            if (cmpAlt != 0) {
                if (rotate) {
                    search.right = insert(search.right, newPoint, !rotate, new RectHV(search.key.x(), rect.ymin(), rect.xmax(), rect.ymax()));
                } else {
                    search.right = insert(search.right, newPoint, !rotate, new RectHV(rect.xmin(), search.key.y(), rect.xmax(), rect.ymax()));
                }

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
        if (topNode == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        topNode.key.draw();
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius();
        StdDraw.line(topNode.key.x(), 0, topNode.key.x(), 1);
        draw(topNode.left, 0, topNode.key.x(), 0, 1, false);
        draw(topNode.right, topNode.key.x(), 1, 0, 1, false);
    }

    private void draw(Node n, double xmin, double xmax, double ymin, double ymax, boolean t) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.key.draw();
        if (t) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(n.key.x(), ymin, n.key.x(), ymax);
            draw(n.left, xmin, n.key.x(), ymin, ymax, !t);
            draw(n.right, n.key.x(), xmax, ymin, ymax, !t);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(xmin, n.key.y(), xmax, n.key.y());
            draw(n.left, xmin, xmax, ymin, n.key.y(), !t);
            draw(n.right, xmin, xmax, n.key.y(), ymax, !t);
        }

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> rangeSet = new TreeSet<>();
        return rangeSet;
    }

    public Point2D nearest(Point2D p) {
        if (p == null || topNode == null) throw new IllegalArgumentException();
        Point2D nearest = topNode.key;
        return nearest(topNode, p, topNode.key);
    }

    private Point2D nearest(Node n, Point2D p, Point2D search) {
        return null;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.7, 0.5));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        StdDraw.clear();
        kdTree.draw();
        StdDraw.show();
    }
}
