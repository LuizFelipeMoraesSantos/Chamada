package com.SENAC.SENAC.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.SENAC.SENAC.model.AlunoModel;

@Repository
public interface AlunoRepository extends JpaRepository<AlunoModel, String> {

}