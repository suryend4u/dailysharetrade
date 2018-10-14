package com.trade.dailytrade.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.trade.dailytrade.model.TradeModel;
import com.trade.dailytrade.tradedef.TradeListener;

public class TradeService {
	private final Object LOCK = new Object();
	private static final Object INSTANCE_LOCK=new Object();
	private Set<TradeListener> trades;
	private static TradeService tradeService;
	
	private TradeService() {
		if (null == trades) {
			trades = new HashSet<>();
		}
	}

	public static TradeService getInstance() {
		if(null==tradeService) {
			synchronized (INSTANCE_LOCK) {
				tradeService = new TradeService();
			}
		}
		return tradeService;
	}

	public void registerTrade(TradeListener trade) {
		if (null == trade)
			return;
		synchronized (LOCK) {
			trades.add(trade);
		}
	}

	public void removeTrade(TradeListener trade) {
		if (null == trade)
			return;
		synchronized (LOCK) {
			trades.remove(trade);
		}
	}
	public void notifyTrades(List<TradeModel> tradeModelList) {
		for(TradeListener trade:trades) {
			trade.updateTrade(tradeModelList);
		}
	}
	
}
