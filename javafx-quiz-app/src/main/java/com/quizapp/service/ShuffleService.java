package com.quizapp.service;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Provides manual Fisher-Yates shuffle logic.
 */
public final class ShuffleService {
    private static final Random RANDOM = new Random();

    private ShuffleService() {
    }

    /**
     * Shuffles a list using Fisher-Yates.
     *
     * @param list list to shuffle
     * @param <T> element type
     */
    public static <T> void shuffle(List<T> list) {
        
        shuffle(list, RANDOM);
   
    }

    /**
     * Shuffles a list using Fisher-Yates and the provided random generator.
     *
     * @param list list to shuffle
     * @param random random generator
     * @param <T> element type
     */
    public static <T> void shuffle(List<T> list, Random random) {
      
        Objects.requireNonNull(list, "list must not be null");
        
        Objects.requireNonNull(random, "random must not be null");
        
        for (int i = list.size() - 1; i > 0; i--) {
        
            int j = random.nextInt(i + 1);
            
            T temp = list.get(i);
            
            list.set(i, list.get(j));
            
            list.set(j, temp);
        
        }
    }
}
