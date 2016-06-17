package org.methodClustering.KMeans;

import java.util.ArrayList;
import java.util.List;

/*
 * @author Habiba Saim
 * Cluster and its components
 * Input and output clusters components through this class
 */

public class Cluster {
	
	public List<List<Double>> methods;		//Methods inside a cluster
	public List<Integer> methodsIDs;		//IDs of methods
	public List<Double> centroid;			//Centroid of cluster
	public int id;							//Cluster's ID
	
	//Create a new cluster
	public Cluster(int id){
		this.id = id;
		this.methods = new ArrayList<List<Double>>();
		this.centroid = new ArrayList<Double>();
		this.methodsIDs = new ArrayList<Integer>();
	}
	
	public List<List<Double>> getMethods(){
		return methods;
	}
	
	public void addMethod(List<Double> method){
		methods.add(method);
	}
	
	public void addMethodID(int id){
		methodsIDs.add(id);
	}
	
	public List<Double> getCentroid(){
		return centroid;
	}
	
	public void setCentroid(List<Double> centroid){
		this.centroid = centroid;
	}
	
	public int getId(){
		return id;
	}

	public void clearCluster(){
		methods.clear();
		methodsIDs.clear();
	}
	
	public void plotCluster(){
		System.out.println("Cluster: " + id);
		//System.out.println("Centroid: " + centroid);
		System.out.println("Methods:");
		/*for (List<Double>method: methods){
			System.out.println(method);
		}*/
		/*for(int i=0; i<methods.size(); i++){
			System.out.println(methodsIDs.get(i)+ ": " + methods.get(i));
		}*/
		for(int i=0; i<methods.size(); i++){
			System.out.print(methodsIDs.get(i)+(i < methods.size()-1 ? ", " : ""));			
		}
		System.out.println("");
	}
}
