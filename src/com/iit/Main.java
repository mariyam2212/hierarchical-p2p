package com.iit;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class Main {

   static String fileName;


   public static void main(String[] args) {
      try {
         byte count = 0;
         ArrayList thread = new ArrayList();
         ArrayList peers = new ArrayList();
         int peer_id = Integer.parseInt(args[1]);
         String sharedDir = args[2];
         Properties prop = new Properties();
         fileName = args[0];
         System.out.println("*******************************************");
         System.out.println("Super-peer " + peer_id + "\navailable directory " + sharedDir + "\nTopology: " + fileName);
         System.out.println("*******************************************");
         FileInputStream is = new FileInputStream(fileName);
         prop.load(is);
         int io = Integer.parseInt(prop.getProperty("peer" + peer_id + ".serverport"));
         FileDownloader sd = new FileDownloader(io, sharedDir);
         sd.start();
         int portserver = Integer.parseInt(prop.getProperty("peer" + peer_id + ".port"));
         Superpeer cs = new Superpeer(portserver, sharedDir, peer_id);
         cs.start();
         System.out.println("Enter the filename to download");
         String filetodownload = (new Scanner(System.in)).nextLine();
         int var23 = count + 1;
         String msgid = peer_id + "." + var23;
         String[] neighbours = prop.getProperty("peer" + peer_id + ".next").split(",");
         System.out.println("neighbours --> " + Arrays.toString(neighbours));
         int ttl = neighbours.length;

         int peerswithfiles;
         int peerfromdownload;
         int porttodownload;
         for(peerswithfiles = 0; peerswithfiles < neighbours.length; ++peerswithfiles) {
            peerfromdownload = Integer.parseInt(prop.getProperty("peer" + neighbours[peerswithfiles] + ".port"));
            porttodownload = Integer.parseInt(neighbours[peerswithfiles]);
            LeafNode j = new LeafNode(peerfromdownload, porttodownload, filetodownload, msgid, peer_id, ttl);
            Thread t = new Thread(j);
            t.start();
            thread.add(t);
            peers.add(j);
         }
         System.out.println(peers);

         for(peerswithfiles = 0; peerswithfiles < thread.size(); ++peerswithfiles) {
            try {
               ((Thread)thread.get(peerswithfiles)).join();
            } catch (InterruptedException var21) {
               var21.printStackTrace();
            }
         }

         System.out.println("leaf-peer containing the file: ");
         peerfromdownload = 0;

         for(porttodownload = 0; porttodownload < peers.size(); ++porttodownload) {
            int[] var24 = ((LeafNode)peers.get(porttodownload)).getarray();

            for(int var25 = 0; var25 < var24.length && var24[var25] != 0; ++var25) {
               System.out.println(var24[var25]);
               peerfromdownload = var24[var25];
            }
         }

         System.out.println("\n Selecting leaf-peer: " + peerfromdownload + " for file download \n");
         porttodownload = Integer.parseInt(prop.getProperty("peer" + peerfromdownload + ".serverport"));
         StreamProcessor(peerfromdownload, porttodownload, filetodownload, sharedDir);
         System.out.println("File: " + filetodownload + " downloaded from leaf-peer: " + peerfromdownload + " to leaf-peer:" + peer_id);
      } catch (IOException var22) {
         var22.printStackTrace();
      }

   }

   public static void StreamProcessor(int cspeerid, int csportno, String filename, String sharedDir) {
      try {
         Socket e = new Socket("localhost", csportno);
         ObjectOutputStream ooos = new ObjectOutputStream(e.getOutputStream());
         ooos.flush();
         ooos.writeObject(filename);
         String outputFile = sharedDir + "/" + filename;
         byte[] mybytearray = new byte[1024];
         InputStream is = e.getInputStream();
         FileOutputStream fos = new FileOutputStream(outputFile);
         BufferedOutputStream bos = new BufferedOutputStream(fos);
         int bytesRead = is.read(mybytearray, 0, mybytearray.length);
         bos.write(mybytearray, 0, bytesRead);
         bos.close();
         e.close();
         System.out.println(filename + " file is transferred to your private storage: " + sharedDir);
      } catch (Exception var12) {
         var12.printStackTrace();
      }

   }
}
