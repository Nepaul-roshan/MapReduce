import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Reduce extends Thread{
	ArrayList<Hashtable<String, Integer>> table;

	public Reduce(ArrayList<Hashtable<String, Integer>> table) {
		this.table = table;
	}

	public void concatHash() {
		String mot;
		int nbOcc_table1;
		int nbOcc;
		int occ;
		for(int i = 1; i < this.table.size(); i++) {
			Enumeration<String> lesMots = table.get(i).keys();
			while (lesMots.hasMoreElements())
			{
				mot = (String)lesMots.nextElement();
				nbOcc_table1 = ((Integer)table.get(i).get(mot)).intValue();
				if (table.get(0).containsKey(mot))
				{
					nbOcc = ((Integer)table.get(0).get(mot)).intValue();
					occ = nbOcc + nbOcc_table1;
				}
				else occ = 1;
				table.get(0).put(mot, occ);
			}
		}


	}

	public void run() {
		concatHash();
	}


}