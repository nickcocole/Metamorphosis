package com.mycompany.metamorphosis.model;

import java.util.List;

/**
 * Representa uma fase do jogo, com número, personagem narrador e objetivos.
 */
public class Fase {

    private int numero;
    private String nomePersonagem;   // "Layla" ou "Nilipe"
    private String dialogoInicio;    // fala de abertura da fase
    private List<String> objetivos;  // nomes dos itens que precisam ser criados

    public Fase(int numero, String nomePersonagem, String dialogoInicio, List<String> objetivos) {
        this.numero          = numero;
        this.nomePersonagem  = nomePersonagem;
        this.dialogoInicio   = dialogoInicio;
        this.objetivos       = objetivos;
    }

    public int getNumero()              { return numero; }
    public String getNomePersonagem()   { return nomePersonagem; }
    public String getDialogoInicio()    { return dialogoInicio; }
    public List<String> getObjetivos()  { return objetivos; }
}
