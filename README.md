# Snake and ladders in Java

A board game simulator in three stages: a player that moves by dice, a heuristic player that rates its six possible moves, and a MinMax player that also looks at the opponent's reply. Data Structures coursework.

## Context

Coursework of the Data Structures course (2018-2019) at the School of Electrical and Computer Engineering of the Aristotle University of Thessaloniki. Group project of two students. The course provided the assignment texts; the three programs and the three reports are the team's own work. The reports were originally written in Greek. The English reports in the `docs` folders were written in 2026 from the code and the Greek originals, and they are longer than the originals.

| Folder | Part | From | Report |
|---|---|---|---|
| [`part-a-random-movement`](part-a-random-movement) | The board, the elements and two players that move by dice | November 2018 | [report](part-a-random-movement/docs/report.md) |
| [`part-b-heuristic-player`](part-b-heuristic-player) | A player that rates its six possible moves | December 2018 | [report](part-b-heuristic-player/docs/report.md) |
| [`part-c-minmax-player`](part-c-minmax-player) | A player that builds a MinMax tree of depth 2 | December 2018 | [report](part-c-minmax-player/docs/report.md) |

The three folders are three separate programs, each handed in on its own. Part A generates its board in its own way, Parts B and C generate theirs differently, and Parts B and C repeat most of each other's classes. All three are written in the same code style. The code was checked and corrected in 2026 before it was published.

## The assignments

The course issued the task texts; they are not included, and what follows is a description in our own words. All three parts are in Java, one class per file, and each part has a `Game` class with `main`.

**Part A, random movement.** Build a board of N × M tiles with snakes, ladders and apples at random places. A snake takes a player from its head down to its tail. A ladder takes a player up, and it breaks after one use. A red apple adds points to the player's score and a black apple subtracts points, and an apple that has been eaten disappears. The head of a snake must be higher on the board than its tail, and no apple may lie on a snake's head. The program prints three tables of where the elements are, using the codes `SH`, `ST`, `LD`, `LU`, `AR` and `AB` followed by the element's id. Two players roll a die in turns and move until one reaches the last tile, and the program prints the rounds, the scores and the winner. The board of `Game` is 20 × 10 with 3 snakes, 3 ladders and 6 apples.

**Part B, heuristic player.** Add a `HeuristicPlayer` that picks its dice. It rates each of the six possible moves with an objective function of the steps gained and the points gained, plays the best one and records every move in a list called `path`. Trying a move must not change the board or the scores, only the real move may. The game sorts the players by their first dice roll, plays a heuristic player against a player that rolls a random die on a 10 × 20 board, and ends when a player reaches the last tile or after a chosen round. The winner is the player who leads on the board and has the best score, with the objective function's weights used to combine the two, and a tie goes to the player who arrived first. The heuristic player prints statistics of its moves.

**Part C, MinMax player.** Replace the heuristic player with a player that builds a tree of its own moves and the opponent's replies, two moves deep, and chooses the dice with the MinMax algorithm. The text of this part is not available any more, so this description follows the report.

## What it does

- Generates a random board and prints where the snakes, ladders and apples are.
- Plays a game turn by turn and prints every move: snake bites, ladders climbed, apples eaten.
- Part B: rates all six dice before each move and plays the best one.
- Part C: builds the tree of 6 own moves and 36 replies, prints the matrix of ratings and plays the dice with the best worst case.
- Prints the number of rounds, the scores, the winner and, for the smart players, statistics of their moves.

How the players choose is explained in the reports. In short: the heuristic player rates a move as 0.6 × steps + 0.4 × points, and the MinMax player multiplies the rating of its own move by the rating of each of the opponent's replies, takes the worst reply for each of its moves, and picks the move whose worst reply is best.

## Getting started

You need a JDK, Java 8 or later. The programs were built and run with Temurin 8u504 and Temurin 21.0.12.1 on Windows 11; the build commands below were tried in PowerShell and in cmd. They were not tested on Linux or macOS.

Pick a part and run, for example for Part C:

```
cd part-c-minmax-player
mkdir out
javac -encoding UTF-8 -d out src/*.java
java -cp out Game
```

`mkdir out` is needed with Java 8, because its `javac` does not create the output folder. In cmd, write `src\*.java`. The programs read no input and take a fraction of a second to run. The boards and the dice are random and there is no seed, so every run is different. Part A prints about 100 lines; Parts B and C print between 250 and 1000 lines, because they print every trial move.

## Results

In 200 games of Part B on Java 8, the heuristic player won 196 against the player that rolls a random die. In 200 games of Part C, the MinMax player won 193. The numbers change from run to run. Part A has no strategy: both players move by dice.

## Project structure

```
part-a-random-movement/src/    Game, Board, Player, Snake, Ladder, Apple
part-a-random-movement/docs/   report.md
part-b-heuristic-player/src/   Game, Board, Player, HeuristicPlayer, Snake, Ladder, Apple
part-b-heuristic-player/docs/  report.md
part-c-minmax-player/src/      Game, Board, Player, MinMaxPlayer, Node, Snake, Ladder, Apple
part-c-minmax-player/docs/     report.md
```

## Limitations

- The strategies are simple. The heuristic player looks at one move. The MinMax player looks two moves ahead and models the opponent only through the product of the two ratings, without taking into account what its own move leaves on the board for the opponent.
- There are no automated tests. The programs were checked by running them many times and by small checks of single methods.
- In Part A the ladder's `upStepId` is the tile where it starts, in Parts B and C `downStepId` is. The reports say so for each part.
- Only Java 8 and Java 21 on Windows were tried.

## Team

- Ioannis Koukouras ([@johnkouk469](https://github.com/johnkouk469))
- Nick_Ladias ([@ladiasnk](https://github.com/ladiasnk))

## Credits and licence

The assignment texts belong to the course and are not part of this repository. The helper `sortByValue` in the `Game` class of Parts B and C follows a widely published way of sorting a map by its values (the entries in a `LinkedList`, `Collections.sort` with a `Comparator`, the result in a `LinkedHashMap`); where the team first saw it is not recorded.

MIT for the code and the reports, see [LICENSE](LICENSE).
