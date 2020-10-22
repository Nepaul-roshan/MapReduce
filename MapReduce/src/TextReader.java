import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TextReader {

    public FileWriter mapReduce(File fichier) throws IOException {
        
      Hashtable<String, Integer> table = new Hashtable<String, Integer>();
      BufferedReader br = new BufferedReader(new FileReader(fichier));
      FileWriter fw = new FileWriter("renvoie.txt");
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
      
      
      return fw;
    
    }
}