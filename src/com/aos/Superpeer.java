package com.aos;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Superpeer extends Thread {

   String FileDir;
   int port_no;
   ServerSocket serverSocket = null;
   Socket socket = null;
   int peer_id;
   static ArrayList<String> msg;
   LogUtility log = new LogUtility();


   Superpeer(int port, String SharedDir, int peer_id) {
      this.port_no = port;
      this.FileDir = SharedDir;
      this.peer_id = peer_id;
      msg = new ArrayList();
   }

   public void run() {
      try {
         this.serverSocket = new ServerSocket(this.port_no);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      while(true) {
         try {
            while(true) {
               this.socket = this.serverSocket.accept();
               System.out.println("\nSuperpeer: " + this.peer_id + ", started at " + this.socket.getRemoteSocketAddress());
               log.write("\nSuperpeer: " + this.peer_id + ", started at " + this.socket.getRemoteSocketAddress());
               (new Download(this.socket, this.FileDir, this.peer_id, msg)).start();
            }
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }
   }
}
