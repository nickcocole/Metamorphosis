package com.mycompany.metamorphosis.model;

import java.util.ArrayList;
import java.util.List;


public class GerenciadorDeJogo {


    private static GerenciadorDeJogo instancia;


    public static GerenciadorDeJogo getInstance() {
        if (instancia == null) {
            instancia = new GerenciadorDeJogo();
        }
        return instancia;
    }


    private Jogador jogador;
    private Inventario inventario;
    private int faseAtual;         
    private List<Receita> receitas;

    private List<Item> snapshotInicioFase;

    private int tempoExtraCronometro = 0;     
    private int protecaoRouboExtra   = 0;     
    private boolean dicaComprada     = false; 
    
    private static final List<String> OBJETIVOS_FASE1 = List.of("CALDEIRÃO");
    private static final List<String> OBJETIVOS_FASE2 = List.of("AVE ASADA", "PESCADO ASADO");
    private static final List<String> OBJETIVOS_FASE3 = List.of("CAIXÃO", "FAMÍLIA", "BUQUÊ");

    public static final int TEMPO_FASE1 = 180; // 3 minutos
    public static final int TEMPO_FASE2 = 300; // 5 minutos
    public static final int TEMPO_FASE3 = 300; // 5 minutos
    
    public static final int TEMPO_PADRAO_FASE = TEMPO_FASE1;
    
    public int getTempoDaFaseAtual() {
        return switch (faseAtual) {
            case 1 -> TEMPO_FASE1;
            case 2 -> TEMPO_FASE2;
            case 3 -> TEMPO_FASE3;
            default -> TEMPO_FASE1;
        };
    }
    
    private GerenciadorDeJogo() {
        inventario = new Inventario();
        faseAtual  = 1;
        receitas   = new ArrayList<>();
        carregarReceitas();
        adicionarElementosBase();
        salvarSnapshotFaseAtual();
    }

    public void novaPartida(String nomeJogador) {
        jogador    = new Jogador(nomeJogador, 0);
        inventario = new Inventario();
        faseAtual  = 1;
        tempoExtraCronometro = 0;
        protecaoRouboExtra   = 0;
        dicaComprada         = false;
        adicionarElementosBase();
        salvarSnapshotFaseAtual();
    }

    private void adicionarElementosBase() {
        inventario.adicionar(new Item("elementos/fogo.png",  "FOGO"));
        inventario.adicionar(new Item("elementos/agua.png",  "ÁGUA"));
        inventario.adicionar(new Item("elementos/terra.png", "TERRA"));
        inventario.adicionar(new Item("elementos/ar.png",    "AR"));
    }

    public void salvarSnapshotFaseAtual() {
        snapshotInicioFase = new ArrayList<>();
        for (Item item : inventario.getItens()) {
            snapshotInicioFase.add(new Item(item.getIcone(), item.getNome()));
        }
    }


    public void restaurarSnapshotFaseAtual() {
        inventario = new Inventario();
        for (Item item : snapshotInicioFase) {
            inventario.adicionar(new Item(item.getIcone(), item.getNome()));
        }
        dicaComprada = false;
    }

