/*
 * Utility methods class
 */
package com.jpmorgan.TradeReport;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;

import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseChar;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.io.CsvBeanReader;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Utils {
	
	static Logger logger = Logger.getLogger(Utils.class.getName());
	
	//Super CSV API to read CSV file using CsvBeanReader class which reads each line into a JavaBean class (POJO).
	public Multimap<Date, Trade> readCSVFile(String csvFileName) {
		
		// create multimap to store key and values
        Multimap<Date, Trade> multiMap = ArrayListMultimap.create();
        
        /*
         * Super CSV provides some classes called cell processors that automate data type conversions and enforce constraints
         * when mapping values in the CSV file with JavaBean�s properties. For example: the NotNull processor ensures that the 
 		 * column is not null; the ParseDate processor converts a String to a Date object using a specified date format; 
         * the ParseDouble processor converts a String to a Double.
         */
		CellProcessor[] processors = new CellProcessor[] {
	            new NotNull(), //entity
	            new ParseChar(), //type
	            new ParseDouble(),
	            new NotNull(),
	            new ParseDate("dd-MMM-yy"), 
	            new ParseDate("dd-MMM-yy"), 
	            new ParseInt(),
	            new ParseDouble()
	    };
	 
	    try(ICsvBeanReader beanReader = new CsvBeanReader(new FileReader(csvFileName),
                CsvPreference.STANDARD_PREFERENCE)) {
	        String[] header = beanReader.getHeader(true);
	        Trade tradeBean = null;
	        //The while loop reads each line (after the first line of column headers) into a Book object until reaching 
	        //the end of the CSV file
	        while ((tradeBean = beanReader.read(Trade.class, header, processors)) != null) {
//	            System.out.printf("%s %c %.2f %s %tD %tD %d %.2f",
//	            		tradeBean.getEntity(), tradeBean.getType(),
//	            		tradeBean.getAgreedFx(), tradeBean.getCurrency(),
//	            		tradeBean.getInstructionDate(), tradeBean.getSettlementDate(),
//	            		tradeBean.getUnits(), tradeBean.getPrice());
//	            System.out.println();
	            
                checkWorkingDay(tradeBean);
	            if(tradeBean.getType() == 'B') {
	            	tradeBean.setOutAmount(BigDecimal.valueOf(tradeBean.getPrice() * tradeBean.getUnits() * tradeBean.getAgreedFx()));
		            multiMap.put(tradeBean.getSettlementDate(), tradeBean);
	            }
	            if(tradeBean.getType() == 'S') {
	            	tradeBean.setIncAmount(BigDecimal.valueOf(tradeBean.getPrice() * tradeBean.getUnits() * tradeBean.getAgreedFx()));
		            multiMap.put(tradeBean.getSettlementDate(), tradeBean);
	            }
	        }
	    } catch (IOException ex) {
	        logger.log(Level.SEVERE, "Could not find the CSV file: ", ex.getMessage());
	    }
		return multiMap;
	}
	
	//A work week starts from Monday till Friday, except for currency type AED and SAR (which work week starts Sunday 
	//till Thursday)
	private void checkWorkingDay(Trade trade) {
		
		Calendar c = Calendar.getInstance();
		
		//SettlementDate of every trade is taken for settling of the trade and this should not fall on weekends
		c.setTime(trade.getSettlementDate());
		
		//An int value which represents the day number
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		
		//For currency type AED and SAR
		if(trade.getCurrency().equalsIgnoreCase("AED") || trade.getCurrency().equalsIgnoreCase("SAR")){
			//settle date falls on friday and saturday
			if(dayOfWeek == 6 || dayOfWeek == 7) {
				//Change the settlement date to next working day
				trade.setSettlementDate(getChangedSettleDateCurrency(trade.getSettlementDate()));
			}
			
		} else {
			//for other currency type, if settle date falls on saturday and sunday
			if(dayOfWeek == 7 || dayOfWeek == 1) {
				//Change the settlement date to next working day
				trade.setSettlementDate(getChangedSettleDate(trade.getSettlementDate()));
			}
		}
	}
	
	//Method which changes the settlement date to a next working day if it falls on a weekend
	private Date getChangedSettleDateCurrency(Date settleDate) {
		
        Calendar calendar = Calendar.getInstance();
        
        calendar.setTime(settleDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.FRIDAY) {
            calendar.add(Calendar.DATE, 2);
        } else if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 1);
        }

        Date nextBusinessDay = calendar.getTime();
        return nextBusinessDay;
	}

	//Method which changes the settlement date to a next working day if it falls on a weekend
	private Date getChangedSettleDate(Date settleDate) {
		
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(settleDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (dayOfWeek == Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 2);
        } else if (dayOfWeek == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }

        Date nextBusinessDay = calendar.getTime();
        return nextBusinessDay;
	}
}
