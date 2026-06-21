package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import com.mycompany.metamorphosis.model.GerenciadorDeJogo;
import com.mycompany.metamorphosis.model.Item;

import java.io.IOException;
import java.io.InputStream;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameplayController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private Label      lblFase;
    @FXML private Label      lblJogador;
    @FXML private Label      lblPontos;
    @FXML private Label      lblCronometro;
    @FXML private Button     btnLoja;
    @FXML private AnchorPane areaJogo;
    @FXML private FlowPane   painelElementos;
    @FXML private ImageView  imgPersonagem;
    @FXML private Label      lblNomePersonagem;
    @FXML private Label      lblDialogo;
    @FXML private VBox       painelObjetivos;
    @FXML private StackPane  overlayCombo;
    @FXML private ImageView  imgComboIcone;
    @FXML private Label      lblComboTitulo;
    @FXML private Label      lblComboNome;
    @FXML private Label      lblComboPontos;
    @FXML private StackPane  overlayTempoEsgotado;
    
    private double mouseXInicial;
    private double mouseYInicial;
    private double cardXInicial;
    private double cardYInicial;

    // ── Drag ──────────────────────────────────────────────────────────────
    private StackPane elementoArrastado = null;
    private double    dragOffsetX, dragOffsetY;

    // ── Flags de controle ────────────────────────────────────────────────
    private boolean faseJaConcluida = false;
    private boolean tempoEsgotado   = false;

    // ── Cronômetro ───────────────────────────────────────────────────────
    private Timeline timerCronometro;
    private int segundosRestantes;

    // ── Diálogos ─────────────────────────────────────────────────────────
    private static final String DIALOGO_FASE1 =
        "Olá, alquimista! Sou Layla, a cigana das estrelas. " +
        "Preciso urgentemente de um CALDEIRÃO para preparar minha poção. " +
        "Combine os elementos e traga-me o que peço... o destino depende disso! 🔮";

    private static final String DIALOGO_FASE2 =
        "GRRR! Eu sou Nilipe, e estou FAMINTO! " +
        "Me traga uma AVE ASSADA e um PESCADO ASSADO agora mesmo... " +
        "ou vou devorar seus elementos! 👾";

    private static final String DIALOGO_FASE3_LAYLA =
        "Meu pai se foi... Preciso preparar seu velório com dignidade. " +
        "Faça o CAIXÃO, reúna a FAMÍLIA e monte o BUQUÊ de despedida. 🕯️";

    // ── Timers ───────────────────────────────────────────────────────────
    private Timeline timerBoss;
    private Timeline timerTexto;

    // ═══════════════════════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        atualizarHUD();
        carregarElementosNaBarraLateral();
        iniciarFase();
    }

    // ── HUD ──────────────────────────────────────────────────────────────
    private void atualizarHUD() {
        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();
        lblFase.setText("Fase " + g.getFaseAtual());
        lblJogador.setText(g.getJogador().getNome());
        lblPontos.setText(String.valueOf(g.getJogador().getPontos()));
        atualizarObjetivos();

        // A loja é visível APENAS na fase 3
        boolean fase3 = g.getFaseAtual() == 3;
        btnLoja.setVisible(fase3);
        btnLoja.setManaged(fase3);
    }

    private void atualizarObjetivos() {
        painelObjetivos.getChildren().clear();
        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();
        for (String obj : g.getObjetivosFaseAtual()) {
            boolean feito = g.getInventario().possui(obj);
            Label lbl = new Label((feito ? "✅ " : "⬜ ") + obj);
            lbl.setStyle("-fx-text-fill: " + (feito ? "#50fa7b" : "#dddddd") +
                         "; -fx-font-size: 12px;");
            painelObjetivos.getChildren().add(lbl);
        }
    }

    // ── Barra lateral ────────────────────────────────────────────────────
    private void carregarElementosNaBarraLateral() {
        painelElementos.getChildren().clear();
        for (Item item : GerenciadorDeJogo.getInstance().getInventario().getItens()) {
            painelElementos.getChildren().add(criarCartaoElemento(item, true));
        }
    }

    private StackPane criarCartaoElemento(Item item, boolean naBarraLateral) {
        // Ícone
        ImageView img = new ImageView();
        img.setFitWidth(55);
        img.setFitHeight(55);
        img.setPreserveRatio(true);
        
        try {
            InputStream is = getClass().getResourceAsStream("/" + item.getIcone());
            if (is != null) img.setImage(new Image(is));
        } catch (Exception ignored) {}

        // Label nome
        Label nome = new Label(item.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-wrap-text: true;  -fx-font-family: 'm5x7';");
        nome.setMaxWidth(70);
        nome.setAlignment(Pos.CENTER);

        // Container vertical
        VBox conteudo = new VBox(4, img, nome);
        conteudo.setAlignment(Pos.CENTER);

        // Card com fundo
        Rectangle bg = new Rectangle(76, 76);
        bg.setFill(Color.web("#6682b5"));
        bg.setStroke(Color.web("#4E6982"));
        bg.setStrokeWidth(1.5);
        bg.setArcWidth(10);
        bg.setArcHeight(10);

        StackPane card = new StackPane(bg, conteudo);
        card.setCursor(Cursor.HAND);
        card.setUserData(item.getNome()); // guarda o nome para combinar

        if (naBarraLateral) {
            // Clique na barra → lança o elemento na área de jogo
            card.setOnMouseClicked(e -> lancarElementoNaArea(item));
        } else {
            // Está na área → pode ser arrastado
            habilitarDrag(card);
        }

        return card;
    }

    private void lancarElementoNaArea(Item item) {
        StackPane card = criarCartaoElemento(item, false);
        double largura = areaJogo.getWidth()  > 0 ? areaJogo.getWidth()  : 900;
        double altura  = areaJogo.getHeight() > 0 ? areaJogo.getHeight() : 500;
        double x = 100 + Math.random() * Math.max(largura  - 180, 50);
        double y = 60  + Math.random() * Math.max(altura   - 160, 50);
        AnchorPane.setLeftAnchor(card, x);
        AnchorPane.setTopAnchor(card, y);
        areaJogo.getChildren().add(card);
    }

    // ── Drag-and-drop ────────────────────────────────────────────────────
     private void habilitarDrag(StackPane card) {

        card.setOnMousePressed(e -> {
            elementoArrastado = card;

            mouseXInicial = e.getSceneX();
            mouseYInicial = e.getSceneY();

            cardXInicial = AnchorPane.getLeftAnchor(card);
            cardYInicial = AnchorPane.getTopAnchor(card);

            card.toFront();
        });

        card.setOnMouseDragged(e -> {
            if (elementoArrastado == null) return;

            double dx = e.getSceneX() - mouseXInicial;
            double dy = e.getSceneY() - mouseYInicial;

            AnchorPane.setLeftAnchor(card, cardXInicial + dx);
            AnchorPane.setTopAnchor(card, cardYInicial + dy);
        });

        card.setOnMouseReleased(e -> {
            if (elementoArrastado == null) return;

            // Verifica colisão com outro card na área
            StackPane colisao = encontrarColisao(card);
            if (colisao != null) {
                String nomeA = (String) card.getUserData();
                String nomeB = (String) colisao.getUserData();
                processarCombinacao(nomeA, nomeB, card, colisao);
            }
            elementoArrastado = null;
        });
    }


    private StackPane encontrarColisao(StackPane origem) {
        double ox = AnchorPane.getLeftAnchor(origem);
        double oy = AnchorPane.getTopAnchor(origem);
        for (var node : areaJogo.getChildren()) {
            if (!(node instanceof StackPane)) continue;
            StackPane alvo = (StackPane) node;
            if (alvo == origem) continue;
            double ax = AnchorPane.getLeftAnchor(alvo);
            double ay = AnchorPane.getTopAnchor(alvo);
            if (Math.abs(ox - ax) < 70 && Math.abs(oy - ay) < 70) return alvo;
        }
        return null;
    }

    // ── Combinação ───────────────────────────────────────────────────────
    private void processarCombinacao(String nomeA, String nomeB,
                                     StackPane cardA, StackPane cardB) {
        if (tempoEsgotado) return;

        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();

        // Verifica se o elemento resultante já está no inventário ANTES de combinar
        boolean jaExistia = g.jaConheceCombinacao(nomeA, nomeB);

        String resultado = g.tentarCombinar(nomeA, nomeB);

        if (resultado != null) {
            areaJogo.getChildren().removeAll(cardA, cardB);
            carregarElementosNaBarraLateral();
            atualizarHUD();

            // Só mostra overlay e conta pontos se o elemento é novo
            if (!jaExistia) {
                mostrarOverlayCombo(resultado);
                if (resultado.equals("AYCABRON")) mostrarEasterEgg();
            }

            if (!faseJaConcluida && g.faseCompleta()) {
                faseJaConcluida = true;
                pararCronometro();
                int faseAtual = g.getFaseAtual();

                PauseTransition pausa = new PauseTransition(Duration.seconds(2.0));
                pausa.setOnFinished(e -> {
                    if (faseAtual >= 3) {
                        finalizarJogo();
                    } else {
                        avancarFase();
                    }
                });
                pausa.play();
            }
        } else {
            piscarVermelho(cardA);
        }
    }

    private void mostrarOverlayCombo(String nomeResultado) {
        boolean easter = nomeResultado.equals("AYCABRON");
        lblComboTitulo.setText(easter ? "🌟 EASTER EGG DESCOBERTO! 🌟" : "✨ Novo elemento!");
        lblComboNome.setText(nomeResultado);
        lblComboPontos.setText("+" + (easter ? "5000" : "500") + " pontos!");

        try {
            InputStream is = getClass().getResourceAsStream(
                "/" + GerenciadorDeJogo.iconeDoItem(nomeResultado));
            if (is != null) imgComboIcone.setImage(new Image(is));
        } catch (Exception ignored) {}

        overlayCombo.setVisible(true);
        overlayCombo.setManaged(true);

        PauseTransition pausa = new PauseTransition(Duration.seconds(easter ? 4.0 : 2.0));
        pausa.setOnFinished(e -> {
            overlayCombo.setVisible(false);
            overlayCombo.setManaged(false);
        });
        pausa.play();
    }

    private void mostrarEasterEgg() {
        lblComboTitulo.setStyle("-fx-font-size: 28px; -fx-text-fill: #ff79c6; -fx-font-weight: bold;");
        overlayCombo.setStyle("-fx-background-color: rgba(80,0,80,0.92);");
    }

    private void piscarVermelho(StackPane card) {
        if (card.getChildren().isEmpty()) return;
        var bg = card.getChildren().get(0);
        String original = bg.getStyle();
        bg.setStyle(original + " -fx-effect: dropshadow(gaussian, red, 12, 0.8, 0, 0);");
        PauseTransition p = new PauseTransition(Duration.millis(400));
        p.setOnFinished(e -> bg.setStyle(original));
        p.play();
    }

    // ── Fases ────────────────────────────────────────────────────────────
    private void iniciarFase() {
        faseJaConcluida = false;
        tempoEsgotado    = false;
        overlayTempoEsgotado.setVisible(false);
        overlayTempoEsgotado.setManaged(false);

        int fase = GerenciadorDeJogo.getInstance().getFaseAtual();
        switch (fase) {
            case 1: iniciarFase1(); break;
            case 2: iniciarFase2(); break;
            case 3: iniciarFase3(); break;
        }
        iniciarCronometro();
    }

    private void iniciarFase1() {
        carregarPersonagem("personagens/cigana.png", "Layla");
        digitarDialogo(DIALOGO_FASE1);
    }

    private void iniciarFase2() {
        carregarPersonagem("personagens/monstro.png", "Nilipe");
        digitarDialogo(DIALOGO_FASE2);
    }

    private void iniciarFase3() {
        carregarPersonagem("personagens/cigana.png", "Layla");
        digitarDialogo(DIALOGO_FASE3_LAYLA);
        iniciarBoss();
    }

    private void avancarFase() {
        pararBoss();
        pararCronometro();
        boolean avancou = GerenciadorDeJogo.getInstance().avancarFase();
        if (!avancou) {
            // Era a última fase — vai para fim de jogo
            finalizarJogo();
            return;
        }
        areaJogo.getChildren().clear();
        atualizarHUD();
        carregarElementosNaBarraLateral();
        iniciarFase();
    }

    // ── Fim de jogo ──────────────────────────────────────────────────────
    private void finalizarJogo() {
        pararBoss();
        pararCronometro();
        com.mycompany.metamorphosis.DAO.RankingDAO dao =
            new com.mycompany.metamorphosis.DAO.RankingDAO();
        dao.salvarPontuacao(GerenciadorDeJogo.getInstance().getJogador());
        try {
            App.setRoot("fimDeJogo");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // ── Cronômetro ───────────────────────────────────────────────────────
    private void iniciarCronometro() {
        pararCronometro();

        int tempoExtra = GerenciadorDeJogo.getInstance().consumirTempoExtra();
        segundosRestantes = GerenciadorDeJogo.TEMPO_PADRAO_FASE + tempoExtra;
        lblCronometro.setStyle("-fx-text-fill: #50d0ff; -fx-font-size: 32px; -fx-font-family: 'm5x7'; -fx-font-weight: bold;");
        atualizarLabelCronometro();

        timerCronometro = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            segundosRestantes--;
            atualizarLabelCronometro();

            if (segundosRestantes <= 10 && segundosRestantes > 0) {
                lblCronometro.setStyle("-fx-text-fill: #ff6b6b; -fx-font-size: 32px; -fx-font-family: 'm5x7'; -fx-font-weight: bold;");
            }

            if (segundosRestantes <= 0) {
                tempoEsgotarSeChegouAZero();
            }
        }));
        timerCronometro.setCycleCount(Timeline.INDEFINITE);
        timerCronometro.play();
    }

    private void atualizarLabelCronometro() {
        int min = Math.max(segundosRestantes, 0) / 60;
        int seg = Math.max(segundosRestantes, 0) % 60;
        lblCronometro.setText(String.format("%02d:%02d", min, seg));
    }

    private void tempoEsgotarSeChegouAZero() {
        if (faseJaConcluida || tempoEsgotado) return;
        tempoEsgotado = true;
        pararCronometro();
        pararBoss();
        Platform.runLater(() -> {
            overlayTempoEsgotado.setVisible(true);
            overlayTempoEsgotado.setManaged(true);
            overlayTempoEsgotado.toFront();
        });
    }

    private void pararCronometro() {
        if (timerCronometro != null) timerCronometro.stop();
    }

    @FXML
    private void recomecarFase() {
        overlayTempoEsgotado.setVisible(false);
        overlayTempoEsgotado.setManaged(false);

        // Restaura inventário ao estado do início da fase (sem os elementos desbloqueados nela)
        GerenciadorDeJogo.getInstance().reiniciarFaseAtual();

        areaJogo.getChildren().clear();
        atualizarHUD();
        carregarElementosNaBarraLateral();
        iniciarFase();
    }

    // ── Boss (Fase 3) ────────────────────────────────────────────────────
    private void iniciarBoss() {
        agendarProximoRoubo();
    }

    private void agendarProximoRoubo() {
        if (timerBoss != null) timerBoss.stop();

        int protecaoExtra = GerenciadorDeJogo.getInstance().consumirProtecaoRoubo();
        double intervalo = 18 + Math.random() * 10 + protecaoExtra;

        timerBoss = new Timeline(new KeyFrame(Duration.seconds(intervalo), e -> {
            if (tempoEsgotado || faseJaConcluida) return;

            Item roubado = GerenciadorDeJogo.getInstance().removerItemAleatorio();
            if (roubado != null) {
                Platform.runLater(() -> {
                    carregarElementosNaBarraLateral();
                    atualizarHUD();
                    mostrarAlertaBoss(roubado.getNome());
                });
            }
            agendarProximoRoubo();
        }));
        timerBoss.setCycleCount(1);
        timerBoss.play();
    }

    private void mostrarAlertaBoss(String nomeRoubado) {
        carregarPersonagem("personagens/monstro.png", "Nilipe");
        digitarDialogo("😈 NOM NOM NOM! Roubei seu " + nomeRoubado + "! Hahaha!");
    }

    private void pararBoss() {
        if (timerBoss != null) timerBoss.stop();
    }

    // ── Diálogo typewriter ───────────────────────────────────────────────
    private void carregarPersonagem(String caminhoImg, String nome) {
        lblNomePersonagem.setText(nome);
        try {
            InputStream is = getClass().getResourceAsStream("/" + caminhoImg);
            if (is != null) imgPersonagem.setImage(new Image(is));
        } catch (Exception ignored) {}
    }

    private void digitarDialogo(String texto) {
        if (timerTexto != null) timerTexto.stop();
        lblDialogo.setText("");
        final int[] idx = {0};
        timerTexto = new Timeline(new KeyFrame(Duration.millis(28), e -> {
            if (idx[0] < texto.length()) {
                lblDialogo.setText(lblDialogo.getText() + texto.charAt(idx[0]));
                idx[0]++;
            } else {
                timerTexto.stop();
            }
        }));
        timerTexto.setCycleCount(Timeline.INDEFINITE);
        timerTexto.play();
    }

    // ── Botões FXML ──────────────────────────────────────────────────────
    @FXML
    private void voltar() throws IOException {
        pararBoss();
        pararCronometro();
        App.setRoot("menuPrincipal");
    }


    @FXML
    private void abrirLoja() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mycompany/metamorphosis/loja.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Loja");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            // Atualiza HUD após fechar loja (pontos podem ter mudado)
            atualizarHUD();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}