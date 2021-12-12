package kdTree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;


public class KdTree {

    private Node topNode;

    private static class Node {
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
        return isEmpty() ? 0 : topNode.size;
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
        } else if (cmp < 0) {
            if (rotate) {
                search.right = insert(search.right, newPoint, !rotate, new RectHV(search.key.x(), rect.ymin(), rect.xmax(), rect.ymax()));
            } else {
                search.right = insert(search.right, newPoint, !rotate, new RectHV(rect.xmin(), search.key.y(), rect.xmax(), rect.ymax()));
            }
        } else if (cmp == 0 && cmpAlt != 0) {
            if (rotate) {
                search.right = insert(search.right, newPoint, !rotate, new RectHV(search.key.x(), rect.ymin(), rect.xmax(), rect.ymax()));
            } else {
                search.right = insert(search.right, newPoint, !rotate, new RectHV(rect.xmin(), search.key.y(), rect.xmax(), rect.ymax()));
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
        StdDraw.line(topNode.key.x(), topNode.rectHV.ymin(), topNode.key.x(), topNode.rectHV.ymax());
        draw(topNode.left, false);
        draw(topNode.right, false);
    }

    private void draw(Node n, boolean t) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.key.draw();
        if (t) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            StdDraw.line(n.key.x(), n.rectHV.ymin(), n.key.x(), n.rectHV.ymax());
            draw(n.left, !t);
            draw(n.right, !t);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(n.rectHV.xmin(), n.key.y(), n.rectHV.xmax(), n.key.y());
            draw(n.left, !t);
            draw(n.right, !t);
        }

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> stack = new Stack<>();
        addStack(topNode, rect, stack);
        return stack;
    }

    private void addStack(Node n, RectHV rect, Stack<Point2D> stack) {
        if (n != null) {
            if (n.rectHV.intersects(rect)) {
                if (rect.contains(n.key)) {
                    stack.push(n.key);
                }
                addStack(n.left, rect, stack);
                addStack(n.right, rect, stack);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null || topNode == null) throw new IllegalArgumentException();
        Point2D result1 = nearest(topNode.left, p, topNode.key);
        Point2D result2 = nearest(topNode.right, p, topNode.key);
        return result1.distanceSquaredTo(p) < result2.distanceSquaredTo(p) ? result1 : result2;
    }

    private Point2D nearest(Node n, Point2D p, Point2D search) {
        if (n == null) return search;
        if (n.rectHV.distanceSquaredTo(p) < search.distanceSquaredTo(p)) {
            if (n.key.distanceSquaredTo(p) < search.distanceSquaredTo(p)) {
                Point2D search1 = nearest(n.left, p, n.key);
                Point2D search2 = nearest(n.right, p, n.key);
                search = search1.distanceSquaredTo(p) < search2.distanceSquaredTo(p) ? search1 : search2;

            } else {
                Point2D search1 = nearest(n.left, p, search);
                Point2D search2 = nearest(n.right, p, search);
                search = search1.distanceSquaredTo(p) < search2.distanceSquaredTo(p) ? search1 : search2;
            }
        }
        return search;
    }

    public static void main(String[] args) {
    }
}
