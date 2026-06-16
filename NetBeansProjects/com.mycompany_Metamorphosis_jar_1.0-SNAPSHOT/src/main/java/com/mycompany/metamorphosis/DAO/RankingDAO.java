
package com.mycompany.metamorphosis.DAO;

import com.mycompany.metamorphosis.model.Jogador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author eu
 */
public class RankingDAO {
     public void salvarPontuacao(Jogador jogador) {

        String sql = "INSERT INTO ranking(nome, pontos) VALUES (?, ?)";

        try (
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, jogador.getNome());
            stmt.setInt(2, jogador.getPontos());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Jogador> listarRanking() {

        List<Jogador> ranking = new ArrayList<>();

        String sql =
            "SELECT * FROM ranking ORDER BY pontos DESC";

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
