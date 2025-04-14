package com.aos;

import java.io.IOException;
import java.net.ServerSocket;

class FileDownloadManager extends Thread {

   int portno;
   String FileDirectory;
   ServerSocket serverSocket;


   FileDownloadManager(int portno, String FileDirectory) {
      this.portno = portno;
      this.FileDirectory = FileDirectory;
   }

   public void run() {
      try {
         this.serverSocket = new ServerSocket(this.portno);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

      (new FileSender(this.serverSocket, this.portno, this.FileDirectory)).start();
   }
}
