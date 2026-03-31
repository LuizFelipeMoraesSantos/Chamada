package com.SENAC.SENAC.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlunoModel {

    private int codigo;
    private String nome;
    private Date diasDaSemana;
    private Date mes;
    private int diasLetivos;

}
