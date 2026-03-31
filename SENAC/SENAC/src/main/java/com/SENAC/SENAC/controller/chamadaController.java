package com.SENAC.SENAC.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class chamadaController {

    @GetMapping("/mensagem")
    private String mensagem(){
       return "Hello,word!";
    }        

    @GetMapping("/apresentacao/{nome}")
    public String apresentacao(@PathVariable String nome){
        return "Olá, " + nome;
    }
}
