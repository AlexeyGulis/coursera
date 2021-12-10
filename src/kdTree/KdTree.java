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
        if (p == null || topNode == null) throw new IllegalArgumentException();
        Point2D nearest = topNode.key;
        return nearest(topNode, p, topNode.key);
    }

    private Point2D nearest(Node n, Point2D p, Point2D search) {

        if (n.left == null) {
            if (n.right == null) {
                return search;
            } else {
                Double temp2 = n.right.key.distanceSquaredTo(p);
                Double temp3 = search.distanceSquaredTo(p);
                if (temp2 < temp3) {
                    search = n.right.key;
                    search = nearest(n.right, p, search);
                } else if (temp2 > temp3) {
                    search = nearest(n.right, p, search);
                } else {
                    return n.right.key;
                }
            }
        } else {
            Double temp1 = n.left.key.distanceSquaredTo(p);
            Double temp3 = search.distanceSquaredTo(p);
            if (temp1 < temp3) {
                if (n.right != null) {
                    Double temp2 = n.right.key.distanceSquaredTo(p);
                    if (temp2 < temp1) {
                        search = n.right.key;
                        search = nearest(n.right, p, search);
                    } else if (temp2 > temp1) {
                        search = n.left.key;
                        search = nearest(n.left, p, search);
                    } else {
                        Point2D d1 = nearest(n.left, p, search);
                        Point2D d2 = nearest(n.right, p, search);
                        if (d1.distanceSquaredTo(p) > d2.distanceSquaredTo(p)) {
                            search = d2;
                        } else {
                            search = d1;
                        }
                    }
                } else {
                    search = n.right.key;
                    search = nearest(n.left, p, search);
                }
            } else if (n.right != null) {
                Double temp2 = n.right.key.distanceSquaredTo(p);
                if (temp2 < temp3) {
                    search = n.right.key;
                    search = nearest(n.right, p, search);
                } else if (temp1 > temp3 && temp2 > temp3) {
                    Point2D d1 = nearest(n.left, p, search);
                    Point2D d2 = nearest(n.right, p, search);
                    if (d1.distanceSquaredTo(p) > d2.distanceSquaredTo(p)) {
                        search = d2;
                    } else {
                        search = d1;
                    }
                } else {
                    if (temp1 == temp3) {
                        return n.left.key;
                    } else return n.right.key;
                }
            } else {
                if (temp1 > temp3) {
                    search = nearest(n.left, p, search);
                } else return n.left.key;
            }
        }
        return search;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(0.7, 0.2));
        kdTree.insert(new Point2D(0.7, 0.3));
        kdTree.insert(new Point2D(0.5, 0.4));
        kdTree.insert(new Point2D(0.2, 0.3));
        kdTree.insert(new Point2D(0.4, 0.7));
        kdTree.insert(new Point2D(0.9, 0.6));
        kdTree.nearest(new Point2D(0.9, 0.9));
    }
}
