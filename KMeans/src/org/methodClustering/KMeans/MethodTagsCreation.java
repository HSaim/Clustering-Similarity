


package org.methodClustering.KMeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 
 * @author Habiba Saim
 * This class populates method vector
 * Then convert the morphological words into their base words
 * And replaces all synonyms by one word 
 *
 */

public class MethodTagsCreation {
	
	private static final double MIN_SIMILARITY_SCORE = 0.9;
	private List<List<String>> methodsTagsOriginal;
	private List<List<String>> methodsTagsLowerCase;
	private List<List<String>> methodsTagsStemmed;
	private List<List<String>> methodsTagsConverted;						//Keywords/tags after synonyms removal
	//List<List<String>> methodsTagsStmdSyn = new ArrayList<List<String>>();
	//String inputFile = System.getProperty("user.dir")+"/SEWordSim-r1.db";   //This path is useful when db is in SOM folder
	String inputFile = "D:/CRP/SEWordSimDB/SEWordSim-r1.db";
	WordSimFinder facade = new WordSimFinder(inputFile);
//	SimilarityCalculation sc = new SimilarityCalculation();	
	private CSVReader reader; 
	
	public MethodTagsCreation(){
		 methodsTagsOriginal = new ArrayList<List<String>>();
		 methodsTagsStemmed = new ArrayList<List<String>>();
		 facade = new WordSimFinder(inputFile);
//		 sc = new SimilarityCalculation();
		 reader  = new CSVReader();
		
	}
	
	
	/**
	 * Creates ArrayList of recommended methods with their tags 
	 */
	public void populateMethodTagsList(){
		
		populateTenthList();
		//populateListfromCSV();
		 //stemMethodsTags();
		convertInLowecase();
		sortArrayList(methodsTagsLowerCase);
		//replaceSimilarTags();
		
		System.out.println("\n\tOriginal Methods Tags in lower case: ");
		System.out.println("---------------------------------------");
		displayMethods( methodsTagsLowerCase);
		
		/*System.out.println("\n\tUpdated Methods Tags after removal of morphological words");
		System.out.println("------------------------------------------------------------------------------");
		displayMethods(methodsTagsConverted);*/
		 // stemMethodsTags();
		/* convertInLowecase();
		replaceSimilarTags();
		
		System.out.println("\n\tOriginal Methods Tags in lower case: ");
		System.out.println("---------------------------------------");
		displayMethods( methodsTagsLowerCase);
		
		System.out.println("\n\tUpdated Methods Tags after removal of morphological words");
		System.out.println("------------------------------------------------------------------------------");
		displayMethods(methodsTagsConverted);*/
		
	}
	
