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
		            	running = Double.parseDouble(inventoryDetails[2]) + feed.getFeedQuantity();
		            	newLine = inventoryDetails[0]+","+inventoryDetails[1]+","+running+","+inventoryDetails[3];
		            	oldLine = line;
		            	if(running > (Double.parseDouble(inventoryDetails[1])*0.95)){
		            		status.setMessage("Notify the Ventor for more inventory");
		            		//If the running inventory crossed 95% of the received inventory the vendor should be notified. email configurations can be done at this point
		            	}
		            	foundInventory = true;
		            }
				}
				
				if(foundInventory && newLine != null){
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
	
}
