package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import com.mycompany.metamorphosis.DAO.RankingDAO;
import com.mycompany.metamorphosis.model.Jogador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;

public class RankingController implements Initializable {

    @FXML private TableView<Jogador>            tableRanking;
    @FXML private TableColumn<Jogador, String>  colNome;
    @FXML private TableColumn<Jogador, Integer> colPontos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPontos.setCellValueFactory(new PropertyValueFactory<>("pontos"));
        carregarRanking();
        Font.loadFont(
        getClass().getResourceAsStream("/styles/m5x7.ttf"),
        16
        );
    }

    private void carregarRanking() {
        RankingDAO dao = new RankingDAO();
        ObservableList<Jogador> lista =
            FXCollections.observableArrayList(dao.listarRanking());
        tableRanking.setItems(lista);
    }

    @FXML
    private void voltarAoMenuPrincipal() throws IOException {
        App.setRoot("menuPrincipal");
    }
}
