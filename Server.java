import java.net.*;
import java.io.*;
class Server
{
    ServerSocket server;
    Socket socket;
    
    BufferedReader br;
    PrintWriter out;
    //Constructer
    public Server()
    {
      try {
        server=new ServerSocket(7778);
        System.out.println("server is ready to accept connection");
        System.out.println("waiting...");
        socket=server.accept();

        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());

        startReading();
        startWriting();
      } catch(Exception e){
          //TODO: handle exception
          e.printStackTrace();
      }
    }


    public void startReading()
    {
      //thread read krke deta rhega 
      Runnable R1=()->{
          
        System.out.println("reader started...");
        
        try{
        while(true)
        {
        
            String msg=br.readLine();
            if(msg.equals("exit"))
            {
                System.out.println("Client terminated the chat");
                break;
            }

            System.out.println("Client : "+msg);
        
        }
        }
        catch(Exception e)
        {
          //  e.printStackTrace();
          System.out.println("Connection is closed...");
        }
      };

      new Thread(R1).start();
    }

    public void startWriting()
    {
       //thread - data user lega and send krega client tak 
        Runnable R2=()->{
            System.out.println("Writer Started");
            try{
            while(true && !socket.isClosed())
            {
            

               BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
               String content=br1.readLine();
               out.println(content);
               out.flush();

               if(content.equals("exit"))
               {
                socket.close();
                break;
               }
                }
            }  
            
            catch(Exception e)
            {
              // e.printStackTrace(); 
              System.out.println("Connection is closed...");
            }
        
        };

       new Thread(R2).start();
    }
    public static void main(String[] args) {
        System.out.println("This is server..going to start server");
        new Server();
    }
}