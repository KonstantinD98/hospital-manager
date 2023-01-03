package com.example.application.data.repository;

import com.example.application.data.entity.Hospital;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {

}
