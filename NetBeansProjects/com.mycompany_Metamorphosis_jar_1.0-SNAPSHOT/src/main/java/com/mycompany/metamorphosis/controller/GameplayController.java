package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class GameplayController {

    @FXML
    private void voltar() throws IOException {
        App.setRoot("menuPrincipal");
    }
    
    @FXML
    private void abrirInventario() throws IOException {
       Stage stage = new Stage();
        stage.setTitle("Inventario");
        stage.show();
    }

}