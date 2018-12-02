package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;


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
						vehicles[j] = new DeliveryVehicle(license, speed);}
				}
			}
			ResourcesHolder resourcesHolder = ResourcesHolder.getInstance();
			resourcesHolder.load(vehicles);
		}
		JSONObject services = (JSONObject) jsonObject.get("services");
		JSONObject timeService = (JSONObject) services.get("time");
		int speed = (int)timeService.get("speed");
		int duration = (int)timeService.get("duration");
		TimeService tService = new TimeService("timeService",speed, duration);
		int numOfSelling = (int) services.get("selling");
		SellingService[] sellingArr = new SellingService[numOfSelling];
		for(int i = 0 ; i<numOfSelling; i++) {
			sellingArr[i] = new SellingService("sellingService"+i);
		}
		int numOfInventory = (int) services.get("inventoryService");
		InventoryService[] inventoryArr = new InventoryService[numOfInventory];
		for(int i = 0; i<numOfInventory; i++){
				inventoryArr[i] = new InventoryService("inventoryService"+i);
		}
		int numOfLogistic= (int) services.get("logistics");
		LogisticsService[] logisticArr = new LogisticsService[numOfLogistic];
		for(int i = 0 ; i<numOfLogistic; i++) {
			logisticArr[i] = new LogisticsService();
		}
		int numOfResources= (int) services.get("resourcesService");
		ResourceService[] resourcesArr = new ResourceService[numOfResources];
		for(int i = 0 ; i<numOfResources; i++) {
			resourcesArr[i] = new ResourceService("resourceService"+i);
		}
		JSONArray customersArray = (JSONArray) jsonObject.get("customers");
		Customer[] customersArr = new Customer[customersArray.size()];
		for(int i = 0 ; i<customersArray.size(); i++) {
			JSONObject customerAtIndex = (JSONObject) customersArray.get(i);
			int id = (int)customerAtIndex.get("id");
			String name = (String)customerAtIndex.get("name");
			String address = (String)customerAtIndex.get("address");
			int distance = (int)customerAtIndex.get("distance");
			JSONObject creditCard =(JSONObject) customerAtIndex.get("creditCard");
			int creditCardNum = (int) creditCard.get("number");
			int amount = (int)creditCard.get("amount");
			JSONArray orderScheduleArr =(JSONArray) customerAtIndex.get("orderSchedule");
			HashMap<Integer,String> orderSchedule = new HashMap<>();
			for(int j = 0 ; j<orderScheduleArr.size(); j++) {
				JSONObject book = (JSONObject) customersArray.get(i);
				String bookTitle = (String) book.get("bookTitle");
				Integer tick = (Integer) book.get("tick");
				orderSchedule.put(tick,bookTitle);
			}
			customersArr[i] = new Customer(id, name, address, distance, creditCardNum,amount, orderSchedule);
		}
	}
}
