package com.gpa.tributario.gerencial.application;

import com.gpa.tributario.gerencial.infrastructure.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public abstract class ArquivoBaseService<T> {

    public void incrementaDados(MultipartFile file){

        if(file.isEmpty()) throw new BusinessException("O Arquivo está vazio");

        try {
            InputStreamReader in = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
            lerArquivoValidacao(in);
            in = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
            lerArquivo(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void lerArquivoValidacao(Reader reader) throws IOException {

        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            long i=1;
            while ((line = br.readLine()) != null) {
                validaLinhaArquivo(i++, line);
            }
        }
    }

    private void validaLinhaArquivo(long numeroLinha, String linha){
        try {
            quebrar(linha);
        }catch (Exception e){
            throw new BusinessException("Arquivo com erro na linha: " + numeroLinha + " - " + e.getMessage());
        }
    }


    protected void lerArquivo(Reader reader) throws IOException {
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                salvar(quebrar(line));
            }
        }
    }

    protected abstract T quebrar(String linha);
    protected abstract void salvar(T entity );
}
