/******** This class converts string method vector into binary method vector according to the feature vector***********/
import java.util.ArrayList;
import java.util.List;


public class MethodVectorCreation {
	
  public List<List<Double>> populateMethodVector(List<List<String>> mTags, List<String> fVector){		
	  List<List<Double>> mVector = new ArrayList<List<Double>>();
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
