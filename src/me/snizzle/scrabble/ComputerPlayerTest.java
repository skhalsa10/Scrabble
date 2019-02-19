package me.snizzle.scrabble;

import me.snizzle.datastructure.Trie;

//
public class ComputerPlayerTest {

    public static void main(String args[]){
        Trie testTrie = new Trie();

        testTrie.insert("peace");
        testTrie.insert("peacers");
        testTrie.insert("elbow");
        testTrie.insert("no");
        testTrie.insert("nachos");

        System.out.println(testTrie.contains("apple"));
    }
}
