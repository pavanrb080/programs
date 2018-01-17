package com.pricing.engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pricing.model.Product;
import com.pricing.model.Product.Pricing;
/**
 * Pricing Engine which will decide price based on the conditions
 * @author pavan
 *
 */
public class PricingEngine {

	/**
	 * Main method to check correct pricing 
	 * @param args
	 */
	public static void main(String[] args) {
		PricingEngine price = new PricingEngine();
		
		
		List<Product> products = price.readFile(price.getClass().getClassLoader());
		
		for (Product prod : products) {
			double avg = price.checkAvg(prod.getProdPrices());
			
			List<Double> check = prod.getProdPrices().stream().filter(value -> value > (avg/2)).filter(value -> (avg + avg/2) > value).collect(Collectors.toList());
		
			Map<Double, Integer> map = new HashMap<Double, Integer>(); 
			
			for (Double temp : check) {
				Integer count = map.get(temp);
				map.put(temp, (count == null) ? 1 : count + 1);
			}
			
			Integer max = map.entrySet()
		            .stream()
		            .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
		            .get()
		            .getValue();
			
			List<Double> listOfMax = map.entrySet()
		            .stream()
		            .filter(entry -> entry.getValue() == max)
		            .map(Map.Entry::getKey)
		            .collect(Collectors.toList());

			Double d= listOfMax.stream().min(Double::compare).get();
			
			d = price.finalPrice(prod, d);
			
			System.out.println(new DecimalFormat("#.##").format(d));
		}
	}
	
	/**
	 * Find th price based on conditions
	 * @param prod
	 * @param price
	 * @return
	 */
	private double finalPrice(Product prod, double price) {
		if(prod.getSupply() == Pricing.L && prod.getDemand() == Pricing.L) {
			price = price + (price * 0.1);
		} else if (prod.getSupply() == Pricing.L && prod.getDemand() == Pricing.H) {
			price = price + (price * 0.05) ;
		} else if (prod.getSupply() == Pricing.H && prod.getDemand() == Pricing.L) { 
			price = price - (price * 0.05) ;
		}
		return price;
	}
	
	/**
	 * Method to check average from list of values
	 * @param values
	 * @return
	 */
	private double checkAvg(List<Double> values) {
		double sum = values.stream().mapToDouble(d -> d.doubleValue()).sum();
		double avg = sum/values.size();
		
		return avg;
	}
	
	/**
	 * Method to read values from file
	 * @param classLoader
	 * @return
	 */
	private List<Product> readFile(ClassLoader classLoader) {
		List<Product> products = null;
		try {
			String line;
			products = new ArrayList<>();
			
			BufferedReader br = new BufferedReader(new FileReader(classLoader.getResource("input.txt").getFile()));
			
			while ((line = br.readLine()) != null) { 
				String sections[] = line.split(" ");
				
				if (sections.length == 3) {
					if(isNumeric(sections[2])) {
						for(Product prod : products) {
							if (prod.getNameOfProduct().equals(sections[0])) {
								prod.getProdPrices().add((Double.parseDouble(sections[2])));
							}
						}
					} else {
						Product product = new Product();
						product.setNameOfProduct(sections[0]);
						product.setSupply(Pricing.fromLetter(sections[1]));
						product.setDemand(Pricing.fromLetter(sections[2]));
						product.setProdPrices(new ArrayList<Double>());
						
						products.add(product);
					}
					
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return products;
	}
	
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
}
