package kn.hqup.gamexo.ai.brutforceway;


public class KeyCell {
    private int x;
    private int y;
    private static final int X = 0;
    private static final int Y = 1;

    public KeyCell(int[] cell) {
        x = cell[X];
        y = cell[Y];
    }

    public KeyCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyCell)) return false;

        KeyCell keyCell = (KeyCell) o;

        if (x != keyCell.x) return false;
        if (y != keyCell.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "KeyCell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
