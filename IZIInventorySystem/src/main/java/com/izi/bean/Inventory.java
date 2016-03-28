package com.izi.bean;

public class Inventory {
	private String zooName;
	private double inventory;
	private double waste;
	private double runningInventory;
	
	public double getInventory(){
		return this.inventory;
	}
	
	public void setInventory(double inventory){
		this.inventory = inventory;
	}
	
	public double getWaste(){
		return this.waste;
	}
	
	public void setWaste(double waste){
		this.waste = waste;
	}

	public String getZooName() {
		return zooName;
	}

	public void setZooName(String zooName) {
		this.zooName = zooName;
	}

	public double getRunningInventory() {
		return runningInventory;
	}

	public void setRunningInventory(double runningInventory) {
		this.runningInventory = runningInventory;
	}

	public Inventory() {
		this.inventory = 0.0;
		this.waste = 0.0;
		this.runningInventory = 0.0;
	}
	
	
}
