import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


/**
 * 
 * All items in the house (rooms, floors, furniture are House Entities) 
 */
interface HouseEntity {
    public void listHouseSpecs(int level);
    public int contentCount();
    public void add(HouseEntity houseentity);
}

 
/**
 * 
 * Composite pattern:  Leaf node
 */
class Furniture implements Serializable, HouseEntity {
	private static final long serialVersionUID = 1L;
	String blockName;
	public Furniture(String blockName){
		this.blockName = blockName;
	}
	
 	@Override
	public void listHouseSpecs(int level) {
		StringBuffer stringbuffer = new StringBuffer();
		for(int i = 0; i < level; i++)
			stringbuffer.append("   ");
		System.out.println(stringbuffer.toString() + blockName);
	}

	@Override
	public int contentCount() {
		return 1;
	}

	@Override
	public void add(HouseEntity houseentity) {
		// TODO Auto-generated method stub
		
	}    
}

/**
 * 
 * Composite Pattern: Composite Class
 * HouseArea is a floor (upstairs, downstairs), the house itself, or a room
 */
class HouseArea implements Serializable, HouseEntity {

	private static final long serialVersionUID = 1L;
	// List of children
	private List<HouseEntity> childGroup = new ArrayList<HouseEntity>();
	String blockName;
	public HouseArea(String blockName){
		this.blockName = blockName;
	}
	
	public void add(HouseEntity group) {
		childGroup.add(group);
	}
	
	public void remove(HouseEntity group) {
		childGroup.remove(group);
	}
	
	@Override
	public void listHouseSpecs(int level) {

		// First display the current group
		StringBuffer stringbuffer = new StringBuffer();
		for(int i = 0; i < level; i++)
			stringbuffer.append("   ");
		System.out.println(stringbuffer.toString() + blockName);
		
		// Now delegate the task of display to any children
		for(HouseEntity group: childGroup){
			group.listHouseSpecs(level+1);
		}	
	}

	@Override
	public int contentCount() {
		int contents = 0;
		for(HouseEntity child: childGroup){
			contents += child.contentCount();
		}
		return contents + 1;
	}
}


/**
 * 
 * This is the main application.  Note that while it is a JavaFX application it doesn't
 * actually "show" the main scene.  We just need the application for the fileChooser.
 */
public class HouseBuilder extends Application{
	
	
	HouseFactory houseArea = new HouseAreaFactory();
	HouseEntity house = houseArea.createItem("House");
	FurnitureAreaFactory furniture= new FurnitureAreaFactory();
	public void buildHouse(){
			HouseEntity hall = houseArea.createItem("Hall");
		    HouseEntity kitchen = houseArea.createItem("Kitchen");
		    HouseEntity diningArea = houseArea.createItem("Dining");
		    HouseEntity bedBath = houseArea.createItem("Bed&Bath");
		    
		    HouseEntity sofa = furniture.createItem("Sofa");
		    HouseEntity table = furniture.createItem("Table");
		    HouseEntity dining = furniture.createItem("DiningTable");
		    HouseEntity bed = furniture.createItem("Bed");
			HouseEntity organiser = furniture.createItem("Organiser");
		    
		    house.add(hall);
		    house.add(kitchen);
		    house.add(diningArea);
		    house.add(bedBath);
		    
		    hall.add(sofa);
		    kitchen.add(table);
		    diningArea.add(dining);
		    bedBath.add(bed);
		    bedBath.add(organiser);
		    
	}
	
	/**
	 * Save using serialization
	 * @param fileName
	 */
	public void save(String fileName){
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream( new FileOutputStream(fileName));
			oos.writeObject(house);  //serializing employee
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void countHouseContents(){
		System.out.println("House includes: " + house.contentCount()+ " areas and/or furniture items.");
	}
	
	public void printHouseSpecs(){
		house.listHouseSpecs(0);
	}
	
	public HouseArea getHouse(){
		return (HouseArea) house;
	}
	
	
	/**
	 * Restore from serialized form
	 * @param fileName
	 */
	public void restore(String fileName){
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream( new FileInputStream(fileName));
			house = (HouseArea) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getFileName(Stage primaryStage){
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setInitialDirectory(new File("D:\\Semester 1\\Software Engineering\\All Labs Github\\lab4-team-pooja\\Lab4"));  // This is optional
		 fileChooser.setTitle("Serialization File");
		 File file = fileChooser.showOpenDialog(primaryStage);
		 return file.getAbsolutePath();
	}
	
	 public static void main(String[] args) {
		 launch(args);
	 }

	@Override
	public void stop() {
		System.out.println("\nstopping javafx application");
		System.exit(1);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		  HouseBuilder houseBuilder = new HouseBuilder();
	      houseBuilder.buildHouse();
	      houseBuilder.save("D:\\Semester 1\\Software Engineering\\All Labs Github\\lab4-team-pooja\\Lab4\\myHouse.ser");
	      String filename = houseBuilder.getFileName(primaryStage);
	      houseBuilder.restore(filename);
	      houseBuilder.printHouseSpecs();
	      houseBuilder.countHouseContents();
	      // or this.stop();
	      stop();
	}      	       
}

