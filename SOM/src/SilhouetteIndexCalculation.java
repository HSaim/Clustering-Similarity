import java.util.ArrayList;
import java.util.List;


public class SilhouetteIndexCalculation {
	private float a, b;
	private List<List<Float>> finalNeurons = new ArrayList<List<Float>>();				//Neurons updated values after clustering
	private List<List<Integer>> finalClusters = new ArrayList<List<Integer>>();			//Clusters after application of SOM
	private List<List<Integer>> methodsVector = new ArrayList<List<Integer>>();			//Original Methods Vectors in 0/1 form
	
	public SilhouetteIndexCalculation(List<List<Float>> neurons, List<List<Integer>> clusters, List<List<Integer>> mVector){
		
		finalNeurons = neurons;
		finalClusters = clusters;
		methodsVector = mVector;
	}
	
	/**** Value a( i ) refers to the average distance between point i and the rest of the points in its cluster.*****/
	private void calc_a(){
		
	}

}
