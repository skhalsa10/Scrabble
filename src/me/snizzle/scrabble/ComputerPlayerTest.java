package me.snizzle.scrabble;

import java.io.*;


public class ComputerPlayerTest {

    public static void main(String args[]){
        String dictionary = "./resources/sowpods.txt";
        String testFile = "./resources/test.txt";
        String compS = "20";

        if(args.length==2){
            dictionary = args[0];
            testFile = args[1];

        }
        else if(args.length==3){
            dictionary = args[0];
            testFile = args[1];
            if(args[2].equals("20") || args[2].equals("blank") || args[2].equals("noblank")){
                compS = args[2];
            }
            else{
                printUsage();
                return;
            }

        }
        else if(args.length==0){
            System.out.println("running default values");
        }
        else{
            printUsage();
            return;
        }

        //compS = "blank";


        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(new FileReader(testFile));
            String s = fileReader.readLine();
            ScrabblePlayer comp = null;
            ScrabbleRules rules = new ScrabbleRules(dictionary);
            ScrabbleTileBag tileBag = new ScrabbleTileBag();
            while(s != null) {
                ScrabbleBoard board = new ScrabbleBoard(fileReader, rules);
                String trayRep = fileReader.readLine();
                switch (compS) {
                    case "20": {
                        comp = new ScrabbleCompPlayer20(board, tileBag, trayRep, rules);
                        break;
                    }
                    case "blank":{
                        comp = new ScrabbleCompPlayerBlank(board, tileBag, trayRep, rules);
                        break;
                    }
                    case "noblank":{
                        comp = new ScrabbleCompPlayerNoBlank(board, tileBag, trayRep, rules);
                        break;
                    }
                }
                comp.takeTurn();
                System.out.println("SCORE: " + comp.getScore());
                board.printBoard();
                s = fileReader.readLine();
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            printUsage();
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private static void printUsage() {
        System.out.println("ComputerPlayerTest Usage:\n\n" +
                "java -jar Scrabble-Comp-Test-Skhalsa10.jar <path/to/file/dictionary/file.txt> <path/to/test/file.txt>\n" +
                "or\n" +
                "java -jar Scrabble-Comp-Test-Skhalsa10.jar <path/to/file/dictionary/file.txt> <path/to/test/file.txt> <computermode>\n" +
                "computermode can be one of the following: 20, blank, or noblank\n\n" +
                "example that runs the defualts: java -jar Scrabble-Comp-Test-Skhalsa10.jar ./resources/sowpods.txt ./resources/test.txt 20\n\n" +
                "The default values require the resources folder full of files to be in the same directory\n where the jar file is. So if you copy the jar file somewhere you should also copy the resources folder\n");
    }


}
