package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.model.GerenciadorDeJogo;
import com.mycompany.metamorphosis.model.Item;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.util.Duration;

/**
 * Controla o comportamento do boss Nilipe na Fase 3.
 *
 * Uso no GameplayController:
 *   BossController boss = new BossController();
 *   boss.setOnRoubo(nomeItem -> { // atualizar UI });
 *   boss.iniciar();
 *   // quando a fase acabar:
 *   boss.parar();
 */
public class BossController implements Initializable {

    private Timeline timer;
    private Consumer<String> onRoubo; // callback para a view reagir ao roubo

    /** Define o callback chamado quando Nilipe rouba um item. */
    public void setOnRoubo(Consumer<String> callback) {
        this.onRoubo = callback;
    }

    /** Inicia o timer de roubo com intervalo aleatório entre 18 e 28 segundos. */
    public void iniciar() {
        agendarProximoRoubo();
        timer.play();
    }

    /** Para o timer (chamar ao fim da fase 3 ou ao sair da gameplay). */
    public void parar() {
        if (timer != null) timer.stop();
    }

    private void agendarProximoRoubo() {
        double intervalo = 18 + Math.random() * 10; // 18–28 segundos

        timer = new Timeline(new KeyFrame(Duration.seconds(intervalo), e -> {
            Item roubado = GerenciadorDeJogo.getInstance().removerItemAleatorio();

            if (roubado != null && onRoubo != null) {
                Platform.runLater(() -> onRoubo.accept(roubado.getNome()));
            }

            // Agenda o próximo roubo com novo intervalo
            parar();
            agendarProximoRoubo();
            timer.play();
        }));
    }

    // Necessário por implementar Initializable (caso seja usado via FXML)
    @Override
    public void initialize(URL url, ResourceBundle rb) { }
}
