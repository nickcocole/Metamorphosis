package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import com.mycompany.metamorphosis.model.GerenciadorDeJogo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class FimDeJogoController implements Initializable {

    @FXML private Label     lblTitulo;
    @FXML private Label     lblFalaLayla;
    @FXML private Label     lblNomeJogador;
    @FXML private Label     lblPontuacao;
    @FXML private ImageView imgLayla;

    private static final String FALA_LAYLA =
        "Muito obrigada, alquimista! O velório do meu pai foi lindo, " +
        "Minha gratidão é eterna... " +
        "Que as estrelas iluminem sempre o seu caminho. 🌟";

    private Timeline timerTexto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();

        lblNomeJogador.setText(g.getJogador().getNome());
        lblPontuacao.setText(String.valueOf(g.getJogador().getPontos()));


        try {
            InputStream is = getClass().getResourceAsStream("/personagens/cigana.png");
            if (is != null) imgLayla.setImage(new Image(is));
        } catch (Exception ignored) {}

 
        digitarTexto(FALA_LAYLA);
    }

    private void digitarTexto(String texto) {
        lblFalaLayla.setText("");
        final int[] idx = {0};
        timerTexto = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            if (idx[0] < texto.length()) {
                lblFalaLayla.setText(lblFalaLayla.getText() + texto.charAt(idx[0]));
                idx[0]++;
            } else {
                timerTexto.stop();
            }
        }));
        timerTexto.setCycleCount(Timeline.INDEFINITE);
        timerTexto.play();
    }

    @FXML
    private void verRanking() throws IOException {
        if (timerTexto != null) timerTexto.stop();
        App.setRoot("ranking");
    }

    @FXML
    private void voltarMenu() throws IOException {
        if (timerTexto != null) timerTexto.stop();
        App.setRoot("menuPrincipal");
    }
}
