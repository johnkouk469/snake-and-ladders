# Snake and Ladders, Part C: MinMax player

Data Structures, 2018-2019 · [The assignment](../../README.md#the-assignments)

## Summary

Part C replaces the heuristic player of Part B with a `MinMaxPlayer`. Instead of rating each of its six moves on its own, the player builds a tree of depth 2 that simulates its own move and the opponent's reply. Every leaf of the tree holds the rating of that pair of moves, and the MinMax algorithm picks the dice whose worst-case reply is the best. The program is in `src/` and starts in `Game.main`. The other classes (`Snake`, `Ladder`, `Apple`, `Board`) are the ones of Part B.

## `Node`

A `Node` is one node of the tree. Its variables are `parent`, `children` (an `ArrayList<Node>`), `nodeDepth`, `nodeMove` (an array with the position and the dice of the move), `nodeBoard` and `nodeEvaluation`, with getters and setters.

There are four constructors: an empty one, one with all the variables, a copy constructor, and one with all variables except `children`, which starts as an empty list. The last one was added for convenience: `createMySubtree` and `createOpponentSubtree` create nodes without knowing their children yet, and the children are added to `children` afterwards.

## `MinMaxPlayer`

The class extends `Player` and has the same variable `path` and the same three constructors as `HeuristicPlayer` of Part B. `Player.move` gets one more parameter in this part, the board `b` to move on, and so does `getNextMove`.

### `double evaluate(int currentPos, int dice, Board board)`

The method rates one move with the objective function of Part B:

```java
double evaluation = (double) steps * 0.6 + (double) gainPoints * 0.4;
```

It plays the move with `move`, computes `steps` and `gainPoints`, and then restores the player's score and the board. The ladders' `broken` flags and the points of the apples are saved before the move and put back after it, so the trial move leaves the board unchanged. All the nodes of the tree refer to the game board itself, which is why this restoring matters.

### Building the tree

- `createMySubtree(parent, depth, currentPos, opponentCurrentPos)` creates, for each of the six dice values, a child of the root with the rating of the player's own move. For each of these children it calls `createOpponentSubtree`.
- `createOpponentSubtree(parent, depth, currentPos, opponentCurrentPos, parentEval)` creates, for each of the six dice values, a child with the rating of the opponent's move from the opponent's position, multiplied by `parentEval`, the rating of the player's move above it.

So the tree has the root, 6 children (the player's moves) and 36 leaves (the opponent's replies to each of them).

The multiplication stands in for a better rating function that would take the opponent into account. The rating of the opponent's move depends only on the opponent's own position, so without the multiplication the six leaf values below each of the player's moves would be the same for every move of the player. With it, each leaf depends on the player's move above it.

### `int chooseMinMaxMove(Node root)`

The method implements MinMax on the tree and returns the dice value. For each of the player's six moves it takes the minimum of the six leaf values below it, which is the opponent's best reply. Among these six minima it takes the largest and returns the dice that belongs to it (the smaller dice if two minima are equal). It prints the matrix of leaf values. In this example no snake, ladder or apple is within reach of either player, and the player chooses dice 6:

```
Matrix of evaluations:
                             If the opponent chooses dice
                             1    2    3    4    5    6
If the player chooses dice 1 0.36 0.72 1.08 1.44 1.80 2.16   min=0.36
If the player chooses dice 2 0.72 1.44 2.16 2.88 3.60 4.32   min=0.72
If the player chooses dice 3 1.08 2.16 3.24 4.32 5.40 6.48   min=1.0799999999999998
If the player chooses dice 4 1.44 2.88 4.32 5.76 7.20 8.64   min=1.44
If the player chooses dice 5 1.80 3.60 5.40 7.20 9.00 10.80   min=1.7999999999999998
If the player chooses dice 6 2.16 4.32 6.48 8.64 10.80 12.96   min=2.1599999999999997
After using the MinMax method player 0 will choose dice 6
```

A value is the rating of the player's move times the rating of the opponent's move. When nothing is within reach, a move of `n` steps is rated 0.6·n, so for the player's dice 1 the row is 0.6 times 0.6, 1.2, 1.8 and so on.

### `int getNextMove(int currentPos, int opponentCurrentPos, Board board)`

The method creates the root, builds the tree, chooses the dice with `chooseMinMaxMove`, plays this dice with `move` on the real board, adds the result array to `path` and returns the new position.

### `void statistics()`

The method prints the totals of the game over `path`: snake bites, ladders used, and red and black apples eaten.

## `Game`

`Game` is as in Part B with the `MinMaxPlayer` (id 0) in place of the `HeuristicPlayer`; the other player (id 1) rolls a random die. The board has 10 rows and 20 columns with 3 snakes, 3 ladders and 6 apples. The turn order comes from `setTurns`, and the players move in turns until one reaches the last tile, or until round 100 (`MAX_ROUNDS`) is over, whichever comes first. The winner is the player with the higher value of 0.6 × position + 0.4 × points, the weights of the objective function; the player who reached the last tile counts with the last tile as position, and if the values are equal, that player wins (if nobody has arrived, the first player of the list wins a tie). `main` passes both positions to `getNextMove`, because the tree needs the opponent's position. At the end the program prints the rounds, each player's steps and points, the combined value for each (0.6 times the position plus 0.4 times the points), the winner and the statistics of the MinMax player.

## Notes

- The tree is shallow, two moves deep, and the opponent is modelled only through the multiplication described above. It does not look at what the player's move leaves on the board for the opponent.
- Boards and dice are random, so every game is different, and in the games played for this report the round limit was never needed: all of them ended with a player reaching the last tile. In 200 games with this version the MinMax player won 193 and earned or lost points in 132 of them. The numbers change from run to run.
