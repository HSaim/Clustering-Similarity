import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;


public class SOMMain {
	
	//public static final int MAX_CLUSTERS = 6;
	public static final double DECAY_RATE = 0.8;
	public static final double MIN_ALPHA = 0.01;
	private double alpha;
	private int maxClusters;
	private List<List<String>> methodsTagsStemmed;
	private List<List<String>> methodsTagsUpdated;
	private List<List<String>> methodsTags;
	private List<List<Double>> methodsVector;
	private List<String> featureVector;
	private List<Integer> neuronsList;
	private MethodVectorCreation mVec;
	private MethodTagsCreation mTag;
	 
	private List<List<Float>> finalNeurons;
	
	//List<List<String>> methodsTagsOriginal = new ArrayList<List<String>>();
	//private DomainIdentification identify = new DomainIdentification();
	
	private void init(){
		this.alpha = 0.6;
		this.methodsTagsStemmed = new ArrayList<List<String>>();
		this.methodsTagsUpdated = new ArrayList<List<String>>();
		this.methodsTags = new ArrayList<List<String>>();
		this.methodsVector = new ArrayList<List<Double>>();
		this.featureVector = new ArrayList<String>();
		this.neuronsList = new ArrayList<Integer>();
		this.mVec = new MethodVectorCreation();
		this.mTag = new MethodTagsCreation();
		this.finalNeurons = new ArrayList<List<Float>>();
	}
	
	private void calculate(){

		 mTag.populateMethodTagsList();
		 //methodsTagsStemmed = mTag.getStemmedMethodsTags();
		 //methodsTagsOriginal = mTag.getOriginalMethodsTags();
		// methodsTags = mTag.getConvertedMethodsTags();
		 
		 
		 /**
		  * temporarily commented to observe WordNet behavior
		  */
		 //methodsTags = mTag.getOriginalMethodsTags();
		methodsTags = mTag.getConvertedMethodsTags();
		 JaccardDifference jD = new JaccardDifference(methodsTags);
		 neuronsList = jD.getNeuronsList();
		 maxClusters = neuronsList.size();
		 
		 
		 //jD.populateMethodsTags(methodsTags);
		 
		 
		 //Population of Feature Vector
		 for (List<String> tags: methodsTags){
			 featureVector.addAll(tags);
		 }
		 	
		 //Duplication removal from Feature Vector
		 featureVector = new ArrayList<String>(new LinkedHashSet<String>(featureVector));
		 //Sorting of Feature Vector
		 Collections.sort(featureVector);
		
		 // Methods in the form of 0 and 1 
		 methodsVector = mVec.populateMethodVector(methodsTags, featureVector);
		 
		 ClusterCreator clusters = new ClusterCreator(neuronsList, maxClusters, featureVector.size(), alpha, MIN_ALPHA, DECAY_RATE );
		
		
		 clusters.initializeNeurons(methodsVector);
		 clusters.trainNeurons(methodsVector);
		 clusters.clusterMethods(methodsVector);
		 finalNeurons = clusters.getNeurons();
		 
		 System.out.println("Feature Vector Size: " + featureVector.size() );
		 System.out.println("Total epochs: " + clusters.getIterations());
		 System.out.println("Final value of learning factor: " + clusters.getAlpha());
		 System.out.println("Final neurons");
		 clusters.displayNeurons();
		 System.out.println("\nFinal clusters:");
		 clusters.displayClusters();
		 
		 
		 //Temporary commenting ends here
		 
		 
		 /********* Silhouette Index Calculation**********/
		 //SilhouetteIndexCalculation SIC = new SilhouetteIndexCalculation(finalNeurons, clusters.getClusters(), methodsVector);
		 
		 
		 
		 /*** Domain Identification ******/
		 /******* Commented for now. will work on it after application of silhoutte****/
		 //identify.setMethodsTags(mTag.getOriginalMethodsTags());
		 //identify.setClusters(clusters.getClusters());
		 //identify.identifyDomains();
		 
		 /********Temporary display of method vector for a test***/
		 /*System.out.println("Method Vectors");
			for (int i=0; i<methodsVector.size(); i++)
				System.out.println("M-" + i + ": " +methodsVector.get(i));*/
		
	}
		public static void main (String args[]) {	 
			 SOMMain SOM = new SOMMain();
			 SOM.init();
			 SOM.calculate();			 
		}	
	}
