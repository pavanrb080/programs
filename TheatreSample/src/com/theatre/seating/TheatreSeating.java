/**
 * 
 */
package com.theatre.seating;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

public class TheatreSeating {
	private static int size=0;
	private static int cusRqSize = 0;

	public static void main(String[] args) {
		TheatreSeating ms = new TheatreSeating();
		ClassLoader classLoader = ms.getClass().getClassLoader();
		// Create 2-dimensional array.
		int rows = ms.getFileLength("input.txt",classLoader);
		
		String[][] availableSeats = new String[rows][20];//In real time scenario size should be dynamic
		boolean resReq = false;
		Map<String, String> customerRequest = new LinkedHashMap<String, String>();

		try (BufferedReader br = new BufferedReader(new FileReader(classLoader.getResource("input.txt").getFile()))) {

			String line;
			int i = 0;

			while ((line = br.readLine()) != null) {
				if (line.trim().equals("")) {
					resReq = true;
				}
				String sections[] = line.split(" ");
				String seatsRow[] = new String[sections.length]; 

				for (int j = 0; j < sections.length; j++) {
					if (!resReq) {
						availableSeats[i][j] = sections[j];
						seatsRow[j] = sections[j];
					} else {
						if (!(line.trim().equals(""))) {
							customerRequest.put(sections[0], sections[1]);
						}
					}
				}
				
				i = i + 1;

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, String> allocatedMap = new LinkedHashMap<String, String>();
		allocatedMap.putAll(customerRequest);
		Map<String, String> copyCustomerRequest = new HashMap<String, String>();
		copyCustomerRequest.putAll(customerRequest);

		Iterator<Entry<String, String>> entries = customerRequest.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry<String, String> entry = entries.next();
			if (entry.getKey() != null && entry.getValue() != null) {
				Integer seatRequested = Integer.parseInt(entry.getValue().toString());
				boolean seatAllocated = false;
				
				for (int row = 0; row < availableSeats.length; row++) {
					for (int col = 0; col < availableSeats[row].length && availableSeats[row][col] != null; col++) {
						if (Integer.parseInt(availableSeats[row][col]) > seatRequested) {
							int remainingVal = Integer.parseInt(availableSeats[row][col]) - seatRequested;
							boolean isInMap = customerRequest.containsValue(String.valueOf(remainingVal));
							boolean isInCopyMap = copyCustomerRequest.containsValue(String.valueOf(remainingVal));
							if (isInMap && isInCopyMap) {
								String key = getKeyByValue(copyCustomerRequest, String.valueOf(remainingVal));
								copyCustomerRequest.remove(key);
								allocatedMap.put(entry.getKey(), "Row " + (row + 1) + " Section" + (col + 1));
								seatAllocated = true;
								Integer newVal = Integer.parseInt(availableSeats[row][col]) - seatRequested;
								availableSeats[row][col] = newVal.toString();
								break;
							}
						} else if (seatRequested == Integer.parseInt(availableSeats[row][col])) {
							allocatedMap.put(entry.getKey(), "Row " + (row + 1) + " Section" + (col + 1));
							seatAllocated = true;
							Integer newVal = Integer.parseInt(availableSeats[row][col]) - seatRequested;
							availableSeats[row][col] = newVal.toString();
							break;
						} else if (seatRequested > getUpdatedQuantity(availableSeats)) {
							allocatedMap.put(entry.getKey(), "Sorry, we can't handle your party.");
						} else if (getLargest(availableSeats) < seatRequested) {
							allocatedMap.put(entry.getKey(), "Call to split party.");
						}
					}
					if (seatAllocated) {
						break;
					}
				}

			}
		}

		for (Map.Entry<String, String> entry : allocatedMap.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}

	/**
	 * This method will return key based on values from Map
	 * @param map
	 * @param value
	 * @return
	 */
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		
		for (Entry<T, E> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * This method will return the sum of the numbers in the array
	 * @param availableSeats
	 * @return
	 */
	private static int getUpdatedQuantity(String[][] availableSeats) {
		int sum = 0;
		for (int i = 0; i < availableSeats.length; i++) {
			for (int j = 0; j < availableSeats[i].length; j++) {
				if (availableSeats[i][j] != null) {
					sum = sum + Integer.parseInt(availableSeats[i][j]);
				}
			}
		}
		return sum;
	}

	 /**
	 * This method will return the largest number in the Array
	 * @param availableSeats
	 * @return
	 */
	private static int getLargest(String[][] availableSeats) {
		
		int maxValue = 0;
		for (int i = 0; i < availableSeats.length; i++) {
			for (int j = 0; j < availableSeats[i].length; j++) {
				if (availableSeats[i][j] != null) {
					if (Integer.parseInt(availableSeats[i][j]) > maxValue) {
						maxValue = Integer.parseInt(availableSeats[i][j]);
					}
				}
			}
		}
		return maxValue;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param classLoader
	 * @return
	 */
	private int getFileLength(String fileName,ClassLoader classLoader) {
		try (BufferedReader br = new BufferedReader(new FileReader(classLoader.getResource("input.txt").getFile()))) {

			String line;
			boolean isCustReq = false;
			while ((line = br.readLine()) != null) {
				if (line.trim().equals("") || isCustReq) {
					cusRqSize++;
					isCustReq = true;
				} else {
					size++;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

}
