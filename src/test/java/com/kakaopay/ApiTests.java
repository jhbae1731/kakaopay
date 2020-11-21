package com.kakaopay;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.jpa.entity.GiveEntity;
import com.kakaopay.jpa.entity.ReceiveEntity;
import com.kakaopay.jpa.repository.GiveRepository;
import com.kakaopay.jpa.repository.ReceiveRepository;
import com.kakaopay.request.ApiRequest;
import com.kakaopay.util.NumberUtil;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class ApiTests {
	 @Autowired
     protected MockMvc mockMvc;
	 
	 @Autowired
     protected ObjectMapper mapper;
	 
	 @Autowired
	 private GiveRepository giveRepository;
	 
	 @Autowired
	 private ReceiveRepository receiveRepository;
	 
	 protected GiveEntity giveStub(String token, String room_id, String user_id, LocalDateTime date) {
		GiveEntity giveEntity = new GiveEntity(token ,room_id, user_id, 7, 1000000);
		if(date != null) {
			giveEntity.setReg_date(date);
		}
		giveRepository.save(giveEntity);
		
		List<GiveEntity> entityList = giveRepository.findAll();
		
		giveEntity = entityList.get(0);
		
		return giveEntity;
     }
	 
	 protected List<ReceiveEntity> receiveStub(GiveEntity giveEntity, String room_id, String user_id) {
		NumberUtil numberUtil = new NumberUtil();
		 
		List<Integer> moneyList = numberUtil.moneyDivision(giveEntity.getGive_money(), giveEntity.getPeople_count(), 0.3);
			
		int i = 0;
		for(i=0; i<moneyList.size(); i++) {
			ReceiveEntity receiveEntity = new ReceiveEntity(giveEntity, (int)moneyList.get(i), room_id);
			
			if(i==0) {
				receiveEntity.setUser_id("user2");
			}
			
			receiveRepository.save(receiveEntity);
		}
		
		List<ReceiveEntity> entityList = receiveRepository.findAll();
		return entityList;
     }

	 protected ResultActions give(String room_id, String user_id, int give_money, int people_count) throws Exception {
		ApiRequest request = new ApiRequest();
		request.setGive_money(give_money);
		request.setPeople_count(people_count);
		
		return mockMvc.perform(
		        post("/kakaopay/api/")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.header("X-ROOM-ID", room_id)
						.header("X-USER-ID", user_id)
		                .content(mapper.writeValueAsString(request))
		)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	 }
	 
	 protected ResultActions receive(String token, String room_id, String user_id) throws Exception {
		return mockMvc.perform(
		put("/kakaopay/api/" + token)
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON)
		        .header("X-ROOM-ID", room_id)
				.header("X-USER-ID", user_id)
		)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
		;
	 }
	
	 protected ResultActions info(String token, String room_id, String user_id) throws Exception {
		return mockMvc.perform(
		get("/kakaopay/api/" + token)
		        .contentType(MediaType.APPLICATION_JSON)
		        .accept(MediaType.APPLICATION_JSON)
		        .header("X-ROOM-ID", room_id)
				.header("X-USER-ID", user_id)
		)
		.andDo(print())
		.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
	 }
}