    private void carregarReceitas() {
        // Fase 1
        receitas.add(new Receita("FOGO",   "ÁGUA",    "VAPOR"));
        receitas.add(new Receita("FOGO",   "FOGO",    "SOL"));
        receitas.add(new Receita("VAPOR",  "AR",      "NUVEM"));
        receitas.add(new Receita("NUVEM",  "ÁGUA",    "CHUVA"));
        receitas.add(new Receita("CHUVA",  "SOL",     "ARCO-ÍRIS"));
        receitas.add(new Receita("TERRA",  "ÁGUA",    "BARRO"));
        receitas.add(new Receita("TERRA",  "CHUVA",   "PLANTA"));
        receitas.add(new Receita("BARRO",  "FOGO",    "CALDEIRÃO"));

        // Fase 2
        receitas.add(new Receita("PLANTA",   "SOL",      "ÁRVORE"));
        receitas.add(new Receita("TERRA",    "FOGO",     "ENERGIA"));
        receitas.add(new Receita("ENERGIA",  "TERRA",    "RNA"));
        receitas.add(new Receita("RNA",      "RNA",      "DNA"));
        receitas.add(new Receita("DNA",      "TERRA",    "ANIMAL"));
        receitas.add(new Receita("ANIMAL",   "ÁGUA",     "PEIXE"));
        receitas.add(new Receita("ANIMAL",   "AR",       "PÁSSARO"));
        receitas.add(new Receita("ANIMAL",   "BARRO",    "HUMANO"));
        receitas.add(new Receita("ANIMAL",   "ARCO-ÍRIS","UNICÓRNIO"));
        receitas.add(new Receita("PÁSSARO",  "FOGUEIRA", "AVE ASADA"));
        receitas.add(new Receita("PEIXE",    "FOGUEIRA", "PESCADO ASADO"));

        // Fase 3
        receitas.add(new Receita("HUMANO",         "ÁRVORE",         "CEPO DE MADEIRA"));
        receitas.add(new Receita("CEPO DE MADEIRA","FOGO",           "FOGUEIRA"));
        receitas.add(new Receita("ANIMAL",         "ENERGIA",        "AYCABRON"));   // easter egg
        receitas.add(new Receita("ENERGIA",        "ENERGIA",        "CHOQUE"));
        receitas.add(new Receita("CHOQUE",         "HUMANO",         "MORTE"));
        receitas.add(new Receita("MORTE",          "CEPO DE MADEIRA","CAIXÃO"));
        receitas.add(new Receita("HUMANO",         "HUMANO",         "FAMÍLIA"));
        receitas.add(new Receita("ANIMAL",         "PLANTA",         "BORBOLETA"));
        receitas.add(new Receita("BORBOLETA",      "PLANTA",         "FLOR"));
        receitas.add(new Receita("FLOR",           "FLOR",           "BUQUÊ"));
    }


    public boolean jaConheceCombinacao(String nomeA, String nomeB) {
        for (Receita r : receitas) {
            if (r.combina(nomeA, nomeB)) {
                return inventario.possui(r.getResultado());
            }
        }
        return false;
    }

    public String tentarCombinar(String nomeA, String nomeB) {
        for (Receita r : receitas) {
            if (r.combina(nomeA, nomeB)) {
                String resultado = r.getResultado();

                if (!inventario.possui(resultado)) {
                    int pontos = resultado.equals("AYCABRON") ? 5000 : 500;
                    inventario.adicionar(new Item(iconeDoItem(resultado), resultado));
                    jogador.setPontos(jogador.getPontos() + pontos);
                }
                return resultado;
            }
        }
        return null; 
    }


    public String getDicaParaObjetivo(String objetivo) {
        for (Receita r : receitas) {
            if (r.getResultado().equals(objetivo)) {
                return r.getItem1() + " + " + r.getItem2() + " = " + objetivo;
            }
        }
        return "Nenhuma dica disponível.";
    }

    public boolean faseCompleta() {
        List<String> objetivos = getObjetivosFaseAtual();
        return objetivos.stream().allMatch(obj -> inventario.possui(obj));
    }

    public List<String> getObjetivosFaseAtual() {
        return switch (faseAtual) {
            case 1 -> OBJETIVOS_FASE1;
            case 2 -> OBJETIVOS_FASE2;
            case 3 -> OBJETIVOS_FASE3;
            default -> List.of();
        };
    }

    public String getProximoObjetivoPendente() {
        for (String obj : getObjetivosFaseAtual()) {
            if (!inventario.possui(obj)) return obj;
        }
        return null;
    }

    public boolean avancarFase() {
        if (faseAtual < 3) {
            faseAtual++;
            dicaComprada = false;
            salvarSnapshotFaseAtual();
            return true;
        }
        return false; 
    }

    public void reiniciarFaseAtual() {
        restaurarSnapshotFaseAtual();
    }

