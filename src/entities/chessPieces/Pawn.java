package entities.chessPieces;

import entities.ChessPiece;
import entities.Tile;

import static java.lang.Math.abs;

public class Pawn extends ChessPiece {

    public Pawn(String color, Tile pos) {
        super(color, pos);
    }

    @Override
    protected boolean canMoveToPosition(int x, int y) {
        return (WHITE.equals(getColor()) && y > 0 || BLACK.equals(getColor()) && y < 0) && check()
                ? abs(y) == 2 && x == 0 || abs(y) == 1 && abs(x) <= 1
                : abs(y) == 1 && abs(x) <= 1;
    }
}
