package com.pricing.model;

import java.util.List;

public class Product {

	private int numberOfProducts;
	private String nameOfProduct;
	private Pricing supply;
	private Pricing demand;
	
	private List<Double> prodPrices;

	public enum Pricing
	{
	    L("L"), H("H");
		
		private String letter;
	    private Pricing(String letter) {
	        this.letter = letter;
	    }

	    public static Pricing fromLetter(String letter) {
	        for (Pricing s : values() ){
	            if (s.letter.equals(letter)) return s;
	        }
	        return null;
	    }
	}

	public int getNumberOfProducts() {
		return numberOfProducts;
	}

	public void setNumberOfProducts(int numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}

	public String getNameOfProduct() {
		return nameOfProduct;
	}

	public void setNameOfProduct(String nameOfProduct) {
		this.nameOfProduct = nameOfProduct;
	}

	public Pricing getSupply() {
		return supply;
	}

	public void setSupply(Pricing supply) {
		this.supply = supply;
	}

	public Pricing getDemand() {
		return demand;
	}

	public void setDemand(Pricing demand) {
		this.demand = demand;
	}

	public List<Double> getProdPrices() {
		return prodPrices;
	}

	public void setProdPrices(List<Double> prodPrices) {
		this.prodPrices = prodPrices;
	}
	
	
}
