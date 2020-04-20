public class HouseAreaFactory extends HouseFactory{

	@Override
	public HouseEntity createItem(String item) { //creates house items
		switch (item){
			case "House":
			case "Hall":
			case "Kitchen":
			case "Dining":
			case "Bed&Bath":
				break;
			default:
				return null;
		}
		return new HouseArea(item);
	}
}