package org.methodClustering.KMeans;

import org.methodClustering.KMeans.JaccardDifference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 
 * @author Habiba Saim
 * Implementation of KMeans Algorithm
 * Basic KMeans algorithm is implemented to cluster certain number of methods
 */

public class KMeansMain {
	
	private List<Cluster> clusters;							//Clusters
	int totalClusters;
	int centroidSize;										//No. of elements in the centroid of cluster = no. of elements in a method
	//List<List<String>> methodsTagsStemmed;				//Stemmed methods to be clustered 
	List<List<String>> methodsTags;							//Methods to be clustered
	List<List<Double>> methodsVector;						//Methods in the form of 0 and 1
	List<String> featureVector;								//Union of methods tags/features
	List<Integer> centroidList;								//Method numbers to be initial centroids
	MethodVectorCreation mVec;
	MethodTagsCreation mTag;
	//List<List<Float>> finalCentorids;
	JaccardDifference jD;
	//DomainIdentification identify;
	
	public KMeansMain(){		
		
		this.clusters = new ArrayList<Cluster>(); 
		//this.methodsTagsStemmed = new ArrayList<List<String>>();
		this.methodsTags = new ArrayList<List<String>>();
		this.methodsVector = new ArrayList<List<Double>>();
		this.featureVector = new ArrayList<String>();
		this.centroidList = new ArrayList<Integer>();
		this.mVec = new MethodVectorCreation();
		this.mTag = new MethodTagsCreation();
		// identify = new DomainIdentification();
		//this.finalCentorids = new ArrayList<List<Float>>();
	}
	
	 public static void main(String[] args) {
	    	
	    	KMeansMain kmeans = new KMeansMain();
	    	kmeans.init();
	    	kmeans.calculate();
	    }
	 
	//Initializes the process
    private void init() {
    	
    	mTag.populateMethodTagsList();
		//methodsTags = mTag.getStemmedMethodsTags();
		
		
		//Exact matching code
		 methodsTags = mTag.getLowerCaseMethodsTags();
		//Exact matching code - Ends
		 
		 //Semantic Matching code using WordNet
		 /*mTag.replaceSimilarTags();		 
		 methodsTags = mTag.getConvertedMethodsTags();
		 System.out.println("\n\tUpdated Methods Tags after removal of morphological words");
		 System.out.println("------------------------------------------------------------------------------");
		 mTag.displayMethods(methodsTags);*/
		//Semantic Matching code - Ends
		 
		jD = new JaccardDifference(methodsTags);
		centroidList = jD.getCentroidList();
		totalClusters = centroidList.size();
		
		//Population of Feature Vector
		 for (List<String> tags: methodsTags){
			 featureVector.addAll(tags);
		 }
		 	
		 //Duplication removal from Feature Vector
		 featureVector = new ArrayList<String>(new LinkedHashSet<String>(featureVector));
		//Sorting of Feature Vector
		 Collections.sort(featureVector);
		 
		 centroidSize =  featureVector.size();
		
		 // Methods in the form of 0 and 1 
		 methodsVector = mVec.populateMethodVector(methodsTags, featureVector);
    	
		 //Create initial clusters with random centroids values
		 /*for (int i=0; i<totalClusters; i++){
			 Cluster cluster = new Cluster(i);
			 List<Double> centroid = ClusterCreation.initializeCentorid(centroidSize);
			 cluster.setCentroid(centroid);
			 clusters.add(cluster);
		 }*/
		 
		//Create initial clusters having centroids on the basis of farthest methods
		 for (int i=0; i<totalClusters; i++){
			 Cluster cluster = new Cluster(i);
			 List<Double> centroid = methodsVector.get(centroidList.get(i));
			 cluster.setCentroid(centroid);
			 
			 clusters.add(cluster);
		 }
		 
		//Print Initial state
		 System.out.println("\nInitial clusters:");
		 plotClusters();
    }
    
   /* private void populateFeatureVector(){
    	
    }*/
    private void plotClusters() {
    	/*for (int i = 0; i < totalClusters; i++) {
    		Cluster c = clusters.get(i);
    		c.plotCluster();
    	}*/
    	for (Cluster c : clusters){
    		c.plotCluster();
    	}
    }
    
  //The process to calculate K Means, with iterating method.
    private void calculate() {
        boolean finish = false;
        int iteration = 0;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        while(!finish) {
        	//Clear cluster state
        	clearClusters();
        	
        	//Get current centroids
        	List<List<Double>> lastCentroids = getCentroids();
        	
        	//Assign points to the closer cluster
        	assignCluster();
            
            //Calculate new centroids.
        	calculateCentroids();
        	
        	iteration++;
        	
        	//Get new centroids after updation of clusters' components
        	List<List<Double>> updatedCentroids = getCentroids();
        	
        	//Calculates total distance between new and old Centroids
        	double distance = 0;
        	for (int i=0; i<updatedCentroids.size(); i++){
        		List<Double> centroid1 =  lastCentroids.get(i);
        		List<Double> centroid2 =  updatedCentroids.get(i);
        		distance += ClusterCreation.calculateDistance(centroid1, centroid2);
        	}
        	
        	System.out.println("#################");
        	System.out.println("Iteration: " + iteration);
        	System.out.println("Centroid distances: " + distance);
        	plotClusters();
        	        	
        	if(distance == 0) {
        		finish = true;
        	}
        }
    }
    
    //Clear methods and their IDs from cluster 
    private void clearClusters() {
    	for(Cluster c: clusters){
	    	c.clearCluster();
    	}
    }
    
    private List<List<Double>> getCentroids() {
    	
    	List<List<Double>> centroids = new ArrayList<List<Double>>(totalClusters);
    	
    	for(int i=0; i<clusters.size(); i++){
    		Cluster cluster= clusters.get(i);
    		List<Double> centroid = cluster.getCentroid();    		
    		centroids.add(centroid);
    	}
    	return centroids;
    }
    
    //Assign closest cluster to each method
    private void assignCluster() {
        double max = Double.MAX_VALUE;
        double min = max; 
        int clusterNumber = 0;                 
        double distance = 0.0; 
        
        for (int i=0; i<methodsVector.size(); i++){
	        List<Double> methodVector = methodsVector.get(i) ;
        	min = max;
            for(int j = 0; j<totalClusters; j++) {
            	Cluster c = clusters.get(j);
            	List<Double> centroid = c.getCentroid();
                distance = ClusterCreation.calculateDistance(methodVector, centroid);
                if(distance < min){
                    min = distance;
                    clusterNumber = j;
                }
            }
            //point.setCluster(clusterNumber);
            clusters.get(clusterNumber).addMethod(methodVector);
            clusters.get(clusterNumber).addMethodID(i);
        }
        
    }
    
    //Update clusters' centroids with respect to the methods that belong to it
    private void calculateCentroids() {
      
    	for (int i=0; i<clusters.size(); i++){
    		Cluster cluster = clusters.get(i);
    		List<List<Double>> methods = cluster.getMethods();
    		List<Double> centroid = cluster.getCentroid();
    		int size = centroid.size();
    		int n = methods.size();
    		
    		List<Double> average = new ArrayList<Double>(size);
    		
    		if(n>0){
	    		for (int j=0; j<size; j++){
	    			//List<Double> method = methods.get(j);
	    			double sum = 0.0;
	    			for (int k=0; k<n; k++){
	    				sum += methods.get(k).get(j);
	    			}
	    			average.add(sum/n);
	    		}
	    		cluster.setCentroid(average);
    		}
    		
    	}
    }
    
   
}
