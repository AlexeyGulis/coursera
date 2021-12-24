package wordNet;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordNet {

    private String[] synsets;
    private ArrayList<String> nouns;
    private Digraph G;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if(synsets == null || hypernyms == null){
            throw new IllegalArgumentException();
        }
        In in1 = new In(synsets);
        while (!in1.isEmpty()){
            String[] temp = in1.readLine().split(",");
            nouns.add(Integer.parseInt(temp[0]),temp[1]);
        }
        this.synsets = in1.readAllLines();
        Digraph G = new Digraph(this.synsets.length);
        In in2 = new In(hypernyms);
        while (!in2.isEmpty()){
            String [] temp = in2.readLine().split(",");
            for (int i = 1; i < temp.length; i++) {
                G.addEdge(Integer.parseInt(temp[0]),Integer.parseInt(temp[i]));
            }
        }
        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        return nouns;
    }

    private class ArrayIterator<T> implements Iterator<T> {
        private int index = 0;
        private int size = 0;
        T[] items;

        public ArrayIterator(int size, final T[] items) {
            this.size = size;
            this.items = items;
        }

        public boolean hasNext() {
            if (size == 0) {
                System.out.println("Элементы не найдены");
                return false;
            }
            return index < size;
        }

        @Override
        public T next() {
            return (T) items[index++];
        }
    }

    public boolean isNoun(String word) {
        if(word == null){
            throw new IllegalArgumentException();
        }
        Pattern pattern = Pattern.compile("//s" + word + "//s");
        for (String p : nouns
             ) {
            Matcher matcher = pattern.matcher(p);
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }

    public int distance(String nounA, String nounB) {
        if(nounA == null || nounB == null || isNoun(nounA) || isNoun(nounB)){
            throw new IllegalArgumentException();
        }
        return 0;
    }

    public String sap(String nounA, String nounB) {
        return null;
    }

    public static void main(String[] args) {

    }
}
