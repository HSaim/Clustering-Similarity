import java.util.ArrayList;
import java.util.List;
//import java.util.Random;
import java.util.Random;


public class ClusterCreator {
	
	private int tVectorLength;							//Total Number of elements in one cluster
	private int maxClusters;							//Total number of clusters
	private double minAlpha;							//Min. value of learning factor for stopping further learning
	private double alpha;								//Learning factor
	private double decayRate;							//Value to decrease value of Learning factor after each iteration
	private int iterations;								//Total number of iterations done for learning
	private int clusterSize;
	
	private double dist[];								//Distance of neurons from methods
	
	
	private List<Integer>neuronsList;// = new ArrayList<Integer>();      				//Method Numbers for initial neurons
	private List<List<Float>> neurons;// = new ArrayList<List<Float>>();				//Neurons - to be compared with methods
	private List<List<Integer>> clusters;// = new ArrayList<List<Integer>>();			//Final clusters - having method numbers
	
	public ClusterCreator(List<Integer> neuronsNumbers, int max_Clusters, int cluster_Size, double alpha_Start, double min_Alpha, double decay_Rate){
		this.neuronsList = neuronsNumbers;
		this.maxClusters =  max_Clusters;
		this.clusterSize = cluster_Size;
		this.tVectorLength = cluster_Size;
		this.alpha = alpha_Start;
		this.minAlpha = min_Alpha;
		this.decayRate = decay_Rate;
		//trainingVectorSize = methodVector_Size;
		this.dist = new double[maxClusters];
		this.iterations = 0;
		this.clusters = new ArrayList<List<Integer>>(maxClusters); 
		this.neurons = new ArrayList<List<Float>>();
	}
	
	public void initializeNeurons(List<List<Double>> methodVector){
		
		//Initial neurons on the basis of farthest methods having dissimilarity 1.0
		for (int i=0; i<neuronsList.size(); i++){
			List<Float> neuron = new ArrayList<Float>();;
			for (Double n: methodVector.get(neuronsList.get(i))){
				neuron.add(n.floatValue());
			}
			neurons.add(neuron);
		}
		
		//default neurons using random values
		/*Random r = new Random();
		for(int i = 0; i <  maxClusters; i++){
	    	List<Float> neuron = new ArrayList<Float>();
	    	for (int k=0; k<clusterSize; k++)
	    		neuron.add((float)(Math.floor(r.nextFloat()*100.0)/100.0));
	    	neurons.add(neuron);
	    }*/
		System.out.println("\nInitial neurons");
		displayNeurons();
	}
	
	public void trainNeurons(List<List<Double>> trainingVector)
	{
		int vecNum;
		int minDistanceIndex;

	    while(alpha > minAlpha)
	    {
	        iterations ++;

	        for(vecNum = 0; vecNum < trainingVector.size(); vecNum++)
	        {
	        	List<Double> trainingData = trainingVector.get(vecNum);
	        	
	        	// Compute distance
	            computeDistance(vecNum, trainingData);
	            
	            //Find the smallest distant neuron
	            minDistanceIndex = computeSmallestDistanceIndex();
	            System.out.println("Method #: " + vecNum + " Closest neuron is: " + minDistanceIndex );
	            List<Float> neuron = neurons.get(minDistanceIndex);
	            
	            
	            //Update the weights on the winning neuron.
	            for(int i = 0; i < trainingData.size(); i++){
	            	float x ;//= neuron.get(i);
	                x= (float)Math.floor((neuron.get(i) + alpha * (trainingData.get(i)-neuron.get(i)))*100)/100;
	                neuron.set(i,x);	                
	            }
	            neurons.set(minDistanceIndex, neuron);
	            /*System.out.println("Upated neurons");
	            for (int i=0; i<neurons.size(); i++){
	            	System.out.println("neuron-" + i + ": " + neurons.get(i));
	            }*/
	        }

	        // Reduce the learning rate.
	        alpha = decayRate * alpha;

	    }
	    return;
	}
	
	public void clusterMethods(List<List<Double>> methodVector){
		
			int minDistanceIndex;
			List<Integer> nearestNeuronList = new ArrayList<Integer>(); //List of neuron numbers which is nearest to the method
			
						
			// Print clusters created.
			//System.out.println("Clusters for training input:");
			for(int vecNum = 0; vecNum < methodVector.size(); vecNum++)
	        {
	        	List<Double> method = methodVector.get(vecNum);
	        	//List<Integer> methodNumber = new ArrayList<Integer>();
	        	//Find nearest neuron
	            computeDistance(vecNum, method);
	            
	            //Find the index of the nearest neuron
	            minDistanceIndex = computeSmallestDistanceIndex();
	            System.out.println("Method # "+ vecNum + " clusters with neuron # "+ minDistanceIndex);
	            
	            //Place the found index number in the arraylist
	            nearestNeuronList.add(minDistanceIndex);
	            
	           // methodNumber.add(vecNum);
	           
	        }
			System.out.println("\nNearest neurons list: " + nearestNeuronList);
			for (int i=0; i<maxClusters; i++){
				List<Integer> methodsIndices = new ArrayList<Integer>();
				for (int j=0;j<nearestNeuronList.size(); j++){
					if (i==nearestNeuronList.get(j)){
						methodsIndices.add(j);
					}
				}
				clusters.add(i, methodsIndices);
				System.out.println("Cluster # " + i + " has methods: " + clusters.get(i));
			}
		    
		    return;		
	}
	
	private void computeDistance(int trainingVectorNumber, List<Double> trainingData)
	{
	
		//Initialize all distances to zero
		int j;
	    for(int i=0; i<dist.length; i++){
	    	dist[i] = 0.0;
	    }
	    
	    for(int i = 0; i < maxClusters; i++){	    
	    	List<Float> neuron = neurons.get(i);
        	
        	/*System.out.println("\n\nneuron:        " + neuron);
        	System.out.println("training data: " + trainingData);*/
        	
        	//Add up distances of a neuron from a training/method vector
	        for( j = 0; j < tVectorLength; j++){	        	
	        	//dist[i] += Math.floor(Math.pow((neuron.get(j) - trainingData.get(j)), 2)*100)/100;	 
	        	dist[i] += Math.pow((neuron.get(j) - trainingData.get(j)), 2);	 
	        } // j
	        //dist[i]= Math.sqrt(dist[i]);
	    } // i
	    
	   /* for(int i=0; i<dist.length; i++){
	    	System.out.print("D=" + i + ": "+  dist[i] + "\t");
	    }
    	System.out.println();*/
	    return;
	}
	
	//Returns index number of nearest neuron
	private int computeSmallestDistanceIndex(){
		int  min = 0; //index number of nearest neuron
		for (int i=1; i<dist.length; i++ ){
			if (dist[i]<dist[min]) min = i;
		}
		return min;
	}
	
	//Returns number of iterations/epochs
	public int getIterations()
	{
		return iterations;
	}

	
	//Returns value of learning factor
		public double getAlpha()
		{
			return alpha;
		}
	//Displays resultant clusters
	public void displayClusters(){
		for (int i=0; i<clusters.size(); i++){
			System.out.println("Cluster# " + i + ": " + clusters.get(i));
		}
			
		
	}
	public List<List<Integer>> getClusters(){
		return clusters;
	}
	
	public List<List<Float>> getNeurons(){
		return neurons;
	}
	
	public void displayNeurons(){
		for (int i=0; i<neurons.size();i++){
	    	List<Float> neuron = neurons.get(i);
	    	System.out.println("neuron-"+ i + ": " + neuron);    	
		}		
	}
	
}
