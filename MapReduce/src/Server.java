import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Server {
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 1600;

    
    public static void mapReduce(String message) throws IOException {
         Hashtable<String, Integer> table = new Hashtable<String, Integer>();
          BufferedReader br = new BufferedReader(new FileReader(message));
          FileWriter fw = new FileWriter("ServeurEnvoie.txt");
          String ligne;
          StringTokenizer st;
          String mot;
          int nbOcc;

     while ((ligne = br.readLine()) != null)
   {
     st = new StringTokenizer(ligne);
     while(st.hasMoreTokens())
       {
         mot = st.nextToken();
         if (table.containsKey(mot))
       {
         nbOcc = ((Integer)table.get(mot)).intValue();
         nbOcc++;
       }
         else nbOcc = 1;
         table.put(mot, nbOcc);
       }
   }

     Enumeration<String> lesMots = table.keys();
     while (lesMots.hasMoreElements())
   {
     mot = (String)lesMots.nextElement();
     nbOcc = ((Integer)table.get(mot)).intValue();
     String phrase = "Le mot " + mot + " figure " +
             nbOcc + " fois";
     fw.write(phrase + "\n");
   }
     
     fw.close();
     br.close();
    
     
    }   
    
    public static void main(String[] args) throws Exception {
        server = new ServerSocket(port);
        int i = 0;
        while(true){
            i++;
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();        
            InputStream is = socket.getInputStream();     
            System.out.println("File received");
            FileOutputStream fr = new FileOutputStream("recuServeur" + i + ".txt");
            byte b[] = new byte[19];
            is.read(b, 0, b.length);
            fr.write(b, 0, b.length);
            System.out.println("File writed");
        
            mapReduce("recuServeur" + i + ".txt");
            System.out.println("map reduce effectué...");
            FileInputStream fr2 = new FileInputStream("ServeurEnvoie.txt");
            byte b2[] = new byte[20000];
            fr2.read(b2, 0, b2.length);
            OutputStream os = socket.getOutputStream();           
            os.write(b2, 0, b2.length);
            is.close();
            os.close();
            fr.close();
            fr2.close();
    }
   }
}
