import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Habiba Saim
 * Jaccard Similarity is applied here 
 * Finds distances between methods and creates a difference matrix
 * On the basis of maximum distant methods initial neurons are decided
 * If yet some method isn't added, it is considered individually 
 * But among the most distant methods that are closer to some other in the list are ignored to be neuron/centroid
 **/


public class JaccardDifference {
	
	List<List<String>> methodsTags = new ArrayList<List<String>>();
	int totalMethods; 
	private double dist[][];  										//Square Matrix of Distance of each method with the rest 
	private int[] minDist;	 										//indices of closest cluster
	private int[] maxDist; 											// indices of farthest cluster
	private double[] maxDistVal;										//Values of indices mentioned in maxDist
	private List<Integer> neuronsList= new ArrayList<Integer>();

	public JaccardDifference(List<List<String>> methodsTags){
		
		this.methodsTags = methodsTags;
		this.totalMethods = this.methodsTags.size();
		this.dist = new double[totalMethods][totalMethods];
		this.maxDist = new int[totalMethods];
		this.minDist = new int[totalMethods];
		this.maxDistVal = new double[totalMethods];
		createDistanceMatrix(this.methodsTags);
	}
	
	//Create a square matrix of pair-wise distances between methods
	private  void createDistanceMatrix(List<List<String>> list){
		
		for (int i=0; i<dist.length; i++){
			for (int j=0; j<dist[i].length; j++){
				if (i==j) dist[i][j]=2.0;
					//dist[i][j] = Double.POSITIVE_INFINITY;
				else {
					dist[i][j] = calculateJaccardDifference(list.get(i), list.get(j));
				}
				if (dist[i][j]<dist[i][minDist[i]] && dist[i][j]!=2)
					minDist[i] = j;
				
				//Maintain  index of farthest cluster				
				if (dist[i][j]>dist[i][maxDist[i]] && i!=j){
					maxDist[i] = j;
					//maxDistVal[i] = d[i][j];
				}
				else if (i==0 && j==0){
					maxDist[i] = 1; 
					//maxDistVal[i] = d[i][j];
				}
			}
		}
		
		//neurons = new ArrayList<Integer>(Arrays.asList(maxD));		
		
		System.out.println("\n****** Distance Matrix **********");
		for (int i=0; i<dist.length; i++){
			System.out.print("Node-" + i + ": ");
			for(int j=0; j<dist[i].length; j++){
			//for (int j=0; j<i; j++){
				System.out.print(Math.round(dist[i][j]*100.0)/100.0 + "	");
			}
			System.out.println();
		}
		initializeNeurons();
		System.out.println();
		for(int i=0; i<maxDist.length; i++){
			System.out.println("max distant node of  node " +i+": "+ maxDist[i] + " value: " + maxDistVal[i]);
		}
		
		System.out.println();
		for(int i=0; i<minDist.length; i++){
			System.out.println("min distant node of  node " +i+": "+ minDist[i]);
		}
		
		
	}
	
	
	    
	private static double calculateJaccardDifference(List<String> list1, List<String> list2){
		double jaccardDifference = 1- calculateJaccardSimilarity(list1, list2);
		return jaccardDifference;
		
	}

	private static double calculateJaccardSimilarity(List<String> list1, List<String> list2){
		 List<String> unions = new ArrayList<String>();
		 List<String> intersections = new ArrayList<String>();
		 double similarity;
		unions = findUnion(list1, list2);
		/* System.out.print("Union: ");
		 for (int i=0; i<unions.size(); i++){
			 System.out.print(unions.get(i) + '\t');
		 }
		 System.out.println();*/
			 
		// Intersection of 1st two methods	 
		 intersections = findIntersection(list1, list2);
		 /*System.out.print("Intersection: ");
		 for (int i=0; i<intersections.size(); i++){
			 System.out.print(intersections.get(i) + '\t');
		 }
		 System.out.println();*/
		 similarity = jaccardSimilarity(intersections, unions);
		 //System.out.println("Similarity: " + similarity);
		 return similarity;
		 
		
	}
	
	private static <T> List<T> findUnion(List<T> list1, List<T> list2) {
	        Set<T> set = new HashSet<T>();

	        set.addAll(list1);
	        set.addAll(list2);

	        return new ArrayList<T>(set);
	    }

    private static <T> List<T> findIntersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
    
    private static  <T> double jaccardSimilarity(List<T> list1, List<T> list2){
    	float intersectionCount = list1.size();
    	float unionCount = list2.size();
    	return intersectionCount/unionCount;
    }
    
    private void initializeNeurons(){		

		//In 'neurons' arraylist place maximum distance nodes 
		for (int i=0; i<maxDist.length; i++){
			maxDistVal[i] = dist[i][maxDist[i]];
			if (dist[i][maxDist[i]]>=1.0){
				neuronsList.add(maxDist[i]);
			}
		}
		
		addMissingNeurons();
		
		//If all methods are related then they will group in one cluster
		if (neuronsList.size() == 0){
			neuronsList.add(0);			
		}
		
		//System.out.println("\nNeurons list with duplication: " + neuronsList);
		
		//Duplication removal from initial neurons list
		neuronsList = new ArrayList<Integer>(new LinkedHashSet<Integer>(neuronsList));
		
		//Sorting of neurons
		Collections.sort(neuronsList);
		System.out.println("\nNeurons list after duplication removal: " + neuronsList + "\n");
		
		//Select one of all closer neurons
		removeCloseNeuronNumbers();
		System.out.println("Neurons list after deletion/merge of very close methods into a single cluster: " + neuronsList + "\n");
		
	}
    
  //Add methods into neurons list which are farthest but not considered into distance calculation
    private void addMissingNeurons(){
    	for (int i=0; i<totalMethods; i++){
			int count=0;
			for (int j=0; j<totalMethods; j++){
				if (dist[i][j]!=0.0 && dist[i][j]!=1.0 && dist[i][j]!=2.0){
					count++;
				}
			}
			if(count == 0){
				neuronsList.add(i);
			}			
		}  	
    }
	
	//If any of the neurons are closer to each other then select one of them as initial neuron
	 private void removeCloseNeuronNumbers(){
	    	
    	List<Integer> removelList = new ArrayList<Integer>();
    	
    	for (int i=0; i<neuronsList.size(); i++){
    		for (int j=i+1; j<neuronsList.size(); j++){
    			int x=neuronsList.get(j);
    			if (!removelList.contains(x)){
	    			if (dist[neuronsList.get(i)][neuronsList.get(j)]<0.9){
	    				removelList.add(neuronsList.get(j));    				
	    			}
    			}
    		}
    	}
    	
    	//System.out.println("close methods to be removed from initial neurons list: " + removelList); 
    	//Duplication removal from removalList
    	removelList = new ArrayList<Integer>(new LinkedHashSet<Integer>(removelList));
    	System.out.println("close methods to be removed from initial neurons list : " + removelList); 
    	for (int i=0; i<removelList.size(); i++){
    		neuronsList.remove(removelList.get(i));
    		//System.out.println("updated neurons after removal iteration no: " + i + "= " +  neuronsList);
    	}
    	System.out.println("updated neurons after removal:" + neuronsList);
	}
 
   
    public List<Integer> getNeuronsList(){
		return neuronsList;
	}
	
}