    public Item removerItemAleatorio() {
        List<Item> roubaveis = new ArrayList<>();
        List<String> bases = List.of("FOGO", "ÁGUA", "TERRA", "AR");
        for (Item item : inventario.getItens()) {
            if (!bases.contains(item.getNome())) {
                roubaveis.add(item);
            }
        }
        if (roubaveis.isEmpty()) return null;
        Item alvo = roubaveis.get((int)(Math.random() * roubaveis.size()));
        inventario.remover(alvo.getNome());
        return alvo;
    }

    public boolean comprarTempoExtraCronometro(int custoPontos, int segundosGanhos) {
        if (jogador.getPontos() < custoPontos) return false;
        jogador.setPontos(jogador.getPontos() - custoPontos);
        tempoExtraCronometro += segundosGanhos;
        return true;
    }

    public boolean comprarProtecaoRoubo(int custoPontos, int segundosGanhos) {
        if (jogador.getPontos() < custoPontos) return false;
        jogador.setPontos(jogador.getPontos() - custoPontos);
        protecaoRouboExtra += segundosGanhos;
        return true;
    }

    public boolean comprarDica(int custoPontos) {
        if (jogador.getPontos() < custoPontos) return false;
        jogador.setPontos(jogador.getPontos() - custoPontos);
        dicaComprada = true;
        return true;
    }

    public int getTempoExtraCronometro() { return tempoExtraCronometro; }
    public int getProtecaoRouboExtra()   { return protecaoRouboExtra; }
    public boolean isDicaComprada()      { return dicaComprada; }

    public int consumirTempoExtra() {
        int t = tempoExtraCronometro;
        tempoExtraCronometro = 0;
        return t;
    }

    public int consumirProtecaoRoubo() {
        int p = protecaoRouboExtra;
        protecaoRouboExtra = 0;
        return p;
    }

    public static String iconeDoItem(String nome) {
        return switch (nome) {
            case "FOGO"            -> "elementos/fogo.png";
            case "ÁGUA"            -> "elementos/agua.png";
            case "TERRA"           -> "elementos/terra.png";
            case "AR"              -> "elementos/ar.png";
            case "VAPOR"           -> "elementos/vapor.png";
            case "SOL"             -> "elementos/sol.png";
            case "NUVEM"           -> "elementos/nuvem.png";
            case "CHUVA"           -> "elementos/chuva.png";
            case "ARCO-ÍRIS"       -> "elementos/arcoiris.png";
            case "BARRO"           -> "elementos/barro.png";
            case "PLANTA"          -> "elementos/planta.png";
            case "CALDEIRÃO"       -> "elementos/caldeirao.png";
            case "ÁRVORE"          -> "elementos/arvore.png";
            case "ENERGIA"         -> "elementos/energia.png";
            case "RNA"             -> "elementos/rna.png";
            case "DNA"             -> "elementos/dna.png";
            case "ANIMAL"          -> "elementos/animal.png";
            case "PEIXE"           -> "elementos/peixe.png";
            case "PÁSSARO"         -> "elementos/passaro.png";
            case "HUMANO"          -> "elementos/humano.png";
            case "UNICÓRNIO"       -> "elementos/unicornio.png";
            case "AVE ASADA"      -> "elementos/aveasada.png";
            case "PESCADO ASADO"  -> "elementos/pescadoasado.png";
            case "CEPO DE MADEIRA" -> "elementos/cepodemadeira.png";
            case "FOGUEIRA"        -> "elementos/fogueira.png";
            case "AYCABRON"        -> "elementos/aycabron.png";
            case "CHOQUE"          -> "elementos/choque.png";
            case "MORTE"           -> "elementos/morte.png";
            case "CAIXÃO"          -> "elementos/caixao.png";
            case "FAMÍLIA"         -> "elementos/familia.png";
            case "BORBOLETA"       -> "elementos/borboleta.png";
            case "FLOR"            -> "elementos/flor.png";
            case "BUQUÊ"           -> "elementos/buque.png";
            default                -> "elementos/fogo.png";
        };
    }

    public Jogador getJogador()       { return jogador; }
    public Inventario getInventario() { return inventario; }
    public int getFaseAtual()         { return faseAtual; }
    public List<Receita> getReceitas(){ return receitas; }
}