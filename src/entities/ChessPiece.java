package entities;

import java.util.Objects;

import static java.util.Objects.isNull;

public abstract class ChessPiece {
    public static final String WHITE = "W";
    public static final String BLACK = "B";
    private final String color;
    private Tile pos;
    private boolean check = true;

    protected ChessPiece(String color, Tile pos) {
        this.color = color;
        this.pos = pos;
    }

    public String getSymbol() {
        return this.getClass().getSimpleName().substring(0, 1);
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return getSymbol() + getColor().charAt(0);
    }

    public void setPos(Tile pos) {
        check = false;
        this.pos = pos;
    }

    public Tile getPos() {
        return pos;
    }

    public boolean check() {
        return check;
    }

    public Tile[] steps(Tile dest) {
        if (isNull(dest) || dest.equals(getPos())) {
            return null;
        }

        int x = getPos().x() - dest.x();
        int y = getPos().y() - dest.y();

        if (!canMoveToPosition(x, y)) {
            return null;
        }

        int directionX = x != 0 ? (x > 0 ? 1 : -1) : x;
        int directionY = y != 0 ? (y > 0 ? 1 : -1) : y;
        int size = Math.max(Math.abs(x), Math.abs(y));
        Tile[] steps = new Tile[size];
        for (int i = 1; i <= steps.length; i++) {
            steps[i - 1] = new Tile(getPos().x() - i * directionX, getPos().y() - i * directionY);
        }
        return steps;
    }

    protected abstract boolean canMoveToPosition(int x, int y);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return Objects.equals(color, that.color) && Objects.equals(pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pos);
    }

    @Override
    public String toString() {
        return getName();
    }
}
