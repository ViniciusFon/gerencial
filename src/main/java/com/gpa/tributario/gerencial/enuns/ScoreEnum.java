package com.gpa.tributario.gerencial.enuns;

public enum ScoreEnum {

    LEVE("Leve"), INTENSO("Intenso"), MODERADO("Moderado");

    private final String descricao;

    ScoreEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


}
