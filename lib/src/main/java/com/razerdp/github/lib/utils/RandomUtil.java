package com.razerdp.github.lib.utils;

import java.util.Random;

/**
 * Created by 大灯泡 on 2019/8/3.
 */
public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static int random(int min, int max) {
        RANDOM.setSeed(System.currentTimeMillis());
        return RANDOM.nextInt(max - min + 1) + min;
    }
}
