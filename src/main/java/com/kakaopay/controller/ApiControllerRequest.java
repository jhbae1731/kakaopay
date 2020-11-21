package com.kakaopay.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiControllerRequest {
	private int give_money;
	private int people_count;
	private String token;
}
