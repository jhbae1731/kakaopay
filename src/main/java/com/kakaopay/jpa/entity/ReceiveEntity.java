package com.kakaopay.jpa.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="receive")
public class ReceiveEntity{
	 
	@Id
	@Column(name="seq")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int seq;
	
	@NonNull
	@ManyToOne
	@JoinColumn(name="token")
	private GiveEntity giveEntity;
	
	@NonNull
	@Column(name="receive_money")
	private int receive_money;
	
	@NonNull
	@Column(name="room_id")
	private String room_id;
	
	@Column(name="user_id")
	private String user_id;
	
	@Column(name = "reg_date")
    private LocalDateTime reg_date = LocalDateTime.now();
}
