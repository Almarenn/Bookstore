package bgu.spl.mics.application.passiveObjects;



/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {

    private static Inventory instance = null;
	private BookInventoryInfo[] inventory;
	
	private Inventory() {}

	//test only method!!
	public void reset(){
		this.inventory=null;
	}
	/**
     * Retrieves the single instance of this class.
     */
	public static Inventory getInstance() {
		if(instance == null) {
            instance = new Inventory();
         }
         return instance;
    }
	
	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		if(this.inventory==null){ //if its the first load
			this.inventory = inventory;
		}
		else { //if its not the first load
			BookInventoryInfo[] temp = this.inventory;
			this.inventory = new BookInventoryInfo[inventory.length+temp.length];
			for(int i=0; i<temp.length; i++){
				this.inventory[i]=temp[i];
			}
			for(int i=temp.length; i<this.inventory.length; i++){
				this.inventory[i]=inventory[i-temp.length];
			}
		}
	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
		BookInventoryInfo b = checkAvailability(book);
		if(b==null){
			return OrderResult.NOT_IN_STOCK;
		}
		b.setAmountInInventory(b.getAmountInInventory()-1);
		return OrderResult.SUCCESSFULLY_TAKEN;
	}
	
		
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabilityAndGetPrice(String book) {
		BookInventoryInfo b = checkAvailability(book);
		if(b==null){
			return -1;
		}
		return b.getPrice();
	}
	
	//private method
	private BookInventoryInfo checkAvailability(String book){
		for(int i=0; i<inventory.length; i++){
			int amount = inventory[i].getAmountInInventory();
			String name =  inventory[i].getBookTitle();
			if(name.equals(book) && amount >0){
				return inventory[i];
			}
		}
		return null;
	}
		
	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		//TODO: Implement this
	}
}