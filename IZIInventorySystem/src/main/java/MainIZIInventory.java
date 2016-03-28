import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.izi.bean.Feed;
import com.izi.bean.Inventory;
import com.izi.bean.Status;
import com.izi.service.ManageFeed;
import com.izi.service.ManageInventory;


public class MainIZIInventory {
	public static void main(String[] args){
		getUserInput();
	}
	
	
	private static void getUserInput(){
		Scanner input = new Scanner(System.in);
		int option = 0;
		try {
			do{
				System.out.println("Enter Your Choice !!!\n1. Add Inventory\n2. Add Feed Information\n3. Add Inventory feed in stock\n4. Individual Animal fed every day in average\n5. Average number of times animals are fed on a day\n6. Waste in each Zoo\n7. Which species of animal fed below/ above average");
				option = input.nextInt();
				if(option == 1){
					System.out.println("Enter Zoo Name");
					String zooName = input.next();
					System.out.println("Enter Quantity Recieved(in lb)");
					double quantity = input.nextDouble();
					Inventory inventory = new Inventory();
					inventory.setZooName(zooName);
					inventory.setInventory(quantity);
					ManageInventory manage = new ManageInventory();
					manage.feedInventoryInformation(inventory);
				}
				else if(option == 2){
					getFeedInformation();
				}
				else if(option == 3){
					
				
				}
				else if(option ==4){
					ManageFeed manage = new ManageFeed();
					Status status = manage.getAnimalAverageFeedByZooName();
					if(!status.isStatus()){
						System.out.println(status.getMessage());
					}
				}
				else if(option == 5){
					
				}
				else if(option ==6){
					
				}
				else if(option == 7){
					
				}
				else{
					System.out.println("Enter correct option");
				}
				
				
			}while(option != 0);
			//input.close();
		} catch (InputMismatchException e) {
			e.printStackTrace();
			System.out.println("Enter numbers 1 to 6");
			getUserInput();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("System Error Occured ..Enter again");
			getUserInput();
		}
	}
	 public static void getFeedInformation(){
		try {
			System.out.println("Enter the Zoo Name:");
			Scanner input = new Scanner(System.in);
			String zooName = "";
			String animalName = "";
			int day =0;
			String time = "";
			double quantity = 0.0;
			if(input.hasNext())
				zooName = input.next();			
			System.out.println("Enter the Animal Name:");
			if(input.hasNext())
				animalName = input.next();
			System.out.println("Enter the Day Number:");
			if(input.hasNext())
				day = input.nextInt();
			System.out.println("Enter the Time(00:00 - 24:00):");
			if(input.hasNext())
				time = input.next();	
			System.out.println("Enter the quantity in lbs");
			if(input.hasNext())
				quantity = input.nextDouble();
			Feed feed = new Feed();
			feed.setZooName(zooName);
			feed.setAnimalName(animalName);
			feed.setDay(day);
			feed.setFeedTime(time);
			feed.setFeedQuantity(quantity);
			
			ManageFeed manageFeed = new ManageFeed();
			Status status = manageFeed.saveFeedDetails(feed);
			if(status.isStatus()){
				System.out.println(status.getMessage());
			}
			else
				System.out.println(status.getMessage());
			//input.close();
		} catch (InputMismatchException e) {
			e.printStackTrace();
			System.out.println("Error in entering day or quantity");
			getFeedInformation();
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("System Errored occured. Enter information again");
			getFeedInformation();
		}
		
	 }
}
