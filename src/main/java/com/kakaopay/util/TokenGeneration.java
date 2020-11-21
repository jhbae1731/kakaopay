package com.kakaopay.util;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGeneration {
	/**
	 * 토큰 생성
	 * a-z, A-Z, 0-9
	 * */
	public String generation(int count) {
		return RandomStringUtils.randomAlphanumeric(count);
	}
}
