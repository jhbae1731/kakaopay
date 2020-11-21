package com.kakaopay.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kakaopay.jpa.entity.GiveEntity;
import com.kakaopay.jpa.entity.QGiveEntity;
import com.kakaopay.jpa.entity.QReceiveEntity;
import com.kakaopay.jpa.entity.ReceiveEntity;
import com.kakaopay.jpa.repository.GiveRepository;
import com.kakaopay.jpa.repository.ReceiveRepository;
import com.kakaopay.response.ApiResponse;
import com.kakaopay.util.DateUtil;
import com.kakaopay.util.NumberUtil;
import com.kakaopay.util.TokenGeneration;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service 
public class ApiServiceImpl implements ApiService {
	@Autowired
    EntityManager em;
	
	private final GiveRepository giveRepository;
	private final ReceiveRepository receiveRepository;
	
	@Transactional
	@Override
	public GiveEntity give(String room_id, String user_id, int give_money, int people_count) {
		TokenGeneration tokenGeneration = new TokenGeneration();
		NumberUtil numberUtil = new NumberUtil();
		
		//토큰 생성
		String token = "";
		boolean dataValidation = false;
		
		//토큰 중복시 새로 생성
		do {
			token = tokenGeneration.generation(3);
			dataValidation = giveRepository.existsById(token);
		}while(dataValidation);
		
		//뿌리기 테이블 insert
		GiveEntity giveEntity = new GiveEntity(token, room_id, user_id, people_count, give_money);
		giveRepository.save(giveEntity);
		
		//금액 분배
		List<Integer> moneyList = numberUtil.moneyDivision(give_money, people_count, 0.3);
		
		//받기 테이블 insert
		int i = 0;
		for(i=0; i<moneyList.size(); i++) {
			ReceiveEntity receiveEntity = new ReceiveEntity(giveEntity, (int)moneyList.get(i), room_id);
			receiveRepository.save(receiveEntity);
		}
		
		return giveEntity;
	}
	
	@Transactional
	@Override
	public ApiResponse receive(String room_id, String user_id, String token) {
		JPAQueryFactory query = new JPAQueryFactory(em);
		QReceiveEntity r = QReceiveEntity.receiveEntity;
		QGiveEntity g = QGiveEntity.giveEntity;
		
		//token검증
		boolean dataValidation = giveRepository.existsById(token);
		int validationCount = 0;
		if(!dataValidation) {
			return new ApiResponse("C003");
		}
		
		//뿌린 본인은 받지 못함
		validationCount = (int)query.selectFrom(g).where(g.user_id.eq(user_id), g.token.eq(token)).fetchCount();
		
		if(validationCount > 0) {
			return new ApiResponse("R001");
		}
		
		//뿌린 사람과 같은 대화방인지
		validationCount = (int)query.selectFrom(g).where(g.room_id.eq(room_id), g.token.eq(token)).fetchCount();
		
		if(validationCount < 1) {
			return new ApiResponse("R002");
		}
		
		
		//받은적이 없는지		
		validationCount = (int)query.selectFrom(r).where(r.user_id.eq(user_id),r.giveEntity.token.eq(token)).fetchCount();
		
		if(validationCount > 0) {
			return new ApiResponse("R003");
		}
		
		//10분 시간검증
		LocalDateTime reg_date = query.selectFrom(g).where(g.token.eq(token)).fetchOne().getReg_date();

		System.out.println(g.reg_date.before(LocalDateTime.now().minusMinutes(10)));
		System.out.println(g.reg_date.before(LocalDateTime.now().plusMinutes(10)));
		
		DateUtil du = new DateUtil();
		
		dataValidation= du.isExpired(reg_date, "MIN", 10);
		
		System.out.println(dataValidation);
		
		if(dataValidation) {
			return new ApiResponse("R004");
		}
		
		//받기		
		List<ReceiveEntity> receive_data = query.selectFrom(r).where(r.giveEntity.token.eq(token),r.user_id.isNull()).fetch();
		
		int seq = receive_data.get(0).getSeq();
		
		query.update(r)
				.set(r.user_id, user_id)
				.where(r.giveEntity.token.eq(token),r.user_id.isNull(),r.seq.eq(seq))
				.execute();
		
		int receive_money = query.selectFrom(r).where(r.seq.eq(seq)).fetchOne().getReceive_money();
		
		ApiResponse apiResponse = new ApiResponse("C001");
		
		apiResponse.setData(receive_money);
		
		return apiResponse;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ApiResponse info(String room_id, String user_id, String token) {
		JPAQueryFactory query = new JPAQueryFactory(em);
		QReceiveEntity r = QReceiveEntity.receiveEntity;
		QGiveEntity g = QGiveEntity.giveEntity;
		//token검증
		boolean dataValidation = giveRepository.existsById(token);
		int validationCount = 0;
		if(!dataValidation) {
			return new ApiResponse("C003");
		}
		
		//뿌린 본인만 조회가능
		validationCount = (int)query.selectFrom(g).where(g.user_id.eq(user_id), g.token.eq(token)).fetchCount();
		
		if(validationCount == 0) {
			return new ApiResponse("I001");
		}
		
		//뿌린 시간 7일 검증
		LocalDateTime reg_date = query.selectFrom(g).where(g.token.eq(token)).fetchOne().getReg_date();

		DateUtil du = new DateUtil();
		
		dataValidation= du.isExpired(reg_date, "DAY", 7);
		
		if(dataValidation) {
			return new ApiResponse("I002");
		}
		
		//조회
		
		List<Tuple> info_data = query.select(g.reg_date, g.give_money, r.receive_money, r.user_id)
									.from(g).innerJoin(r).on(g.token.eq(r.giveEntity.token))
									.where(g.token.eq(token),r.user_id.isNotNull()).fetch();
		
		Map resMap = new HashMap();
		resMap.put("give_date",info_data.get(0).get(g.reg_date));
		resMap.put("give_money",info_data.get(0).get(g.give_money));
		
		int complete_money = 0; 
		
		int i = 0;
		int dataCnt =info_data.size(); 
		List receiveList = new ArrayList();
		for(i =0; i<dataCnt; i++) {
			Map dataMap = new HashMap();
			dataMap.put("receive_money",info_data.get(i).get(r.receive_money));
			dataMap.put("receive_user_id",info_data.get(i).get(r.user_id));
			receiveList.add(dataMap);
			
			complete_money += info_data.get(i).get(r.receive_money);
		}
		
		
		resMap.put("complete_money",complete_money);
		resMap.put("receive_info", receiveList);
		
		ApiResponse apiResponse = new ApiResponse("C001");

		apiResponse.setData(resMap);
		
		return apiResponse;
	}
	
}
