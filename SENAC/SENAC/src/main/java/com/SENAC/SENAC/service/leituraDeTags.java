package com.SENAC.SENAC.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.SENAC.SENAC.model.alunoModel;
import com.SENAC.SENAC.repository.alunoRepository;
import jakarta.annotation.PostConstruct;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortList;

@Service
public class leituraDeTags {

    private final alunoRepository bancoRepository; // Nome genérico pois você vai usar MySQL
    private SerialPort portaUsb; 
    private String tagTemporaria; // Corrigido o nome da variável

    public leituraDeTags(alunoRepository bancoRepository) {
        this.bancoRepository = bancoRepository;
    }

    // Método para o Controller buscar a última tag para o formulário de cadastro
    public String getTagTemporaria() {
        return this.tagTemporaria;
    }

    @PostConstruct
    public void init() {
        String[] portasDisponiveis = SerialPortList.getPortNames();

        if (portasDisponiveis.length == 0) {
            System.err.println("--- ERRO: Nenhum Arduino detectado nas portas USB! ---");
            return;
        }

        String portaSelecionada = portasDisponiveis[0];
        this.portaUsb = new SerialPort(portaSelecionada); 

        try {
            this.portaUsb.openPort(); 
            this.portaUsb.setParams(
                SerialPort.BAUDRATE_9600, 
                SerialPort.DATABITS_8, 
                SerialPort.STOPBITS_1, 
                SerialPort.PARITY_NONE
            );

            this.portaUsb.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.isRXCHAR() && event.getEventValue() > 0) { 
                        try {
                            // Pequena pausa para garantir que o buffer da USB receba a tag completa
                            Thread.sleep(50); 
                            
                            // Lógica de limpeza: Remove espaços extras e espaços entre os IDs
                            String tagLida = portaUsb.readString().trim().replace(" ", " ");
                            
                            if (tagLida != null && !tagLida.isEmpty()) {
                                // Guarda a tag para o fluxo de CADASTRAR
                                tagTemporaria = tagLida; 
                                
                                // Processa para o fluxo de PRESENÇA
                                processarTag(tagLida);
                            }
                        } catch (Exception e) {
                            System.err.println("Erro ao ler dados da porta: " + e.getMessage());
                        }
                    }
                }
            });

            System.out.println("--- Conectado em: " + portaSelecionada + ". Aguardando tags... ---");

        } catch (Exception e) {
            System.err.println("Erro ao abrir a porta " + portaSelecionada + ": " + e.getMessage());
        }
    }

    private void processarTag(String tag) {
        // Busca no banco (agora MySQL) usando a tag limpa
        Optional<alunoModel> alunoOpt = bancoRepository.findById(tag);

        if (alunoOpt.isPresent()) {
            alunoModel aluno = alunoOpt.get();
            
            // Incrementa presença
            aluno.setPresenca(aluno.getPresenca() + 1);
            bancoRepository.save(aluno);

            System.out.println("\n==============================");
            System.out.println("   CHAMADA REGISTRADA!");
            System.out.println("   Aluno: " + aluno.getNome());
            System.out.println("   Total de Presenças: " + aluno.getPresenca());
            System.out.println("==============================\n");
        } else {
            System.out.println("\n[AVISO] Nova tag detectada: " + tag);
            System.out.println("[AVISO] Pronta para ser vinculada a um novo aluno.\n");
        }
    }
}