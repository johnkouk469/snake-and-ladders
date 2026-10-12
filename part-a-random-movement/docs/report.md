# Snake and Ladders, Part A: random movement

12 November 2018 · Data Structures, 2018-2019 · [The assignment](../../README.md#the-assignments)

## Summary

Part A builds the game's data model and a first player that moves by dice only. A board of N × M tiles is generated at random with snakes, ladders and apples; two players roll a die in turn and move; the game ends when a player reaches the last tile. The program is written in Java, one class per file in `src/`, and starts in `Game.main`.

## The elements: Snake, Ladder and Apple

These are small data classes. Each has an empty constructor, a constructor that takes all fields, a copy constructor, and getters and setters.

- **Snake**: `snakeId`, `headId`, `tailId`. A player who lands on the head goes down to the tail.
- **Ladder**: `ladderId`, `upStepId`, `downStepId` and a flag `broken`. A player who lands on the tile `upStepId` (where the ladder starts) is moved to the tile `downStepId` (where it ends, higher up). A ladder can be used once: after a player has climbed it, `broken` is set and it no longer works.
- **Apple**: `appleId`, `appleTileId`, `color` (`"red"` or `"black"`) and `points`. A red apple adds its points to the score of the player who lands on it, a black apple subtracts them. An eaten apple disappears, which is done by setting its points to 0.

## Board

A board has the dimensions `N` (rows) and `M` (columns), a two-dimensional array `tiles` with the tile ids, and the arrays `snakes`, `ladders` and `apples`. There are three constructors: an empty one, one that takes the dimensions and the numbers of snakes, ladders and apples (in this order), and a copy constructor.

### `createBoard()`

The method builds the board at random, in four steps.

1. **Tile ids.** The tiles are numbered 1 to N·M from the bottom row up, as on a printed board. The bottom row runs from left to right, the row above it from right to left, and so on. Row 0 of the array is the top row, which holds the highest ids. On the 20 × 10 board of `Game` the bottom row is 1 to 10 from left to right and the row above it is 20 down to 11, with 11 directly above 10.
2. **Snakes.** Each snake gets its id and a random tail among the tiles of all rows except the top one (tiles 1 to (N−1)·M). The head is then drawn from the tiles above the tail's row, so a snake always leads down to a lower tile. The draw is repeated until no other snake has the same head and the head is not on the last tile, because a snake on the last tile would stop anyone from finishing.
3. **Ladders.** Each ladder gets its id and a random starting tile in the lower rows. The starting tile is drawn again if it is the head of a snake or the start of another ladder. The end tile is drawn from the tiles above the start's row, in the same way as the snake heads.
4. **Apples.** Each apple gets its id and a random tile. If the tile holds a snake head, the tile is drawn again until it does not. The colour is red or black with equal probability, and the points are 5, 10 or 15.

### `createElementBoard()`

The method prints three tables of the same size as `tiles`: one for the snakes, one for the ladders and one for the apples. A table cell holds a code and the element's id, or `___` if the tile is empty. The codes are `SH` and `ST` for the head and tail of a snake, `LD` and `LU` for the lower and the upper end of a ladder, and `AR` and `AB` for a red and a black apple.

## Player

A player has `playerId`, `name`, `score` and the `board` it plays on. The game uses the constructor that takes all four values. There are also getters and setters.

`int[] move(int id, int die)` moves a player from tile `id` by `die` steps and returns an array with five numbers: the new tile, the number of snake bites, the number of ladders used, and the numbers of red and black apples eaten in this move.

1. The target tile is `id + die`. If this reaches or passes the last tile (N·M), the player is placed on the last tile.
2. The apple on the target tile, if any, is eaten.
3. In a loop, the method then checks whether the player stands on the start of an unbroken ladder (the player climbs and the ladder breaks) or on the head of a snake (the player goes down to the tail). After each such step the apple check is repeated for the new tile.
4. A flag records whether a ladder or a snake has moved the player in one pass of the loop. The loop runs again as long as the flag is set, because one move can be a chain: a snake can end on the start of a ladder, and a ladder can end on the head of another snake.

When a player is bitten by a snake, climbs a ladder or eats an apple, a message is printed.

## Game

`Game` has the variable `round`, two constructors and the methods `setRound` and `getRound`. `main` plays one game:

1. A 20 × 10 board with 3 snakes, 3 ladders and 6 apples is created and printed, followed by the three element tables.
2. Two players are created. Both roll a die and the player with the higher number plays first; on a tie the second player goes first.
3. Both players start on the first tile and each makes a first move. From then on both players roll and move in every round until a player has reached the last tile. A round is always played to the end, so both players can reach the last tile in the same round.
4. The winner is the player with the higher score at the end. The program prints the number of rounds and the scores. For example:

```
Well,Player 1 wins after 79 rounds with a score of 15! .Congratulations!
Player 2 finished with -5 points.Next time Player 2...
```

Boards and dice are random and the random generators have no fixed seed, so every run is different.
