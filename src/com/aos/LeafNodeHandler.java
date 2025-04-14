package com.aos;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

class LeafNodeHandler extends Thread {

   int leafNodePort;
   int connectedSuperpeer;
   int frompeerId;
   int timeToLive;
   String filetodownload;
   String msgid;
   Socket socket = null;
   int[] peersArray;
   MessageFormat MF = new MessageFormat();


   public LeafNodeHandler(int leafNodePort, int connectedSuperpeer, String filetodownload, String msgid, int frompeerId, int timeToLive) {
      this.leafNodePort = leafNodePort;
      this.connectedSuperpeer = connectedSuperpeer;
      this.filetodownload = filetodownload;
      this.msgid = msgid;
      this.frompeerId = frompeerId;
      this.timeToLive = timeToLive;
   }

   public void run() {
      try {
         this.socket = new Socket("localhost", this.leafNodePort);
         OutputStream cp = this.socket.getOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(cp);
         InputStream is = this.socket.getInputStream();
         ObjectInputStream ois = new ObjectInputStream(is);
         this.MF.file_name = this.filetodownload;
         this.MF.message_ID = this.msgid;
         this.MF.fromPeerId = this.frompeerId;
         this.MF.ttl = this.timeToLive;
         oos.writeObject(this.MF);
         this.peersArray = (int[])((int[])ois.readObject());
      } catch (IOException var5) {
         var5.printStackTrace();
      } catch (ClassNotFoundException var6) {
         var6.printStackTrace();
      }

   }

   public int[] getarray() {
      return this.peersArray;
   }

   @Override
    public String toString() {
        return "\nLeaf-Peer {SuperPeer=" + connectedSuperpeer + ", port=" + leafNodePort + ", filetodownload=" + filetodownload + ", fromPeer=" + frompeerId + ", message-id=" + msgid + "}";
    }
}
