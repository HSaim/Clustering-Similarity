
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * WordSimDBFinder, implementing several basic functionalies of using a word similarity database.
 */

public class WordSimFinder {

	private static String driverName = "org.sqlite.JDBC";
	private static String dataBase;

	/**
	 * Creates a WordSimDBFacade from a SQLite database file, which stores word similarity information.
	 * @param dataFile The location of the SQLite database file
	 */  
	public WordSimFinder(String dataFile) {	
		this.dataBase = dataFile;
	}

	
	/** 
	 * Transforms a word into its root form, this function relies on Porter stemmer in Java.
	 * @param word The original word
	 * @return The word after stemming.
	 */  
	public String stemWord(String word)
	{
		Stemmer stemmer = new Stemmer();
		stemmer.add(word.toCharArray(), word.length());
		stemmer.stem();
		return stemmer.toString();
	}
	
	/** 
	 * Checks whether a word exists in the word similarity database.
	 * @param word The word to be checked
	 * @return true if and only if the input word exists in the database; false otherwise.
	 * @throws Exception If exception happens during database manipulation.
	 */  
	public Boolean isInDatabase(String word) {
		Connection c = null;
		Statement stmt = null;
		Boolean isIn = false;

		try {
			//connect with database
			Class.forName(this.driverName);
			c = DriverManager.getConnection("jdbc:sqlite:" + this.dataBase);
			c.setAutoCommit(false);

			//execute related query
			stmt = c.createStatement();
			String query = "select exists (select 1 from Word_Similarity where term_1="
					+ "'" + word + "'" + ");";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String returnValue = rs.getString(1);
				if (returnValue.equals("1")) {
					isIn = true;
				}
			}
			
			//release resource
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		return isIn;
	}

	/** 
	 * Returns the similarity between two given words.
	 * @param word1 One of the given words
	 * @param word2 One of the given words
	 * @return A similarity for the given two words. It returns 1 
	 * if word1 and word2 are the same. It returns -1 
	 * if anyone of the two given words does not exist in the word similarity database.
	 * @throws Exception If exception happens during database manipulation.
	 */  
	public double computeSimilarity(String word1, String word2) {
		double similarity = 0;
		
		if(word1.equals(word2)) return 1;
		//check whether the input words exist in the database
		if (isInDatabase(word1) && isInDatabase(word2)) {
			Connection c = null;
			Statement stmt = null;

			try {
				//connect with database
				Class.forName(this.driverName);
				c = DriverManager.getConnection("jdbc:sqlite:" + this.dataBase);
				c.setAutoCommit(false);

				//execute related query
				stmt = c.createStatement();
				String query = "select similarity from Word_Similarity where term_1='"
						+ word1 + "'" + "and term_2='" + word2 + "';";
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					similarity = rs.getDouble("similarity");
				}
				
				//release resource
				rs.close();
				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
		} else {
			similarity = -1;
		}
		return similarity;
	}

	/** 
	 * Returns the most similar word for an input word.
	 * @param word The input word
	 * @return The most similar word of the input word. It returns null if the input 
	 * word does not exist in the database.
	 * @throws Exception If exception happens during database manipulation.
	 */  
	public String findMostSimilarWord(String word) {		
		//check whether the input words exist in the database
		if (isInDatabase(word)) {
			String mostSimilarWord = "";
			Connection c = null;
			Statement stmt = null;

			try {
				//connect with database
				Class.forName(this.driverName);
				c = DriverManager.getConnection("jdbc:sqlite:" + this.dataBase);
				c.setAutoCommit(false);

				//execute related query
				stmt = c.createStatement();
				String query = "select term_2 from Word_Similarity where term_1='"
						+ word + "'" + " order by similarity desc limit 1;";
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					mostSimilarWord = rs.getString("term_2");
				}
				
				//release resource
				rs.close();
				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
			return mostSimilarWord;
		} else {
			return null;
		}
	}

	/** 
	 * Returns all words whose similarity with an input word is equal or greater than a minimum similarity threshold.
	 * @param word The input word
	 * @param minSim The minimum similarity threshold
	 * @return A list of words whose similarity with an input word is equal or grater than the given 
	 * minimum similarity threshold. It returns null if the input word does not exist in the database.
	 * @throws Exception If exception happens during database manipulation.
	 */  
	public List<String> findMostSimilarWords(String word, double minSim) {	
		//check whether the input words exist in the database
		if (isInDatabase(word)) {
			List<String> mostSimilarWords = new ArrayList<String>();
			Connection c = null;
			Statement stmt = null;

			try {
				//connect with the database
				Class.forName(this.driverName);
				c = DriverManager.getConnection("jdbc:sqlite:" + this.dataBase);
				c.setAutoCommit(false);

				//execute related query
				stmt = c.createStatement();
				String query = "select term_2 from Word_Similarity where term_1='"
						+ word
						+ "'"
						+ " and Word_Similarity.similarity >="
						+ minSim + ";";
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					String similarWord = rs.getString("term_2");
					mostSimilarWords.add(similarWord);
				}
				
				//release resource
				rs.close();
				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
			return mostSimilarWords;
		} else {
			return null;
		}

	}
	
	/** 
	 * Returns the Top-N most similar words to an input word.
	 * @param word The input word
	 * @param n The number of returned words
	 * @return A list of n words which are most similar with the input word. 
	 * It returns null if the input word does not exist in the database.
	 * @throws Exception If exception happens during database manipulation.
	 */  
	public List<String> findTopNWords(String word, Integer n) {	
		//check whether the input words exist in the database
		if (isInDatabase(word)) {
			List<String> topNWords = new ArrayList<String>();
			Connection c = null;
			Statement stmt = null;

			try {
				//connect with database
				Class.forName(this.driverName);
				c = DriverManager.getConnection("jdbc:sqlite:" + this.dataBase);
				c.setAutoCommit(false);

				//execute related query
				stmt = c.createStatement();
				String query = "select term_2 from Word_Similarity where term_1='"
						+ word
						+ "'"
						+ " order by similarity desc limit "
						+ n
						+ ";";
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					String SimilarWord = rs.getString("term_2");
					topNWords.add(SimilarWord);
				}
				
				//release resource
				rs.close();
				stmt.close();
				c.close();
			} catch (Exception e) {
				System.err.println(e.getClass().getName() + ": "
						+ e.getMessage());
				System.exit(0);
			}
			return topNWords;
		} else {
			return null;
		}

	}

	/** 
	 * Returns all words that appear in the database.
	 * @return All words exist in the database.
	 * @throws Exception If exception happens during database manipulation.
	 */  
	public int getAllWords() {		
		List<String> allWords = new ArrayList<String>();
		Connection c = null;
		Statement stmt = null;

		try {
			//connect with database
			Class.forName(this.driverName);
			c = DriverManager.getConnection("jdbc:sqlite:" + this.dataBase);
			c.setAutoCommit(false);

			//execute related query
			stmt = c.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT distinct term_1 FROM Word_Similarity order by term_1 desc;");
			while (rs.next()) {
				String term1 = rs.getString("term_1");
				allWords.add(term1);
			}
			
			//release resource
			rs.close();
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		//shuffle
		//return RandomizeArray(allWords);
		return allWords.size();
	}
	
	private List<String> RandomizeArray(List<String> array){
		Random rgen = new Random();  // Random number generator			
 
		for (int i=0; i<array.size(); i++) {
		    int randomPosition = rgen.nextInt(array.size());
		    String temp = array.get(i);
		    array.set(i,array.get(randomPosition));
		    array.set(randomPosition, temp);
		}
 
		return array;
	}
}

