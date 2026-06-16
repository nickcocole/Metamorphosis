
package com.mycompany.metamorphosis.model;

/**
 *
 * @author eu
 */
public class Item {
    
    private String icone;
    private String nome;
    private String descricao;
    private String tipo;
    private int quantidade;

    public Item(String icone, String nome, String descricao, String tipo, int quantidade) {
        this.icone = icone;
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.quantidade = quantidade;
    }

    public String getIcone() {
        return icone;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    
    
    //somar quantidade ao pegar o item mais de uma vez
    
    public void adicionarQuantidade(int qtd){
        this.quantidade += qtd; 
    }
    
}
