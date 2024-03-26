package com.iit;

import com.iit.LeafNode;
import com.iit.Main;
import com.iit.MessageFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

class Download extends Thread {

   protected Socket socket;
   String FileDirectory;
   int port;
   String fname;
   int peer_id;
   ArrayList<String> peermsg;
   ArrayList<Thread> thread = new ArrayList();
   ArrayList<LeafNode> peerswithfiles = new ArrayList();
   int[] peersArray_list = new int[20];
   int[] a = new int[20];
   int countofpeers = 0;
   int messageId;
   int set = 0;
   int TTL_value;
   MessageFormat MF = new MessageFormat();


   Download(Socket socket, String FileDirectory, int peer_id, ArrayList<String> peermsg) {
      this.socket = socket;
      this.FileDirectory = FileDirectory;
      this.peer_id = peer_id;
      this.peermsg = peermsg;
   }

   public void run() {
      try {
         InputStream e = this.socket.getInputStream();
         ObjectInputStream ois = new ObjectInputStream(e);
         OutputStream os = this.socket.getOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(os);
         this.MF = (MessageFormat)ois.readObject();
         System.out.println("New Query received from peer with peer-id : " + this.MF.fromPeerId);
         boolean peerduplicate = this.peermsg.contains(this.MF.message_ID);
         if(!peerduplicate) {
            this.peermsg.add(this.MF.message_ID);
         } else {
            System.out.println("Same query received!");
         }

         this.fname = this.MF.file_name;
         if(!peerduplicate) {
            File directoryObj = new File(this.FileDirectory);
            String[] filesList = directoryObj.list();

            for(int prop = 0; prop < filesList.length; ++prop) {
               File newfind = new File(filesList[prop]);
               if(newfind.getName().equals(this.fname)) {
                  System.out.println("File Found! queryhit: " + this.fname);
                  this.peersArray_list[this.countofpeers++] = this.peer_id;
                  break;
               }
            }

            System.out.println("Superpeer: \"Search in leaf-peer completed\"");
            Properties var21 = new Properties();
            Main M = new Main();
            String fileName = Main.fileName;
            FileInputStream var20 = new FileInputStream(fileName);
            var21.load(var20);
            String temp = var21.getProperty("peer" + this.peer_id + ".next");
            int j;
            if(temp != null && this.MF.ttl > 0) {
               String[] i = temp.split(",");

               for(j = 0; j < i.length; ++j) {
                  if(this.MF.fromPeerId != Integer.parseInt(i[j])) {
                     int connectingport = Integer.parseInt(var21.getProperty("peer" + i[j] + ".port"));
                     int neighbouringpeer = Integer.parseInt(i[j]);
                     System.out.println("Query forwarded to next neighbour: " + neighbouringpeer);
                     LeafNode cp = new LeafNode(connectingport, neighbouringpeer, this.fname, this.MF.message_ID, this.peer_id, this.MF.ttl--);
                     Thread t = new Thread(cp);
                     t.start();
                     this.thread.add(t);
                     this.peerswithfiles.add(cp);
                  }
               }
            }

            int var22;
            for(var22 = 0; var22 < this.thread.size(); ++var22) {
               ((Thread)this.thread.get(var22)).join();
            }

            for(var22 = 0; var22 < this.peerswithfiles.size(); ++var22) {
               this.a = ((LeafNode)this.peerswithfiles.get(var22)).getarray();

               for(j = 0; j < this.a.length && this.a[j] != 0; ++j) {
                  this.peersArray_list[this.countofpeers++] = this.a[j];
               }
            }
         }

         oos.writeObject(this.peersArray_list);
      } catch (Exception var19) {
         var19.printStackTrace();
      }

   }
}
