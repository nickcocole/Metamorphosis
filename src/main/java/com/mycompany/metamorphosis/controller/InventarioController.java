package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.model.GerenciadorDeJogo;
import com.mycompany.metamorphosis.model.Item;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class InventarioController implements Initializable {

    @FXML private Label     lblNome;
    @FXML private GridPane  gridItens;
    @FXML private VBox      painelDetalhe;
    @FXML private Label     lblDetalheNome;
    @FXML private Label     lblTotal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNome.setText("🎒 Inventário");
        carregarItens(GerenciadorDeJogo.getInstance().getInventario().getItens());
    }

    public void carregarItens(List<Item> itens) {
        gridItens.getChildren().clear();
        int coluna = 0, linha = 0;

        for (Item item : itens) {
            VBox card = criarCard(item);
            gridItens.add(card, coluna, linha);
            coluna++;
            if (coluna == 4) { coluna = 0; linha++; }
        }

        lblTotal.setText("Total de itens: " + itens.size());
    }

    private VBox criarCard(Item item) {
        ImageView img = new ImageView();
        img.setFitWidth(48);
        img.setFitHeight(48);
        img.setPreserveRatio(true);
        try {
            InputStream is = getClass().getResourceAsStream("/" + item.getIcone());
            if (is != null) img.setImage(new Image(is));
        } catch (Exception ignored) {}

        Label nome = new Label(item.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-size: 9px;");
        nome.setMaxWidth(70);
        nome.setAlignment(Pos.CENTER);
        nome.setWrapText(true);

        VBox card = new VBox(4, img, nome);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #2d1b4e; -fx-border-color: #9b59b6;" +
                      "-fx-border-radius: 6; -fx-background-radius: 6; -fx-padding: 6;" +
                      "-fx-cursor: hand;");
        card.setPrefWidth(80);

        card.setOnMouseClicked(e -> {
            lblDetalheNome.setText(item.getNome() + " — " + item.getPontos() + " pts");
            painelDetalhe.setVisible(true);
            painelDetalhe.setManaged(true);
        });

        return card;
    }

    @FXML
    private void onFechar() {
        lblNome.getScene().getWindow().hide();
    }
}
