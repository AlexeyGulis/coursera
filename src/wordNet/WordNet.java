
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.DirectedDFS;

import java.util.TreeSet;
import java.util.HashMap;
import java.util.Iterator;

public class WordNet {

    private String[] synsets;
    private HashMap<String, TreeSet<Integer>> nouns;
    private Digraph G;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        In in1 = new In(synsets);
        this.synsets = in1.readAllLines();
        createListNouns();
        G = new Digraph(this.synsets.length);
        In in2 = new In(hypernyms);
        while (!in2.isEmpty()) {
            String[] temp = in2.readLine().split(",");
            for (int i = 1; i < temp.length; i++) {
                G.addEdge(Integer.parseInt(temp[0]), Integer.parseInt(temp[i]));
            }
        }
        checkCorrectDAG();
        sap = new SAP(G);
    }

    private void checkCorrectDAG() {
        DirectedCycle checkCycle = new DirectedCycle(G);
        if (checkCycle.hasCycle()) {
            throw new IllegalArgumentException("Cycle");
        }
        Iterator<Integer> iterator;
        boolean flag = true;
        for (int i = 0; i < G.V(); i++) {
            iterator = G.adj(i).iterator();
            if (!iterator.hasNext()) {
                DirectedDFS dfs = new DirectedDFS(G.reverse(), i);
                for (int j = 0; j < G.V(); j++) {
                    if (!dfs.marked(j)) {
                        throw new IllegalArgumentException("More than 1 root");
                    }
                }
                flag = false;
            }
        }
        if(flag) throw new IllegalArgumentException("No root");
    }

    private void createListNouns() {
        nouns = new HashMap<>();
        for (int i = 0; i < this.synsets.length; i++) {
            String[] temp = this.synsets[i].split(",");
            this.synsets[i] = temp[1];
            temp = this.synsets[i].split(" ");
            for (int j = 0; j < temp.length; j++) {
                if (nouns.containsKey(temp[j])) {
                    nouns.get(temp[j]).add(i);
                } else {
                    nouns.put(temp[j], new TreeSet<>());
                    nouns.get(temp[j]).add(i);
                }
            }
        }
    }

    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nouns.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        if (nounA.equals(nounB)) {
            return 0;
        }
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return synsets[sap.ancestor(nouns.get(nounA), nouns.get(nounB))];
    }

    public static void main(String[] args) {
        WordNet t = new WordNet("D:\\java\\coursera\\src\\wordNet\\synsets.txt", "D:\\java\\coursera\\src\\wordNet\\hypernyms.txt");
        boolean ts = t.isNoun("worm");
        System.out.println(t.distance("Ambrose", "Saint_Ambrose"));
        System.out.println(t.sap("Ambrose", "Saint_Ambrose"));
        System.out.println(ts);
    }
}
