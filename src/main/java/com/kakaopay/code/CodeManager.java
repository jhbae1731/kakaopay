package com.kakaopay.code;

import lombok.Getter;

@Getter
public enum CodeManager{
	
	C001("C001","SUCCESS"),
	C002("C002","시스템 오류 입니다. 고객센터로 문의 주세요."),
	C003("C003","유효하지 않은 토큰입니다."),
	
	R001("R001","뿌린사람은 받을 수 없습니다."),
	R002("R002","뿌린 대화방이 아닙니다."),
	R003("R003","한번만 받을 수 있습니다."),
	R004("R004","받을 수 있는 시간이 지났습니다."),
	
	I001("I001","뿌린사람만 조회할 수 있습니다."),
	I002("I002","조회할 수 있는 시간이 지났습니다.");
	
	private String code;
	private String msg;
	
	public String getCode() {
		return this.code;
	}
	
    public String getMsg() {
    	return this.msg;
    }

   CodeManager(String code, String msg) {
	   this.code = code;
	   this.msg = msg;
   }
}
