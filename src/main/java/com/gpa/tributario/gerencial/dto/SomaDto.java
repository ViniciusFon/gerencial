package com.gpa.tributario.gerencial.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SomaDto {

    private LocalDate _id;
    private double total;
}
