package com.SENAC.SENAC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.SENAC.SENAC.model.alunoModel;

@Repository
public interface alunoRepository extends JpaRepository<alunoModel, String> {

}