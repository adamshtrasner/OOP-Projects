# Tic-Tac-Toe
A Java implementation of the famous Tic Tac Toe game.

## Table of contents
* [Description](#description)
* [Input](#input)
* [Strategies](#strategies)
* [Examples](#examples)
* [AdditionalFiles](#additionalfiles)

## Description
The game consists of:
* a SIZE x SIZE board, where SIZE can range from 2 to 7.
* In order to win, a player must have a squence of O's or X's (depends on the mark with which the player plays) of size WIN_STREAK.
* There are 2 players. The players can be either humans or AI.
  There are 3 types of AI players: Mr.Whatever, Ms.Clever and Mr.SnartyPamts, which 
  their strategies are explained below.
* The game is over when there is a sequence of O's or X's of size WIN_STREAK, or 
  the board is full (tie).

## Input
The input for the program is of the form:
``` 
  java Tournament [number-of-rounds] [render-target: console/none] [player: human/clever/whatever/snartypamts]
```
The input is given through the command line.

## Strategies

### The Strategy of Mr.SnartyPamts Player
The strategy of Mr.SnartyPamts is just picking the last cell which can make
Ms.Clever win the game, as the first cell to be picked,
that is - the (0, Board.WIN_STREAK-1) cell, and then going downwards until there's
a streak. This strategy will guarantee that Mr.Snartypamts will always win Ms.Clever.
But when playing against Mr.Whatever, Mr.Snartypamts can choose a cell that has already
been used because Mr.Whatever picks a cell at random. In that case,
Mr.Snartypamts will just act as Mr.Whatever, which is why both classes inherit the class
RandomStrategy.

### The Strategy of Mr.Whatever Player
The strategy of Mr.Whatever is implemented by going over all the cells in the board
at each turn, and picking the cell which is still not marked, at random.

## Examples

### 500 Games Tournament Examples
Board Size: 6
Win Streak: 4

(1) === player 1: 30 | player 2: 469 | Draws: 1 ===
    player1: whatever, player2: clever

(2) === player 1: 60 | player 2: 440 | Draws: 0 ===
    player1: whatever, player2: snartypamts

(3) === player 1: 0 | player 2: 500 | Draws: 0 ===
    player1: clever, player2: snartypamts

### 10000 games Tournament Example Between 2 Whatever Players 

=== player 1: 5047 | player 2: 4860 | Draws: 93 ===

## Additional Files
* Mark.java, BoardStatus.java - enums, explained in the files.
* RandomStrategy.java - as stated in the file, the class implements a single method
that describes a random strategy where you choose a cell uniformly between all
empty cells. The reason for constructing such a class is to prevent code repetition
when using the same tactic both with snartypamts and whatever players.
Instead, RandomStrategy is an abstract class that can only be used through inheritance -
both of those classes (SnartypamtsPlayer and WhateverPlayer) inherit from
RandomStrategy and then they can use the random tactic discussed above.
