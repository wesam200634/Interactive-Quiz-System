package com.quizapp;

import com.quizapp.service.ShuffleService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests manual shuffle service.
 */
public class ShuffleServiceTest {
    @Test
    void shuffleShouldKeepSameElements() {
        ArrayList<Integer> values = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        ArrayList<Integer> copy = new ArrayList<>(values);
        ShuffleService.shuffle(values, new Random(42));
        values.sort(Integer::compareTo);
        copy.sort(Integer::compareTo);
        Assertions.assertEquals(copy, values);
    }
}
