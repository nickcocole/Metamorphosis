
package com.mycompany.metamorphosis.DAO;


import com.mycompany.metamorphosis.model.Jogador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankingDAO {

    public void salvarPontuacao(Jogador jogador) {

        String sql = "INSERT INTO ranking (nome, pontos) VALUES (?, ?)";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getPontos());

            stmt.executeUpdate();

            System.out.println("Pontuação salva com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar pontuação:");
            e.printStackTrace();
        }
    }

    public List<Jogador> listarRanking() {

        List<Jogador> ranking = new ArrayList<>();

          String sql = "SELECT id, nome, pontos FROM ranking ORDER BY pontos DESC";

          try (
              Connection conn = ConnectionFactory.getConnection();
              PreparedStatement stmt = conn.prepareStatement(sql);
              ResultSet rs = stmt.executeQuery()
          ) {

              while (rs.next()) {

                  Jogador jogador = new Jogador();

                  jogador.setId(rs.getInt("id"));
                  jogador.setNome(rs.getString("nome"));
                  jogador.setPontos(rs.getInt("pontos"));

                  ranking.add(jogador);
              }

          } catch (SQLException e) {
              e.printStackTrace();
          }

          return ranking;
    }
}
