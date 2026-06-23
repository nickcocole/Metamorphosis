/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;


/**
 * FXML Controller class
 *
 * @author aluno
 */
public class CreditosController implements Initializable {
    
        
    @FXML
    private ImageView backgroundcreditos;
    
    @FXML
    private ImageView btnSair;
    
    @FXML
    private VBox vbox;
    
    @FXML
    private Label lblCreditos;
    
    @FXML
    private Label l1;
    @FXML
    private Label l2;
    @FXML
    private Label l3;
    @FXML
    private Label l4;
    
    private Image sairNormal;
    private Image sairPressionado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println(
        getClass().getResource("/styles/Pixellari.ttf")
        );
        
        Font fonte = Font.loadFont(
            getClass().getResourceAsStream("/styles/Pixellari.ttf"),
            24
        );

        lblCreditos.setFont(fonte);
        l1.setFont(fonte);
        l2.setFont(fonte);
        l3.setFont(fonte);
        l4.setFont(fonte);
        
        TranslateTransition subirFundo = new TranslateTransition(Duration.seconds(1), backgroundcreditos);
        subirFundo.setToY(-408);

        TranslateTransition subirCreditos = new TranslateTransition(Duration.seconds(2), vbox);
        subirCreditos.setFromY(600);
        subirCreditos.setToY(-700);
        
        TranslateTransition descerFundo = new TranslateTransition(Duration.seconds(1), backgroundcreditos);
        descerFundo.setToY(0);

        TranslateTransition descerCreditos = new TranslateTransition(Duration.seconds(1), vbox);
        descerCreditos.setToY(0);
        
        ParallelTransition saida = new ParallelTransition(descerFundo, descerCreditos);
        
        saida.setOnFinished(eh -> {
            
            try {
                
                App.setRoot("menuPrincipal");
                
            } catch (IOException ex) {
                
                ex.printStackTrace();
                
            }
            
        });

        subirFundo.play();
        subirCreditos.play();
        

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

            saida.play();
        });
        
    }    
    
    @FXML
    private void voltar() throws IOException {
        App.setRoot("menuPrincipal");
    }
    
}
