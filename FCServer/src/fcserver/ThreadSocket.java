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

                if (st.equals("rankingRequest")) {
                    String table = getRanking();
                    dos.writeUTF(table);
                } else if (st.equals("fixtureRequest")) {
                    String table = getFixture();
                    dos.writeUTF(table);
                } else if (st.equals("clubRequest")) {
                    String table = getClub();
                    dos.writeUTF(table);
                }

                arr = st.split("#");
                //login
                if (arr != null && arr.length == 3) {
                    if (arr[2].equals("login")) {
                        int isExisted = authenticate(arr[0], arr[1]);

                        System.out.println(isExisted);
                        if (isExisted != 0) {
                            dos.writeUTF("OK");
                        } else {
                            dos.writeUTF("notOK");
                        }
                    }
                }

//                String result = getUsers();
//                System.out.print(result);
                Thread.sleep(1000);

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

    public String getRanking() throws Exception {

        connectDB();
        sql = "select * from Ranking order by pos";
        rs = stm.executeQuery(sql);
        StringBuilder table = new StringBuilder();

//        if (!rs.next()) {
//            return "";
//        }
        while (rs.next()) {
            String pos = rs.getString("pos");
            String team = rs.getString("team");
            String pl = rs.getString("pl");
            String w = rs.getString("w");
            String d = rs.getString("d");
            String l = rs.getString("l");
            String f = rs.getString("f");
            String a = rs.getString("a");
            String gd = rs.getString("gd");
            String pts = rs.getString("pts");

            String row = pos.trim() + "," + team.trim() + "," + pl.trim() + ","
                    + w.trim() + "," + d.trim() + "," + l.trim() + "," + f.trim() + "," + a.trim() + "," + gd.trim() + "," + pts.trim() + ";";
            table.append(row);
        }

//        System.out.print(table.toString());
        return table.toString();
    }

    public String getFixture() throws Exception {

        connectDB();
//        sql = "select * from Fixtures";
        sql = "SELECT id, home, home_goal, away_goal, away, CONVERT(VARCHAR(10), time, 100) as time, CONVERT(VARCHAR(10), date, 105) as date from Fixtures";
        rs = stm.executeQuery(sql);
        StringBuilder table = new StringBuilder();

//        if (!rs.next()) {
//            return "";
//        }
        while (rs.next()) {
            String id = rs.getString("id");
            String home = rs.getString("home");
            String home_goal = rs.getString("home_goal");
            String away_goal = rs.getString("away_goal");
            String away = rs.getString("away");
            String time = rs.getString("time");
            String date = rs.getString("date");
//            String a = rs.getString("a");
//            String gd = rs.getString("gd");
//            String pts = rs.getString("pts");
//            

            String row = id.trim() + "," + home.trim() + "," + home_goal.trim() + ","
                    + away_goal.trim() + "," + away.trim() + "," + time.trim() + "," + date.trim() + ";";
            table.append(row);
        }

//        System.out.print(table.toString());
        return table.toString();
    }
    
    public String getClub() throws Exception {
        connectDB();
//        sql = "select * from Fixtures";
        sql = "SELECT name from Clubs";
        rs = stm.executeQuery(sql);
        StringBuilder table = new StringBuilder();


        while (rs.next()) {
            String name = rs.getString("name");


            String row = name.trim() + ";";
            table.append(row);
        }

//        System.out.print(table.toString());
        return table.toString();
    }
    //
    public int authenticate(String username, String password) throws SQLException {
        try {
            connectDB();
        } catch (Exception ex) {
            Logger.getLogger(ThreadSocket.class.getName()).log(Level.SEVERE, null, ex);
        }

        sql = "select * from Users where username = '" + username + "' and password = '" + password + "'";
        System.out.println(sql);
        rs = stm.executeQuery(sql);
//        StringBuilder user = new StringBuilder();
        if (!rs.next()) {
            return 0;
        }

        return 1;
    }

}
