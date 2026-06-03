
package com.mycompany.metamorphosis.model;

/**
 *
 * @author eu
 */
public class Jogador {
    
    private int id;
    private String nome;
    private int pontos;

    public Jogador() {
    }

    public Jogador(String nome, int pontos) {
        this.nome = nome;
        this.pontos = pontos;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getPontos() {
        return pontos;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
    
}
