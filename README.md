# Project: Scrabble
## Student(s):  Siri Khalsa

## Introduction
This project builds a scrabble game. There are multiple versions explained below.

## Java Version
This application is built with Java 10(oracle not openJDK).
it also uses JavaFX 10 bundled with JDK10

## Usage
________

######GUI Version

________
######Computer Player Test CMD version

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
Where are the .jar files?
### V1
explain about how version 1 works
### V2
explain about how version 2 works etc...

## Docs
What folder is your documentation (diagram and class diagram) in? TODO

## Status
### Implemented Features
- I have a pretty decent looking GUI with a nice color scheme. I probably spent to much time on this.
- I have implemented a scoring calculator, but it is not finished see bugs below to see what is missing.
- I have 3 computers. one will stop checking for moves after it found one with points greater than 18


### Known Issues
WOW where to start! 

1. the computer AI sucks it is super slow when finding the best solution. please see the time results below in the testing and debugging section
2. the score does not calculate right when the game ends. I do not suptract tile values or anything like this
3. In fact now that I think about ti the game probably does not handle the end of the game at all. I have never played a game long enough to finish it. I know I didnt code logic for it.
4. you can not skip your turn. if the human player happens to get stuck with a turn where they cant make a move then this will be an endless loop and the game cannot move forward.
 

## Testing and Debugging
the computer player was tested with a file called "test.txt" you can find it in the /Docs directory.
this. When testing the computer player test please follow this format. the general format is

newline
boardsize
board representation
tile tray representation

make sure when you feed in the file it starts with a NEWLINE and does NOT end with one see test.txt for example

I mention this again BE CAREFUL running the computer version that checks all the blanks. IT WILL TAKE TIME. please reference the following times for how long my test.txt files ran:
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