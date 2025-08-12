package DayClasses;

import java.util.Arrays;
import java.util.List;

public record Tuple<T extends Number>(List<T> values) {
    @SafeVarargs
    public Tuple(T... values) {
        this(Arrays.asList(values));
    }

    public T x() {
        return values.getFirst();
    }

    public T y() {
        return values.get(1);
    }

    public T z() {
        return values.get(2);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}