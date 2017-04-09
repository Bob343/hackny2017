package services;
import java.io.File;
import java.util.*;
import clarifai2.api.ClarifaiResponse;
import clarifai2.api.request.input.SearchClause;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.SearchHit;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.ConceptModel;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.model.output_info.ConceptOutputInfo;
import clarifai2.dto.prediction.Concept;
import clarifai2.dto.prediction.Prediction;
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
	public static void addImages(ArrayList<ArrayList<String>> pairs)throws Exception{
		for(int i =0; i < pairs.size();i++){
			for(int j = 1; j < pairs.get(i).size();j++)
				client.addInputs().plus(ClarifaiInput.forImage(ClarifaiImage.of(pairs.get(i).get(j))).withConcepts(Concept.forID(pairs.get(i).get(0)))).executeSync();
				Thread.sleep(200);
		}
	}
	public static void trainModel()throws Exception{
		System.out.println(client.trainModel("insight").executeSync().get().toString());
		Thread.sleep(200);
		
	}
	public static void predictModel(File url)throws Exception{
		while(!client.getModelByID("insight").executeSync().get().modelVersion().status().toString().equals("TRAINED")){
			Thread.sleep(200);
		}
		
			List<Prediction> ls = client.predict("insight").withInputs(ClarifaiInput.forImage(ClarifaiImage.of(url))).executeSync().get().get(0).data();
			Thread.sleep(200);
			for(Prediction p:ls){
				Concept c = p.asConcept();
				int perc = (int)(100*c.value());
				String name = c.name();
				System.out.println(name+" - "+perc);
			}
	}
	public static void run(ArrayList<ArrayList<String>> args,File url)throws Exception{
		client = new ClarifaiBuilder(cid,ckey).buildSync();
		client.deleteModel("insight").executeSync();
		addConcepts(args);
		addImages(args);
		model = client.createModel("insight").withOutputInfo(ConceptOutputInfo.forConcepts(concepts)).executeSync().get();
		trainModel();
		
		predictModel(url);

	}
	public static void main(String[] args)throws Exception{
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
		arr = new ArrayList<String>();
		arr.add("Empire State Building");
		arr.add("http://cdn.history.com/sites/2/2016/04/GettyImages-480218741.jpg");
		arr.add("https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/Empire_State_Building_by_David_Shankbone_crop.jpg/250px-Empire_State_Building_by_David_Shankbone_crop.jpg");
		arr.add("http://server.empirestaterealtytrust.com/images/properties/empire-state-building2.jpg");
		arr.add("https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcQV7N2A87AUcoYn8-G6nWwstVW0JpEMjCtyt4YfM0R9v3jUQzdfAw");
		arr.add("http://www.unmuseum.org/7wonders/empirestatebld1.jpg");
		arrs.add(arr);
		run(arrs,"https://res.cloudinary.com/protenders/image/upload/orcsgd4ssmkp2kib1mxj.jpg");
		predictModel("https://www.askideas.com/media/39/Awesome-Empire-State-Building-Picture.jpg");
		System.out.println("Done running");
	}
}
