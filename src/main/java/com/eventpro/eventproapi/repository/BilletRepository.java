package com.eventpro.eventproapi.repository;

import com.eventpro.eventproapi.entity.Billet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BilletRepository extends JpaRepository<Billet,Long> {
    Optional<Billet> findByCode(String id);
}
