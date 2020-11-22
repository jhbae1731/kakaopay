package com.kakaopay.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kakaopay.jpa.entity.GiveEntity;
import com.kakaopay.jpa.entity.ReceiveEntity;

public interface ReceiveRepository extends JpaRepository<ReceiveEntity, String> {
}
