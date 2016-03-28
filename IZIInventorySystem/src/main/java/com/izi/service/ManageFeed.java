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
	
	
	public Status saveFeedDetails(Feed feed) {//this method will get the feed information and store it in file with ',' as separator and also update the running inventory
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
	
	public Status getAnimalAverageFeed() {//to calculate the average feed per day
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
	private Map getFeedDetails() {//read the file and put the details in a map. The map key is zoo name and value is map of animals with animal name as key and list of feed objects as value
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
	
public Status getAnimalAverageFeedPerDay() {// to calculate the number of timea animals are fed
		
		// TODO Auto-generated method stub
		Status status = new Status();
		try {
			double averagecount = 0.0;
			Map<String, Map<String, List<Feed>>> feedMap= getFeedDetails();
			for(Map.Entry<String, Map<String, List<Feed>>> feedsMap : feedMap.entrySet()){
				Map <String, List<Feed>> animalInZoo = feedsMap.getValue();
				for(Map.Entry<String, List<Feed>> animals : animalInZoo.entrySet()){
					ArrayList<Integer> days= new ArrayList<Integer>();
					days.add(animals.getValue().get(0).getDay());
					int animalFeedCount = 0;
					for(Feed animal : animals.getValue()){
						animalFeedCount++;
						if(!days.contains(animal.getDay()))
							days.add(animal.getDay());
					}
					averagecount = (double)animalFeedCount/days.size();
					System.out.println("Zoo Name: "+animals.getValue().get(0).getZooName()+"; Animal Name: "+animals.getKey()+"; Average Count: "+averagecount);
				}				
			}
			status.setStatus(true);
			status.setMessage("Success!");
		} catch (Exception e) {
			status.setStatus(false);
			status.setMessage(e.getMessage());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return status;
	}

public void getAnimalFedAverage() {
	// TODO Auto-generated method stub
	try {
		FileReader fileReader = new FileReader("feed.txt");
		String line = null;
		double feedCount = 0.0;
		double feedAverage = 0.0;
		double individualAver = 0.0;
		BufferedReader bufferedReader =  new BufferedReader(fileReader);
		Map<String, Map<String, Double>> animalMap = new HashMap<String, Map<String, Double>>();
		while((line = bufferedReader.readLine()) != null) {
		    String[] feedDetails = line.split(",");
		    double feedQuantity = Double.parseDouble(feedDetails[4]);
		    String zooName = feedDetails[0];
		    Map zooMap = animalMap.get(feedDetails[1]);
		    if(zooMap == null){
		    	zooMap = new HashMap<String, Double>();
		    	zooMap.put(zooName, feedQuantity);
		    }
		    else{
		    	Double feed = (Double) zooMap.get(zooName);
		    	if(feed == null){
		    		feed = feedQuantity;
		    	}
		    	else
		    		feed = feed + feedQuantity;
		    	zooMap.put(zooName, feed);
		    }
		    animalMap.put(feedDetails[1], zooMap);
		    //System.out.println(zooMap);
		
		}
		bufferedReader.close();
		
		for(Map.Entry<String, Map<String, Double>> animalsMap : animalMap.entrySet()){
			Map<String, Double> zooMap = animalsMap.getValue();
			for(Map.Entry<String, Double> zoosMap : zooMap.entrySet()){
				feedCount += (Double)zoosMap.getValue();
			}
			feedAverage = feedCount/zooMap.size();
			System.out.println(feedAverage);
			for(Map.Entry<String, Double> zoosMap : zooMap.entrySet()){
				feedCount = (Double)zoosMap.getValue();
				individualAver = (feedCount/feedAverage)*100 - 100;
				StringBuffer sb = new StringBuffer();
				sb.append("Animal "+animalsMap.getKey()+" in Zoo "+zoosMap.getKey()+" is "+individualAver+"%");
				if(individualAver < 0)
					sb.append(" (below average)");
				else if (individualAver  > 0)
					sb.append(" (above average)");
				sb.append(" of total average by animal species");
				System.out.println(sb.toString());
			}
			feedCount = 0.0;
			feedAverage = 0.0;
		}
	} catch (NumberFormatException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
