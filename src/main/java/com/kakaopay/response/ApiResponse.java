package com.kakaopay.response;

import com.kakaopay.code.CodeManager;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResponse {
	@NonNull
	private String code;
	@NonNull
	private String msg;
	private Object data;
	
	public ApiResponse(String code) {
		this.code=code;
		this.msg=CodeManager.valueOf(code).getMsg();
	}
}
