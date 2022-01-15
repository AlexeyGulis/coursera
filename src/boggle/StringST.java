package boggle;

import edu.princeton.cs.algs4.Queue;

public class StringST<Value> {


    private int n;
    private StringST.Node<Value> root;

    private static class Node<Value> {
        private char c;
        private StringST.Node<Value> left;
        private StringST.Node<Value> mid;
        private StringST.Node<Value> right;
        private Value val;

        private Node() {
        }
    }


    public StringST() {
    }

    public int size() {
        return this.n;
    }

    public boolean containsPrefix(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        } else if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        } else {
            StringST.Node<Value> x = this.get(this.root, key, 0);
            return x == null ? false : true;
        }
    }

    public boolean contains(String key) {
        if (key == null) {
            throw new IllegalArgumentException("argument to contains() is null");
        } else {
            return this.get(key) != null;
        }
    }

    public Value get(String key) {
        if (key == null) {
            throw new IllegalArgumentException("calls get() with null argument");
        } else if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        } else {
            StringST.Node<Value> x = this.get(this.root, key, 0);
            return x == null ? null : x.val;
        }
    }

    private StringST.Node<Value> get(StringST.Node<Value> x, String key, int d) {
        if (x == null) {
            return null;
        } else if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        } else {
            char c = key.charAt(d);
            if (c < x.c) {
                return this.get(x.left, key, d);
            } else if (c > x.c) {
                return this.get(x.right, key, d);
            } else {
                return d < key.length() - 1 ? this.get(x.mid, key, d + 1) : x;
            }
        }
    }

    public void put(String key, Value val) {
        if (key == null) {
            throw new IllegalArgumentException("calls put() with null key");
        } else {
            if (!this.contains(key)) {
                ++this.n;
            } else if (val == null) {
                --this.n;
            }

            this.root = this.put(this.root, key, val, 0);
        }
    }

    private StringST.Node<Value> put(StringST.Node<Value> x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new StringST.Node();
            x.c = c;
        }

        if (c < x.c) {
            x.left = this.put(x.left, key, val, d);
        } else if (c > x.c) {
            x.right = this.put(x.right, key, val, d);
        } else if (d < key.length() - 1) {
            x.mid = this.put(x.mid, key, val, d + 1);
        } else {
            x.val = val;
        }

        return x;
    }

    public String longestPrefixOf(String query) {
        if (query == null) {
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
        } else if (query.length() == 0) {
            return null;
        } else {
            int length = 0;
            StringST.Node<Value> x = this.root;
            int i = 0;

            while (x != null && i < query.length()) {
                char c = query.charAt(i);
                if (c < x.c) {
                    x = x.left;
                } else if (c > x.c) {
                    x = x.right;
                } else {
                    ++i;
                    if (x.val != null) {
                        length = i;
                    }

                    x = x.mid;
                }
            }

            return query.substring(0, length);
        }
    }

    public Iterable<String> keys() {
        Queue<String> queue = new Queue();
        this.collect(this.root, new StringBuilder(), queue);
        return queue;
    }

    public Iterable<String> keysWithPrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
        } else {
            Queue<String> queue = new Queue();
            StringST.Node<Value> x = this.get(this.root, prefix, 0);
            if (x == null) {
                return queue;
            } else {
                if (x.val != null) {
                    queue.enqueue(prefix);
                }

                this.collect(x.mid, new StringBuilder(prefix), queue);
                return queue;
            }
        }
    }

    private void collect(StringST.Node<Value> x, StringBuilder prefix, Queue<String> queue) {
        if (x != null) {
            this.collect(x.left, prefix, queue);
            if (x.val != null) {
                queue.enqueue(prefix.toString() + x.c);
            }

            this.collect(x.mid, prefix.append(x.c), queue);
            prefix.deleteCharAt(prefix.length() - 1);
            this.collect(x.right, prefix, queue);
        }
    }

    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> queue = new Queue();
        this.collect(this.root, new StringBuilder(), 0, pattern, queue);
        return queue;
    }

    private void collect(StringST.Node<Value> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
        if (x != null) {
            char c = pattern.charAt(i);
            if (c == '.' || c < x.c) {
                this.collect(x.left, prefix, i, pattern, queue);
            }

            if (c == '.' || c == x.c) {
                if (i == pattern.length() - 1 && x.val != null) {
                    queue.enqueue(prefix.toString() + x.c);
                }

                if (i < pattern.length() - 1) {
                    this.collect(x.mid, prefix.append(x.c), i + 1, pattern, queue);
                    prefix.deleteCharAt(prefix.length() - 1);
                }
            }

            if (c == '.' || c > x.c) {
                this.collect(x.right, prefix, i, pattern, queue);
            }

        }
    }
}
