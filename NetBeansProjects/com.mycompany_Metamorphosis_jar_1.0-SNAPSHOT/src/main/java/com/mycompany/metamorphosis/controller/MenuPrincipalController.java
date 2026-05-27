package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class MenuPrincipalController {

    @FXML
    private void jogar() throws IOException {
        App.setRoot("gameplay");
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
