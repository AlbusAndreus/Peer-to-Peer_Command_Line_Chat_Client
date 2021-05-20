package com.company;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    static Socket socket = null;
    static Socket sendSocket = null;
    static ServerSocket serverSocket = null;

    static boolean chatClosed = false;

    static BufferedReader br = null;
    static BufferedWriter bw = null;
    public static void main(String[] args) {

        ConnectionListener conlistnr = new ConnectionListener();
        conlistnr.start();
        Scanner scan = new Scanner(System.in);
        String ipAddress = scan.nextLine();
        try {
            sendSocket = new Socket(ipAddress,3067 );
            MessageSender msgsndr = new MessageSender();
            msgsndr.start();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
static class ConnectionListener extends Thread{
        public void run(){
            while(!chatClosed) {
                try {
                    if (socket == null) {

                        serverSocket = new ServerSocket(3067);
                        socket = serverSocket.accept();


                    }

                    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = "";
                    if ((message = br.readLine()) != null) {
                        System.out.println("Partner: " + message);
                    } else if ((message = br.readLine()).equalsIgnoreCase("exit")) {
                        closeChat();
                        System.out.println("Partner has disconnected");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
static class MessageSender extends Thread{
        public void run() {
            while (!chatClosed) {
                try {
                    bw = new BufferedWriter(new OutputStreamWriter(sendSocket.getOutputStream()));
                    Scanner messageInput = new Scanner(System.in);
                    String messageToSend = messageInput.nextLine();
                    if (messageToSend.equalsIgnoreCase("exit")) {
                        closeChat();
                    }
                    bw.write(messageToSend);
                    bw.newLine();
                    System.out.println("You: " + messageToSend);
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
public static void closeChat(){
        try {
            chatClosed = true;
            bw.close();
            br.close();
            socket.close();
            serverSocket.close();
            sendSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
}

}

