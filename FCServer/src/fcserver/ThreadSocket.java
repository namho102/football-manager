/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcserver;

import java.net.Socket;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author namho
 */
public class ThreadSocket extends Thread {

    Socket socket = null;
    private Connection con;
    private Statement stm;
    private ResultSet rs;
    private String sql;

    public ThreadSocket(Socket socket) {
        System.out.println("Call to thread socket. ");
        System.out.println("Socket is connected: " + socket.isConnected());
        System.out.println("Socket port: " + socket.getPort());
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String[] arr = {null};
            String st = "";
            
            DataInputStream din = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            
            System.out.println("Processing data. . .");

            while (din.available() != 0) {
                st = din.readUTF();
                System.out.println("Client sent: " + st);
//                arr = st.split("#");
                
                
//                String result = getUsers();
                
//                System.out.print(result);
                Thread.sleep(1000);
//                dos.writeUTF(result);
                dos.flush();
            }
      
        } catch (IOException | InterruptedException e) {
            
        } catch (Exception ex) {
            Logger.getLogger(ThreadSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // connect to Database
    public void connectDB() throws Exception {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL = "jdbc:sqlserver://localhost:1433;databaseName=FootballDB";
//            String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyDiemSV2";
            String user = "tester01";
            String pass = "123456"; 
            con = DriverManager.getConnection(URL, user, pass);
            stm = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    //get Users data 
    public String getUsers() throws Exception {
        connectDB();
        sql = "select * from Users";
        rs = stm.executeQuery(sql);
        StringBuilder users = new StringBuilder();
        
//        if (!rs.next()) {
//            return "";
//        }
        while (rs.next()) {
            String sName = rs.getString("username");
            String sPassword = rs.getString("password");

            if (sName == null) {
                sName = "";
            }
            if (sPassword == null) {
                sPassword = "";
            }
        
            String user = sName.trim() + "," + sPassword.trim() + ";";
            users.append(user);
        }
        
        
        
//        System.out.println("end");
        System.out.println(users.toString());
        con.close();
        return users.toString();
        
    }
}
