package com.mycompany.metamorphosis.controller;

import com.mycompany.metamorphosis.App;
import com.mycompany.metamorphosis.model.GerenciadorDeJogo;
import com.mycompany.metamorphosis.model.Item;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
    @FXML private Label     lblFase;
    @FXML private Label     lblJogador;
    @FXML private Label     lblPontos;
    @FXML private AnchorPane areaJogo;
    @FXML private FlowPane  painelElementos;
    @FXML private ImageView imgPersonagem;
    @FXML private Label     lblNomePersonagem;
    @FXML private Label     lblDialogo;
    @FXML private VBox      painelObjetivos;
    @FXML private StackPane overlayCombo;
    @FXML private ImageView imgComboIcone;
    @FXML private Label     lblComboTitulo;
    @FXML private Label     lblComboNome;
    @FXML private Label     lblComboPontos;

    // ── Estado interno do drag ─────────────────────────────────────────────
    private StackPane elementoArrastado = null;
    private double    dragOffsetX, dragOffsetY;

    // ── Diálogos das fases ─────────────────────────────────────────────────
    private static final String DIALOGO_FASE1 =
        "Olá, transmorfo! Sou Layla, a cigana das estrelas. " +
        "Preciso urgentemente de um CALDEIRÃO para preparar minha poção. " +
        "Combine os elementos e traga-me o que peço... o destino depende disso! 🔮";

    private static final String DIALOGO_FASE2 =
        "¡GRRR! ¡Soy Nilipe y estoy HAMBRIENTO! " +
        "Tráeme una AVE ASADA y un PESCADO ASADO ahora mismo..." +
        "¡o devoraré tus elementos! 👾";

    private static final String DIALOGO_FASE3_LAYLA =
        "Meu pai se foi... Preciso preparar seu velório com dignidade. " +
        "Faça o CAIXÃO, reúna a FAMÍLIA e monte o BUQUÊ de despedida. 🕯️";

    private static final String DIALOGO_FASE3_NILIPE =
        "¡Sigo aquí! ¡Y con MÁS HAMBRE que antes! " +
        "Voy a robar tus ítems hasta que termines... ¡si es que puedes! 😈";

    // ── Timer do Boss ──────────────────────────────────────────────────────
    private Timeline timerBoss;

    // ── Typewriter ─────────────────────────────────────────────────────────
    private Timeline timerTexto;

    // ═══════════════════════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        atualizarHUD();
        carregarElementosNaBarraLateral();
        iniciarFase();
    }

    // ── HUD ────────────────────────────────────────────────────────────────
    private void atualizarHUD() {
        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();
        lblFase.setText("Fase " + g.getFaseAtual());
        lblJogador.setText(g.getJogador().getNome());
        lblPontos.setText(String.valueOf(g.getJogador().getPontos()));
        atualizarObjetivos();
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

    // ── Barra lateral com os elementos disponíveis ─────────────────────────
    private void carregarElementosNaBarraLateral() {
        painelElementos.getChildren().clear();
        for (Item item : GerenciadorDeJogo.getInstance().getInventario().getItens()) {
            painelElementos.getChildren().add(criarCartaoElemento(item, true));
        }
    }

    /** Cria um card visual do elemento (na barra OU na área de jogo). */
    private StackPane criarCartaoElemento(Item item, boolean naBarraLateral) {
        // Ícone
        ImageView img = new ImageView();
        img.setFitWidth(40);
        img.setFitHeight(40);
        img.setPreserveRatio(true);
        try {
            InputStream is = getClass().getResourceAsStream("/" + item.getIcone());
            if (is != null) img.setImage(new Image(is));
        } catch (Exception ignored) {}

        // Label nome
        Label nome = new Label(item.getNome());
        nome.setStyle("-fx-text-fill: white; -fx-font-size: 9px; -fx-wrap-text: true;");
        nome.setMaxWidth(70);
        nome.setAlignment(Pos.CENTER);

        // Container vertical
        VBox conteudo = new VBox(4, img, nome);
        conteudo.setAlignment(Pos.CENTER);

        // Card com fundo
        Rectangle bg = new Rectangle(76, 76);
        bg.setFill(Color.web("#2d1b4e"));
        bg.setStroke(Color.web("#9b59b6"));
        bg.setStrokeWidth(1.5);
        bg.setArcWidth(10);
        bg.setArcHeight(10);

        StackPane card = new StackPane(bg, conteudo);
        card.setCursor(Cursor.HAND);
        card.setUserData(item.getNome()); // guarda o nome para combinar

        if (naBarraLateral) {
            // Clique na barra → lança o elemento na área de jogo
            card.setOnMouseClicked(e -> lançarElementoNaArea(item));
        } else {
            // Está na área → pode ser arrastado
            habilitarDrag(card);
        }

        return card;
    }

    /** Lança uma cópia do elemento na área de drop no centro. */
    private void lançarElementoNaArea(Item item) {
        StackPane card = criarCartaoElemento(item, false);

        // Posição aleatória na área
        double x = 100 + Math.random() * (areaJogo.getWidth()  - 180);
        double y = 60  + Math.random() * (areaJogo.getHeight() - 160);

        AnchorPane.setLeftAnchor(card, x);
        AnchorPane.setTopAnchor(card, y);
        areaJogo.getChildren().add(card);
    }

    // ── Drag-and-drop na área de jogo ──────────────────────────────────────
    private void habilitarDrag(StackPane card) {

        card.setOnMousePressed(e -> {
            elementoArrastado = card;
            dragOffsetX = e.getX();
            dragOffsetY = e.getY();
            card.toFront();
        });

        card.setOnMouseDragged(e -> {
            if (elementoArrastado == null) return;
            double novoX = AnchorPane.getLeftAnchor(card) + e.getX() - dragOffsetX;
            double novoY = AnchorPane.getTopAnchor(card)  + e.getY() - dragOffsetY;
            AnchorPane.setLeftAnchor(card, novoX);
            AnchorPane.setTopAnchor(card,  novoY);
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
            if (!(node instanceof StackPane alvo)) continue;
            if (alvo == origem) continue;

            double ax = AnchorPane.getLeftAnchor(alvo);
            double ay = AnchorPane.getTopAnchor(alvo);

            // Colisão simples por distância
            if (Math.abs(ox - ax) < 70 && Math.abs(oy - ay) < 70) {
                return alvo;
            }
        }
        return null;
    }

    // ── Lógica de combinação ───────────────────────────────────────────────
    private void processarCombinacao(String nomeA, String nomeB,
                                     StackPane cardA, StackPane cardB) {
        String resultado = GerenciadorDeJogo.getInstance().tentarCombinar(nomeA, nomeB);

        if (resultado != null) {
            // Remove os dois cards usados
            areaJogo.getChildren().removeAll(cardA, cardB);

            // Recarrega a barra lateral com o novo item
            carregarElementosNaBarraLateral();
            atualizarHUD();

            // Mostra overlay de descoberta
            mostrarOverlayCombo(resultado);

            // Verifica se é easter egg
            if (resultado.equals("AYCABRON")) {
                mostrarEasterEgg();
            }

            // Verifica conclusão de fase
            if (GerenciadorDeJogo.getInstance().faseCompleta()) {
                PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));
                pausa.setOnFinished(e -> avancarFase());
                pausa.play();
            }
        } else {
            // Combinação inválida: devolve card A para posição anterior
            piscarVermelho(cardA);
        }
    }

    private void mostrarOverlayCombo(String nomeResultado) {
        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();
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

        PauseTransition pausa = new PauseTransition(
            Duration.seconds(easter ? 4.0 : 2.0));
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

    // ── Fases ─────────────────────────────────────────────────────────────
    private void iniciarFase() {
        int fase = GerenciadorDeJogo.getInstance().getFaseAtual();
        switch (fase) {
            case 1 -> iniciarFase1();
            case 2 -> iniciarFase2();
            case 3 -> iniciarFase3();
        }
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
        GerenciadorDeJogo.getInstance().avancarFase();
        int novaFase = GerenciadorDeJogo.getInstance().getFaseAtual();

        if (novaFase > 3) {
            // Jogo concluído → salva e vai para ranking
            salvarEIrParaFimDeJogo();
        } else {
            areaJogo.getChildren().clear();
            atualizarHUD();
            carregarElementosNaBarraLateral();
            iniciarFase();
        }
    }

    // ── Boss (Fase 3) ──────────────────────────────────────────────────────
    private void iniciarBoss() {
        // Nilipe rouba um item a cada 18–28 segundos
        timerBoss = new Timeline(new KeyFrame(
            Duration.seconds(18 + Math.random() * 10),
            e -> {
                Item roubado = GerenciadorDeJogo.getInstance().removerItemAleatorio();
                if (roubado != null) {
                    Platform.runLater(() -> {
                        carregarElementosNaBarraLateral();
                        atualizarHUD();
                        mostrarAlertaBoss(roubado.getNome());
                        digitarDialogo(DIALOGO_FASE3_NILIPE);
                    });
                }
                // Reagenda com novo intervalo aleatório
                timerBoss.getKeyFrames().setAll(new KeyFrame(
                    Duration.seconds(18 + Math.random() * 10), ev -> {
                        Item r = GerenciadorDeJogo.getInstance().removerItemAleatorio();
                        if (r != null) Platform.runLater(() -> {
                            carregarElementosNaBarraLateral();
                            atualizarHUD();
                            mostrarAlertaBoss(r.getNome());
                        });
                    }
                ));
            }
        ));
        timerBoss.setCycleCount(Timeline.INDEFINITE);
        timerBoss.play();
    }

    private void mostrarAlertaBoss(String nomeRoubado) {
        carregarPersonagem("personagens/monstro.png", "Nilipe");
        digitarDialogo("😈 NOM NOM NOM! Roubei seu " + nomeRoubado + "! Hahaha!");
    }

    private void pararBoss() {
        if (timerBoss != null) timerBoss.stop();
    }

    // ── Diálogo estilo visual novel (typewriter) ───────────────────────────
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

    // ── Salvar e ranking ──────────────────────────────────────────────────
    private void salvarEIrParaFimDeJogo() {
        pararBoss();
        GerenciadorDeJogo g = GerenciadorDeJogo.getInstance();
        com.mycompany.metamorphosis.DAO.RankingDAO dao =
            new com.mycompany.metamorphosis.DAO.RankingDAO();
        dao.salvarPontuacao(g.getJogador());
        try { App.setRoot("fimDeJogo"); }
        catch (IOException ex) { ex.printStackTrace(); }
    }

    // ── Botões FXML ────────────────────────────────────────────────────────
    @FXML
    private void voltar() throws IOException {
        pararBoss();
        App.setRoot("menuPrincipal");
    }

 
}
