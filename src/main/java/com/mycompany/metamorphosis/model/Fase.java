package com.mycompany.metamorphosis.model;

import java.util.List;

public class Fase {

    private int numero;
    private String nomePersonagem;   
    private String dialogoInicio;    
    private List<String> objetivos;  

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
