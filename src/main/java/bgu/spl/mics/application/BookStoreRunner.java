package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
import bgu.spl.mics.application.passiveObjects.Inventory;
import com.google.gson.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/** This is the Main class of the application. You should parse the input file, 
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
	public static void main(String[] args) {

		File jsonFile = Paths.get("path").toFile();
		JSONObject jsonObject = null;
		JSONParser parser = new JSONParser();
		try {
			jsonObject = (JSONObject) parser.parse (new FileReader(jsonFile));
		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONArray booksArray = (JSONArray) jsonObject.get("initialInventory");
		if (booksArray != null) {
			BookInventoryInfo[] books = new BookInventoryInfo[booksArray.size()];
			for (int i = 0; i < booksArray.size(); i++) {
				JSONObject objAtIndex = (JSONObject) booksArray.get(i);
				if (objAtIndex != null) {
					String title = objAtIndex.get("bookTitle").toString();
					int amount = (int) objAtIndex.get("amount");
					int price = (int) objAtIndex.get("price");
					books[i] = new BookInventoryInfo(title, amount, price);
				}
			}
			Inventory inventory = Inventory.getInstance();
			inventory.load(books);
		}
		JSONArray resourcesArray = (JSONArray) jsonObject.get("initialResources");
		if (resourcesArray != null) {
			DeliveryVehicle[] vehicles = new DeliveryVehicle[resourcesArray.size()];
			for (int i = 0; i < resourcesArray.size(); i++) {
				JSONArray arrAtIndex = (JSONArray) resourcesArray.get(i);
				for (int j = 0; j < arrAtIndex.size(); j++) {
					JSONObject objAtIndex = (JSONObject) booksArray.get(j);
					if (objAtIndex != null) {
						int license = (int) objAtIndex.get("license");
						int speed = (int) objAtIndex.get("speed");
						vehicles[j] = new DeliveryVehicle(license, speed);
					}
				}

			}

		}
		JSONArray servicesArray = (JSONArray) jsonObject.get("services");

	}
}
