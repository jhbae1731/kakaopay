package com.kakaopay;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.kakaopay.jpa.entity.GiveEntity;

class InfoApiTest extends ApiTests {

	@Test
	@DisplayName("뿌린 건에 대한 상태 호출")
	void test001() throws Exception {
		GiveEntity giveEntity = new GiveEntity();
		giveEntity = giveStub("aaa", "room1", "user1", null);
		receiveStub(giveEntity, "room1", "user1");
		
		info("aaa", "room1", "user1") .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("C001")));
    }

	@Test
	@DisplayName("뿌린 본인만 호출 가능")
	void test002() throws Exception {
		info("aaa", "room1", "user2") .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("I001")));
    }
	
	@Test
	@DisplayName("유효하지 않은 토큰값")
	void test003() throws Exception {
		info("ABC", "room1", "user1") .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("C003")));
    }
	
	@Test
	@DisplayName("7일동안 조회가능")
	void test004() throws Exception {
		GiveEntity giveEntity = new GiveEntity();
		giveEntity = giveStub("bbb", "room1", "user1", LocalDateTime.now().minusDays(7));
		receiveStub(giveEntity, "room1", "user1");
		
		info("bbb", "room1", "user1") .andExpect(status().isOk())
		.andExpect(jsonPath("$.code", is("I002")));
    }
}
