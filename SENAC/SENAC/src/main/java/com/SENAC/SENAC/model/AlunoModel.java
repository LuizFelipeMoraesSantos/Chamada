package com.SENAC.SENAC.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tb_alunos")
@Entity
public class alunoModel extends presencaModel {
    
    @Id
    private String tagRfid;
    private String nome;

}
