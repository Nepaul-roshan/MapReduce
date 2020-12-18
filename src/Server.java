import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Server {
	private static ServerSocket server;
	private static int port = 1600;
	
	private static String readFile(String path) throws IOException {
		  FileInputStream stream = new FileInputStream(new File(path));
		  try {
		    FileChannel fc = stream.getChannel();
		    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
		    /* Instead of using default, pass in a decoder. */
		    return Charset.defaultCharset().decode(bb).toString();
		  }
		  finally {
		    stream.close();
		  }
		}
	
	
	private static void split(String texte, Integer threads) throws IOException{
		String replacedTxt = readFile(texte).replaceAll(",", " ");
		try (PrintWriter out = new PrintWriter("texte.txt")){out.println(replacedTxt);}
		File monFichier = new File("texte.txt");
		
		Compteur nbligne = new Compteur();
		Fichier f = new Fichier();
		
		int nbLigne = nbligne.compteLigne(monFichier);
		int separation = nbLigne/threads;
		System.out.println(nbLigne);
		Path chemin = Paths.get("texte.txt");
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
	
	public static int start() throws IOException {
		String text;
		int threads;
        try ( Scanner scanner = new Scanner( System.in ) ) {
            
            System.out.print( "Quel texte souhaitez vous étudier?\n" );
            text = scanner.nextLine();
            
            System.out.print( "Combien de Threads souhaitez vous utiliser?\n" );
            threads = scanner.nextInt();
        }
        split(text, threads);
		return threads;
	}
	
	public static ArrayList<Hashtable<String, Integer>> creation(Integer threads, String parite){
		ArrayList<Hashtable<String, Integer>> create = new ArrayList<Hashtable<String, Integer>>();
		int a,loc;
		List<Hashtable<String, Integer>> liste = IntStream.range(0,16)
			.mapToObj(i -> new Hashtable<String, Integer>())
			.collect(Collectors.toList());
		if (parite == "pair") {
			a = 0;
			loc=threads-1;
			create.add(liste.get(0));
		}
		else {
			a= 1;
			loc=threads;
			create.add(liste.get(1));
		}
		for (int j = a;j < loc; j++) {
			create.add(liste.get(j+2));
		}
				
		return create;
		
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
		int threads = start();
		paires =creation(threads,"pair");
		impaires = creation(threads,"impair");
		System.out.println(paires);
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