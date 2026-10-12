# Snake and Ladders, Part B: heuristic player

3 December 2018 · Data Structures, 2018-2019 · [The assignment](../../README.md#the-assignments)

## Summary

Part B adds a player that chooses its dice instead of rolling: the `HeuristicPlayer`. In every turn it evaluates the six possible moves with an objective function and plays the best one. The `Game` class is extended so that the turn order is decided by a dice roll and a game is played between the heuristic player and a player that rolls a random die.

The classes of Part A are rewritten for this part with the same ideas and the same code style, and the board is generated differently, see below. The program is in `src/` and starts in `Game.main`.

## Differences from Part A

- `Game` creates a board of 10 rows and 20 columns with 3 snakes, 3 ladders and 6 apples (Part A: 20 rows and 10 columns).
- A snake head is a random tile from tile `N` (10 on this board) up to tile `N·M − 1` (199), so it can never be on the last tile. The tail is drawn from lower-numbered tiles, so a snake always leads down. A ladder's top is drawn like a snake head and its base from lower-numbered tiles, never on a snake head. An apple is on a random tile that is not a snake head, is red or black with equal probability and is worth 0 to 10 points.
- For ladders the names are the other way round: `downStepId` is the tile where the ladder starts (its base) and `upStepId` the tile it leads to (its top).
- `Player.move(id, die)` does not stop at the last tile: a position of N·M or more means that the player has arrived.

## `Player.move`

The method moves a player from tile `id` by `die` steps and returns the array `{new position, snake bites, ladders used, red apples eaten, black apples eaten}`. It repeats the following until nothing moves the player any more: a snake on the tile takes the player to its tail; an unbroken ladder whose base is on the tile takes the player to its top and breaks; an apple on the tile is eaten (a red apple adds its points to the score, a black one subtracts them, and the apple's points are set to 0). A message is printed for each of these events.

## `HeuristicPlayer`

The class extends `Player` and has one more variable, `path`, an `ArrayList<int[]>`. Each element is the result array of one move. There are three constructors: an empty one, one with the variables of `Player`, and one that also takes a `path`.

### `double evaluate(int currentPos, int dice)`

The method rates the move of a player who stands on `currentPos` and plays `dice`:

```java
double evaluation = (double) steps * 0.6 + (double) gainPoints * 0.4;
```

`steps` is the number of tiles the move advances (a ladder gives many, a snake a negative number) and `gainPoints` is the points the move earns or loses. The weights put somewhat more value on moving forward than on points, in line with the goal of reaching the last tile first. For example, a move of 5 steps that eats a red apple worth 10 points is rated 5 × 0.6 + 10 × 0.4 = 7.0, and a plain move of 6 steps is rated 3.6.

To rate a move, the method plays it with `move`. A trial move must not change the game, so the method first saves which ladders are broken and the points of every apple, and after the move it restores the ladders, the apples and the player's score. The values of the six possible moves are only numbers; the board and the score change in the real move.

### `int getNextMove(int currentPos)`

The method calls `evaluate` six times (dice 1 to 6) and stores the values in an array. The best value gives the dice (the smaller dice if two values are equal). The player then plays this dice with `move`, adds the result array to `path` and returns the new position. The evaluation is printed while it runs. For a player at the start of the board, where dice 5 reaches the base of a ladder, it looks like this:

```
EVALUATION for dice = 4
MoveResults: 
4, 0, 0, 0, 0, Evaluation: 2.4 steps: 4 gainPoints: 0
EVALUATION for dice = 5
player 0 climbed a ladder
MoveResults: 
22, 0, 1, 0, 0, Evaluation: 13.2 steps: 22 gainPoints: 0
```

The position, snake bites, ladders, red and black apples are the five numbers of the result array. The ladder move (22 steps, rated 13.2) is the best of the six.

### `void statistics()`

The method prints, for every round in `path`, the new position and the numbers of snake bites, ladders and red and black apples, followed by the totals over the whole game.

## `Game`

`Game` has the variable `round`, constructors, and getters and setters.

- `Map<Integer, Integer> setTurns(ArrayList<Player> players)` lets every player roll a die and returns a map from player id to roll. It sorts the map by its values with `sortByValue`, which copies the entries into a `LinkedList`, sorts them with `Collections.sort` and a `Comparator` on `Map.Entry`, and puts them in a `LinkedHashMap`. The player with the lowest roll plays first.
- `main` creates the board and prints it, creates a `HeuristicPlayer` (id 0) and a player that rolls a random die (id 1), and sets the turn order. The players then play in turns until one reaches the last tile, or until round 100 (`MAX_ROUNDS`) is over, whichever comes first. The winner is the player with the higher value of 0.6 × position + 0.4 × points, the weights of the objective function; the player who reached the last tile counts with the last tile as position, and if the values are equal, that player wins (if nobody has arrived, the first player of the list wins a tie). At the end the program prints the number of rounds, the winner and the scores, and the statistics of the heuristic player.

## Notes

- The evaluation does not take the opponent into account. It considers a single move of the player itself.
- Boards and dice are random, so every game is different, and in the games played for this report the round limit was never needed: all of them ended with a player reaching the last tile. In 200 games with this version the heuristic player won 196 and earned or lost points in 123 of them. The numbers change from run to run.
