package com.trade.dailytrade.tradeimpl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.trade.dailytrade.model.TradeModel;
import com.trade.dailytrade.service.ReportService;
import com.trade.dailytrade.service.TradeService;
import com.trade.dailytrade.tradedef.TradeListener;
import com.trade.dailytrade.util.Util;

public class SellTrade implements TradeListener {
	private static Double settlementAmountForDay = new Double(0.0);

	public SellTrade() {
		TradeService tradeService = TradeService.getInstance();
		tradeService.registerTrade(this);
	}

	@Override
	public void updateTrade(List<TradeModel> modelList) {
		List<TradeModel> sellList = filterSellTrade(modelList);
		if (!sellList.isEmpty()) {
			sortTrades(sellList);
			publishList(sellList);
		}
	}

	private void sortTrades(List<TradeModel> sellList) {
		Collections.sort(sellList, new Comparator<TradeModel>() {

			@Override
			public int compare(TradeModel model1, TradeModel model2) {
				return model1.getTradeAmount().compareTo(model2.getTradeAmount());
			}

		});
		TradeModel model = sellList.get(0);
		model.setTotalSettlementAmount(settlementAmountForDay);
	}

	private List<TradeModel> filterSellTrade(List<TradeModel> modelList) {
		List<TradeModel> sellList = new ArrayList<>();
		for (TradeModel tradeModel : modelList) {
			String tradeType = tradeModel.getTradeType();
			if (null != tradeType && (tradeType.equalsIgnoreCase("Sell") || tradeType.equalsIgnoreCase("S"))) {
				TradeModel sellModel = new TradeModel();
				sellModel.setTradeType(tradeType);
				sellModel.setAgreedFx(tradeModel.getAgreedFx());
				sellModel.setCurrency(tradeModel.getCurrency());
				sellModel.setEntityName(tradeModel.getEntityName());
				sellModel.setInstructionDate(tradeModel.getInstructionDate());
				sellModel.setSettlementDate(tradeModel.getSettlementDate());
				sellModel.setUnitPrice(tradeModel.getUnitPrice());
				sellModel.setUnits(tradeModel.getUnits());
				calculateTradeAmount(sellModel);
				setSettlementDate(sellModel);
				sellList.add(sellModel);
			}
		}
		return sellList;
	}

	private void calculateTradeAmount(TradeModel model) {
		Double amt = new Double(model.getUnitPrice() * model.getUnits() * model.getAgreedFx());
		model.setTradeAmount(amt);
		if (Util.isTodaySettlementDay(model.getSettlementDate()))
			totalSettlementAmt(amt);
	}

	public static void totalSettlementAmt(Double amt) {
		settlementAmountForDay += amt;
	}

	private void setSettlementDate(TradeModel model) {
		Date date = Util.getSettlementDate(model.getCurrency(), model.getSettlementDate());
		try {
			model.setSettlementDate(Util.getDate(Util.getDate(date)));
		} catch (ParseException ex) {
		}
	}

	private void publishList(List<TradeModel> tradeModelList) {
		ReportService reportService = ReportService.getInstance();
		reportService.addData("Sell", tradeModelList);
	}
}
