package com.jdroid.java.utils;

import java.util.Random;

public class IdGenerator {
	
	private static Integer ID = 10000;
	
	public static synchronized Long getLongId() {
		ID++;
		return ID.longValue();
	}
	
	public static synchronized Integer getIntId() {
		return ID++;
	}
	
	public static Long getRandomLongId() {
		return Math.abs(new Random().nextLong());
	}
	
	public static Integer getRandomIntId() {
		return Math.abs(new Random().nextInt());
	}

	public static Integer getRandomIntId(int bound) {
		return Math.abs(new Random().nextInt(bound));
	}
}
