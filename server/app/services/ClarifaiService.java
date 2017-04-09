package services;

import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.input.SearchClause;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.SearchHit;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.model.output_info.ConceptOutputInfo;
import clarifai2.dto.prediction.Concept;
import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import java.util.ArrayList;
import java.util.List;


public class ClarifaiService {
	public static String cid = "rCvdeyQM1h8dcVSR5f0VgSE-EiZmyJwCuWNPXAxo";
	public static String ckey = "A7bbZUK0cQIwWFq55R3Uql_TS_o85tOA0k4YXqQk";
	public static ClarifaiClient client;
	public static ConceptModel model;
	public static List<Concept> concepts = new ArrayList<Concept>();


	public static void addConcepts(ArrayList<ArrayList<String>> pairs){
		for(int i =0 ; i<pairs.size(); i++){
			Concept cnc = Concept.forID(pairs.get(i).get(0));
			if(!concepts.contains(cnc))
				concepts.add(cnc);
		}
		client.addConcepts().plus(concepts).executeSync();

	}
	public static void addImages(ArrayList<ArrayList<String>> pairs){
		for(int i =0; i < pairs.size();i++){
			for(int j = 1; j < pairs.get(i).size();j++)
				client.addInputs().plus(ClarifaiInput.forImage(ClarifaiImage.of(pairs.get(i).get(j))).withConcepts(Concept.forID(pairs.get(i).get(0))));
		}
	}
	public static void trainModel(){
		client.trainModel("insight").executeSync();
		
	}
	public static void predictModel(String url){

	client.predict("insight").withInputs(ClarifaiInput.forImage(ClarifaiImage.of(url))).executeSync();
	}
	public static void run(ArrayList<ArrayList<String>> args,String url){
		client = new ClarifaiBuilder(cid,ckey).buildSync();
		model = client.createModel("insight").withOutputInfo(ConceptOutputInfo.forConcepts(concepts)).executeSync().get();
		addConcepts(args);
		addImages(args);
		trainModel();
		predictModel(url);
		client.searchInputs(SearchClause.matchImageURL(ClarifaiImage.of(url))).getPage(1).executeSync();

	}
	public static void main(String[] args){
		System.out.println("Start run");
		ArrayList<String> arr = new ArrayList<String>();
		arr.add("One WTC");
		arr.add("https://cdn0.vox-cdn.com/uploads/chorus_asset/file/4802093/1wtc-wtcprogress.0.jpg");
		arr.add("https://timedotcom.files.wordpress.com/2014/03/world-trade-center.jpg");
		arr.add("https://cdn0.vox-cdn.com/uploads/chorus_image/image/50875865/160902_12-55-55_5DSR4035.0.0.jpg");
		arr.add("http://www.panynj.gov/wtcprogress/gallery/photos/oneWTC/wtc1_030216c.jpg");
		arr.add("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/OneWorldTradeCenter.jpg/240px-OneWorldTradeCenter.jpg");
		ArrayList<ArrayList<String>> arrs = new ArrayList<ArrayList<String>>();
		arrs.add(arr);
		run(arrs,"https://res.cloudinary.com/protenders/image/upload/orcsgd4ssmkp2kib1mxj.jpg");
		System.out.println("Done running");
	}
}
