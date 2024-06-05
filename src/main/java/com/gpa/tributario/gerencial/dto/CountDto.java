package com.gpa.tributario.gerencial.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountDto {

    private String chave;
    private long total;
}
