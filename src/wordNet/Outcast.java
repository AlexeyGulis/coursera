package wordNet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] listNoun) {
        int minDistance = -1;
        String outcast = "";
        for (int i = 0; i < listNoun.length; i++) {
            int temp = 0;
            for (String p : wordNet.nouns()
            ) {
                temp += wordNet.distance(listNoun[i], p);
            }
            System.out.println(temp + listNoun[i]);
            if (minDistance == -1) {
                minDistance = temp;
                outcast = listNoun[i];
            } else if (minDistance < temp) {
                minDistance = temp;
                outcast = listNoun[i];
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
