package com.kakaopay.util;

import java.time.LocalDateTime;

public class DateUtil {
	/**
	 * 날짜 검증
	 * 
	 * @param date_gubun : MIN=분, DAY=일
	 * */
	 public boolean isExpired(LocalDateTime date1, String date_gubun, int diffNum) {
		 
		LocalDateTime ldf = LocalDateTime.now();
		
		if(date_gubun.equals("MIN")) {
			ldf = ldf.minusMinutes(diffNum);
		}
		
		if(date_gubun.equals("DAY")) {
			ldf = ldf.minusDays(diffNum);
		}
		
		 
        return date1.isBefore(ldf);
	 }
}
