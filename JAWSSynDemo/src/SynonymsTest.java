import java.io.File;

import edu.smu.tspell.wordnet.Synset;
//import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
//import edu.smu.tspell.wordnet.impl.file.Morphology;

/**
 * 
 * @author Habiba Saim
 * This class is created to retrieve all synonyms of a word
 * We will later incorporate it in our SOM and KMeans Projects
 *
 */
public class SynonymsTest {
	
	String wordNetDirectory;
    String path;
	
	String[] arr;
	String wordType = "null";
	String word; 
	WordNetDatabase database; 
	
	public static void main(String[] args) {
		
		SynonymsTest test = new SynonymsTest();
		test.init();
		test.getSynonyms();
	}
	
	private void init(){
		wordNetDirectory = "WordNet-2-1";
		path  = wordNetDirectory + File.separator + "dict";
		System.setProperty("wordnet.database.dir",path);
		word = "prerequisites";
		database = WordNetDatabase.getFileInstance();
	}
	
	private void getSynonyms(){
		
		Synset[] synsets = database.getSynsets(word);		

		if (synsets.length > 0){			
			System.out.println("The following synsets contain '" +
					word + "' or a possible base form " + 	"of that text:");
			System.out.println("----------------------------------------------------------------------------");
			
			for (int i = 0; i < synsets.length; i++){
				System.out.println("");
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++){
					System.out.print((j > 0 ? ", " : "") + wordForms[j]);							
				}				
			}			
		}
		
		else{
			System.err.println("No synsets exist that contain the word form '" + word + "'");			
		}
		
	}
}
