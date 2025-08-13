package DayClasses;

import java.util.Objects;

public class Node18 implements Comparable<Node18> {
    int x, y;
    int g;
    int h;
    public Node18(int x, int y, int g, int exitIndex) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = calculateHeight(new Tuple<>(exitIndex, exitIndex));
    }

    private int calculateHeight(Tuple<Integer> exit) {
        double sum = 0;
        sum += Math.pow(exit.x() - x, 2);
        sum += Math.pow(exit.y() - y, 2);
        return Math.toIntExact(Math.round(Math.sqrt(sum)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node18 node)) return false;
        return x == node.x && y == node.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Node18 o) {
        return Integer.compare(g + h, o.g + o.h);
    }
}
