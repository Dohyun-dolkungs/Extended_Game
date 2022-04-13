
// A partially full array that holds a list of Item objects
// Note there is no validation for > maxSize number of items
public class ItemList{
	// Current array state
	private Item[] items; // The array of items. 
	private int currentIndex = 0; // the current item count of valid items
	private int maxSize = 100; // the max size of the array

	// Initilize the array and reset the currentIndex to zero
	public ItemList(){
		items = new Item[maxSize];
		currentIndex = 0;
	}

	// Add the next item to the array
	public void addItem(Item newItem){
		items[currentIndex] = newItem;
		currentIndex ++; // add to array then update

	}

	// Returns the current number of Item objects stored
	public int getCount(){
		return currentIndex;
	}

	// Returns the item stored at index i, or null if it is out of bounds
	public Item getItem(int i ){
		if (i >= 0 && i< currentIndex){
			return items[i];
		}else{
			System.out.println("Warning: No Item found at index " + i);
			return null;
		}
	}

	// Add the contents of a second CreatureList to this CreatureList
	public void addItems(ItemList newList){
		for(int i = 0; i < newList.getCount(); i++){
			// handles blank entries
			Item temp = newList.getItem(i);
			if( temp != null){
				// Add the Item 
				// Note this is the local list (aka this.addItem)
				addItem(newList.getItem(i));
			}else{
				System.out.println("Warning: Item was null when reading in from addItems " + i);
			}
		}
	}

	// The current number of Item objects stored
	public int itemCount(){
		return currentIndex;
	}

	// Print the whole item list as a String
	// formatting and returning all the strings
	public String toString(){

		String list = "[";		

		if(currentIndex == 0){
			return "[EMPTY]";
		}

		// Note you are using current Index not max here. 
		for(int i = 0; i < currentIndex-1; i++){
			list += items[i].toString() + ",";
		}

		// handle the end of the list
		list+= items[currentIndex-1].toString() + "]";

		return list;
	}

		// Print the whole item list as a String
	// formatting and returning all the strings
	public String toStringForFiles(){

		String list = "";		

		if(currentIndex == 0){
			return "";
		}

		// Note you are using current Index not max here. 
		for(int i = 0; i < currentIndex-1; i++){
			list += items[i].toString() + ",";
		}

		// handle the end of the list
		list+= items[currentIndex-1].toString() + "";

		return list;
	}
	
}