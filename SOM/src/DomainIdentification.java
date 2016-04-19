
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import cc.mallet.TopicExtraction.TopicModel;


public class DomainIdentification {
	private List<List<String>> methodsTags = new ArrayList<List<String>>();
	private List<List<Integer>> clustersMethodsList = new ArrayList<List<Integer>>();
	private List<List<String>> clustersMethodsTags = new ArrayList<List<String>>();
	private TopicModel topic = new TopicModel();
	
	public void identifyDomains(){
		for (int i=0; i<clustersMethodsList.size(); i++){
			List<Integer> cluster = clustersMethodsList.get(i);
			List<String> tags = new ArrayList<String>();
			for (int j=0; j<cluster.size(); j++){
				tags.addAll(methodsTags.get(cluster.get(j)));								
			}
			System.out.println("Tags: " + tags);
			clustersMethodsTags.add(tags);
			try{
				FileWriter writer = new FileWriter(TopicModel.Compile_Code_Directory); 
				for(String str: tags) {
				  writer.write(str + " ");
				}
				writer.close();
			}catch(Exception ex) {
			    ex.printStackTrace();
			}
			try{
			TopicModel.identify();
			}catch(Exception ex) {
			    ex.printStackTrace();
			}
			/*try {
			    FileOutputStream fos = new FileOutputStream(TopicModel.Compile_Code_Directory);//"Outfile\\output.txt");
			    ObjectOutputStream oos = new ObjectOutputStream(fos);   
			    oos.writeObject(tags); // write tags to ObjectOutputStream
			    oos.close(); 
			} catch(Exception ex) {
			    ex.printStackTrace();
			}*/
		}
	}
	
	public void setMethodsTags(List<List<String>> methodsTagsOriginal){
		methodsTags = methodsTagsOriginal;		
	}
	
	public void setClusters(List<List<Integer>> clustersArray){
		clustersMethodsList = clustersArray;		
	}
	
	private void populateInputFile(){
		
	}

}
