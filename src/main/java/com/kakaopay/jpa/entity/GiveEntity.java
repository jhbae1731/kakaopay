package com.kakaopay.jpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name="give")
public class GiveEntity{
	@Id
	@NonNull
	@Column(name="token", length=3)
	private String token;
	
	@NonNull
	@Column(name="room_id")
	private String room_id;
	
	@NonNull
	@Column(name="user_id")
	private int user_id;
	
	@NonNull
	@Column(name="people_count")
	private int people_count;
	
	@NonNull
	@Column(name="give_money")
	private int give_money;
	
	@Column(name = "reg_date")
    private LocalDateTime reg_date = LocalDateTime.now();
}
