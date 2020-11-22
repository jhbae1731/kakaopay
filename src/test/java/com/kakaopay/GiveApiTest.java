package com.kakaopay;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import com.kakaopay.request.ApiRequest;


class GiveApiTest extends ApiTests{
	@Test
	@DisplayName("뿌리기 token 발급 및 응답")
	void test001() throws Exception {
		ApiRequest request = new ApiRequest();
        request.setGive_money(50000);
        request.setPeople_count(8);
        
        give("roomtest1", 1, 50000, 8)
	        .andExpect(status().isCreated())
	        .andExpect(header().exists(HttpHeaders.LOCATION))
	        .andExpect(jsonPath("$.code", is("C001")))
	        .andExpect(jsonPath("$.data", hasLength(3)))
                
        ;
        
    }
}
