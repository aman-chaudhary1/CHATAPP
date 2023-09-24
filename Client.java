import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.*;
import java.io.*;


public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    int i;

    //Declare Component
     private JLabel heading =new JLabel("Client Area");
     private JTextArea messageArea=new JTextArea();
     private JTextField messageInput=new JTextField();
     private Font font=new Font("Roboto",Font.PLAIN,20);
   //constructor
    public Client()
    {
        try{
            System.out.println("Sending Request to server");
            socket=new Socket("127.0.0.1",7778);
            System.out.println("connection done.");

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
             createGUI();
             handleEvents();
             startReading();
            // startWriting();
        }
        catch(Exception e)
        {

        }
    }

    private void handleEvents() {
       messageInput.addKeyListener(new KeyListener() {

        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
           
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // TODO Auto-generated method stub
            if(e.getKeyCode()==10)
            {
                String contentToSend = messageInput.getText();
                messageArea.append("Me :"+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();
                messageInput.setText("");
                messageInput.requestFocus();
           // System.out.println("you have pressed enter button");
             
        }
        }
        
       }); 
    }

    private void createGUI()
    {
        //GUI code...
       this.setTitle("Client Messager[END]");
       this.setSize(600,700);
       this.setLocationRelativeTo(null);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       this.setVisible(true);
       //Coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("IMAGE.JPG"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //frame ka Layout set Karege
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);

    }
    

     //start reading [method]
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
                System.out.println("Server terminated the chat");
                JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                messageInput.setEnabled(false);
                socket.close();
                break;
            }

            //System.out.println("Server : "+msg);
            messageArea.append("Server : " + msg+"\n");
        
        }
        }catch(Exception e)
        {
           // e.printStackTrace();
           System.out.println("Connection is closed....");
        }
      };

      new Thread(R1).start();
    }
     //start writing[method]
    public void startWriting()
    {
       //thread - data user lega and send krega client tak 
        Runnable R2=()->{
            System.out.println("Writer Started");
            try{
            while(true && !socket.isClosed() )
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
         
                System.out.println("Connection close");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        };

       new Thread(R2).start();
    }
    public static void main(String[] args) {
       System.out.println("this is client..."); 
       new Client();
    }
}
