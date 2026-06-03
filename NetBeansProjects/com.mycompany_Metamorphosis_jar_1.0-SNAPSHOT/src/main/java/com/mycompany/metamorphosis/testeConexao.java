/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.metamorphosis;


import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author eu
 */
public class testeConexao {
    
    
    public static void main(String[] args) {

        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/Metamorphosis",
                "postgres",
                "postgresql"
            );

            System.out.println("Conectado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
}
