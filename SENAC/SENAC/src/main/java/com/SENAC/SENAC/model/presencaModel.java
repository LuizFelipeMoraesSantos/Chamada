package com.SENAC.SENAC.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass 
public abstract class presencaModel {
    private int presenca;
    private List<String> diaDaSemana; 
    private Date mes;
    private int diasLetivos;
}