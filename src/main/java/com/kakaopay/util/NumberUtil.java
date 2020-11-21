package com.kakaopay.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NumberUtil {
	public List<Integer> moneyDivision(int give_money, int people_count, double desiredStandardDeviation){
		List<Integer> moneyList = new ArrayList<Integer>();
		Random random = new Random();
		
		int mean = give_money/people_count; //평균
		double deviation = mean*desiredStandardDeviation; //편차
		
		int i = 0;
		int total = 0; //분배금액 합계
		int maxMoney = give_money-(mean*2); //첫번째 분배할 max값
		int money = 0;
		int remainMoney = 0;
		int remainCount = 0;
		//분배로직
		for(i=0; i<people_count; i++) {
			money = (int)((random.nextGaussian()*deviation)+mean);
			remainMoney = give_money-total; //남은금액
			remainCount = people_count-(i+1); //남은인원
			if(remainCount == 0) {
				money = give_money-total;
				moneyList.add(money);
				total += money;
			}else if(total>=maxMoney) {//이 이상을 넘어가면 분배금액이 오버될 수 있으므로 break;
				moneyList.add(money);
				total += money;
				
				mean = remainMoney/remainCount; //평균 재 계산
				if(remainCount == 1) {
					money = give_money-total;
					moneyList.add(money);
					total += money;
					break;
				}
				int j=0;
				
				//재분배 균등분할
				for(j=0;j<remainCount;j++) {
					//최후의 1인은 남은 금액
					if(j==(remainCount-1)) {
						money = give_money-total;
					}else {
						money = mean;
					}
					moneyList.add(money);
					total += money;
				}
				break;
			}else {
				moneyList.add(money);
				total += money;
			}
		}
		return moneyList;
	}
}
