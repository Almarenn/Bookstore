package bgu.spl.mics.application;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
	public static void main(String[] args) {
		Gson gson= new Gson();
		jason j=null;
		try{
			JsonReader read= new JsonReader(new FileReader(args[0]));
			j= gson.fromJson(read,jason.class);
		}
		catch(FileNotFoundException e){}
		Inventory inventory = Inventory.getInstance();
		inventory.load(j.initialInventory);
		ResourcesHolder resourcesHolder = ResourcesHolder.getInstance();
		resourcesHolder.load(j.initialResources[0].vehicles);
		List<Thread> allThreads = new LinkedList<>();
		Thread timeThread = new Thread(j.services.time);
		allThreads.add(timeThread);
		//create Selling Service
		int numOfSelling = j.services.selling;
		SellingService[] sellingArr = new SellingService[numOfSelling];
		for(int i = 0 ; i<numOfSelling; i++) {
			sellingArr[i] = new SellingService("sellingService" + i);
			Thread t = new Thread(sellingArr[i]);
			allThreads.add(t);
			t.start();
		}
		//create Inventory Service
		int numOfInventory = j.services.inventoryService;
		InventoryService[] inventoryArr = new InventoryService[numOfInventory];
		for(int i = 0; i<numOfInventory; i++){
			inventoryArr[i] = new InventoryService("inventoryService"+i);
			Thread t = new Thread(inventoryArr[i]);
			allThreads.add(t);
			t.start();
		}
		//create Logistic Service
		int numOfLogistic= j.services.logistics;
		LogisticsService[] logisticArr = new LogisticsService[numOfLogistic];
		for(int i = 0 ; i<numOfLogistic; i++) {
			logisticArr[i] = new LogisticsService("logistocService" + i);
			Thread t = new Thread(logisticArr[i]);
			allThreads.add(t);
			t.start();
		}
		//create Resources Service
		int numOfResources= j.services.resourcesService;
		ResourceService[] resourcesArr = new ResourceService[numOfResources];
		for(int i = 0 ; i<numOfResources; i++) {
			resourcesArr[i] = new ResourceService("resourceService"+i);
			Thread t = new Thread(resourcesArr[i]);
			allThreads.add(t);
			t.start();
		}

		//create API Services
		APIService[] APIArr = new APIService[j.services.customers.length];
		for(int i=0; i<APIArr.length; i++){
			APIArr[i] = new APIService("APIService"+i,j.services.customers[i] );
			Thread t = new Thread(APIArr[i]);
			allThreads.add(t);
			t.start();
		}




//		File jsonFile = Paths.get(args[0]).toFile();
//		JSONObject jsonObject = null;
//		JSONParser parser = new JSONParser();
//		try {
//			jsonObject = (JSONObject) parser.parse (new FileReader(jsonFile));
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		//load the books inventory
//		JSONArray booksArray = (JSONArray) jsonObject.get("initialInventory");
//		Inventory inventory = Inventory.getInstance();
//		if (booksArray != null) {
//			BookInventoryInfo[] books = new BookInventoryInfo[booksArray.size()];
//			for (int i = 0; i < booksArray.size(); i++) {
//				JSONObject objAtIndex = (JSONObject) booksArray.get(i);
//				if (objAtIndex != null) {
//					String title = objAtIndex.get("bookTitle").toString();
//					int amount = (int) objAtIndex.get("amount");
//					int price = (int) objAtIndex.get("price");
//					books[i] = new BookInventoryInfo(title, amount, price);
//				}
//			}
//			inventory.load(books);
//		}
//		//load the resources holder
//		JSONArray resourcesArray = (JSONArray) jsonObject.get("initialResources");
//		ResourcesHolder resourcesHolder = ResourcesHolder.getInstance();
//		if (resourcesArray != null) {
//			DeliveryVehicle[] vehicles = new DeliveryVehicle[resourcesArray.size()];
//			for (int i = 0; i < resourcesArray.size(); i++) {
//				JSONArray arrAtIndex = (JSONArray) resourcesArray.get(i);
//				for (int j = 0; j < arrAtIndex.size(); j++) {
//					JSONObject objAtIndex = (JSONObject) booksArray.get(j);
//					if (objAtIndex != null) {
//						int license = (int) objAtIndex.get("license");
//						int speed = (int) objAtIndex.get("speed");
//						vehicles[j] = new DeliveryVehicle(license, speed);}
//				}
//			}
//			resourcesHolder.load(vehicles);
//		}
//		//create micro services
//		JSONObject services = (JSONObject) jsonObject.get("services");
//		List<Thread> allThreads = new LinkedList<>();
//		//create the time service
//		JSONObject timeService = (JSONObject) services.get("time");
//		int speed = (int)timeService.get("speed");
//		int duration = (int)timeService.get("duration");
//		TimeService tService = new TimeService("timeService",speed, duration);
//		Thread timeThread = new Thread(tService);
//		allThreads.add(timeThread);
//		//create selling services
//		int numOfSelling = (int) services.get("selling");
//		SellingService[] sellingArr = new SellingService[numOfSelling];
//		for(int i = 0 ; i<numOfSelling; i++) {
//			sellingArr[i] = new SellingService("sellingService"+i);
//			Thread t = new Thread(sellingArr[i]);
//			allThreads.add(t);
//			t.start();
//		}
//		//create inventory services
//		int numOfInventory = (int) services.get("inventoryService");
//		InventoryService[] inventoryArr = new InventoryService[numOfInventory];
//		for(int i = 0; i<numOfInventory; i++){
//			inventoryArr[i] = new InventoryService("inventoryService"+i);
//			Thread t = new Thread(inventoryArr[i]);
//			allThreads.add(t);
//			t.start();
//		}
//		//create logistic services
//		int numOfLogistic= (int) services.get("logistics");
//		LogisticsService[] logisticArr = new LogisticsService[numOfLogistic];
//		for(int i = 0 ; i<numOfLogistic; i++) {
//			logisticArr[i] = new LogisticsService("logistocService"+i);
//			Thread t = new Thread(logisticArr[i]);
//			allThreads.add(t);
//			t.start();
//		}
//		//create resource services
//		int numOfResources= (int) services.get("resourcesService");
//		ResourceService[] resourcesArr = new ResourceService[numOfResources];
//		for(int i = 0 ; i<numOfResources; i++) {
//			resourcesArr[i] = new ResourceService("resourceService"+i);
//			Thread t = new Thread(resourcesArr[i]);
//			allThreads.add(t);
//			t.start();
//		}
//		//create customers
//		JSONArray customersArray = (JSONArray) jsonObject.get("customers");
//		Customer[] customersArr = new Customer[customersArray.size()];
//		for(int i = 0 ; i<customersArray.size(); i++) {
//			JSONObject customerAtIndex = (JSONObject) customersArray.get(i);
//			int id = (int) customerAtIndex.get("id");
//			String name = (String) customerAtIndex.get("name");
//			String address = (String) customerAtIndex.get("address");
//			int distance = (int) customerAtIndex.get("distance");
//			JSONObject creditCard = (JSONObject) customerAtIndex.get("creditCard");
//			int creditCardNum = (int) creditCard.get("number");
//			int amount = (int) creditCard.get("amount");
//			JSONArray orderScheduleArr = (JSONArray) customerAtIndex.get("orderSchedule");
//			HashMap<Integer, List<String>> orderSchedule = new HashMap<>();
//			for (int j = 0; j < orderScheduleArr.size(); j++) {
//				JSONObject book = (JSONObject) customersArray.get(i);
//				String bookTitle = (String) book.get("bookTitle");
//				Integer tick = (Integer) book.get("tick");
//				List<String> l;
//				if(!orderSchedule.containsKey(tick)){
//					l = new LinkedList<>();
//					orderSchedule.put(tick, l);
//				}
//				else {
//					l = orderSchedule.get(tick);
//				}
//				l.add(bookTitle);
//			}
//			customersArr[i] = new Customer(id, name, address, distance, creditCardNum, amount, orderSchedule);
//		}
//		//create API services
//		APIService[] APIArr = new APIService[customersArr.length];
//		for(int i=0; i<APIArr.length; i++){
//			APIArr[i] = new APIService("APIService"+i,customersArr[i] );
//			Thread t = new Thread(APIArr[i]);
//			allThreads.add(t);
//			t.start();
//		}
		timeThread.start();
		for(Thread t: allThreads){
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		inventory.printInventoryToFile(args[2]);
		MoneyRegister money = MoneyRegister.getInstance();
		money.printOrderReceipts(args[3]);
		try{
		FileOutputStream file = new FileOutputStream(args[4]);
		ObjectOutputStream obj = new ObjectOutputStream(file);
		obj.writeObject(money);
		obj.close();
		file.close();
		}catch(IOException e){}
		HashMap<Integer, Customer> customersMap = new HashMap<>();
		for (Customer c: j.services.customers){
			customersMap.put(c.getId(), c);
		}
		try{
			FileOutputStream file = 	new FileOutputStream(args[1]);
			ObjectOutputStream obj = new ObjectOutputStream(file);
			obj.writeObject(customersMap);
			obj.close();
			file.close();
		}catch(IOException e){}
	}
}