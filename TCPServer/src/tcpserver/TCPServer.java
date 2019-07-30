package tcpserver;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class TCPServer implements Runnable {
    
       ArrayList<String> uname = new ArrayList<String>(15);
         ArrayList<String> pass = new ArrayList<String>(15);
        ArrayList<ArrayList<String> > friends=  
                  new ArrayList<ArrayList<String> >(15); 
 

 

    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocket server = null;
    private Thread thread = null;
    private int clientCount = 0;

    public static void main(String args[]) {
        TCPServer server = new TCPServer(2000);
    }

    public TCPServer(int port) {
        
  

     

        
        try {
            
                 for(int i=0;i<15;i++)
             friends.add(i,new ArrayList<String>());
       
           
            
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        } catch (IOException ioe) {
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            } catch (IOException ioe) {
                System.out.println("Server accept error: " + ioe);
                stop();
            }
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < clients.length) {
            System.out.println("Client accepted: " + socket);
           
            
           
            
            
            clients[clientCount] = new ChatServerThread(this, socket);
            try {
                clients[clientCount].open();
                clients[clientCount].start();

                clientCount++;

            } catch (IOException ioe) {
                System.out.println("Error opening thread: " + ioe);
            }
        } else {
            System.out.println("Client refused: maximum " + clients.length + " reached.");
        }
    }

    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (clients[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void handle(int ID, String input) {
        
      
     
        
        if (input.equals("logout")) {
             clients[findClient(ID)].setLogin(findClient(ID),0);
            clients[findClient(ID)].send("user logged out");
           
        } 
       else {
           
            
         
            String[] arr=input.split(":");
           
            if(arr[0].equals("reg") && !uname.contains(arr[1]) )
           {
               uname.add(findClient(ID) ,arr[1]);
               pass.add(findClient(ID),arr[2]);
             
               clients[findClient(ID)].send(" registered as " + uname.get(findClient(ID)) + " input username and pass to login" );
               
             
               
           }
            else{
                System.out.println("Username already exists");
            }
           if (arr[0].equals("log")   )  {
           
             if(uname.contains(arr[1]) && pass.contains(arr[2])  )
                 
             {     clients[findClient(ID)].send("user logged in" );
                   clients[findClient(ID)].setLogin( findClient(ID),1 );
                   
                          
             
        
             
             }
           }
           
          
             
             
             if(clients[findClient(ID)].getLogin(findClient(ID))==1)
             
             {
                 
                 if(arr[0].equals("u"))
                 {
                     
                     int i=uname.indexOf(arr[1]);
                     String me=uname.get(findClient(ID));
                     clients[i].send(me+" : "+ arr[2]);
                     
                     
                     
                 }
                 
                  if(arr[0].equals("m"))
                 {
                     String[] ids=arr[1].split(",");
                    
                     String me=uname.get(findClient(ID));
                     
                     for(int i=0; i<ids.length;i++  )
                     {
                         int j=uname.indexOf(ids[i]);
                         clients[j].send(me+" : "+ arr[2]);
                     }
                     
                     
                     
                 }
                 
                   if(arr[0].equals("b"))
                 {
                     
                    
                     String me=uname.get(findClient(ID));
                     
                    for (int i = 0; i < uname.size(); i++) {
                      if(findClient(ID)!=i)
                      clients[i].send(me + " : " + arr[1] );
            }
                     
                     
                 }
                 
                 if(arr[0].equals("add"))
                 {
                     String me=uname.get(findClient(ID));
                     int friend=uname.indexOf(arr[1]);
                      clients[friend].send(me+" sent a friend req ");
                     
                 }
                 
                 if(arr[0].equals("ok"))
                 {
                     
                     
                     int friend=uname.indexOf(arr[1]);
                     
                 
                     
                     int me=findClient(ID);
                     
                  
                  friends.get(me).add(arr[1]);
                   
                  friends.get(friend).add(uname.get(me));
                     
                     
                     
                  
                     
                    
           
                     
                     clients[findClient(ID)].send( uname.get(friend) + " is a friend now" );
                     System.out.println("okok");
                     clients[friend].send( uname.get(me) + " is a friend now" );
                     System.out.println("okok");
                     
                   
                     
                     
                 }
                 
                 
                 if(arr[0].equals("show"))
                 {
                     if(arr[1].equals("online"))
                     {
                         for(int i=0;i<clientCount;i++)
                         { 
                             if(clients[i].getLogin(i)==1)
                             {
                                 clients[findClient(ID)].send( uname.get(i) + " is online" );
                             }
                             
                             
                             
                         }
                         
                     }
                     
                      if(arr[1].equals("friends"))
                      {
                           int me=findClient(ID);
                           
                           for(int i=0;i<friends.get(me).size();i++)
                           {
                               
                               clients[findClient(ID)].send( friends.get(me).get(i)  );
                               
                           }
                          
                          
                          
                      }
                     
                     
                     
                     
                     
                     
                     
                 }
                 
                 
                 
             }
             
             else{
                 
                  clients[findClient(ID)].send("pls enter username to login first" );
                 
             }
             
               
       
                   
      
          
        }
    }

    public synchronized void remove(int ID) {
        int pos = findClient(ID);

        if (pos >= 0) {
            ChatServerThread toTerminate = clients[pos];
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    clients[i - 1] = clients[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                System.out.println("Error closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }
}
