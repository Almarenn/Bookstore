package Java;

import bgu.spl.mics.application.passiveObjects.BookInventoryInfo;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {
    private Inventory inventory;
    @Before
    public void setUp(){
        inventory= new Inventory();
    }

    @Test
    public void getInstance() throws Exception {
        assert(this.inventory.getInstance() instanceof Inventory): "the function doesn't return an Inventory instance";
    }

    private BookInventoryInfo[] bookInventory= new BookInventoryInfo[1];
    private BookInventoryInfo b;

    public void initialzeBookArr(){
        BookInventoryInfo b=new BookInventoryInfo();
        bookInventory[0]=b;
    }

    @Test
    public void load() throws Exception {
        initialzeBookArr();
        this.inventory.load(bookInventory);
        assertTrue("the function doesn't load the book",this.inventory.take(b.getBookTitle()).equals(OrderResult.SUCCESSFULLY_TAKEN));
    }

    @Test
    public void take() throws Exception {
        initialzeBookArr();
        int amount= b.getAmountInInventory();
        this.inventory.load(bookInventory);
        assertTrue("the function doesn't take the book",this.inventory.take(b.getBookTitle()).equals(OrderResult.SUCCESSFULLY_TAKEN));
        assertTrue("the function doesn't reduce the amount of the book",b.getAmountInInventory()==amount-1);

        setUp();
        BookInventoryInfo orign= b;
        assertTrue("the function did found a book in stock", this.inventory.take(b.getBookTitle()).equals(OrderResult.NOT_IN_STOCK));
        assertTrue("The book has changed", b.equals(orign));
    }

    @Test
    public void checkAvailabiltyAndGetPrice() throws Exception {


    }

    @Test
    public void printInventoryToFile() throws Exception {
    }


}