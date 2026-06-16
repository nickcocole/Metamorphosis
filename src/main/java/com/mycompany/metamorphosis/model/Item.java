package com.mycompany.metamorphosis.model;

public class Item {

    private String icone;
    private String nome;
    private int pontos;

    public Item(String icone, String nome) {
        this.icone  = icone;
        this.nome   = nome;
        this.pontos = nome.equals("AYCABRON") ? 5000 : 500;
    }

    public String getIcone()      { return icone; }
    public void setIcone(String i){ this.icone = i; }

    public String getNome()       { return nome; }
    public void setNome(String n) { this.nome = n; }

    public int getPontos()        { return pontos; }
    public void setPontos(int p)  { this.pontos = p; }
}
