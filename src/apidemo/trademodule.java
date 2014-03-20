package apidemo;

import java.util.LinkedList;

import apidemo.Test.dataFrame;

public class trademodule {
	private LinkedList<dataFrame> acopy;
	public trademodule(LinkedList<dataFrame> a){this.acopy=a;};
	public void Makeacopy(LinkedList<dataFrame> a){this.acopy=a;}
	public void touch()
	{
		System.out.println(this.acopy.size());
	}
	public void model1()
	{
		
	}
	
}
