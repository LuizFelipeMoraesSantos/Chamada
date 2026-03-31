package com.SENAC.SENAC.service;

import java.util.Optional;

// Importação correta para o JpaRepository
import org.springframework.stereotype.Service;
import com.SENAC.SENAC.model.AlunoModel;
import com.SENAC.SENAC.repository.AlunoRepository;
import jakarta.annotation.PostConstruct;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortList;

@Service
public class leituraDeTags {

    private final AlunoRepository bancoH2;
    private SerialPort portaUsb; 

    public leituraDeTags(AlunoRepository bancoH2) {
        this.bancoH2 = bancoH2;
    }

    @PostConstruct
    public void init() {
        // 1. Lista as portas COM disponíveis no Windows
        String[] portasDisponiveis = SerialPortList.getPortNames();

        if (portasDisponiveis.length == 0) {
            System.err.println("--- ERRO: Nenhum Arduino detectado nas portas USB! ---");
            return;
        }

        // 2. Seleciona a primeira porta (ex: COM3)
        String portaSelecionada = portasDisponiveis[0];
        this.portaUsb = new SerialPort(portaSelecionada); 

        System.out.println("--- Tentando conectar na porta: " + portaSelecionada + " ---");

        try {
            this.portaUsb.openPort(); 
            
            // 3. Configura os parâmetros (Baudrate 9600 deve bater com o Serial.begin do Arduino)
            this.portaUsb.setParams(
                SerialPort.BAUDRATE_9600, 
                SerialPort.DATABITS_8, 
                SerialPort.STOPBITS_1, 
                SerialPort.PARITY_NONE
            );

            // 4. Ativa o "Ouvinte" de eventos serial
            this.portaUsb.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.isRXCHAR() && event.getEventValue() > 0) { 
                        try {
                            // TODO: tag de leitura
                            String tagLida = portaUsb.readString().trim();
                            
                            if (tagLida != null && !tagLida.isEmpty()) {
                                // O processamento é feito fora do escopo da porta para não travar a leitura
                                processarTag(tagLida);
                            }
                        } catch (Exception e) {
                            System.err.println("Erro ao ler dados da porta: " + e.getMessage());
                        }
                    }
                }
            });

            System.out.println("--- Conexão estabelecida com sucesso! Aguardando tags... ---");

        } catch (Exception e) {
            System.err.println("Erro ao abrir a porta " + portaSelecionada + ": " + e.getMessage());
        }
    }

    private void processarTag(String tag) {
        // 5. Busca no H2 usando a tag como ID (Chave Primária)
        Optional<AlunoModel> alunoOpt = bancoH2.findById(tag);

        if (alunoOpt.isPresent()) {
            AlunoModel aluno = alunoOpt.get();
            
            // Lógica de Presença: Aumenta o contador e salva no banco
            aluno.setPresenca(aluno.getPresenca() + 1);
            bancoH2.save(aluno);

            System.out.println("\n==============================");
            System.out.println("   CHAMADA REGISTRADA!");
            System.out.println("   Aluno: " + aluno.getNome());
            System.out.println("   Total de Presenças: " + aluno.getPresenca());
            System.out.println("==============================\n");
        } else {
            System.out.println("\n[AVISO] Tag detectada: " + tag);
            System.out.println("[AVISO] Esta tag não está vinculada a nenhum aluno no banco H2.\n");
        }
    }
}