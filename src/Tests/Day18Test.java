package Tests;

import DayClasses.Day18;
import DayClasses.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Day18Test {

    @Test
    void euclideanDistanceTest() {
        Day18 day18 = new Day18();
        Assertions.assertEquals(3, day18.euclideanDistance(new Tuple<>(2, 3, -1), new Tuple<>(4, 1, -2)));
        Assertions.assertEquals(199, Math.floor(day18.euclideanDistance(new Tuple<>(34, 12), new Tuple<>(233, 4))));
    }
}