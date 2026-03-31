package com.SENAC.SENAC.model;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Alunos")
@Entity
public class AlunoModel {

    private int codigo;
    private String nome;
    private Date diasDaSemana;
    private Date mes;
    private int diasLetivos;

}
