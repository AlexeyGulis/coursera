package wordNet;

import edu.princeton.cs.algs4.*;

import java.util.Set;
import java.util.TreeSet;

public class WordNet {

    private String[] synsets;
    private Set<String> nouns;
    private Digraph G;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        nouns = new TreeSet<>();
        In in1 = new In(synsets);
        this.synsets = in1.readAllLines();
        for (int i = 0; i < this.synsets.length; i++) {
            String[] temp = this.synsets[i].split(",");
            this.synsets[i] = temp[1];
            temp = this.synsets[i].split(" ");
            for (int j = 0; j < temp.length; j++) {
                nouns.add(temp[j]);
            }
        }
        G = new Digraph(this.synsets.length);
        In in2 = new In(hypernyms);
        while (!in2.isEmpty()) {
            String[] temp = in2.readLine().split(",");
            for (int i = 1; i < temp.length; i++) {
                G.addEdge(Integer.parseInt(temp[0]), Integer.parseInt(temp[i]));
            }
        }
        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        return nouns;
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nouns.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Set<Integer> listNounA = new TreeSet<>();
        Set<Integer> listNounB = new TreeSet<>();
        for (int i = 0; i < synsets.length; i++) {
            String[] temp = synsets[i].split(" ");
            for (String t : temp
            ) {
                if (t.equals(nounA)) {
                    listNounA.add(i);
                }
                if (t.equals(nounB)) {
                    listNounB.add(i);
                }
            }
        }
        return sap.length(listNounA, listNounB);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        Set<Integer> listNounA = new TreeSet<>();
        Set<Integer> listNounB = new TreeSet<>();
        for (int i = 0; i < synsets.length; i++) {
            String[] temp = synsets[i].split(" ");
            for (String t : temp
            ) {
                if (t.equals(nounA)) {
                    listNounA.add(i);
                }
                if (t.equals(nounB)) {
                    listNounB.add(i);
                }
            }
        }
        return synsets[sap.ancestor(listNounA,listNounB)];
    }

    public static void main(String[] args) {
        WordNet t = new WordNet("E:\\JavaFolder\\coursera\\src\\wordNet\\synsets.txt", "E:\\JavaFolder\\coursera\\src\\wordNet\\hypernyms.txt");
        boolean ts = t.isNoun("worm");
        System.out.println(t.distance("municipality", "region"));
        System.out.println(t.sap("municipality", "region"));
        System.out.println(ts);
    }
}
