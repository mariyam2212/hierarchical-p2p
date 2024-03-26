package com.iit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class FileSender extends Thread {

   int portno;
   String sharedDirectory;
   String filename;
   ServerSocket socket;


   FileSender(ServerSocket socket, int portno, String FileDir) {
      this.socket = socket;
      this.portno = portno;
      this.sharedDirectory = FileDir;
   }

   public void run() {
      try {
         while(true) {
            System.out.println("\n\n --- Running...... Ready to receive download request ---\n\n");
            Socket e = this.socket.accept();
            InputStream is = e.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            this.filename = (String)ois.readObject();
            File myFile = new File(this.sharedDirectory + "/" + this.filename);
            byte[] mybytearray = new byte[(int)myFile.length()];
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = e.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            e.close();
         }
      } catch (Exception var8) {
         var8.printStackTrace();
      }
   }
}
