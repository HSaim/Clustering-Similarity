

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
/**
 * 
 * @author Habiba Saim
 * This class reads the output(in CSV format) recommended from code recommender
 * 	and stores it into a 2D arraylist
 */
public class CSVReader {
	
	String[] project;									//Line by line storage of CSV file
	List<List<String>> projects;						//All project numbers and their tags, extracted from project[]
	List<List<String>> methodsTags;						//Final 2D Arraylist(containing tags of each method/project in each row), to be processed for clustering
	
	/**
	 * Reads CSV file and stores in 'projects' arraylist
	 */
	public void readCSV() {

		String csvFile = "project_domain_keyword(ordered by frequency of words).csv";
		projects = new ArrayList<List<String>>();
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ";";		
		
		
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

			    // use semi cololn as separator
				project = line.split(csvSplitBy);
				//x = C(project[1]);
				//methodsTags.add( x, project[4] );
				project[1] = project[1].replace("\"","");					//project code
				project[4] = project[4].replace("\"","");					//project tag
				projects.add(Arrays.asList(project[1], project[4]));
//				System.out.println("Project [code= " + project[1] 
//	                                 + " , tag=" + project[4] + "]");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/*System.out.println("Project and its tags");
		for (int i=0; i<projects.size(); i++){
			System.out.println(i + " - " + projects.get(i));
		}*/
	  }
	 
	/**
	 * @return methodsTags 
	 * Stores data extracted from CSV file into methodsTags ArrayList
	 */
	 public List<List<String>> populateArrayList(){
		 
		 methodsTags = new ArrayList<List<String>>();
		 int x=1;
		 int y;
		 List<String> methodTags = new ArrayList<String>();
		 /*for (int i=0; i<projects.size(); i++){
			 methodTags = new ArrayList();
			 x = Integer.parseInt(projects.get(i).get(0));
			 while(x == i+1){
				 methodTags.add(projects.get(i).get(1));
			 }
		 }*/
		 
		 for (int i=0; i<projects.size(); i++){
			 //y=Integer.parseInt(projects.get(i).get(0));
			 y=Integer.valueOf(projects.get(i).get(0));
			 if(y==x){
				 methodTags.add(projects.get(i).get(1));
			 }
			 else{
				 x++;
				 
				//Keywords Duplication removal from Methods ArrayList
				 methodTags = new ArrayList<String>(new LinkedHashSet<String>(methodTags));
				 
				 methodsTags.add(methodTags);
				 methodTags = new ArrayList<String>();
				 methodTags.add(projects.get(i).get(1));
			 }
		 }
		 
		//Keywords Duplication removal from Methods ArrayList
		 methodTags = new ArrayList<String>(new LinkedHashSet<String>(methodTags));
		 
		 methodsTags.add(methodTags);
		 
		 System.out.println("Methods and their tags");
		 System.out.println("===========================");
		 for (int i=0; i<methodsTags.size(); i++){
			 System.out.println(i + " - " + methodsTags.get(i).size() + " tags - " + methodsTags.get(i));
		 }
		 
		 return methodsTags;
	 }

}
