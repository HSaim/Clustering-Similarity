

import java.io.File;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.smu.tspell.wordnet.impl.file.Morphology;

/**
 * 
 * @author Habiba Saim
 * Implementation of 'Java API for WordNet Search (JAWS)' 
 * 		for simple word search and base word search
 * 
 *
 */

public class InflectionalMorphology {
	String wordNetDirectory;
	String path;
	WordNetDatabase database;
	
	public InflectionalMorphology(){
		this.wordNetDirectory = "WordNet-2-1";
        this.path = wordNetDirectory + File.separator + "dict";
		System.setProperty("wordnet.database.dir",path);
		this.database = WordNetDatabase.getFileInstance();		
	}
	
	/**
	 * 
	 * @param word - to be searched in WordNet DB
	 * @return true if word found
	 */
	public boolean isInWordNet(String word){
		boolean flag = true;
		Synset[] synsets = database.getSynsets(word);
		
		if (synsets.length > 0){	
			flag = true;
			/*System.out.println("The following synsets contain '" +
					word + "' or a possible base form " + 	"of that text:");
			System.out.println("----------------------------------------------------------");
			
			for (int i = 0; i < synsets.length; i++){
				System.out.println("");
				String[] wordForms = synsets[i].getWordForms();
				for (int j = 0; j < wordForms.length; j++){
					System.out.print((j > 0 ? ", " : "") + wordForms[j]);							
				}
				//System.out.println(": " + synsets[i].getDefinition());
				System.out.println();
			}*/
			
		}		
		else{
			//System.err.println("No synsets exist that contain the word form '" + word + "'");
			flag = false;
		}
		return flag;		
	}
	
	/**
	 * 
	 * @param word - morphological word to find its base word in WordNet DB
	 * @return - base word
	 */
	public String getBaseWord(String word){
		
		String[] arr;
		 
		Morphology id = Morphology.getInstance();
 
		arr = id.getBaseFormCandidates(word, SynsetType.VERB);		
		if (arr.length != 0)
			return arr[arr.length-1];
		
		arr = id.getBaseFormCandidates(word, SynsetType.ADJECTIVE);
		if (arr.length != 0)
			return arr[arr.length-1];
		
		arr = id.getBaseFormCandidates(word,SynsetType.ADVERB);
		if (arr.length != 0)
			return arr[arr.length-1];
			
		arr = id.getBaseFormCandidates(word, SynsetType.NOUN);
		if (arr.length != 0)
			return arr[arr.length-1];
		
		return word;
	}
}


