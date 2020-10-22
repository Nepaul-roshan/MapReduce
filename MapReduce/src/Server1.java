import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Server1 {
	private static ServerSocket server;
	private static int port = 1600;
	
	
	private static void split() throws IOException{
		File monFichier = new File("test.txt");
		
		Compteur nbligne = new Compteur();
		Fichier f = new Fichier();
		
		int nbLigne = nbligne.compteLigne(monFichier);
		int separation = nbLigne/3;
		System.out.println(nbLigne);
		Path chemin = Paths.get("test.txt");
		f.splitTextFiles(chemin, separation);
	}
		
	
	 
	public static void connexion() throws IOException {
		server = new ServerSocket(port);
		Socket socket = server.accept();
		InputStream is = socket.getInputStream();  
	}
	
	public static void ecritureFichier(Hashtable<String, Integer> table_paire, Hashtable<String, Integer> table_impaire) throws IOException {
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();
		String mot;
		Integer nbOcc;
		Integer cpt = 0;
		FileWriter fw = new FileWriter("fichier_final.txt");
		while (cpt != 2) {
		table = (cpt == 0) ? table_paire : table_impaire;
		Enumeration<String> lesMots = table.keys();
		while (lesMots.hasMoreElements())
		{
			mot = (String)lesMots.nextElement();
			nbOcc = ((Integer)table.get(mot)).intValue();
			String phrase = "Le mot " + mot + " figure " +
					nbOcc + " fois";
			fw.write(phrase + "\n");
		}
		cpt++;
		}

		fw.close();
	}

	public static void main(String[] args) throws InterruptedException, IOException {
		Hashtable<String, Integer> table0 = new Hashtable<String, Integer>();
		Hashtable<String, Integer> table1 = new Hashtable<String, Integer>();
		Hashtable<String, Integer> table2 = new Hashtable<String, Integer>();
		Hashtable<String, Integer> table3 = new Hashtable<String, Integer>();
		Hashtable<String, Integer> table4 = new Hashtable<String, Integer>();
		Hashtable<String, Integer> table5 = new Hashtable<String, Integer>();
		ArrayList<Hashtable<String, Integer>> paires = new ArrayList<Hashtable<String, Integer>>();
		ArrayList<Hashtable<String, Integer>> impaires = new ArrayList<Hashtable<String, Integer>>();
		split();
		paires.add(table0);
		paires.add(table2);
		paires.add(table4);
		impaires.add(table1);
		impaires.add(table3);
		impaires.add(table5);
		Map t = new Map(table0,table1, "1split.txt");
		t.start();
		t = new Map(table2,table3, "2split.txt");
		t.start();
		t = new Map(table4,table5, "3split.txt");
		t.start();
		t.join();
		System.out.println("0 " + table0);
		System.out.println("1 " + table1);
		System.out.println("2 " + table2);
		System.out.println("3 " + table3);
		System.out.println("4 " + table4);
		System.out.println("5 " + table5);
		Reduce r = new Reduce(paires);
		r.start();
		r = new Reduce(impaires);
		r.start();		
		r.join();
		System.out.println(table0);
		System.out.println(table1);
		ecritureFichier(table0,table1);

	}
}
