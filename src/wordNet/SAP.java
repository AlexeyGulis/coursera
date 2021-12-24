package wordNet;

import edu.princeton.cs.algs4.*;

public class SAP {

    private Digraph cDigraph;

    public SAP(Digraph G) {
        if(G == null){
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
                }else if(dist >= temp){
                    dist = temp;
                }
                flag = true;
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
                }else if(dist >= temp){
                    dist = temp;
                    minI = i;
                }
                flag = true;
            }
        }
        return flag ? minI : -1;
    }


    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null || w == null){
            throw new IllegalArgumentException();
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
                }else if(dist >= temp){
                    dist = temp;
                }
                flag = true;
            }
        }
        return flag ? dist : -1;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if(v == null || w == null){
            throw new IllegalArgumentException();
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
                }else if(dist >= temp){
                    dist = temp;
                    minI = i;
                }
                flag = true;
            }
        }
        return flag ? minI : -1;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
