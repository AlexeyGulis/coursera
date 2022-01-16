package boggle;

import edu.princeton.cs.algs4.Queue;

import java.util.Iterator;

public class TrieSet {
    private static final int R = 26;
    private TrieSet.Node root;
    private int n;

    public TrieSet() {
    }

    public boolean containsPrefix(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        } else if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        } else {
            TrieSet.Node x = this.get(this.root, key, 0);
            return x == null ? false : true;
        }
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        } else {
            TrieSet.Node x = this.get(this.root, key, 0);
            return x == null ? false : x.isString;
        }
    }

    private TrieSet.Node get(TrieSet.Node x, String key, int d) {
        if (x == null) {
            return null;
        } else if (d == key.length()) {
            return x;
        } else {
            char c = key.charAt(d);
            return this.get(x.next[c - 65], key, d + 1);
        }
    }

    public void add(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to add() is null");
        } else {
            this.root = this.add(this.root, key, 0);
        }
    }

    private TrieSet.Node add(TrieSet.Node x, String key, int d) {
        if (x == null) {
            x = new TrieSet.Node();
        }

        if (d == key.length()) {
            if (!x.isString) {
                ++this.n;
            }

            x.isString = true;
        } else {
            char c = key.charAt(d);
            x.next[c - 65] = this.add(x.next[c - 65], key, d + 1);
        }

        return x;
    }

    public int size() {
        return this.n;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Iterator<String> iterator() {
        return this.keysWithPrefix("").iterator();
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<>();
        TrieSet.Node x = this.get(this.root, prefix, 0);
        this.collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(TrieSet.Node x, StringBuilder prefix, Queue<String> results) {
        if (x != null) {
            if (x.isString) {
                results.enqueue(prefix.toString());
            }

            for (char c = 65; c < 91; ++c) {
                prefix.append(c);
                this.collect(x.next[c - 65], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }

        }
    }

    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<>();
        StringBuilder prefix = new StringBuilder();
        this.collect(this.root, prefix, pattern, results);
        return results;
    }

    private void collect(TrieSet.Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x != null) {
            int d = prefix.length();
            if (d == pattern.length() && x.isString) {
                results.enqueue(prefix.toString());
            }

            if (d != pattern.length()) {
                char c = pattern.charAt(d);
                if (c == '.') {
                    for (char ch = 65; ch < 91; ++ch) {
                        prefix.append(ch);
                        this.collect(x.next[ch - 65], prefix, pattern, results);
                        prefix.deleteCharAt(prefix.length() - 1);
                    }
                } else {
                    prefix.append(c);
                    this.collect(x.next[c - 65], prefix, pattern, results);
                    prefix.deleteCharAt(prefix.length() - 1);
                }

            }
        }
    }

    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("argument to longestPrefixOf() is null");
        } else {
            int length = this.longestPrefixOf(this.root, query, 0, -1);
            return length == -1 ? null : query.substring(0, length);
        }
    }

    private int longestPrefixOf(TrieSet.Node x, String query, int d, int length) {
        if (x == null) {
            return length;
        } else {
            if (x.isString) {
                length = d;
            }

            if (d == query.length()) {
                return length;
            } else {
                char c = query.charAt(d);
                return this.longestPrefixOf(x.next[c - 65], query, d + 1, length);
            }
        }
    }

    public void delete(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to delete() is null");
        } else {
            this.root = this.delete(this.root, key, 0);
        }
    }

    private TrieSet.Node delete(TrieSet.Node x, String key, int d) {
        if (x == null) {
            return null;
        } else {
            if (d == key.length()) {
                if (x.isString) {
                    --this.n;
                }

                x.isString = false;
            } else {
                char c = key.charAt(d);
                x.next[c - 65] = this.delete(x.next[c - 65], key, d + 1);
            }

            if (x.isString) {
                return x;
            } else {
                for (int c = 0; c < 26; ++c) {
                    if (x.next[c] != null) {
                        return x;
                    }
                }

                return null;
            }
        }
    }

    private static class Node {
        private TrieSet.Node[] next;
        private boolean isString;

        private Node() {
            this.next = new TrieSet.Node[26];
        }
    }
}
