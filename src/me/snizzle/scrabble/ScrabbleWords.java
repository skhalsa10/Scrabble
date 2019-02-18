package me.snizzle.scrabble;

import me.snizzle.datastructure.Trie;

import java.io.*;

/**
 * I will begin by using the Trie datastructure to build the word list.
 *
 * this essentially wraps the Trie data structure (or can wrap another type if available)
 * and loading it with words.
 */
public class ScrabbleWords {
    private Trie words;
    private BufferedReader fileReader;

    /**
     * instantiates a new object using the filename as the file of words to load. the
     * words must be words delineated with a new line
     * @param fileName file of words seperated by new lines.
     */
    public ScrabbleWords(String fileName) {
        loadWords(fileName);
    }

    /**
     * this expects a file with words delineated with newlines.
     * @param fileName
     */
    private void loadWords(String fileName) {
        try{
            String line;
            fileReader = new BufferedReader(new FileReader(fileName));
            while((line = fileReader.readLine()) != null){
                words.insert(line);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * wrapper for the data structures version
     */
    public boolean verify(String s){
        return words.contains(s);
    }

    /**
     * wrapper for the data structures version
     * @param prefix
     * @return
     */
    public boolean isPrefix(String prefix){
        return words.isPrefix(prefix);
    }
}
