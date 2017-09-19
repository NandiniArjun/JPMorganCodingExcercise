/**
 * Main Class
 */
package com.jpmorgan.TradeReport;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

//Single Key and Multiple Values Using Google Guava Collections
import com.google.common.collect.Multimap;

/**
 * @author Nandini Mandya
 *
 */
public class TradeReport {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//To print the final trade report to console
        StringBuilder output = new StringBuilder();
        
        //List which stores all trade pojo
        List<Trade> rankList = new ArrayList<>();
        
		Utils utils = new Utils();
		
		//data.csv path which has trade data
		String csvFileName = args[0];
		
		Multimap<Date, Trade> multiMap = utils.readCSVFile(csvFileName);
		
		List<Trade> trades = null;
		if(multiMap.size() > 0) {
			// get all the set of keys
	        Set<Date> keysSettleDate = multiMap.keySet();
	        // iterate through the key set and display key and values
	        for (Date settleDate : keysSettleDate) {
	        	trades = (List<Trade>) multiMap.get(settleDate);
	        	for(Trade trade : trades) {
	        		rankList.add(trade);
	        	}
	            output
	            .append("\nTrade on : "+settleDate+"\n")           
	            .append("------------------------------------------\n");
	            do {
	            	output
	            	.append("Amount in USD settled incoming : $"+trades.stream().map(Trade::getIncAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
	            	.append("\nAmount in USD settled outgoing : $"+trades.stream().map(Trade::getOutAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
	            }while(false);
	            output
	        	.append("\n------------------------------------------");
	        }
	        output
	    	.append("\nRanking of Entities:")
	    	.append("\n------------------------------------------\n");
	        //sort to obtain the rank for incoming
	        rankList.sort((r1, r2)->r2.getIncAmount().intValue()-r1.getIncAmount().intValue());
	        output
	        .append(rankList.get(0).getEntity()+" is rank 1 for incoming\n");
	        //sort to obtain the rank for outgoing
	        rankList.sort((r1, r2)->r2.getOutAmount().intValue()-r1.getOutAmount().intValue());
	        output
	        .append(rankList.get(0).getEntity()+" is rank 1 for outgoing\n");
	
	        System.out.println(output.toString());
		}
	}	
}
