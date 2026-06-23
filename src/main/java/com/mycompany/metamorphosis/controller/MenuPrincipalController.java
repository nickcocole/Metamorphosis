package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class MenuPrincipalController {
    
    @FXML
    private ImageView background;
    
    @FXML
    private ImageView btnJogar;
    @FXML
    private ImageView btnSair;
    @FXML
    private ImageView btnRanking;
    @FXML
    private ImageView btnCreditos;
    @FXML
    private Rectangle fade;

    private Image jogarNormal;
    private Image jogarPressionado;
    private Image sairNormal;
    private Image sairPressionado;
    private Image rankingNormal;
    private Image rankingPressionado;
    private Image creditosNormal;
    private Image creditosPressionado;
    
    private double larguraOriginal;
    private double alturaOriginal;
    private double xOriginal;
    private double yOriginal;

    @FXML
    public void initialize() {
        
        FadeTransition transicao = new FadeTransition(Duration.seconds(1), fade);
        
        transicao.setFromValue(0);
        transicao.setToValue(1);

        jogarNormal = new Image(
                getClass().getResourceAsStream("/stages/jogarnormal.png"));

        jogarPressionado = new Image(
                getClass().getResourceAsStream("/stages/jogarpressionado.png"));

        btnJogar.setImage(jogarNormal);

        larguraOriginal = btnJogar.getFitWidth();
        alturaOriginal = btnJogar.getFitHeight();

        xOriginal = btnJogar.getLayoutX();
        yOriginal = btnJogar.getLayoutY();

        btnJogar.setOnMouseEntered(e -> {

            btnJogar.setFitWidth(larguraOriginal + 2);
            btnJogar.setFitHeight(alturaOriginal + 2);

            btnJogar.setLayoutX(xOriginal - 1);
            btnJogar.setLayoutY(yOriginal - 1);
        });

        btnJogar.setOnMouseExited(e -> {

            btnJogar.setFitWidth(larguraOriginal);
            btnJogar.setFitHeight(alturaOriginal);

            btnJogar.setLayoutX(xOriginal);
            btnJogar.setLayoutY(yOriginal);

            btnJogar.setImage(jogarNormal);
        });

        btnJogar.setOnMousePressed(e ->
                btnJogar.setImage(jogarPressionado)
        );

        btnJogar.setOnMouseReleased(e -> {

            btnJogar.setImage(jogarNormal);

        transicao.setOnFinished(eh -> {
            
                try {
                    
                    jogar();
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            
            });
            
            transicao.play();
            
        });
        

        rankingNormal = new Image(
                getClass().getResourceAsStream("/stages/rankingnormal.png"));

        rankingPressionado = new Image(
                getClass().getResourceAsStream("/stages/rankingpressionado.png"));

        btnRanking.setImage(rankingNormal);

        double larguraOriginalRanking = btnRanking.getFitWidth();
        double alturaOriginalRanking = btnRanking.getFitHeight();

        double xOriginalRanking = btnRanking.getLayoutX();
        double yOriginalRanking = btnRanking.getLayoutY();

        btnRanking.setOnMouseEntered(e -> {

            btnRanking.setFitWidth(larguraOriginalRanking + 2);
            btnRanking.setFitHeight(alturaOriginalRanking + 2);

            btnRanking.setLayoutX(xOriginalRanking - 1);
            btnRanking.setLayoutY(yOriginalRanking - 1);
        });

        btnRanking.setOnMouseExited(e -> {

            btnRanking.setFitWidth(larguraOriginalRanking);
            btnRanking.setFitHeight(alturaOriginalRanking);

            btnRanking.setLayoutX(xOriginalRanking);
            btnRanking.setLayoutY(yOriginalRanking);

            btnRanking.setImage(rankingNormal);
        });

        btnRanking.setOnMousePressed(e ->
                btnRanking.setImage(rankingPressionado)
        );

        btnRanking.setOnMouseReleased(e -> {

            btnRanking.setImage(rankingNormal);

        transicao.setOnFinished(eh -> {
            
                try {
                    
                    ranking();
                    
                } catch (IOException ex) {
                    
                    Logger.getLogger(MenuPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
                    
                }
            
            });
            
            transicao.play();
            
        });

        creditosNormal = new Image(
                getClass().getResourceAsStream("/stages/creditosnormal.png"));

        creditosPressionado = new Image(
                getClass().getResourceAsStream("/stages/creditospressionado.png"));

        btnCreditos.setImage(creditosNormal);

        double larguraOriginalCreditos = btnCreditos.getFitWidth();
        double alturaOriginalCreditos = btnCreditos.getFitHeight();

        double xOriginalCreditos = btnCreditos.getLayoutX();
        double yOriginalCreditos = btnCreditos.getLayoutY();

        btnCreditos.setOnMouseEntered(e -> {

            btnCreditos.setFitWidth(larguraOriginalCreditos + 2);
            btnCreditos.setFitHeight(alturaOriginalCreditos + 2);

            btnCreditos.setLayoutX(xOriginalCreditos - 1);
            btnCreditos.setLayoutY(yOriginalCreditos - 1);
        });

            btnCreditos.setOnMouseExited(e -> {

            btnCreditos.setFitWidth(larguraOriginalCreditos);
            btnCreditos.setFitHeight(alturaOriginalCreditos);

            btnCreditos.setLayoutX(xOriginalCreditos);
            btnCreditos.setLayoutY(yOriginalCreditos);

            btnCreditos.setImage(creditosNormal);
        });

        btnCreditos.setOnMousePressed(e ->
                btnCreditos.setImage(creditosPressionado)
        );

        btnCreditos.setOnMouseReleased(e -> {

            try {
                creditos();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
                

        sairNormal = new Image(
                getClass().getResourceAsStream("/stages/sairnormal.png"));

        sairPressionado = new Image(
                getClass().getResourceAsStream("/stages/sairpressionado.png"));

        btnSair.setImage(sairNormal);

        double larguraOriginalSair = btnSair.getFitWidth();
        double alturaOriginalSair = btnSair.getFitHeight();

        double xOriginalSair = btnSair.getLayoutX();
        double yOriginalSair = btnSair.getLayoutY();

        btnSair.setOnMouseEntered(e -> {

            btnSair.setFitWidth(larguraOriginalSair + 2);
            btnSair.setFitHeight(alturaOriginalSair + 2);

            btnSair.setLayoutX(xOriginalSair - 1);
            btnSair.setLayoutY(yOriginalSair - 1);
        });

        btnSair.setOnMouseExited(e -> {

            btnSair.setFitWidth(larguraOriginalSair);
            btnSair.setFitHeight(alturaOriginalSair);

            btnSair.setLayoutX(xOriginalSair);
            btnSair.setLayoutY(yOriginalSair);

            btnSair.setImage(sairNormal);
        });

        btnSair.setOnMousePressed(e ->
                btnSair.setImage(sairPressionado)
        );

        btnSair.setOnMouseReleased(e -> {

            btnSair.setImage(sairNormal);

            transicao.setOnFinished(eh -> {
                System.exit(0);
            });

            transicao.play();
        });
        
    }

    @FXML
    private void jogar() throws IOException {
        App.setRoot("nomeJogador");
    }
    
    @FXML
    private void ranking() throws IOException {
        App.setRoot("ranking");
    }
    
    @FXML
    private void creditos() throws IOException {
        App.setRoot("creditos");
    }
    
    @FXML
    private void encerrar() throws IOException {
       System.exit(0);
    }
}