	/**
	 * Convert all tags into same case i.e. lower case 
	 */
	private void convertInLowecase(){
		
		methodsTagsLowerCase  = new ArrayList<List<String>>();
		final int arrSize = 10;											//Max number of  keywords of each recommended method/project 
		
		String[] arr;
		
		//Code to get all keywords of recommended methods/keywords
		/*for (int i=0; i<methodsTagsOriginal.size(); i++){
			arr =methodsTagsOriginal.get(i).toArray(new String[methodsTagsOriginal.get(i).size()]);
			methodsTagsLowerCase.add(Arrays.asList(arr));
		}*/
		
		//Code to get 'arrSize' keywords of recommended methods/keywords
		for (int i=0; i<methodsTagsOriginal.size(); i++){
			arr =methodsTagsOriginal.get(i).toArray(new String[methodsTagsOriginal.get(i).size()]);
			String[] arr20 = new String[arrSize];
			for (int j=0; j<arrSize && j<arr.length; j++){
				arr20[j] = arr[j];				
			}
			methodsTagsLowerCase.add(Arrays.asList(arr20));
			System.out.println(methodsTagsLowerCase.get(i));
		}
	
		for (int i=0; i<methodsTagsLowerCase.size();  i++){
			for(int j=0; j < methodsTagsLowerCase.get(i).size(); j++) {
//				System.out.print(methodsTagsLowerCase.get(i).get(j));
				methodsTagsLowerCase.get(i).set(j, methodsTagsLowerCase.get(i).get(j).toLowerCase()); 
			}	
		}
	}

	
	/**
	 * Handling of synonyms with WordNet API - WS4J
	 */
	public void replaceSimilarTags(){
		
		methodsTagsConverted  = new ArrayList<List<String>>();
			
		for (int i=0; i<methodsTagsLowerCase.size(); i++){
			String []arr =methodsTagsLowerCase.get(i).toArray(new String[methodsTagsLowerCase.get(i).size()]);
			methodsTagsConverted.add(Arrays.asList(arr));
		}
		 

		InflectionalMorphology morph = new InflectionalMorphology();
			
			for (int i=0; i<methodsTagsConverted.size(); i++){
				List<String> tags1 = methodsTagsConverted.get(i);
				//System.out.println("Methd for comparison\n" + tags1);
				for (int j=0; j<tags1.size(); j++){
					String tag = tags1.get(j);
						for (int k=i; k<methodsTagsConverted.size(); k++){
							List<String> tags2 = methodsTagsConverted.get(k);
							//System.out.println("Method "+ k + " for syn removal\n" + tags2);
							for(int l=0; l<tags2.size(); l++){
//								System.out.println("Tag before replace: " + tags2.get(l));
								if (!tag.equals(tags2.get(l))){
//									System.out.println(morph.isSynonym(tag, tags2.get(l)));
//									System.out.println(tag+" "+tags2.get(l));
									if (morph.isSynonym(tag, tags2.get(l))){
//										System.out.println("\nIn method: " + i + " & method: " +k);
//										System.out.println("Original n syn tags: " + tag + " - " +tags2.get(l) );
										tags2.set(l, tag);	
//										System.out.println("Tag after replace: " + tags2.get(l));
										break;
									}
								}
							}
							methodsTagsConverted.set(k, tags2);
//							System.out.println("Updated method after synonym removal " + k + "\n" + methodsTagsConverted.get(k));
						}
					//}
				}
			}
			
		//Duplication removal from converted methods tags
		for (int i=0; i<methodsTagsConverted.size(); i++){
			for (int j=0; j<methodsTagsConverted.get(i).size(); j++){
				List<String> tags;
				tags = new ArrayList<String>(new LinkedHashSet<String>(methodsTagsConverted.get(i)));
				//Sorting of methods tags
				// Collections.sort(tags);
				methodsTagsConverted.set(i, tags);
			}
		}
			 
		sortArrayList(methodsTagsConverted);
			
	}

	public List<List<String>> getOriginalMethodsTags(){
		return methodsTagsOriginal;
	}
	
	public List<List<String>> getStemmedMethodsTags(){
		return methodsTagsStemmed;
	}
	
	public List<List<String>> getConvertedMethodsTags(){
		return methodsTagsConverted;
	}
	
	public List<List<String>> getLowerCaseMethodsTags(){
		return methodsTagsLowerCase;
	}
	
	public void displayMethods(List<List<String>> methods){		
		
		for(int i=0; i<methods.size(); i++){
			System.out.println(methods.get(i));
		}
	}
	
	private void sortArrayList(List<List<String>> methods){
		for (int i=0; i<methods.size(); i++){
			 Collections.sort(methods.get(i));
		}		
	}
	
	/**
	 * Creates the 1st testing batch of methods tags 
	 */
	private void populateFirstList(){
		//Due to  more than one word in tags, following data is not good for clustering 
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "pre requisite", "fee criteria", "tuition fee"));
		 
