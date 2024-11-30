package logging;

import entities.Tile;

import java.util.Arrays;

public class Logging {
    private Log[] logs = new Log[100];
    private int size;

    public void add(String figureName, Tile from, Tile to, String captured) {
        logs[size++] = new Log(figureName, from, to, captured);
        if (size == logs.length) {
            logs = Arrays.copyOf(logs, logs.length + 100);
        }
    }

    public Tile getLastDestTile() {
        return size != 0 ? logs[size - 1].to : null;
    }

    private record Log(String figure, Tile from, Tile to, String captured) {

        @Override
            public String toString() {
                return figure + " " + from + " -> " + to + (captured != null ? " x " + captured : "");
            }
        }
}
