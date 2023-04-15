package org.buildr.HOAmanager;

import java.util.Random;

public class IDGenerator {
    public static int generate(int numberOfDigits) {
        Random random = new Random();
        int min = (int) Math.pow(10, numberOfDigits - 1);
        int max = (int) Math.pow(10, numberOfDigits) - 1;
        return random.nextInt(max - min + 1) + min;
    }
}
