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

public class BossController implements Initializable {

    private Timeline timer;
    private Consumer<String> onRoubo; 

  
    public void setOnRoubo(Consumer<String> callback) {
        this.onRoubo = callback;
    }


    public void iniciar() {
        agendarProximoRoubo();
        timer.play();
    }

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

            parar();
            agendarProximoRoubo();
            timer.play();
        }));
    }

 
    @Override
    public void initialize(URL url, ResourceBundle rb) { }
}
