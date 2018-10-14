package com.trade.dailytrade.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.trade.dailytrade.common.Constants;
import com.trade.dailytrade.common.TradeFields;
import com.trade.dailytrade.model.TradeModel;
import com.trade.dailytrade.tradeimpl.BuyTrade;
import com.trade.dailytrade.tradeimpl.SellTrade;
import com.trade.dailytrade.util.Util;

public class ProcessTrade {
	private List<TradeModel> tradeModelList;
	private final ExecutorService threadPoolService = Executors.newFixedThreadPool(5);

	public ProcessTrade() {
		initTradeObjects();
	}

	private void processInput(String fileName) throws IOException {
		File file = new File(fileName);
		BufferedReader fileReader = null;
		try {
			fileReader = new BufferedReader(new FileReader(file));
			String line = null;
			int rowCounter = 0;
			tradeModelList=new ArrayList<>();
			while ((line = fileReader.readLine()) != null) {
				if (Constants.SKIP_FIRST_ROW) {
					if (rowCounter == 0) {
						rowCounter++;
						continue;
					}
				} else {
					if (rowCounter == 0) {
						createHeaderRow(line);
						rowCounter++;
						continue;
					}
				}
				processTradeRow(line);
				rowCounter++;
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (null != fileReader) {
				try {
					fileReader.close();
				} catch (IOException ex) {
				}
			}
		}
	}

	private void notifyTrades() {
		TradeService service = TradeService.getInstance();
		service.notifyTrades(tradeModelList);
	}

	private Boolean startProcess(String fileName) throws Exception {
		Boolean status = false;
		processInput(fileName);
		notifyTrades();
		status = true;
		return status;
	}

	private void processTradeRow(String line) {
		String[] tradeData = line.split(Constants.TEXT_FIELD_SEP);
		if (null != tradeData) {
			int counter = 0;
			TradeModel model = new TradeModel();
			for (String tradeLine : tradeData) {
				createTradeRow(tradeLine, counter, model);
				counter++;
			}
			tradeModelList.add(model);
		}
	}

	private void createHeaderRow(String data) {
		// process first row, which presumably will be header details of trade file.
	}

	private void createTradeRow(String data, int counter, TradeModel model) {
		TradeFields tradeFields = TradeFields.values()[counter];
		switch (tradeFields) {
		case ENTITY:
			model.setEntityName(data.trim());
			break;
		case TRADETYPE:
			model.setTradeType(data.trim());
			break;
		case FX:
			model.setAgreedFx(new Double(data.trim()));
			break;
		case CURRENCY:
			model.setCurrency(data.trim());
			break;
		case INSTRUCTION_DATE:
			try {
				model.setInstructionDate(Util.getDate(data.trim()));
			} catch (ParseException e) {
			}
			break;
		case SETTLEMENT_DATE:
			try {
				model.setSettlementDate(Util.getDate(data.trim()));
			} catch (ParseException e) {
			}
			break;
		case UNITS:
			model.setUnits(new Integer(data.trim()));
			break;
		case PRICE:
			model.setUnitPrice(new Double(data.trim()));
			break;
		default:
			break;
		}
	}

	private void initTradeObjects() {
		new BuyTrade();
		new SellTrade();
	}
	private Future<Boolean> asyncStartProcess(final String fileName)throws Exception {
		return threadPoolService.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return startProcess(fileName);
			}
		});
	}
	private void doReport()throws Exception {
		ReportService report=ReportService.getInstance();
		report.doSummarryReport();
		report.doTodayReport();
	}
	
	public Boolean process(String fileName) throws Exception {
		Boolean status=false;
		if (Constants.PROCESS_FLOW == TradeFields.SYNC) {
			status=startProcess(fileName);
		} else if (Constants.PROCESS_FLOW == TradeFields.ASYNC) {
			Future<Boolean> process=asyncStartProcess(fileName);
			while(!process.isDone()) {
				//some extra process 
			}
			status=process.get();
		}
		return status;
	}
	public void processReport() throws Exception{
		doReport();
	}
}
