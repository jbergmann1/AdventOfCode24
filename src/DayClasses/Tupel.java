package DayClasses;

public record Tupel<T extends Number>(T x, T y) {

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}