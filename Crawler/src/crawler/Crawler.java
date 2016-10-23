/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

/**
 *
 * @author namho
 */
public class Crawler {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    private Connection con;
    private Statement stm;
    private ResultSet rs;
    private String sql;
    
    public void connectDB() throws Exception {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL = "jdbc:sqlserver://localhost:1433;databaseName=FootballDB";

            String user = "tester01";
            String pass = "123456"; 
            con = DriverManager.getConnection(URL, user, pass);
            stm = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void getFixtures() throws IOException, Exception {
        connectDB();
        
        Document doc = Jsoup.connect("http://www.skysports.com/premier-league-results/september-2016").get();
        String title = doc.title();
        System.out.println(title);
        

        Elements dates = doc.select("h4.matches__group-header");
        dates.stream().forEach((date) -> {
//            System.out.println(date.text());
            String d = date.text().trim().substring(4, 12).replace("st", "").replace("rd", "").replace("nd", "").replace("th", "") + " 2016";
//            String d = date.text().trim().replace("st", "").replace("rd", "").replace("nd", "").replace("th", "").substring(4) + " 2016";
            System.out.println(d);


            Element group = date.nextElementSibling();
            Elements matchs = group.select(".matches__list-item"); 
            
            matchs.stream().forEach((Element match) -> {
                String home = match.select("span.matches__participant--side1").text();
                String away = match.select("span.matches__participant--side2").text();
                String t = match.select("span.matches__date").text();
                
                sql = "insert into Fixtures (home, home_goal, away_goal, away, Fixtures.time, Fixtures.date) "
                + "values ('" + home + "',  NULL, NULL, '" + away + "', '" + t + "', '" + d + "')";
                System.out.println(sql);
                int row = 0;
                try {
                    row = stm.executeUpdate(sql);

                } catch (SQLException ex) {
                    Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
        
        
        
        
    }
    public static void main(String[] args) throws IOException, Exception {
        Crawler c = new Crawler(); 
        c.getFixtures();
        
    }
    
}
