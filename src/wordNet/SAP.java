package wordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {

    private Digraph cDigraph;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException();
        }
        cDigraph = new Digraph(G);
    }

    public int length(int v, int w) {
        BreadthFirstDirectedPaths bfd1 = new BreadthFirstDirectedPaths(cDigraph, v);
        BreadthFirstDirectedPaths bfd2 = new BreadthFirstDirectedPaths(cDigraph, w);
        int dist = 0;
        boolean flag = false;
        for (int i = 0; i < cDigraph.V(); i++) {
            if (bfd1.hasPathTo(i) && bfd2.hasPathTo(i)) {
                int temp = bfd1.distTo(i) + bfd2.distTo(i);
                if (!flag) {
                    dist = temp;
                    flag = true;
                } else if (dist > temp) {
                    dist = temp;
                }
            }
        }
        return flag ? dist : -1;
    }

    public int ancestor(int v, int w) {
        BreadthFirstDirectedPaths bfd1 = new BreadthFirstDirectedPaths(cDigraph, v);
        BreadthFirstDirectedPaths bfd2 = new BreadthFirstDirectedPaths(cDigraph, w);
        int dist = 0;
        int minI = 0;
        boolean flag = false;
        for (int i = 0; i < cDigraph.V(); i++) {
            if (bfd1.hasPathTo(i) && bfd2.hasPathTo(i)) {
                int temp = bfd1.distTo(i) + bfd2.distTo(i);
                if (!flag) {
                    dist = temp;
                    minI = i;
                    flag = true;
                } else if (dist > temp) {
                    dist = temp;
                    minI = i;
                }
            }
        }
        return flag ? minI : -1;
    }


    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        BreadthFirstDirectedPaths bfd1 = new BreadthFirstDirectedPaths(cDigraph, v);
        BreadthFirstDirectedPaths bfd2 = new BreadthFirstDirectedPaths(cDigraph, w);
        int dist = 0;
        boolean flag = false;
        for (int i = 0; i < cDigraph.V(); i++) {
            if (bfd1.hasPathTo(i) && bfd2.hasPathTo(i)) {
                int temp = bfd1.distTo(i) + bfd2.distTo(i);
                if (!flag) {
                    dist = temp;
                    flag = true;
                } else if (dist > temp) {
                    dist = temp;
                }
            }
        }
        return flag ? dist : -1;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        if (!v.iterator().hasNext() || !w.iterator().hasNext()) {
            return -1;
        }
        BreadthFirstDirectedPaths bfd1 = new BreadthFirstDirectedPaths(cDigraph, v);
        BreadthFirstDirectedPaths bfd2 = new BreadthFirstDirectedPaths(cDigraph, w);
        int dist = 0;
        int minI = 0;
        boolean flag = false;
        for (int i = 0; i < cDigraph.V(); i++) {
            if (bfd1.hasPathTo(i) && bfd2.hasPathTo(i)) {
                int temp = bfd1.distTo(i) + bfd2.distTo(i);
                if (!flag) {
                    dist = temp;
                    minI = i;
                    flag = true;
                } else if (dist > temp) {
                    dist = temp;
                    minI = i;
                }
            }
        }
        return flag ? minI : -1;
    }
}
