package com.izi.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;


import java.io.IOException;

import com.izi.bean.Inventory;
import com.izi.bean.Status;

public class ManageInventory {

	public Status feedInventoryInformation(Inventory inventory) {
		Status status = new Status();
		// TODO Auto-generated method stub
		try{
			String line, newLine = null, oldLine = null;
			boolean foundInventory = false;
			File file =new File("inventory.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			FileReader fr = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fr);
			newLine = inventory.getZooName()+","+inventory.getInventory()+","+"0.0,0.0"+"\n";
			StringBuffer sb = new StringBuffer();
			 while((line = bufferedReader.readLine()) != null) {
				 	sb.append(line+"\n");
				 	String[] inventoryDetails = line.split(",");
		            if(inventoryDetails[0].equals(inventory.getZooName())){
		            	double newInventory = inventory.getInventory() + Double.parseDouble(inventoryDetails[1]);
		            	newLine = inventory.getZooName()+","+newInventory+","+"0.0"+","+inventoryDetails[3];
		            	oldLine = line;
		            	foundInventory = true;
		            }
			 }
			if(!foundInventory){
				FileWriter fileWritter = new FileWriter(file.getName(),true);
   			 	BufferedWriter bufferedWritter = new BufferedWriter(fileWritter);
   			 	bufferedWritter.write(newLine);
   			 	foundInventory = true;
   			 	bufferedWritter.close();
			 }
			else{
				FileWriter fileWritter = new FileWriter(file.getName());
   			 	BufferedWriter bufferedWritter = new BufferedWriter(fileWritter);
   			 	bufferedWritter.write(sb.toString().replace(oldLine, newLine));
   			 	foundInventory = true;
   			 	bufferedWritter.close();
			}
			bufferedReader.close();
		    status.setStatus(true);
		    status.setMessage("Added!!!");
		
			
		}
		catch(Exception e){
			status.setStatus(false);
		    status.setMessage(e.toString());
		}
		return status;
	}

	

}
