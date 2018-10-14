package com.trade.dailytrade;

import com.trade.dailytrade.service.ProcessTrade;

public class Startup {
	public void start(String args[]) throws Exception {

		if (args.length==1) {
			if (null != args[0]) {
				ProcessTrade process = new ProcessTrade();
				if(process.process(args[0])) {
					process.processReport();
				}
			}
		}else {
			System.out.println("File name missing in command argument list");
		}

	}

	public static void main(String ar[]) {
		Startup startup = new Startup();
		try {
			startup.start(ar);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
