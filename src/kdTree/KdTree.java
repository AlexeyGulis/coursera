package kdTree;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node topNode;

    private static class Node {
        private final RectHV rectHV;
        private Node left;
        private Node right;
        private int size;
        private final Point2D key;

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
        topNode = insert(topNode, p.x(), p.y(), true, 0, 0, 1, 1);
    }

    private Node insert(Node search, double xnew, double ynew, boolean rotate, double xmin, double ymin, double xmax, double ymax) {
        if (search == null) return new Node(null, null, 1, new Point2D(xnew, ynew), new RectHV(xmin, ymin, xmax, ymax));
        double x = search.key.x();
        double y = search.key.y();
        if (rotate) {
            if (xnew < x) {
                search.left = insert(search.left, xnew, ynew, false, xmin, ymin, x, ymax);
            } else if (xnew > x) {
                search.right = insert(search.right, xnew, ynew, false, x, ymin, xmax, ymax);
            } else if (x == xnew && y != ynew) {
                search.right = insert(search.right, xnew, ynew, false, x, ymin, xmax, ymax);
            }
        } else {
            if (ynew < y) {
                search.left = insert(search.left, xnew, ynew, true, xmin, ymin, xmax, y);
            } else if (ynew > y) {
                search.right = insert(search.right, xnew, ynew, true, xmin, y, xmax, ymax);
            } else if (y == ynew && x != xnew) {
                search.right = insert(search.right, xnew, ynew, true, xmin, y, xmax, ymax);
            }
        }
        search.size = 1 + size(search.left) + size(search.right);
        return search;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double x = p.x();
        double y = p.y();
        boolean rotate = true;
        Node t = topNode;
        while (t != null) {
            double tx = t.key.x();
            double ty = t.key.y();
            if (tx == x && ty == y) {
                return true;
            }
            if (rotate) {
                if (x < tx) {
                    t = t.left;
                } else {
                    t = t.right;
                }
                rotate = false;
            } else {
                if (y < ty) {
                    t = t.left;
                } else {
                    t = t.right;
                }
                rotate = true;
            }
        }
        return false;
    }

    private int size(Node t) {
        if (t == null) return 0;
        else return t.size;
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
            draw(n.left, false);
            draw(n.right, false);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            StdDraw.line(n.rectHV.xmin(), n.key.y(), n.rectHV.xmax(), n.key.y());
            draw(n.left, true);
            draw(n.right, true);
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
        if (p == null) throw new IllegalArgumentException();
        if (topNode == null) return null;
        return nearest(topNode, p, topNode.key, true);
    }

    private Point2D nearest(Node n, Point2D p, Point2D search, boolean rotate) {
        if (n == null) return search;
        double searchDistance = search.distanceSquaredTo(p);
        if (n.rectHV.distanceSquaredTo(p) < searchDistance) {
            if (n.key.distanceSquaredTo(p) < searchDistance) {
                search = nearestRotate(n, p, search, rotate, n.key);
            } else {
                search = nearestRotate(n, p, search, rotate, search);
            }
        }
        return search;
    }

    private Point2D nearestRotate(Node n, Point2D p, Point2D search, boolean rotate, Point2D choose) {
        if (rotate) {
            if (p.x() < n.key.x()) {
                search = nearest(n.left, p, choose, false);
                if ((p.x() - n.key.x()) * (p.x() - n.key.x()) < search.distanceSquaredTo(p)) {
                    search = nearest(n.right, p, search, false);
                }
            } else {
                search = nearest(n.right, p, choose, false);
                if ((p.x() - n.key.x()) * (p.x() - n.key.x()) < search.distanceSquaredTo(p)) {
                    search = nearest(n.left, p, search, false);
                }
            }
        } else {
            if (p.y() < n.key.y()) {
                search = nearest(n.left, p, choose, true);
                if ((p.y() - n.key.y()) * (p.y() - n.key.y()) < search.distanceSquaredTo(p)) {
                    search = nearest(n.right, p, search, true);
                }
            } else {
                search = nearest(n.right, p, choose, true);
                if ((p.y() - n.key.y()) * (p.y() - n.key.y()) < search.distanceSquaredTo(p)) {
                    search = nearest(n.left, p, search, true);
                }
            }
        }
        return search;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        StdDraw.setPenRadius(0.03);
        StdDraw.setPenColor(StdDraw.RED);
        Point2D p = new Point2D(0.25, 1.0);
        p.draw();
        StdDraw.show();
        kdtree.nearest(p);
    }
}
