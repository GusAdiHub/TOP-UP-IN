/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package belajarui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author evoal
 */
public class Koneksi {
    public static final String URL = "jdbc:mysql://localhost:3306/topup-in";
    public static final String USER = "root";
    public static final String PASS = "";
    public static Connection getconnection(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(URL, USER, PASS);
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return conn;
}
}
