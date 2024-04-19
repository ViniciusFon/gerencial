package com.gpa.tributario.gerencial.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioID {

    private LocalDate data;
    private String uf;
}
