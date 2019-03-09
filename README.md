# Project: Scrabble
## Student(s):  Siri Khalsa

## Introduction
This project builds a scrabble game. There are multiple versions explained below.

## Java Version
This application is built with Java 10(oracle not openJDK).
it also uses JavaFX 10 bundled with JDK10

## Usage
________

SCRABBLE GUI:


this program needs the resources folder to work so if you copy the Scrabble_GUI_skhalsa10.jar to a folder called "test", "test" should contain at least the following:

test  
 |_resources/  
 |_Scrabble_GUI_skhalsa10.jar
 
Unfortuantely I ran out of time and was never able to set up the GUI to take in a dictionary argument. I ran out of time and my gui design complicated things

run the game as follows:

java -jar Scrabble_GUI_skhalsa10.jar

HAVE FUN!

________
ComputerPlayerTest Usage:

java -jar Scrabble-Comp-Test-Skhalsa10.jar <path/to/file/dictionary/file.txt> <path/to/test/file.txt>
or
java -jar Scrabble-Comp-Test-Skhalsa10.jar <path/to/file/dictionary/file.txt> <path/to/test/file.txt> <computermode>
computermode can be one of the following: 20, blank, or noblank

example that runs the defualts: java -jar Scrabble-Comp-Test-Skhalsa10.jar ./resources/sowpods.txt ./resources/test.txt 20

The default values require the resources folder full of files to be in the same directory
where the jar file is. So if you copy the jar file somewhere you should also copy the resources folder

PLEASE LOOK AT THE TEST FILE TO UNDERSTAND WHAT I AM EXPECTING TO READ IN THE GENERAL FORMAT IS:

newline
boardsize
board representation
tile tray representation

make sure when you feed in the file it starts with a NEWLINE and does NOT end with one see test.txt for example

I mention this again BE CAREFUL running the computer version that checks all the blanks. IT WILL TAKE TIME.

UPDATE- I have cahnged the logic to the computer AI. It is still slow with blanks! but I have improved the speed dramatically
the test that took 5 minutes now take about a minute! way better. it will still be incredibly slow and painful to try but
at least not nearly as bad :) please see updated times under testing and debugging section

*******there is a possibility for the test to crash on a case where tiles have been played on the edge********** 
I dont have time to fix so hopefully it doesnt screw up any tests.

________


## Project Assumptions
- Java10
- FOR CHECKING THE COMPUTER PLEASE MAKE A FILE THAT HAS THE SAME FORMAT AS test.txt FILE LOCATED IN THE /Docs DIRECTORY
- default dictionary file is sowpods.txt
- the tile tray CANNOT be bigger that 7
- I have not programmed a way to trade tiles for new ones on a turn.
- I have not built a way to skip a turn
- you have to want to lose as the computer is rediculous 
- the GUI version uses the computer that stops checking for moves after one greater than 18 is found.

## Versions 

Both of the .jars are located on the root of the project

### V1

ComputerPlayerTest Usage:

java -jar Scrabble-Comp-Test-Skhalsa10.jar <path/to/file/dictionary/file.txt> <path/to/test/file.txt>
or
java -jar Scrabble-Comp-Test-Skhalsa10.jar <path/to/file/dictionary/file.txt> <path/to/test/file.txt> <computermode>
computermode can be one of the following: 20, blank, or noblank

example that runs the defualts: java -jar Scrabble-Comp-Test-Skhalsa10.jar ./resources/sowpods.txt ./resources/test.txt 20

The default values require the resources folder full of files to be in the same directory
where the jar file is. So if you copy the jar file somewhere you should also copy the resources folder

PLEASE LOOK AT THE TEST FILE TO UNDERSTAND WHAT I AM EXPECTING TO READ IN THE GENERAL FORMAT IS:

newline
boardsize
board representation
tile tray representation

make sure when you feed in the file it starts with a NEWLINE and does NOT end with one see test.txt for example

