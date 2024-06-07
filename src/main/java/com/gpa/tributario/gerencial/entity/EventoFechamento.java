package com.gpa.tributario.gerencial.entity;

import com.gpa.tributario.gerencial.enuns.EmpresaEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "eventoFechamento")
public class EventoFechamento {

    @Id
    private String id;
    private String UF;
    private String nome;
    private EmpresaEnum empresa;
    private String nomeRelatorio;
    private boolean concluido;
    private List<HistoricoEventoFechamento> historico;

    public void addHistorico(HistoricoEventoFechamento hist){
        if(this.historico == null){
            this.historico = new ArrayList<>();
        }

        this.historico.add(hist);
    }

}
