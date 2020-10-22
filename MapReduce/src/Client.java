
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
		byte []b= new byte[200];
        Socket sr= new Socket("localhost",1600);
        Scanner sc = new Scanner(System.in);
        System.out.println("Quel fichier souahitez envoyer? :");
        String fichier = sc.nextLine();
        System.out.println("Sending request to Socket Server");
        FileInputStream fr = new FileInputStream(fichier);
        fr.read(b, 0, b.length);
        OutputStream os = sr.getOutputStream();
        os.write(b, 0, b.length);
        System.out.println("Vous avez bien envoyer le fichier :" + fichier);            
        InputStream is = sr.getInputStream();
        is.read(b, 0, b.length);
        FileOutputStream fr2 = new FileOutputStream("recuClient.txt");        
        fr2.write(b, 0, b.length);
        sr.close();
        is.close();
        sc.close();
        fr.close();
        fr2.close();
        os.close();
		
		
    }
	

}

