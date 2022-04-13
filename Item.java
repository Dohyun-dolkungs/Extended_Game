// Some treasure found in the map
public class Item{
	
	// the name of the item
	private String itemName;

	// Default Constructor
	// This will assign a random name to the Item
	public Item(){
		itemName = GameData.getRandomItemName();
	}

	// An overloaded constructor that accepts a custom item name. 
	// This may be useful in debugging to ensure the ItemList is working as expected.
	public Item(String newName) {
		itemName = newName;
	}

	// returns the itemName. Don’t add additional formatting here.
	public String toString() {
		return itemName;
	}

}