I mention this again BE CAREFUL running the computer version that checks all the blanks. IT WILL TAKE TIME.

UPDATE- I have changed the logic to the computer AI. It is still slow with blanks! but I have improved the speed dramatically
the test that took 5 minutes now take about a minute! way better. it will still be incredibly slow and painful to try but
at least not nearly as bad :) please see updated times under testing and debugging section


*******there is a possibility for the test to crash on a case where tiles have been played on the edge********** 
I dont have time to fix so hopefully it doesnt screw up any tests.

### V2

SCRABBLE GUI:


this program needs the resources folder to work so if you copy the Scrabble_GUI_skhalsa10.jar to a folder called "test", "test" should contain at least the following:

test  
 |_resources/  
 |_Scrabble_GUI_skhalsa10.jar
 
Unfortuantely I ran out of time and was never able to set up the GUI to take in a dictionary argument. I ran out of time and my gui design complicated things

run the game as follows:

java -jar Scrabble_GUI_skhalsa10.jar

## Docs
please find object document in the /Doc folder this should be included in all jars and on the git repository. It also includes a copy of the test.txt file that canbe used as a reference.


## Status
### Implemented Features
- I have a pretty decent looking GUI with a nice color scheme. I probably spent to much time on this.
- I have implemented a scoring calculator, but it is not finished see bugs below to see what is missing.
- I have 3 computers. one will stop checking for moves after it found one with points greater than 18 this is the computer the GUI uses.


### Known Issues
WOW where to start! 

1. the computer AI sucks it is super slow when finding the best solution. please see the time results below in the testing and debugging section
2. the score does not calculate right when the game ends. I do not suptract tile values or anything like this
3. In fact now that I think about ti the game probably does not handle the end of the game at all. I have never played a game long enough to finish it. I know I didnt code logic for it.
4. you can not skip your turn. if the human player happens to get stuck with a turn where they cant make a move then this will be an endless loop and the game cannot move forward.
5. Found a bug in the gui (and probably in the computer test as well) where the computer program crashes whe a tile is playe don the edge and the computer looks for a move around this location. I dont have enough time to fix this unfortuantely sorry. 

## Testing and Debugging
the computer player was tested with a file called "test.txt" you can find it in the /Docs directory.
this. When testing the computer player test please follow this format. the general format is

newline
boardsize
board representation
tile tray representation

make sure when you feed in the file it starts with a NEWLINE and does NOT end with one see test.txt for example

I mention this again BE CAREFUL running the computer version that checks all the blanks. IT WILL TAKE TIME. please reference the following times for how long my test.txt files ran:

UPDATE!!!!!- I have changed the logic to the computer AI. It is still slow with blanks! but I have improved the speed dramatically
the test that took 5 minutes now take about a minute! way better. it will still be incredibly slow and painful to try but
at least not nearly as bad :) please see updated times below and run at your own risk


__________________________
test.txt - version that stops checking after a score greater than 18 is found and plugs in 'e' for blanks
Going down the list of tests in the test.txt file the times:
1. 1469ms
2. 1480ms
3. 10505ms
4. 606ms
______________________________________________
test.txt - version that plugs in 'e' for blanks but finds the best solution
1. 14568ms
2. 2900ms
3. 18395ms
4. 3964ms
_____________________________________________
test.txt - no restrictions (besides maybe a poorly designed algorithm? or poor design? haha )

1. 291544ms this is 4.86 minutes! this is INSANE DO YOU REALLY WANT TO RUN THIS? PROBABLY NOT RUN AT YOUR OWN RISK
2. 2690ms
3. 18526ms
4. 100337ms

______________________________________________
test.txt no restrictions with improved algorithm! HUGE SPEED INCREASE RESULTS.

1. 77503ms 1 minute and 17 seconds! much better
2. 918ms this is better then my first case!
3. 5588ms this is better now than the first case! :)
4. 30961ms this is a 70% speed increase than before. 