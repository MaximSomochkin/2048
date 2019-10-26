package game;

import java.util.Comparator;

public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int compareTo(MoveEfficiency anotherMoveEfficiency) {

        return Comparator.comparingInt((MoveEfficiency m)->m.numberOfEmptyTiles)
                .thenComparingInt((MoveEfficiency m)->m.score)
                .compare(this, anotherMoveEfficiency);

    }
}
