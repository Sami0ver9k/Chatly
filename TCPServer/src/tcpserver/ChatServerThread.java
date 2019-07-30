/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tcpserver;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatServerThread extends Thread {
    private TCPServer server = null;
    private Socket socket = null;
    private int ID = -1;
    private DataInputStream streamIn = null;
    private DataOutputStream streamOut = null;
    int[] login= new int[15] ;
     

    
    
    
   
        

    public ChatServerThread(TCPServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();
          for(int i=0;i<15;i++)
        {
           if (login[i]!=1)
            login[i]=0;
            
            
        }
       
       
        
    }
    
        public void setLogin(int i, int j)
    {
        login[i]=j;
    }
    public int getLogin(int i)
    {
        return login[i];
        
    }
    
    

    public void send(String msg) {
        try {
            
            streamOut.writeUTF(msg);
            streamOut.flush();
        } catch (IOException ioe) {
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();
        }
    }
    

 
    
    
    public int getID() {
        return ID;
    }

    public void run() {
        
        
        
        System.out.println("Server Thread " + ID + " running.");
        
       
        
        while (true) {
            try {
                server.handle(ID, streamIn.readUTF());
            } catch (IOException ioe) {
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }

    public void open() throws IOException {
        streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }
}
