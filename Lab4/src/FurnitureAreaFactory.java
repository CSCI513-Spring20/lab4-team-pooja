public class FurnitureAreaFactory extends HouseFactory{

	@Override
	public HouseEntity createItem(String item) { // creates furniture items
		switch (item){
			case "Sofa":
			case "Table":
			case "DiningTable":
			case "Bed":
			case "Organiser":
				break;
			default:
				return null;
		}
		return new Furniture(item);
	}
}