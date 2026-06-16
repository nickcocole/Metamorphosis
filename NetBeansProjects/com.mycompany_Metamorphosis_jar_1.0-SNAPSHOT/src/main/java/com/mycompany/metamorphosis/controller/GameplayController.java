package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameplayController {

    
    
    
    @FXML
    private void voltar() throws IOException {
        App.setRoot("menuPrincipal");
    }
    
    @FXML
    private void abrirInventario() throws IOException {
        
        try {
            // 1. Carregar o FXML do inventário
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/mycompany/metamorphosis/inventario.fxml")
            );
            Parent root = loader.load();
                       
            // 2. Pegar o controller do inventário
             InventarioController invCtrl = loader.getController();
                         
             // 3. Passar os dados do jogador
             //invCtrl.setDados(nomeJogador, vida, vidaMax, ouro, listaDeItens);
                         
             // 4. Criar o novo Stage (nova janela)
             Stage invStage = new Stage();
             invStage.setTitle("Inventario");
             invStage.setScene(new Scene(root));
             invStage.setResizable(false);
             
             // 5. Bloquear o jogo e abrir
             invStage.initModality(Modality.APPLICATION_MODAL);
             invStage.showAndWait();
             } 
        catch (Exception e) {
             e.printStackTrace();
        }

    }

}