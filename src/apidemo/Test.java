package apidemo;

/* Copyright (C) 2013 Interactive Brokers LLC. All rights reserved.  This code is subject to the terms
 * and conditions of the IB API Non-Commercial License or the IB API Commercial License, as applicable. */



import static apidemo.util.Util.sleep;
import com.ib.client.CommissionReport;
import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import com.ib.client.EClientSocket;
import com.ib.client.EWrapper;
import com.ib.client.Execution;
import com.ib.client.Order;
import com.ib.client.OrderState;
import com.ib.client.TickType;
import com.ib.client.UnderComp;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class Test implements EWrapper {
        /*public class dataFrame_p{
            private int id;
            private long timestamp;
            private double price;
            public void setdataFrame(int id, long timestamp,double price)
            {
            this.id=id;
            this.timestamp=timestamp;
            this.price=price;
            }
             }
        public class dataFrame_v{
        	private int id;
            private long timestamp;
            private int vol;
            
            public void setdataFrame(int id,long time, int v)
            {
            	this.id=id;
            	this.vol=v;
            	this.timestamp=time;
            }
            
             }
     */   
	public class dataFrame{
        private int id;
        private long timestamp;
        private double p_close;
        private double p_open;
        private double p_high;
        private double p_low;
        private int vol;
        public dataFrame(){this.vol=0;};
        public void initialize(int id,double close,long time)
        {
        	this.id=id;
        	this.timestamp=time;
        	this.p_close=close;
        	this.p_open=close;
        	this.p_high=close;
        	this.p_low=close;
        	this.vol=0;
        }
        public void updatevol(int vol)
        {
        	this.vol=vol+this.vol;
        }
        
        public int getvol(){return this.vol;};
        public void updateprice(double close)
        {
        	setclose(close);
        	sethigh(close);
        	setlow(close);
        }
        public void setclose(double close){this.p_close=close;}
        public void sethigh(double close){if(this.p_high<close) this.p_high=close;}
        public void setlow(double close){if(this.p_low>close) this.p_low=close;}
        public void printall(){System.out.println(this.id+" "+this.p_close+" "+this.p_open+" "+this.p_high+" "+this.p_low+" "+this.vol);};
         }
        	
	EClientSocket m_s = new EClientSocket(this);
		static final int numbertoload=1;
		LinkedList<dataFrame>[] mp= new LinkedList[numbertoload];
	    dataFrame[] temp=new dataFrame[numbertoload];
	    boolean[] ifhit=new boolean[numbertoload];
	    static long current_time[]=new long[numbertoload];
	    static final long timestep=5000;
        static int idx=0;
	public static void main(String[] args) throws FileNotFoundException, IOException {
		new Test().run();
            
	}


	private void run() throws FileNotFoundException, IOException {
		m_s.eConnect(null, 7496, 0);
                BufferedReader reader=new BufferedReader(new FileReader("Z:\\Berich\\tickerlist.txt"));
                String line=null;
                String[] symbols = new String[100];
               
                int number_of_vechicle=0;
               
                while((line = reader.readLine()) != null)
                {symbols[number_of_vechicle]=line;
                number_of_vechicle++;
                }
                
                Contract[] test= new Contract[number_of_vechicle];//this only create pointers
                for( int i=0; i<number_of_vechicle; i++ )
                {test[i] = new Contract();
                }
                //int i=0;
                for(int i=0;i<numbertoload;i++)     
                {
               
                test[i].m_symbol=symbols[i];
                
                test[i].m_secType="STK";
                test[i].m_exchange =  "SMART";
                test[i].m_primaryExch= "ISLAND";
                test[i].m_currency="USD";
                test[i].m_localSymbol=symbols[i];
                mp[i]= new LinkedList();
        	    temp[i]=new dataFrame();
        	    ifhit[i]=false;
                 m_s.reqMktData(i+1, test[i],"", false);
                 if(i==0)
                 {
                	 current_time[i]=System.currentTimeMillis()-(System.currentTimeMillis()%1000)+timestep;
                 }
                 else
                 {
                	 current_time[i]=current_time[0]; 
                 }
                 }
               
                //m_s.reqMktData(2, test[1],"", false);
                long finish_time=current_time[0]+timestep*100;
                trademodule[] trial = new trademodule[numbertoload];
                
                {
                while(current_time[0]<finish_time)
                {
                	sleep(5000);
                	
                	{
                	for(int j=0;j<numbertoload;j++)
                	{	
                		//if(ifhit[j])
                		{
                		System.out.print(symbols[j]+' ');
                		trial[j] = new trademodule(mp[j]);
                		trial[j].touch();
                		}
                		}
                	}
                }
                }
                
                
                
                for(int i=0;i<numbertoload;i++)     
                {
                 m_s.cancelMktData(i+1);
                    }
                
                
               m_s.eDisconnect();
           
	}

	@Override public void nextValidId(int orderId) {
		
	}

	@Override public void error(Exception e) {
	}

	@Override public void error(int id, int errorCode, String errorMsg) {
	}

	@Override public void connectionClosed() {
	}

	@Override public void error(String str) {
	}

	@Override public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
            
			if (field==4)
            { 
			if(System.currentTimeMillis()>(current_time[tickerId-1]))
			{	
				current_time[tickerId-1]=current_time[tickerId-1]+timestep;
				mp[tickerId-1].add(temp[tickerId-1]);
				/*
				 if(tickerId==1)
				{
				System.out.println(current_time);
				temp[tickerId-1].printall();
				}
				*/
				temp[tickerId-1].initialize(tickerId, price, current_time[tickerId-1]);
				//ifhit[tickerId-1]=true;
			}
			else
			{
				temp[tickerId-1].updateprice(price);
				}
			
            System.out.println(tickerId+" "+price+" "+System.currentTimeMillis()+" price");
            }
            //return "id=" + tickerId + "  " + TickType.getField( field) + "=" + price + " " + 
        //((canAutoExecute != 0) ? " canAutoExecute" : " noAutoExecute");
            
	}

	@Override public void tickSize(int tickerId, int field, int size) {
	if(field==5)
	{
		
			temp[tickerId-1].updatevol(size);
			System.out.println(tickerId+" "+size+" "+System.currentTimeMillis()+" vol ");
	}
	}

	@Override public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
	}

	@Override public void tickGeneric(int tickerId, int tickType, double value) {
	}

	@Override public void tickString(int tickerId, int tickType, String value) {
	}

	@Override public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureExpiry, double dividendImpact,
			double dividendsToExpiry) {
	}

	@Override public void orderStatus(int orderId, String status, int filled, int remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
	}

	@Override public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
	}

	@Override public void openOrderEnd() {
	}

	@Override public void updateAccountValue(String key, String value, String currency, String accountName) {
	}

	@Override public void updatePortfolio(Contract contract, int position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
	}

	@Override public void updateAccountTime(String timeStamp) {
	}

	@Override public void accountDownloadEnd(String accountName) {
	}

	@Override public void contractDetails(int reqId, ContractDetails contractDetails) {
	}

	@Override public void bondContractDetails(int reqId, ContractDetails contractDetails) {
	}

	@Override public void contractDetailsEnd(int reqId) {
	}

	@Override public void execDetails(int reqId, Contract contract, Execution execution) {
	}

	@Override public void execDetailsEnd(int reqId) {
	}

	@Override public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
	}

	@Override public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
	}

	@Override public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
	}

	@Override public void managedAccounts(String accountsList) {
	}

	@Override public void receiveFA(int faDataType, String xml) {
	}

	@Override public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
	}

	@Override public void scannerParameters(String xml) {
	}

	@Override public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {
	}

	@Override public void scannerDataEnd(int reqId) {
	}

	@Override public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
	}

	@Override public void currentTime(long time) {
          
	}

	@Override public void fundamentalData(int reqId, String data) {
	}

	@Override public void deltaNeutralValidation(int reqId, UnderComp underComp) {
	}

	@Override public void tickSnapshotEnd(int reqId) {
	}

	@Override public void marketDataType(int reqId, int marketDataType) {
	}

	@Override public void commissionReport(CommissionReport commissionReport) {
	}

	@Override public void position(String account, Contract contract, int pos, double avgCost) {
	}

	@Override public void positionEnd() {
	}

	@Override public void accountSummary(int reqId, String account, String tag, String value, String currency) {
	}

	@Override public void accountSummaryEnd(int reqId) {
	}

    private void While(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
