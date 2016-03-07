package com.jdroid.java.utils;

import java.util.Random;

public class RandomUtils {

	public static Long getLong() {
		return Math.abs(new Random().nextLong());
	}

	public static Integer getInt() {
		return Math.abs(new Random().nextInt());
	}

	public static Integer get16BitsInt() {
		return Math.abs(new Random().nextInt(16));
	}

	public static Integer getInt(int bound) {
		return Math.abs(new Random().nextInt(bound));
	}
}
