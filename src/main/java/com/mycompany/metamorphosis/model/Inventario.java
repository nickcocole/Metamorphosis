package com.mycompany.metamorphosis.model;

import java.util.ArrayList;
import java.util.List;

public class Inventario {

    private List<Item> itens = new ArrayList<>();

    public void adicionar(Item item) {
        if (!possui(item.getNome())) {
            itens.add(item);
        }
    }

    public boolean possui(String nome) {
        return itens.stream().anyMatch(i -> i.getNome().equals(nome));
    }


    public boolean remover(String nome) {
        return itens.removeIf(i -> i.getNome().equals(nome));
    }

    public List<Item> getItens() {
        return itens;
    }
}
