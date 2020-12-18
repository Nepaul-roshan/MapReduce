import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Compteur {	
	public int compteLigne(File monFichier) throws IOException {	
		BufferedReader br = new BufferedReader(new FileReader(monFichier));
		int nbligne = 0;   
		while ((br.readLine()) != null){
			nbligne ++;
		}
		br.close();
		return nbligne;
	}
}