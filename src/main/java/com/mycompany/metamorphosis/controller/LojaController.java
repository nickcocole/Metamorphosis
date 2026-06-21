package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.model.GerenciadorDeJogo;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * Controla a loja de upgrades disponível na Fase 3.
 * Permite trocar pontos por: tempo extra de cronômetro,
 * proteção contra o roubo do boss Nilipe, e dicas de combinação.
 */
public class LojaController implements Initializable {

    @FXML private Label lblPontosAtuais;
    @FXML private Label lblMensagem;

    private static final int CUSTO_TEMPO_EXTRA = 800;
    private static final int SEGUNDOS_TEMPO_EXTRA = 60;

    private static final int CUSTO_PROTECAO = 600;
    private static final int SEGUNDOS_PROTECAO = 30;

    private static final int CUSTO_DICA = 400;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        atualizarPontos();
    }

    private void atualizarPontos() {
        lblPontosAtuais.setText(
            String.valueOf(GerenciadorDeJogo.getInstance().getJogador().getPontos()));
    }

    @FXML
    private void comprarTempoExtra() {
        boolean sucesso = GerenciadorDeJogo.getInstance()
            .comprarTempoExtraCronometro(CUSTO_TEMPO_EXTRA, SEGUNDOS_TEMPO_EXTRA);

        if (sucesso) {
            mostrarMensagem("✅ +60 segundos serão adicionados ao cronômetro!", true);
        } else {
            mostrarMensagem("❌ Pontos insuficientes para esse upgrade.", false);
        }
        atualizarPontos();
    }

    @FXML
    private void comprarProtecao() {
        boolean sucesso = GerenciadorDeJogo.getInstance()
            .comprarProtecaoRoubo(CUSTO_PROTECAO, SEGUNDOS_PROTECAO);

        if (sucesso) {
            mostrarMensagem("✅ Nilipe vai esperar mais 30s para roubar de novo!", true);
        } else {
            mostrarMensagem("❌ Pontos insuficientes para esse upgrade.", false);
        }
        atualizarPontos();
    }

    @FXML
    private void comprarDica() {
        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();
        String objetivoPendente = g.getProximoObjetivoPendente();

        if (objetivoPendente == null) {
            mostrarMensagem("Você já completou todos os objetivos!", true);
            return;
        }

        boolean sucesso = g.comprarDica(CUSTO_DICA);
        if (sucesso) {
            String dica = g.getDicaParaObjetivo(objetivoPendente);
            mostrarMensagem("💡 Dica: " + dica, true);
        } else {
            mostrarMensagem("❌ Pontos insuficientes para essa dica.", false);
        }
        atualizarPontos();
    }

    private void mostrarMensagem(String texto, boolean sucesso) {
        lblMensagem.setText(texto);
        lblMensagem.setStyle("-fx-font-size: 12px; -fx-text-fill: " +
            (sucesso ? "#50fa7b" : "#ff6b6b") + ";");
    }

    @FXML
    private void fechar() {
        lblPontosAtuais.getScene().getWindow().hide();
    }
}