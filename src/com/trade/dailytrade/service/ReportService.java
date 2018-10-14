package com.trade.dailytrade.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.trade.dailytrade.model.TradeModel;
import com.trade.dailytrade.util.Util;

public class ReportService {
	private static final Object INSTANCE_LOCK = new Object();
	private static ReportService reportService;
	private Map<String, List<TradeModel>> reportMap;
	private String buyStatement;
	private String sellStatement;

	private ReportService() {
		reportMap = new ConcurrentHashMap<>();
	}

	public static ReportService getInstance() {
		if (null == reportService) {
			synchronized (INSTANCE_LOCK) {
				reportService = new ReportService();
			}
		}
		return reportService;
	}

	public void addData(String key, List<TradeModel> tradeModelList) {
		List<TradeModel> list = reportMap.get(key);
		TradeModel model = tradeModelList.get(0);
		String tradeType = null;
		if (null == list) {
			list = tradeModelList;
		} else {
			list.addAll(tradeModelList);
		}
		if (null != model) {
			tradeType = model.getTradeType();
		}
		if (null != tradeType && (tradeType.equalsIgnoreCase("Buy") || tradeType.equalsIgnoreCase("B"))) {
			buyStatement = "Amount in USD settled incoming ";
			buyStatement += model.getTotalSettlementAmount();
		} else if (null != tradeType && (tradeType.equalsIgnoreCase("Sell")||tradeType.equalsIgnoreCase("S"))) {
			sellStatement = "Amount in USD settled outgoing ";
			sellStatement += model.getTotalSettlementAmount();
		}

		// list.addAll(tradeModelList);
		reportMap.put(key, list);
	}

	public void doSummarryReport() {
		System.out.println("**** Summarry Report ****\n\n");
		System.out.println(buyStatement);
		System.out.println(sellStatement);

	}

	public void doTodayReport() throws Exception {
		System.out.println("\n\n****Today's summarry report****\n\n");
		System.out.println(buyStatement);
		System.out.println(sellStatement);
		List<TradeModel> sellList = reportMap.get("Sell");
		List<TradeModel> buyList = reportMap.get("Buy");

		System.out.println("***********sellList************");
		for (TradeModel tradeModel : sellList) {
			if (tradeModel.getSettlementDate().equals(Util.getTodaysDate()))
				System.out.println(tradeModel);
		}
		System.out.println("***********BuyList************");
		for (TradeModel tradeModel : buyList) {
			if (tradeModel.getSettlementDate().equals(Util.getTodaysDate()))
				System.out.println(tradeModel);
		}
	}

}
