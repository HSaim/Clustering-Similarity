package org.methodClustering.KMeans;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Habiba Saim
 * Conversion of string method vector into binary method vector with respect to the feature vector
 * If a feature exists then place 1 otherwise 0 in the method vector
 */

public class MethodVectorCreation {
	private List<List<Double>> mVector;
	
  public List<List<Double>> populateMethodVector(List<List<String>> mTags, List<String> fVector){		
	  this.mVector = new ArrayList<List<Double>>();
	  for (int i=0; i<mTags.size(); i++){
			List<String> tags = mTags.get(i);
			//System.out.println("mTags-"+ i + ": " + tags);
			List<Double> features = new ArrayList<Double>();
			for (int j=0; j<fVector.size(); j++){
				String tag = fVector.get(j);
				int flag =0;
				for (int k=0; k<tags.size(); k++){				
					if (tag.equalsIgnoreCase(tags.get(k))){
						features.add(1.0);
						flag =1;
						break;
					}
				}
				if (flag == 0)
					features.add(0.0);
			}
			mVector.add(features);	
		}
			
		for (int i=0; i<mVector.size(); i++){
			System.out.println(i + ": "+mVector.get(i));
		}
		
		return mVector;
	}

}
