package com.trade.dailytrade.tradedef;

import java.util.List;

import com.trade.dailytrade.model.TradeModel;

public interface TradeListener {
	void updateTrade(List<TradeModel> modelList);
}
