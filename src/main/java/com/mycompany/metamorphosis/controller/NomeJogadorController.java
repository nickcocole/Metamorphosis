package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import com.mycompany.metamorphosis.model.GerenciadorDeJogo;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

public class NomeJogadorController {

    @FXML private TextField txtNome;
    @FXML private Label     lblErro;
    
    @FXML
    public void initialize () {
        
        
        Font.loadFont(
        getClass().getResourceAsStream("/styles/m5x7.ttf"),
        16
        );
        
    }

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
