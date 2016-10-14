/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcserver;

import java.io.IOException; 
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.logging.*;


/**
 *
 * @author namho
 */
public class FCServer {

    /**
     * 
     */
    public static ServerSocket ss;
    
    public static void main(String[] args) {
        try {
            ss = new ServerSocket(9876);
            
            InetAddress iAddress = InetAddress.getLocalHost();
            String serverIP = iAddress.getHostAddress();
            System.out.println("Server IP address : " + serverIP);
            
            while (true) // wait for client
            {
                System.out.println("Call to here.");
                new ThreadSocket(ss.accept()).start();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FCServer.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
    
}
