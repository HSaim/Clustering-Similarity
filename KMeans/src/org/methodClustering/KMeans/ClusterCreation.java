package org.methodClustering.KMeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Habiba Saim
 * 	Initializes centorids with random values
 * 	Calculates Euclidean Distance between a method and the centroid of the cluster this method belongs to
 */
public class ClusterCreation {
	
	public ClusterCreation(){
		
	}
	
	//Random Initial Centroids
	protected static List<Double> initializeCentorid(int size){
		
		List<Double> centroid = new ArrayList<Double>();
		Random r = new Random();
		for (int i=0; i<size; i++){
			centroid.add((double)(Math.floor(r.nextDouble()*100.0)/100.0));
		}
		
		return centroid;		
	}
	
	//Initial Centroids: Farthest methods	
	/*protected static List<Double> initializeCentorid(List<Double> methodVector){
		
		List<Double> centroid = methodVector;
		for (int i=0; i<methodVector.size(); i++){
			centroid.add(methodVector.get(i));
		}
		
		return centroid;		
	}*/

	//Euclidean Distance between two array lists 
	protected static double calculateDistance(List<Double> list1, List<Double> list2) {
    	
    	double distance = 0.0;
    	for (int i=0; i<list1.size(); i++){
    		distance+= Math.pow(list1.get(i)-list2.get(i), 2);
    	}
    	return distance;
    }


}