//		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "pre requisite", "fee criteria", "tuition fee"));
//		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "pre requisite", "fee criteria", "tuition fee"));
//		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "pre requisite", "fee criteria", "tuition fee"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "tuition fee", "application requirements", "qualification", "admission dates", "admission documents"));
		 //methodsTagsOriginal.add(Arrays.asList("weapon styles", "weapon types", "number of players", "degree", "game modes", "colour"));
		 methodsTagsOriginal.add(Arrays.asList("weapon styles", "weapon types", "number of players","degree", "game modes", "colour selection"));
		 methodsTagsOriginal.add(Arrays.asList("weapon styles",  "number of players", "weapon types", "instructions", "demo", "colour selection"));
		 methodsTagsOriginal.add(Arrays.asList("weapon styles", "game modes", "number of players", "demo", "level selection", "player info"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "admission form", "admission dates", "eligibility criteria"));
		 methodsTagsOriginal.add(Arrays.asList("fee criteria", "pre requisite", "application requirements", "financial aid", "test schedule", "degree"));
		 methodsTagsOriginal.add(Arrays.asList("fee criteria",  "application requirements", "pre requisite", "financial aid", "test schedule", "degree"));
		 //methodsTagsOriginal.add(Arrays.asList("pay online", "registration", "pay modes", "website", "test criteria", "FAQ"));
		 methodsTagsOriginal.add(Arrays.asList("game", "quick instructions", "3D", "number of players", "help", "demo"));
		 methodsTagsOriginal.add(Arrays.asList("SAT I", "Programmes", "admission process", "Admissions and Aid", "entry test", "apply online"));
		 methodsTagsOriginal.add(Arrays.asList("admissions", "weapon styles", "degree", "number of players", "game modes", "demo"));
		 methodsTagsOriginal.add(Arrays.asList("weapon styles", "weapon types", "number of players", "applications", "game modes", "colour"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "pre requisite", "fee criteria", "tuition fee"));		 
	}
	
	/**
	 * Creates the 2nd testing batch of methods tags 
	 */
	private void populateSecondList(){
		
		//Compound words don't help in strong clustering
		
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "pre-requisite", "fee-criteria", "tuition-fee"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "tuition-fee", "application-requirements", "qualification", "admission-dates", "admission-documents"));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles", "weapon-types", "number-of-players", "degree", "game-modes", "colour"));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles", "weapon-types", "number-of-players","degree", "game-modes", "colour-selection"));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles",  "numbe-of-players", "weapon-types", "instructions", "demo", "colour-selection"));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles", "game-modes", "number-of-players", "demo", "level-selection", "player-info"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "admission-form", "admission-dates", "eligibility-criteria"));
		 methodsTagsOriginal.add(Arrays.asList("fee-criteria", "pre-requisite", "application-requirements", "financial-aid", "test-schedule", "degree"));
		 methodsTagsOriginal.add(Arrays.asList("fee-criteria",  "application-requirements", "pre-requisite", "financial-aid", "test-schedule", "degree"));
		 methodsTagsOriginal.add(Arrays.asList("admissions", "weapon-styles", "degree", "number-of-players", "game-modes", "demo"));
		 methodsTagsOriginal.add(Arrays.asList("SAT-I", "Programmes", "admission-process", "Admissions-and-Aid", "entry-test", "apply-online"));
		 methodsTagsOriginal.add(Arrays.asList("game", "quick-instructions", "3D", "number-of-players", "help", "demo"));
		 methodsTagsOriginal.add(Arrays.asList("pay-online", "registration", "pay-modes", "website", "test-criteria", "FAQ"));	
	}
	
	/**
	 * Creates the 3rd testing batch of methods tags 
	 */
	private void populateThirdList(){
		
		//Compound words don't help in strong clustering
		/******************Method Updates after adding new tags*****************/
		
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "pre-requisite", "fee-criteria", "tuition-fee", "help", "Undergraduate-Admissions", "Executive-Education", "Transfer-Admissions"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "tuition-fee", "application-requirements", "qualification", "admission-dates", "admission-documents", "FAQs", "Fee-Structure", "Funds", "tuition-fee"));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles", "weapon-types", "number-of-players", "degree", "game-modes", "colour", "Utilities", "Software", "Process", "Smart-Phones"));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles", "weapon-types", "number-of-players","degree", "game-modes", "colour-selection", "Name", "Updates", "Software", "Platform"));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles",  "number-of-players", "weapon-types", "instructions", "demo", "colour-selection","Updates", "Selections", "Options", "Mobile-Games" ));
		 methodsTagsOriginal.add(Arrays.asList("weapon-styles", "game-modes", "number-of-players", "demo", "level-selection", "player-info","Starred", "Objects", "Weapons", "Play"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "admission-form", "admission-dates", "eligibility-criteria","Qualification", "Programs-Offered", "Admission-Policies", "Options"));
		 methodsTagsOriginal.add(Arrays.asList("fee-criteria", "pre-requisite", "application-requirements", "financial-aid", "test-schedule", "degree","FAQs", "Demo", "Instructions", "Programs"));
		 methodsTagsOriginal.add(Arrays.asList("fee-criteria",  "application-requirements", "pre-requisite", "financial-aid", "test-schedule", "degree","Objects", "Apply-Online", "Required-Documents", "Transfer-Admissions"));
		 methodsTagsOriginal.add(Arrays.asList("admissions", "weapon-styles", "degree", "number-of-players", "game-modes", "demo","level-selection", "Software", "Platform", "FAQs"));
		 methodsTagsOriginal.add(Arrays.asList("SAT-I", "Programmes", "admission-process", "Admissions-and-Aid", "entry-test", "apply-online","Transfer-Admissions", "Executive-Programs", "NOP", "Admission-Policy"));
		 methodsTagsOriginal.add(Arrays.asList("game", "quick-instructions", "3D", "number-of-players", "help", "demo","Objects", "Play", "level-selection", "Utilities"));
		 methodsTagsOriginal.add(Arrays.asList("pay-online", "registration", "pay-modes", "website", "test-criteria", "FAQs","Transfer-Admissions", "Executive-Programs", "NOP", "Smart-Phones"));
		 
	}
	
	/**
	 * Creates the 4th testing batch of methods tags 
	 */
	private void populateFourthList(){
		
		//Compound words don't help in strong clustering
		/********** More Updations with small broken method tags *********/
		
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "feecriteria", "tuitionfee", "help", "Undergraduate", "Executive", "Transfer"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "tuitionfee",  "qualification", "admission", "documents", "FAQs", "FeeStructure", "Dates", "Tuitionfee"));
		 methodsTagsOriginal.add(Arrays.asList("weapon","styles", "types", "numberofplayers", "degree", "gamemodes", "colour", "Utilities", "Software", "Process", "Smart", "Phones"));
		 methodsTagsOriginal.add(Arrays.asList("weapon","styles", "degree", "game", "modes", "colour", "Name", "Updates", "Software", "Platform"));
		 methodsTagsOriginal.add(Arrays.asList("weapon",  "number-of-players", "types", "instructions", "demo", "colourselection","Updates", "Selections", "Options", "Mobile" ));
		 methodsTagsOriginal.add(Arrays.asList("styles", "gamemodes", "number-of-players", "demo", "levelselection", "player-info","Starred", "Objects", "Weapons", "Play"));
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "admission-form", "dates", "eligibility","Qualification", "Programs", "Policies", "Options"));
		 methodsTagsOriginal.add(Arrays.asList("feecriteria", "prerequisite", "requirements", "financialaid", "testschedule", "degree","FAQs", "Demo", "Instructions", "Programs"));
		 methodsTagsOriginal.add(Arrays.asList("fee",  "requirements", "prerequisite", "financialaid", "testschedule", "degree","Objects", "ApplyOnline", "Required-Documents", "Transfer-Admissions"));
		 methodsTagsOriginal.add(Arrays.asList("admissions", "weaponstyles", "degree", "number-of-players", "gamemodes", "demo","levelselection", "Software", "Platform", "FAQs"));
		 methodsTagsOriginal.add(Arrays.asList("SATI", "Programmes", "admissionprocess", "AdmissionsandAid", "entrytest", "applye","Transfer", "Executive", "NOP", "Admission"));
		 methodsTagsOriginal.add(Arrays.asList("game", "quickinstructions", "3D", "number-of-players", "help", "demo","Objects", "Play", "levelselection", "Utilities"));
		 methodsTagsOriginal.add(Arrays.asList("payonline", "registration", "paymodes", "website", "testcriteria", "FAQs","Transfer", "Executive", "NOP", "SmartPhones"));		 
	}
	
	/**
	 * Creates 5th testing batch of methods tags 
	 */
	private void populateFifthList(){
		 /********** More Updations with single words tags *********/
		/***** 5th methodsTags ArrayList **********/
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "undergraduate", "executive", "transfer"));
		 
		 methodsTagsOriginal.add(Arrays.asList("degree", "fee",  "qualification", "entrance", "documents", "faqs", "structure", "dates", "tuition"));
		
		 methodsTagsOriginal.add(Arrays.asList("weapon","styles", "types", "players", "degree", "game","modes", "colour", "utilities", "software", "Process", "Smart", "Phones"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "game", "modes", "colour", "name", "updates", "software", "platform"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon",  "players", "types", "instructions", "demo", "colour","updates", "selections", "options", "mobile" ));
		 
		 methodsTagsOriginal.add(Arrays.asList("styles", "game", "modes", "players", "demo", "level", "selection", "info","starred", "objects", "weapons", "play"));
		 
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "form", "dates", "eligibility","qualification", "programs", "policies", "options"));
		 
		 methodsTagsOriginal.add(Arrays.asList("fee","criteria", "prerequisite", "requirements", "financial","aid", "test", "schedule", "degree","faqs", "demo", "instructions", "Programs"));
		 
		 methodsTagsOriginal.add(Arrays.asList("fee",  "requirements", "prerequisite", "financial", "aid", "test", "schedule", "degree","objects", "apply", "online", "required", "documents", "transfer", "admissions"));
		 
		 methodsTagsOriginal.add(Arrays.asList("admissions", "weapon", "styles", "degree", "players", "game", "modes", "demo","level", "selection", "Software", "Platform", "FAQs"));
		
		 methodsTagsOriginal.add(Arrays.asList("SAT1", "Programmes", "process", "Admissions", "Aid", "entry", "test", "apply","Transfer", "Executive", "NOP"));
		 
		 methodsTagsOriginal.add(Arrays.asList("game", "instructions", "3D", "players", "help", "demo","Objects", "Play", "level", "Utilities"));
		
		 methodsTagsOriginal.add(Arrays.asList("pay", "online", "registration", "modes", "website", "test", "criteria", "FAQs","Transfer", "Executive", "NOP", "Phones"));
		 
	}
	

	/**
	 * Creates 6th testing batch of methods tags 
	 */
	private void populateSixthList(){
		
		/******************Method Updates after having more meaningful tags relevant to domains*****************/
		/***** 6th methodsTags ArrayList **********/
		methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "Undergraduate", "Executive", "Transfer"));
		 
		methodsTagsOriginal.add(Arrays.asList("SATI", "Programes", "process", "Admissions", "Aid", "entry", "test", "apply","Transfer", "Executive", "NOP"));
		 
		 methodsTagsOriginal.add(Arrays.asList("fee","criteria", "prerequisite", "requirements", "financial","aid", "test", "schedule", "degree","FAQs", "Demo", "Instructions", "Programs"));
		 
		methodsTagsOriginal.add(Arrays.asList("degree", "fee",  "qualification", "admission", "documents", "FAQs", "Structure", "Dates", "tuition"));
		 
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "form", "dates", "eligibility","Qualification", "Programs", "Policies", "Options"));
		
		 methodsTagsOriginal.add(Arrays.asList("pay", "online", "registration", "modes", "website", "test", "criteria", "FAQs","Transfer", "Executive", "NOP", "Phones"));
		 		 
		 methodsTagsOriginal.add(Arrays.asList("fee",  "requirements", "prerequisite", "financial", "aid", "test", "schedule", "degree","Objects", "Apply", "Online", "Required", "Documents", "Transfer", "Admissions"));
		 
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon",  "players", "types", "instructions", "demo", "colour","Updates", "Selections", "Options", "Mobile" ));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "game", "modes", "colour", "Name", "Updates", "Software", "Platform"));
		 
		 methodsTagsOriginal.add(Arrays.asList("admissions", "weapon", "styles", "degree", "players", "game", "modes", "demo","level", "selection", "Software", "Platform", "FAQs"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon","styles", "types", "players", "degree", "game","modes", "colour", "Utilities", "Software", "Process", "Smart", "Phones"));
		 
		 methodsTagsOriginal.add(Arrays.asList("styles", "game", "modes", "players", "demo", "level", "selection", "info","Starred", "Objects", "Weapons", "Play"));
		 		 			 
		 methodsTagsOriginal.add(Arrays.asList("game", "instructions", "3D", "players", "help", "demo","Objects", "Play", "level", "Utilities"));		 
	
	}
	
	/**
	 * Creates 7th testing batch of methods tags 
	 */
	private void populateSeventhList(){
		/******************Method Updates after having more meaningful tags relevant to 3 domains*****************/
		/***** 7th methodsTags ArrayList **********/
	
		methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "Undergraduate", "Executive", "Transfer"));
		 
		methodsTagsOriginal.add(Arrays.asList("SATI", "Programes", "process", "Admissions", "Aid", "entry", "test", "apply","Transfer", "Executive", "NOP"));
		 
		 methodsTagsOriginal.add(Arrays.asList("fee","criteria", "prerequisite", "requirements", "financial","aid", "test", "schedule", "degree","FAQs", "Admissions", "Programs"));
		 
		methodsTagsOriginal.add(Arrays.asList("degree", "fee",  "qualification", "admission", "documents", "FAQs", "Structure", "Dates", "tuition"));
		 
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "form", "dates", "eligibility","Qualification", "Programs", "Policies", "Options"));
		
		 methodsTagsOriginal.add(Arrays.asList("Admissions","pay", "online", "registration", "modes", "website", "test", "criteria", "FAQs","Transfer", "Executive", "NOP", "Phones"));
		 		 
		 methodsTagsOriginal.add(Arrays.asList("fee",  "requirements", "prerequisite", "financial", "aid", "test", "schedule", "degree", "Apply", "Online", "Required", "SATI", "Transfer", "Admissions"));
		 
		
		 methodsTagsOriginal.add(Arrays.asList("weapon",  "players", "types", "instructions", "demo", "colour","Updates", "Selections", "Options", "Mobile" ));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "game", "modes", "colour", "Name", "Updates", "Software", "Platform"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "players", "game", "modes", "demo","level", "selection", "Software", "Platform", "FAQs"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon","styles", "types", "players", "degree", "game","modes", "colour", "Utilities", "Software", "Process", "Smart", "Phones"));
		 
		 methodsTagsOriginal.add(Arrays.asList("styles", "game", "modes", "players", "demo", "level", "selection", "info","Starred", "Objects", "Weapons", "Play"));
		 		 			 
		 methodsTagsOriginal.add(Arrays.asList("game", "instructions", "players", "help", "demo","Objects", "Play", "level", "Utilities"));
		 
	 
		 methodsTagsOriginal.add(Arrays.asList("tax", "return", "File", "review", "refund", "statement", "wage", "offices", "website", "income"));
		 
		 methodsTagsOriginal.add(Arrays.asList("File", "office", "software", "department", "instructions",  "profit", "loss"));
		 
		 methodsTagsOriginal.add(Arrays.asList("online", "tax", "file", "act", "income", "return", "forms", "credit", "refund"));
		 
		 methodsTagsOriginal.add(Arrays.asList("tax", "payer", "help", "stats", "bill", "income", "profit", "loss", "office"));	
	}
	
	/**
	 * Creates 8th testing batch of methods tags 
	 */
	private void populateEighthList(){
		/******************Method Updates after having same tags relevant to 3 domains*****************/
		/***** 8th methodsTags ArrayList **********/
			 
		methodsTagsOriginal.add(Arrays.asList("weapon",  "players", "types", "instructions", "demo", "colour","Updates", "Selections", "Options", "Mobile" ));
		
		methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "Undergraduate", "Executive", "Transfer"));
		
		methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "Undergraduate", "Executive", "Transfer"));
		//methodsTagsOriginal.add(Arrays.asList("SATI", "Programes", "process", "Admissions", "Aid", "entry", "test", "apply","Transfer", "Executive", "NOP"));
		 
		 //methodsTagsOriginal.add(Arrays.asList("fee","criteria", "prerequisite", "requirements", "financial","aid", "test", "schedule", "degree","FAQs", "Admissions", "Programs"));
		 
		//methodsTagsOriginal.add(Arrays.asList("degree", "fee",  "qualification", "admission", "documents", "FAQs", "Structure", "Dates", "tuition"));
		 
		// methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "form", "dates", "eligibility","Qualification", "Programs", "Policies", "Options"));
		
		// methodsTagsOriginal.add(Arrays.asList("Admissions","pay", "online", "registration", "modes", "website", "test", "criteria", "FAQs","Transfer", "Executive", "NOP", "Phones"));
		 		 
		// methodsTagsOriginal.add(Arrays.asList("fee",  "requirements", "prerequisite", "financial", "aid", "test", "schedule", "degree", "Apply", "Online", "Required", "SATI", "Transfer", "Admissions"));
		 
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon",  "players", "types", "instructions", "demo", "colour","Updates", "Selections", "Options", "Mobile" ));
		 
		// methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "Undergraduate", "Executive", "Transfer"));
		 
		// methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "game", "modes", "colour", "Name", "Updates", "Software", "Platform"));
		 
		// methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "players", "game", "modes", "demo","level", "selection", "Software", "Platform", "FAQs"));
		 
		// methodsTagsOriginal.add(Arrays.asList("weapon","styles", "types", "players", "degree", "game","modes", "colour", "Utilities", "Software", "Process", "Smart", "Phones"));
		 
		// methodsTagsOriginal.add(Arrays.asList("styles", "game", "modes", "players", "demo", "level", "selection", "info","Starred", "Objects", "Weapons", "Play"));
		 		 			 
		// methodsTagsOriginal.add(Arrays.asList("game", "instructions", "players", "help", "demo","Objects", "Play", "level", "Utilities"));
		 
	 
		 methodsTagsOriginal.add(Arrays.asList("tax", "return", "File", "review", "refund", "statement", "wage", "offices", "website", "income"));
		 methodsTagsOriginal.add(Arrays.asList("tax", "return", "File", "review", "refund", "statement", "wage", "offices", "website", "income"));
		 
		// methodsTagsOriginal.add(Arrays.asList("File", "office", "software", "department", "instructions",  "profit", "loss"));
		 
		// methodsTagsOriginal.add(Arrays.asList("online", "tax", "file", "act", "income", "return", "forms", "credit", "refund"));
		 
		// methodsTagsOriginal.add(Arrays.asList("tax", "payer", "help", "stats", "bill", "income", "profit", "loss", "office"));
		 
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "Undergraduate", "Executive", "Transfer"));
		 methodsTagsOriginal.add(Arrays.asList("tax", "return", "File", "review", "refund", "statement", "wage", "offices", "website", "income"));				
	}
	
	/**
	 * Creates 9th testing batch of methods tags 
	 */
	private void populateNinthList(){

		 /******************
		  * Method Updates after having more meaningful tags relevant to 3 domains 
		  * but methods of different domains are mixed up. Its 7th one. Just methods sequence is changed
		  * 	*****************/
		/***** 9th methodsTags ArrayList **********/
		
		methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "Undergraduate", "Executive", "Transfer"));
		 		 
		 methodsTagsOriginal.add(Arrays.asList("fee","criteria", "prerequisite", "requirements", "financial","aid", "test", "schedule", "degree","FAQs", "Admissions", "Programs"));
		 		 
		 methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "form", "dates", "eligibility","Qualification", "Programs", "Policies", "Options"));
		
		 methodsTagsOriginal.add(Arrays.asList("fee",  "requirements", "prerequisite", "financial", "aid", "test", "schedule", "degree", "Apply", "Online", "Required", "SATI", "Transfer", "Admissions"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon",  "players", "types", "instructions", "demo", "colour","Updates", "Selections", "Options", "Mobile" ));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "game", "modes", "colour", "Name", "Updates", "Software", "Platform"));
		 
		 methodsTagsOriginal.add(Arrays.asList("File", "office", "software", "department", "instructions",  "profit", "loss"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "players", "game", "modes", "demo","level", "selection", "Software", "Platform", "FAQs"));
		 
		 methodsTagsOriginal.add(Arrays.asList("weapon","styles", "types", "players", "degree", "game","modes", "colour", "Utilities", "Software", "Process", "Smart", "Phones"));
		 
		 methodsTagsOriginal.add(Arrays.asList("SATI", "Programes", "process", "Admissions", "Aid", "entry", "test", "apply","Transfer", "Executive", "NOP"));
		 
		 methodsTagsOriginal.add(Arrays.asList("styles", "game", "modes", "players", "demo", "level", "selection", "info","Starred", "Objects", "Weapons", "Play"));
		 		 			 
		 methodsTagsOriginal.add(Arrays.asList("game", "instructions", "players", "help", "demo","Objects", "Play", "level", "Utilities"));
		 
		 methodsTagsOriginal.add(Arrays.asList("degree", "fee",  "qualification", "admission", "documents", "FAQs", "Structure", "Dates", "tuition"));
		 
		 methodsTagsOriginal.add(Arrays.asList("tax", "return", "File", "review", "refund", "statement", "wage", "offices", "website", "income"));
		 
		 methodsTagsOriginal.add(Arrays.asList("Admissions","pay", "online", "registration", "modes", "website", "test", "criteria", "FAQs","Transfer", "Executive", "NOP", "Phones"));
		 
		 methodsTagsOriginal.add(Arrays.asList("online", "tax", "file", "act", "income", "return", "forms", "credit", "refund"));
		 
		 methodsTagsOriginal.add(Arrays.asList("tax", "payer", "help", "stats", "bill", "income", "profit", "loss", "office"));			
	}
	
	/**
	 * Creates 10th testing batch of methods tags 
	 */
	private void populateTenthList(){
		 /******************
		  * Method Updates after having more meaningful tags relevant to 3 domains including synonyms and morphological words
		  * but methods of different domains are mixed up. Its 7th one. Just methods sequence is changed
		  * 	*****************/
		/***** 10th methodsTags ArrayList **********/
		
		methodsTagsOriginal.add(Arrays.asList("degree", "admissions", "applications", "prerequisite", "fee","criteria", "tuition", "help", "undergraduate", "executive", "transfer"));
		 		 
		methodsTagsOriginal.add(Arrays.asList("fee","criteria", "prerequisites", "requirements", "financial","aid", "test", "schedule", "degree","faqs", "admissions", "programs"));
		 		 
		methodsTagsOriginal.add(Arrays.asList("documents", "admission", "applications", "form", "dates", "eligibility","qualification", "programs", "policies", "options"));
		
		methodsTagsOriginal.add(Arrays.asList("fee",  "requirements", "prerequisite", "financial", "aid", "test", "timetable", "degree", "apply", "online", "required", "sat1", "transfer", "admissions"));
		 
		methodsTagsOriginal.add(Arrays.asList("weapon",  "players", "types", "instructions", "demo", "colour","updates", "selections", "options", "mobile" ));
		 
		methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "game", "modes", "colour", "name", "updates", "software", "platform"));
		 
		methodsTagsOriginal.add(Arrays.asList("file", "office", "software", "department", "instructions",  "profit", "loss"));
		 
		methodsTagsOriginal.add(Arrays.asList("weapon", "styles", "degree", "players", "game", "modes", "demo","level", "selection", "software", "platform", "faqs"));
		 
		methodsTagsOriginal.add(Arrays.asList("weapon","styles", "types", "players", "degree", "game","modes", "colour", "utilities", "software", "process", "smart", "Phones"));
		 
	    methodsTagsOriginal.add(Arrays.asList("sat1", "programes", "process", "admissions", "aid", "entry", "test", "apply","transfer", "executive", "nop"));
		 
		methodsTagsOriginal.add(Arrays.asList("styles", "game", "modes", "players", "demo", "level", "selection", "info","Starred", "objects", "weapons", "play"));
		 		 			 
		methodsTagsOriginal.add(Arrays.asList("game", "instructions", "players", "help", "demo","objects", "play", "level", "Utilities"));
		 
		methodsTagsOriginal.add(Arrays.asList("degree", "fee",  "qualification", "admission", "documents", "faqs", "structure", "dates", "tuition"));
		 
		methodsTagsOriginal.add(Arrays.asList("tax", "return", "file", "review", "refund", "statement", "wage", "offices", "website", "income"));
		 
		methodsTagsOriginal.add(Arrays.asList("admissions","pay", "online", "registration", "modes", "website", "test", "criteria", "faqs","transfer", "executive", "nop", "phones"));
		 
		methodsTagsOriginal.add(Arrays.asList("online", "tax", "file", "act", "income", "return", "forms", "credit", "refund"));
		
		methodsTagsOriginal.add(Arrays.asList("tax", "payer", "help", "stats", "bill", "income", "profit", "loss", "office"));		
	}
	
	/**
	 * Populates methods tags arraylist from  CSV file, which contains actual recommended methods/projects and their tags
	 */
	private void populateListfromCSV(){
		reader.readCSV();
		methodsTagsOriginal = reader.populateArrayList();
		
	}
	
	
	//Stem methods tags
	//Store them in a new arraylist methodsTagsStemmed
	private void stemMethodsTags(){
					
			for (int i=0; i<methodsTagsLowerCase.size(); i++){
				List<String> tags= methodsTagsLowerCase.get(i);
				List<String> stemmedTags = new ArrayList<String>();
				for(int j=0; j<tags.size(); j++){
					//String tag =tags.get(j).toLowerCase() ;
					stemmedTags.add(facade.stemWord(tags.get(j).toLowerCase()));
				}
				methodsTagsStemmed.add(stemmedTags);
				
			}
			System.out.println("\nStemmed Methods Tags with synonyms");
			for(int i=0; i<methodsTagsStemmed.size(); i++){
				System.out.println(methodsTagsStemmed.get(i));
			}
			
			/**** Temporarily commented to check rest of the code, bcos it takes much time ******/
			/*handleSynonyms();
			System.out.println("\nStemmed Methods Tags with synonyms removed");
			for(int i=0; i<methodsTagsStemmed.size(); i++){
				System.out.println(methodsTagsStemmed.get(i));
			}
			*/
		}
		
		
	//Replace all synonyms with one word
	private void handleSynonyms (){
			
			
			double similarityScore;
			
			for (int i=0; i<methodsTagsStemmed.size(); i++){
				List<String> tags1 = methodsTagsStemmed.get(i);
				//System.out.println("Methd for comparison\n" + tags1);
				for (int j=0; j<tags1.size(); j++){
					String tag = tags1.get(j);
					if(facade.isInDatabase(tag)){
						//System.out.println(tag+ " is in DB");
						for (int k=i+1; k<methodsTagsStemmed.size(); k++){
							List<String> tags2 = methodsTagsStemmed.get(k);
							//System.out.println("Method "+ k + " for syn removal\n" + tags2);
							for(int l=0; l<tags2.size(); l++){
								//System.out.println("Tag before replace: " + tags2.get(l));
								if (!tag.equals(tags2.get(l))){
									similarityScore = facade.computeSimilarity(tag, tags2.get(l));
									if (similarityScore>=MIN_SIMILARITY_SCORE){
										System.out.println("\nIn method: " + i + " & method: " +k);
										System.out.println("Original n syn tags: " + tag + " - " +tags2.get(l) + ", Sim. Score: " + similarityScore);
										tags2.set(l, tag);	
										//System.out.println("Tag after replace: " + tags2.get(l));
										break;
									}
								}
							}
							methodsTagsStemmed.set(k, tags2);
							//System.out.println("Updated method after synonym removal " + k + "\n" + methodsTagsStemmed.get(k));
						}
					}
				}
			}
			
		}
		
}
