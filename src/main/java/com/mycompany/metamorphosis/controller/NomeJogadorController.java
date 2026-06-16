package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import com.mycompany.metamorphosis.model.GerenciadorDeJogo;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NomeJogadorController {

    @FXML private TextField txtNome;
    @FXML private Label     lblErro;

    @FXML
    private void confirmar() throws IOException {
        String nome = txtNome.getText().trim();

        if (nome.isEmpty()) {
            lblErro.setText("Por favor, informe seu nome para continuar.");
            lblErro.setVisible(true);
            return;
        }

        // Inicia uma nova partida com o nome digitado
        GerenciadorDeJogo.getInstance().novaPartida(nome);

        // Vai para a gameplay
        App.setRoot("gameplay");
    }
}
