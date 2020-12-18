import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Map extends Thread {
	Hashtable<String, Integer> table_paire;
	Hashtable<String, Integer> table_impaire;
	private String fichier;

	public Map(Hashtable<String, Integer> table_paire,Hashtable<String, Integer> table_impaire, String fichier) {
		this.table_paire = table_paire;
		this.table_impaire = table_impaire;
		this.fichier = fichier;
	}

	public void ajoutHash(BufferedReader br ) throws IOException {
		String ligne;
		StringTokenizer st;
		String mot;
		int nbOcc;
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();

		while ((ligne = br.readLine()) != null)
		{
			st = new StringTokenizer(ligne);				
			while(st.hasMoreTokens()){
				mot = st.nextToken();
				table = (mot.length() % 2 == 0) ? table_paire : table_impaire;
				if (table.containsKey(mot)){
					nbOcc = ((Integer)table.get(mot)).intValue();
					nbOcc++;
				}
				else nbOcc = 1;
				table.put(mot, nbOcc);
			}
		}
		br.close();
	}

	@Override
	public void run(){
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(fichier));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			ajoutHash(br);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}