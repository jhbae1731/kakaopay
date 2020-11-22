package com.kakaopay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.kakaopay.jpa.entity.GiveEntity;
import com.kakaopay.response.ApiResponse;
import com.kakaopay.service.ApiService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value="/kakaopay/api")
public class ApiController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final ApiService apiService;
	
	public static final String ROOM_ID = "X-ROOM-ID";
    public static final String USER_ID = "X-USER-ID";
	
	@PostMapping
	ResponseEntity<ApiResponse> giveMoney(@RequestHeader(ROOM_ID) String room_id, 
										@RequestHeader(USER_ID) String user_id,
										@RequestBody ApiControllerRequest param,
										UriComponentsBuilder b) {
		ApiResponse apiResponse = new ApiResponse("C001");
		HttpHeaders headers = new HttpHeaders();
		
		try {
			logger.info("뿌리기 시작!!");
			GiveEntity giveEntity = apiService.give(room_id, user_id, param.getGive_money(), param.getPeople_count());
			
	        headers.setLocation(b.path("/kakaopay/api").path("/{token}").buildAndExpand(giveEntity.getToken()).toUri());
			
			logger.info("뿌리기 끝!!");			
			
			apiResponse.setData(giveEntity.getToken());
			
			return new ResponseEntity<>(apiResponse, headers, HttpStatus.CREATED);
			
		}catch(Exception e) {
			logger.error(e.getMessage());
			apiResponse = new ApiResponse("C002");
			return new ResponseEntity<>(apiResponse, headers, HttpStatus.CREATED);
		}
	}
	
	@PutMapping(value="/{token:[a-zA-Z0-9]{3}}")
	ApiResponse receiveMoney(@RequestHeader(ROOM_ID) String room_id, 
							@RequestHeader(USER_ID) String user_id,
						    @PathVariable("token") String token) {
		try {
			logger.info("받기 시작!!");
			ApiResponse apiResponse = apiService.receive(room_id, user_id, token);
			logger.info("받기 끝!!");
			return apiResponse;
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ApiResponse("C002");
		}
	}
	
	@GetMapping(value="/{token:[a-zA-Z0-9]{3}}")
	ApiResponse infoMoney(@RequestHeader(ROOM_ID) String room_id, 
							@RequestHeader(USER_ID) String user_id,
							@PathVariable("token") String token) {
		try {
			logger.info("조회 시작!!");
			ApiResponse apiResponse = apiService.info(room_id, user_id, token);
			logger.info("조회 끝!!");
			
			return apiResponse;
		}catch(Exception e) {
			logger.error(e.getMessage());
			return new ApiResponse("C002");
		}
	}
}
