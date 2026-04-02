package com.SENAC.SENAC.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

import com.SENAC.SENAC.model.alunoModel;
import com.SENAC.SENAC.repository.alunoRepository;


@RequiredArgsConstructor
@RestController
@RequestMapping("/alunos")
public class chamadaController {

    private final alunoRepository bancoH2;

    @GetMapping("/listaDeAlunos")
    public Iterable<alunoModel> listarAlunos(){
        return this.bancoH2.findAll();
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<alunoModel> cadastrarAluno(@RequestBody alunoModel novoAluno) {
        // Verifica se a tag já existe para não duplicar
        if (bancoH2.existsById(novoAluno.getTagRfid())) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bancoH2.save(novoAluno));
    }

}        



    

