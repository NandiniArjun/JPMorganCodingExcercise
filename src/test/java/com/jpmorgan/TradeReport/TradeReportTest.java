package com.jpmorgan.TradeReport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Multimap;
import com.jpmorgan.TradeReport.Trade;
import com.jpmorgan.TradeReport.Utils;

public class TradeReportTest {
	/*
	 * The methods covers possible test scenarios.
	 * 
	 */
    @SuppressWarnings("deprecation")
	@Test
    public void shouldReadAllTradeData() {
    	Multimap<Date, Trade> multiMap = new Utils().readCSVFile("src/test/data.csv");
    	List<Trade> trades = null;
    	trades = (List<Trade>)multiMap.get(new Date("04-Jan-16"));
    	
    	assertThat(trades).hasSize(3);// The count contains entities test1 and test2 and foo(as the settlement date comes on weekend, 
    	//so compiling to next working day which falls on 4th Jan 2016
    	
    	assertThat(trades.stream().map(Trade::getIncAmount).reduce(BigDecimal.ZERO, BigDecimal::add)).isEqualTo(new BigDecimal("3000.0"));
    	assertThat(trades.stream().map(Trade::getOutAmount).reduce(BigDecimal.ZERO, BigDecimal::add)).isEqualTo(new BigDecimal("13150.0"));
    }
    
    @Test
    public void shouldReadTypeTradeData() {
    	Multimap<Date, Trade> multiMap = new Utils().readCSVFile("src/test/data.csv");
    	List<Trade> trades = null;
    	trades = (List<Trade>)multiMap.get(new Date("04-Jan-16"));
    	
    	assertThat(trades).hasSize(3);// data.csv has 4 records for 4th Jan, but the entity test3 
    	//not considered as it doesn't have a valid Buy/Sell
    }
    
    @Test
    public void shouldHighestIncomingRank() {
    	Multimap<Date, Trade> multiMap = new Utils().readCSVFile("src/test/data.csv");
    	Set<Date> keysSettleDate = multiMap.keySet();
    	List<Trade> rankList = new ArrayList<>();
    	List<Trade> trades = null;
    	
    	for (Date settleDate : keysSettleDate) {
        	trades = (List<Trade>) multiMap.get(settleDate);
        	for(Trade trade : trades) {
        		rankList.add(trade);
        	}
    	}
    	rankList.sort((r1, r2)->r2.getIncAmount().intValue()-r1.getIncAmount().intValue());
    	
    	assertThat(rankList.get(0).getEntity()).isEqualTo("bar");
    }
    
    @Test
    public void shouldHighestOutgoingRank() {
    	Multimap<Date, Trade> multiMap = new Utils().readCSVFile("src/test/data.csv");
    	Set<Date> keysSettleDate = multiMap.keySet();
    	List<Trade> rankList = new ArrayList<>();
    	List<Trade> trades = null;
    	
    	for (Date settleDate : keysSettleDate) {
        	trades = (List<Trade>) multiMap.get(settleDate);
        	for(Trade trade : trades) {
        		rankList.add(trade);
        	}
    	}
    	rankList.sort((r1, r2)->r2.getOutAmount().intValue()-r1.getOutAmount().intValue());
    	
    	assertThat(rankList.get(0).getEntity()).isEqualTo("foo");
    }

}
