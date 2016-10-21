/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crawler;

import java.io.IOException;

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
    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("http://www.skysports.com/premier-league-fixtures").get();
        String title = doc.title();
        System.out.println(title);
        

        Elements dates = doc.select("h4.matches__group-header");
        dates.stream().forEach((date) -> {
            System.out.println(date.text());
            Element group = date.nextElementSibling();
            Elements matchs = group.select(".matches__list-item"); 
            
            matchs.stream().forEach((match) -> {
                System.out.print(match.select("span.matches__participant--side1").text());
                System.out.print(" vs ");
                System.out.println(match.select("span.matches__participant--side2").text());
            });//  
        });
        
        
    }
    
}
