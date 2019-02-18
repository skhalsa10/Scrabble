package me.snizzle.datastructure;

/**
 * This is a simple Trie datastructure I used the following websites as a guide to building the structure
 *
 * https://www.baeldung.com/trie-java
 * https://www.toptal.com/java/the-trie-a-neglected-data-structure
 * https://www.hackerearth.com/practice/data-structures/advanced-data-structures/trie-keyword-tree/tutorial/
 * https://medium.com/basecs/trying-to-understand-tries-3ec6bede0014
 * https://www.geeksforgeeks.org/trie-insert-and-search/
 */
public class Trie {
    private TrieNode root;

    public Trie(){
        root = new TrieNode();
    }

    /**
     * Insert a string into the data structure I can make this recursive
     * @param s
     */
    public void insert(String s){
        TrieNode current = root;
        s.toLowerCase();
        for(int i = 0; i < s.length(); i++){
            //this calculates an index where a is 0 and z is 25
            int c = s.charAt(i)-'a';
            //make sure appropriate child is not null
            if(current.children[c] == null){
                current.children[c] = new TrieNode();
            }
            current = current.children[c];
        }
        //TODO we could add the full string to the node if we want
        current.setIsWord(true);

    }

    /**
     * checks to see if string is contained in the Trie
     * @param s string to check
     * @return true or false depending on the strings existence in the structure
     */
    public boolean contains(String s){
        TrieNode current = root;
        s.toLowerCase();
        for(int i = 0; i < s.length(); i++){
            int c = s.charAt(i)-'a';
            //if any of the nodes in the path are null the string is not in the Trie
            if(current.children[c] == null){
                return false;
            }
            current = current.children[c];

        }
        //this could be a node in a bigger word so we must return the boolean value as source of truth.
        return current.isWord;
    }

    /**
     * returns true if the given string is a prefix of a word added to the Trie.
     *
     * is and a prefix ? and + the empty string? hmmm
     *
     * I am only going to say a prefix returns true if at least one child is not null
     *
     * @param prefix prefix to check against
     * @return true or false accordingly
     */
    public boolean isPrefix(String prefix){
        TrieNode current = root;
        prefix.toLowerCase();
        for(int i = 0; i < prefix.length(); i++){
            int c = prefix.charAt(i)-'a';
            //if any of the nodes in the path are null the prefix doesnt exist
            if(current.children[c] == null){
                return false;
            }
            current = current.children[c];
        }
        //this could be a node in a bigger word so we must return the boolean value as source of truth.
        for (TrieNode n: current.children) {
            if(n != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * this inner class defines a Trie node that is used in the Trie datastructure
     */
    class TrieNode {
        private TrieNode[] children;
        private boolean isWord;

        public TrieNode(){
            isWord = false;
            children = new TrieNode[26];
            for(int i = 0; i <26; i++){
                children[i] = null;
            }
        }

        public void setIsWord(boolean b) {
            this.isWord = b;
        }
    }
}
