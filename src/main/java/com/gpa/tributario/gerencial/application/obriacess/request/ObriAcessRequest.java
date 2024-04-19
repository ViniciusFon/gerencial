package com.gpa.tributario.gerencial.application.obriacess.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObriAcessRequest {

    @NotNull
    private LocalDate dataObri;
    @NotNull
    @NotBlank
    private String empresa;
    @NotNull
    @Size(min = 2, max = 2)
    private String uf;
    @NotNull
    @NotBlank
    private String codEstabelecimento;
    @NotNull
    @CNPJ
    private String cnpj;
    private String ie;
    private Integer dapiMg;
    private Integer giaSt;
    private Integer devecRj;
    private Integer sintegra;
    private Integer giaRs;
    private Integer dmaBa;
    private Integer giaSp;
    private Integer sped;
    private Integer dimpMktpSp;
    private Integer dipamSp;
    private Integer total;
    private String formatoBandeira;
    private String im;
    private String cnpjSit;
    private String ieSit;
    private String imSit;
    private String ac01GPA;
    private boolean emOperacao;
    private String endereco;
    private String bairro;
    private String cep;
    private LocalDate dtAberturaCnpj;
    private String cnaePrincipal;
}
