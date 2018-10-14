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

public class BuyTrade implements TradeListener {
	private static Double settlementAmountForDay = new Double(0.0);

	public BuyTrade() {
		TradeService tradeService = TradeService.getInstance();
		tradeService.registerTrade(this);
	}

	@Override
	public void updateTrade(List<TradeModel> modelList) {
		List<TradeModel> buyList = filterBuyTrade(modelList);
		if (!buyList.isEmpty()) {
			sortTrades(buyList);
			publishList(buyList);
		}
	}

	private void sortTrades(List<TradeModel> buyList) {
		Collections.sort(buyList, new Comparator<TradeModel>() {
			@Override
			public int compare(TradeModel model1, TradeModel model2) {
				return model1.getTradeAmount().compareTo(model2.getTradeAmount());
			}
		});
		TradeModel model = buyList.get(0);
		model.setTotalSettlementAmount(settlementAmountForDay);
	}

	private List<TradeModel> filterBuyTrade(List<TradeModel> modelList) {
		List<TradeModel> buyList = new ArrayList<>();
		for (TradeModel tradeModel : modelList) {
			String tradeType = tradeModel.getTradeType();
			if (null != tradeType && (tradeType.equalsIgnoreCase("Buy") || tradeType.equalsIgnoreCase("B"))) {
				TradeModel buyModel = new TradeModel();
				buyModel.setTradeType(tradeType);
				buyModel.setEntityName(tradeModel.getEntityName());
				buyModel.setAgreedFx(tradeModel.getAgreedFx());
				buyModel.setCurrency(tradeModel.getCurrency());
				buyModel.setEntityName(tradeModel.getEntityName());
				buyModel.setInstructionDate(tradeModel.getInstructionDate());
				buyModel.setSettlementDate(tradeModel.getSettlementDate());
				buyModel.setUnitPrice(tradeModel.getUnitPrice());
				buyModel.setUnits(tradeModel.getUnits());
				calculateTradeAmount(buyModel);
				setSettlementDate(buyModel);
				buyList.add(buyModel);
			}
		}
		return buyList;
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
		reportService.addData("Buy", tradeModelList);
	}

}
