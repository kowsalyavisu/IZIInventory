package com.izi.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.izi.bean.*;

public class ManageFeed {
	
	
	public Status saveFeedDetails(Feed feed) {
		// TODO Auto-generated method stub
		Status status = new Status();
		try {
			File file =new File("feed.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			String data = feed.getZooName()+","+feed.getAnimalName()+","+feed.getDay()+","+feed.getFeedTime()+","+feed.getFeedQuantity()+"\n";
			    String line, newLine = null, oldLine = null;
			    double running = 0.0;
			    boolean foundInventory = false;
				File inventoryFile =new File("inventory.txt");
				FileReader fr = new FileReader(inventoryFile);
				BufferedReader bufferedReader = new BufferedReader(fr);
				StringBuffer sb = new StringBuffer();
				while((line = bufferedReader.readLine()) != null) {
					//System.out.println(line);
					sb.append(line+"\n");
		            String[] inventoryDetails = line.split(",");
		            if(inventoryDetails[0].equals(feed.getZooName())){
		            	running = Double.parseDouble(inventoryDetails[2]) + feed.getFeedQuantity();// the running inventory is increased after every feed
		            	newLine = inventoryDetails[0]+","+inventoryDetails[1]+","+running+","+inventoryDetails[3];
		            	oldLine = line;
		            	if(running > (Double.parseDouble(inventoryDetails[1])*0.95)){
		            		status.setMessage("Notify the Ventor for more inventory");
		            		//If the running inventory crossed 95% of the received inventory the vendor should be notified. email configurations can be done at this point
		            	}
		            	foundInventory = true;
		            }
				}
				
				if(foundInventory && newLine != null){//If the inventory is found then the feed details are added and the inventory is updated
					FileWriter InventoryWritter = new FileWriter(inventoryFile.getName());
					BufferedWriter bufferedWritter = new BufferedWriter(InventoryWritter);
					bufferedWritter.write(sb.toString().replace(oldLine,  newLine));
					bufferedWritter.close();
					FileWriter fileWritter = new FileWriter(file.getName(),true);
				    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				    bufferWritter.write(data);
				    bufferWritter.close();
				    if(status.getMessage() != null)
				    	status.setMessage("Feed Added "+ status.getMessage());
				    else
				    	status.setMessage("Feed Added!!");
					status.setStatus(true);
				}
				else{
					status.setMessage("Inventory for zoo not present");
					status.setStatus(false);
				}
				bufferedReader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			status.setMessage(e.toString());
			status.setStatus(false);
		}
		return status;
	}
	
	public Status getAnimalAverageFeedByZooName() {
		// TODO Auto-generated method stub
		Map<String, Map<String, List<Feed>>> feedMap= getFeedDetails();
		Status status = new Status();
		try {
			for(Map.Entry<String, Map<String, List<Feed>>> feedsMap : feedMap.entrySet()){
				Map <String, List<Feed>> animalInZoo = feedsMap.getValue();
				for(Map.Entry<String, List<Feed>> animals : animalInZoo.entrySet()){
					ArrayList<Integer> days= new ArrayList<Integer>();
					days.add(animals.getValue().get(0).getDay());
					double feedQuantity = 0.0;
					for(Feed animal : animals.getValue()){
						feedQuantity += animal.getFeedQuantity();
						if(!days.contains(animal.getDay()))
							days.add(animal.getDay());
					}
					feedQuantity = feedQuantity/days.size();
					System.out.println("Zoo Name: "+animals.getValue().get(0).getZooName()+"; Animal Name: "+animals.getKey()+"; Average Feed: "+feedQuantity);
				}
			}
			status.setStatus(true);
			status.setMessage("success!!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			status.setStatus(false);
			status.setMessage(e.getMessage());
			//e.printStackTrace();
		}
		return status;
	}
	private Map getFeedDetails() {
		// TODO Auto-generated method stub
		 Map<String, Map<String, List<Feed>>> animalFeed = new HashMap<String, Map<String, List<Feed>>>();
			
		try {
			FileReader fileReader = new FileReader("feed.txt");
			String line = null;
	        BufferedReader bufferedReader =  new BufferedReader(fileReader);
	       while((line = bufferedReader.readLine()) != null) {
	            String[] feedDetails = line.split(",");
	            Feed feed = new Feed();
	            feed.setZooName(feedDetails[0]);
	            feed.setAnimalName(feedDetails[1]);
	            feed.setDay(Integer.parseInt(feedDetails[2]));
	            feed.setFeedTime(feedDetails[3]);
	            feed.setFeedQuantity(Double.parseDouble(feedDetails[4]));
	            if(!animalFeed.containsKey(feed.getZooName())){
					animalFeed.put(feed.getZooName(), null);
				}
				Map<String, List<Feed>> animalMap = animalFeed.get(feed.getZooName());
				if(animalMap == null){
					animalMap = new HashMap<String, List<Feed>>();
					animalMap.put(feed.getAnimalName(), new ArrayList<Feed>());
				}
				List animalList = animalMap.get(feed.getAnimalName());
				if(animalList == null){
					animalList = new ArrayList<Feed>();
				}
				//System.out.println("animal"+animalList);
				animalList.add(feed);
				animalMap.put(feed.getAnimalName(), animalList);
				animalFeed.put(feed.getZooName(), animalMap);
				
	        }
	       bufferedReader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("System Error occured");
		}
		return animalFeed;
	}
}
