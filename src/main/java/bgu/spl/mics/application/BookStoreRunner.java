package bgu.spl.mics.application;
import com.google.gson.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {

    	Gson gson = new Gson();
    	File jsonFile = Paths.get("path").toFile();
    	JsonObject jsonObject = null;
		try {
			jsonObject = gson.fromJson(new FileReader(jsonFile), JsonObject.class);
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		}
    
    	   JsonObject booksArray = (JsonObject) jsonObject.get("initialInventory");
    	  for(JsonObject book: booksArray) {
        	  String title = book.get("bookTitle").getAsString();
        	  int amount = book.get("amount").getAsInt();
        	  int price = book.get("price").getAsInt();


    		  
    	  }
    	  JsonObject address = jsonObject.getAsJsonObject("address");
    	  JsonArray vehiclesArray = jsonObject.getAsJsonArray("initialResources");

    	  String firstLine = address.get("firstLine").getAsString();
    	  String secondLine = address.get("secondLine").getAsString();
    	  String postCode = address.get("postCode").getAsString();
    	 
    	  System.out.println("title = " + title);
    	  System.out.println("firstName = " + firstName);
    	  System.out.println("age = " + age);
    	 
    	  for (JsonElement book : booksArray) {
    	      System.out.println("book = " + book.getAsString());
    	  }
    	 
    	 
    	
    }
}
