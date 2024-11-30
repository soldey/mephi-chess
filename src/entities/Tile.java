package entities;

public record Tile(int x, int y) {
    private static final String HORIZONTAL = "ABCDEFGH";
    private static final String VERTICAL = "12345678";

    public Tile {
        if (!isTile(x, y)) {
            throw new RuntimeException("Некорректные координаты клетки " + x + " " + y);
        }
    }

    public static Tile of(String Tile) {
        if (!isTile(Tile)) {
            throw new RuntimeException("Некорректная клетка " + Tile);
        }
        return new Tile(Tile.charAt(0) - 65, 8 - Integer.parseInt(Tile.substring(1)));
    }

    public static boolean isTile(String Tile) {
        return Tile != null && Tile.length() == 2 && HORIZONTAL.contains(Tile.substring(0, 1))
                && VERTICAL.contains(Tile.substring(1));
    }

    public static boolean isTile(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    @Override
    public String toString() {
        return String.valueOf((char) (x + 65)) + (8 - y);
    }
}
