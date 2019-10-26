package game;

import java.util.*;
import java.util.stream.Collectors;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    int score = 0;
    int maxTile = 0;
    private boolean aBoolean;
    private Stack<Tile[][]> previousStates;
    private Stack<Integer> previousScores;
    private boolean isSaveNeeded = true;

    public Model() {
        previousScores = new Stack<>();
        previousStates = new Stack<>();
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public void resetGameTiles() {
        score = 0;
        maxTile = 0;
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();

    }

    public boolean canMove() {
        boolean canMove = false;
        for (int i = 0; i < FIELD_WIDTH - 1 /*&&!canMove*/; i++) {
            for (int j = 0; j < FIELD_WIDTH - 1; j++) {
                if (gameTiles[i][j].isEmpty() || gameTiles[i + 1][j + 1].isEmpty() ||
                        gameTiles[i][j].value == gameTiles[i][j + 1].value ||
                        gameTiles[i][j].value == gameTiles[i + 1][j].value
                    /*gameTiles[i][j].value==gameTiles[i-1][j].value*/) {
                    canMove = true;
                    break;
                }


            }

        }
        return canMove;
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();

        if (!emptyTiles.isEmpty()) {
            //int randomIndex=new Random().nextInt(emptyTiles.size());
            int randomIndex = (int) (Math.random() * emptyTiles.size());
            emptyTiles.get(randomIndex).
                    value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private List<Tile> getEmptyTiles() {
        return Arrays.stream(gameTiles)
                .flatMap(Arrays::stream)
                .filter(Tile::isEmpty)
                .collect(Collectors.toList());
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean change = false;
        Tile[] reference;


//
        for (int i = 0; i < tiles.length - 1; i++)
            for (int j = 0; j < tiles.length - 1; j++) {
                if (tiles[j].value == 0 && tiles[j + 1].value != 0) {
                    tiles[j].value = tiles[j + 1].value;
                    tiles[j + 1].value = 0;
                    change = true;
                }
            }

        return change;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean change = false;
        int oldScore = score;

        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].value == tiles[i + 1].value && tiles[i].value != 0) {
                tiles[i].value *= 2;
                tiles[i + 1].value = 0;
                score += tiles[i].value;
                maxTile = tiles[i].value > maxTile ? tiles[i].value : maxTile;
                compressTiles(tiles);
                change = score > oldScore;

            }
        }
        return change;
    }

    private void rotateCW() {
        Tile[][] newGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                newGameTiles[i][j] = gameTiles[FIELD_WIDTH - 1 - j][i];
            }
        }
        gameTiles = newGameTiles;
    }

    public void left() {
        if (isSaveNeeded)
            saveState(gameTiles);
        boolean compressChange = false, mergeChange = false;
        boolean add = false;
        for (Tile[] tiles : gameTiles) {
            compressChange = compressTiles(tiles);
            mergeChange = mergeTiles(tiles);
            if ((compressChange || mergeChange) && !add) {
                addTile();
                add = true;
//                compressChange=false;
//                mergeChange=false;
            }
        }
        isSaveNeeded = true;
    }

    public void down() {
        saveState(gameTiles);
        rotateCW();
        left();
        rotateCW();
        rotateCW();
        rotateCW();
    }

    public void right() {
        saveState(gameTiles);
        rotateCW();
        rotateCW();
        left();
        rotateCW();
        rotateCW();
    }

    public void up() {
        saveState(gameTiles);
        rotateCW();
        rotateCW();
        rotateCW();
        left();
        rotateCW();

    }

    public void saveState(Tile[][] tiles) {
        Tile[][] copyTiles = new Tile[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                copyTiles[i][j] = new Tile(tiles[i][j].value);
            }
        }

        previousStates.push(copyTiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousStates.empty() && !previousScores.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public void randomMove() {

        switch (((int) (Math.random() * 100)) % 4) {
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
        }
    }

    public boolean hasBoardChanged() {
        int sumCurrent = Arrays.stream(gameTiles)
                .flatMap(Arrays::stream)
                .filter(tile -> (!tile.isEmpty()))
                .mapToInt(tile -> tile.value).sum();

        int sumPrevious = Arrays.stream(previousStates.peek())
                .flatMap(Arrays::stream)
                .filter(tile -> (!tile.isEmpty()))
                .mapToInt(tile -> tile.value).sum();

        return sumCurrent != sumPrevious;

    }

    public MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency;

        move.move();

        if (hasBoardChanged())
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        else
            moveEfficiency = new MoveEfficiency(-1, 0, move);

        rollback();

        return moveEfficiency;
    }

    public void autoMove(){
        Queue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4,Collections.reverseOrder());
        Queue<PriorityQueue<MoveEfficiency>> priorityQueueDeep2 = new PriorityQueue<>(4,Collections.reverseOrder());

        priorityQueue.add(getMoveEfficiency(this::left));
        priorityQueue.add(getMoveEfficiency(this::right));
        priorityQueue.add(getMoveEfficiency(this::up));
        priorityQueue.add(getMoveEfficiency(this::down));

       // priorityQueue.poll().getMove().move();

    }

}
