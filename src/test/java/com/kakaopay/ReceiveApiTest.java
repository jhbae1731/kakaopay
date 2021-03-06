package com.kakaopay;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kakaopay.jpa.entity.GiveEntity;
import com.kakaopay.jpa.entity.ReceiveEntity;

class ReceiveApiTest extends ApiTests{
	@Test
	@DisplayName("뿌린 금액 받기")
	void test001() throws Exception {
		GiveEntity giveEntity = new GiveEntity();
		giveEntity = giveStub("ccc", "room1", 1, null);
		
		List<ReceiveEntity> receiveList = receiveStub(giveEntity, "room1", 1);
		
		receive("ccc", "room1", 3) .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("C001")))
		.andExpect(jsonPath("$.data").value(receiveList.get(1).getReceive_money()));
    }
	
	@Test
	@DisplayName("한번만 받을 수 있음")
	void test002() throws Exception {
		receive("ccc", "room1", 2) .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("R003")));
    }
	
	@Test
	@DisplayName("자신이 뿌린건 받을 수 없음")
	void test003() throws Exception {
		receive("ccc", "room1", 1) .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("R001")));
    }
	
	@Test
	@DisplayName("대화방이 같아야됨")
	void test004() throws Exception {
		receive("ccc", "room2", 3) .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("R002")));
    }
	
	@Test
	@DisplayName("뿌린건은 10분간만 유효")
	void test005() throws Exception {
		GiveEntity giveEntity = new GiveEntity();
		giveEntity = giveStub("ddd", "room1", 1, LocalDateTime.now().minusMinutes(11));
		receiveStub(giveEntity, "room1", 1);
		
		receive("ddd", "room1", 3) .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("R004")));
    }
}
