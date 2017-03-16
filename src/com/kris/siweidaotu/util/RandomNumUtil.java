package com.kris.siweidaotu.util;

import java.util.Random;

public class RandomNumUtil {


	/**
	 * 生成不重复的随机数
	 * @param total
	 * @return
	 */
	public static int[] GetRandomSequence(int total) {
		int[] sequence = new int[total];
		int[] output = new int[total];

		for (int i = 0; i < total; i++) {
			sequence[i] = i;
		}
		Random random = new Random();
		int end = total - 1;
		for (int i = 0; i < total; i++) {
			int num = random.nextInt(end + 1);
			output[i] = sequence[num];
			sequence[num] = sequence[end];
			end--;
		}
		return output;
	}
	
	/**
	 * 生成不重复的随机数
	 * @param total
	 * @return
	 */
	public static int[] GetRandomSequence(int total,int maxNum) {
		int[] sequence = new int[maxNum];
		int[] output = new int[total];

		for (int i = 0; i < maxNum; i++) {
			sequence[i] = i;
		}
		Random random = new Random();
		int end = maxNum - 1;
		for (int i = 0; i < total; i++) {
			int num = random.nextInt(end + 1);
			output[i] = sequence[num];
			sequence[num] = sequence[end];
			end--;
		}
		return output;
	}
	
}
