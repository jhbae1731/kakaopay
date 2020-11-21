package com.kakaopay.service;

import com.kakaopay.jpa.entity.GiveEntity;
import com.kakaopay.response.ApiResponse;

public interface ApiService {
	GiveEntity give(String room_id, String user_id, int give_money, int people_count);
	ApiResponse receive(String room_id, String user_id, String token);
	ApiResponse info(String room_id, String user_id, String token);
}
