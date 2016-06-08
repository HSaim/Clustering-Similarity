package org.methodClustering.KMeans;




import java.util.ArrayList;
import java.util.List;



import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Synset;
import edu.cmu.lti.jawjaw.util.Configuration;
import edu.cmu.lti.jawjaw.util.WordNetUtil;
import edu.cmu.lti.lexical_db.data.Concept;
/**
 * 
 * @author Habiba Saim
 * Implementation of WordNet Search for Java(WS4J) API
 * Similarity is calculated in three ways here
 * 1- Similarity between original words
 * 2- Similarity between stemmed words
 * 3- Similarity between words after converting morphological words into their base.
 * The conversion of morphological words is done through Java API for WordNet Search(JAWS)
 */

public class SimilarityCalculation {
 
	private  static ILexicalDatabase db;
	
	// similarity calculators
	private  static RelatednessCalculator lin; 
	private static  RelatednessCalculator wup; 
	private  static RelatednessCalculator hso;
	private  static RelatednessCalculator lch; 
	private  static RelatednessCalculator jcn; 
	private  static RelatednessCalculator lesk;
	private  static RelatednessCalculator path; 
	private  static RelatednessCalculator res;
	private  static InflectionalMorphology morph;
	
	
	public SimilarityCalculation (){
		
		db =  new NictWordNet();
		lin = new Lin(db);
		wup = new WuPalmer(db);
		hso = new HirstStOnge(db);
		lch = new LeacockChodorow(db);
		jcn = new JiangConrath(db);
		lesk  = new Lesk(db);
		path  = new Path(db);
		res  = new Resnik(db);
		
		morph = new InflectionalMorphology();		
	}
	
	/**
	 * 
	 * @param word1 First word to be compared for similarity calculation
	 * @param word2	Second word to be compared for similarity calculation
	 * 
	 * Calculates similarity between word1 and word2
	 * 
	 */
	public double computeSimilarity(String word1, String word2){		
				
        String baseWord1, baseWord2;
        boolean wordFound;
        double score = -1;
        double score1 = -1;
        double score2 = -1;
        
		//Similarity Calculation on original words
		System.out.println("Similarity calculation on original words");
		System.out.println("---------------------------------------");
		System.out.println("Original Words: " + word1 + ", "+word2);
		score1 = findSimilarity(word1, word2);
		System.out.println("");		
		
		//Similarity calculation after converting morphological words to their base
		//baseWord1 = morph.getBaseWord(word1);
		//baseWord2 = morph.getBaseWord(word2);
		
		
		//Check if word1 does not exist in WordNet, then convert it into base word  
		//Use WS4J API to check whether a word is in WordNet DB or not
		//wordFound = isInWordNet(word1);  
		
		//Use JAWS API to check whether a word is in WordNet DB or not
		wordFound = morph.isInWordNet(word1);
		if (!wordFound)
			baseWord1 = morph.getBaseWord(word1);
		else
			baseWord1 = word1;
		
		//Use WS4J API to check whether a word is in WordNet DB or not
		//wordFound = isInWordNet(word2);
		
		//Use JAWS API to check whether a word is in WordNet DB or not
		wordFound = morph.isInWordNet(word2);
		if (!wordFound)
			baseWord2 = morph.getBaseWord(word2);
		else
			baseWord2 = word2;
		
		System.out.println("Similarity calculation on base words");
		System.out.println("---------------------------------------");
		System.out.println("Inflectional Morphological Words: " + word1 + ", "+word2);
		System.out.println("Base Words: " + baseWord1 + ", "+baseWord2);
		score2 = findSimilarity(baseWord1, baseWord2);
		
		if (score1>score2){
			score = score1;
		}
		else{
			score = score2;
		}
		return score;
		
	}
		/**
	 * 
	 * @param word1
	 * @param word2
	 * @return
	 
	 * If we use compute() to find the similarity,
	 * This uses the first word sense by default. 
	 * For example When I calculate WuPalmer similarity between "gender" and "sex", it will return 0.26.
	 * If I use online demo, it will return 1.0. But if we use "gender#n#1" and "sex#n#1" the online demo will return 0.26, so there is no discrepancy. 
	 * The online demo calculates the max of all pos tag / word sense pairs. 
	 * That is why code of findSimilarity() function is written.
	 */
	private void compute(String word1, String word2) {
		WS4JConfiguration.getInstance().setMFS(true);
		double s = wup.calcRelatednessOfWords(word1, word2);
		//return s;
		System.out.println("sim('" + word1 + "', '" + word2 + "') =  " +s);
	}	
	
	/**
	 * 
	 * Calculates similarity between two words.
	 * Any RelatednessCalculator can be used here by changing 'wup' with any other calculator
	 * already defined in the constructor.
	 * While calculating the relatedness,
	 *  it gets the score of relatedness and stores the highest score in the variable maxScore
	 */
	private static double findSimilarity(String word1, String word2){
		
		WS4JConfiguration.getInstance().setMFS(true);
		List<POS[]> posPairs = wup.getPOSPairs();
		double maxScore = -1D;

		for(POS[] posPair: posPairs) {
		    List<Concept> synsets1 = (List<Concept>)db.getAllConcepts(word1, posPair[0].toString());
		    List<Concept> synsets2 = (List<Concept>)db.getAllConcepts(word2, posPair[1].toString());

		    for(Concept synset1: synsets1) {
		        for (Concept synset2: synsets2) {
		            Relatedness relatedness = wup.calcRelatednessOfSynset(synset1, synset2);
		            double score = relatedness.getScore();
		            if (score > maxScore && score<=1.0) { 
		                maxScore = score;
		            }
		        }
		    }
		}

		if (maxScore == -1D) {
		    maxScore = 0.0;
		}

		System.out.println("sim('" + word1 + "', '" + word2 + "') =  " + maxScore);
		return maxScore;
	}
	
	private boolean isInWordNet(String word){
		boolean flag = false;
		WS4JConfiguration.getInstance().setMFS(true);
		List<POS[]> posPairs = wup.getPOSPairs();
		List<Concept> synsets = new ArrayList<Concept>();
		
		for(POS[] posPair: posPairs) {
			 synsets = (List<Concept>) db.getAllConcepts(word, posPair[0].toString());
		}
		System.out.println("Concepts of '" + word + "' are:");
		if (!synsets.isEmpty()){
			flag = true;
			for (int i=0; i<synsets.size(); i++){
				System.out.println("Concept: " +i + ": " + synsets.get(i));
			}
		}
		return flag;		
	}	
}
