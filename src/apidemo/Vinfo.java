package apidemo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wshen
 */

public class Vinfo {
    public int id;
    public int idl;
    public double V_price;
    public long timeinmili;
    public long timeinmili2;
    public int field;
    public int vol;
    public boolean Isupdated;
    
    
    public  boolean getUpdateFlag(){
    return Isupdated;
    }
    
    public  void setUpdateFlag(boolean flag) {
        Isupdated = flag;
    }
    
    public void setvinfo(int id, double price,long time,int field)
    {
    	this.id=id;
    	this.V_price=price;
    	this.field=field;
    	this.timeinmili=time;
    }
    public void setvinfo(int vol)
    {
    	this.vol=vol;
    }
    public   void printVinfo(){
    //System.out.println(id+" "+V_price+" "+field+" "+timeinmili);
        if(Isupdated == true)
        {
            System.out.println(id+" "+V_price+" "+field+" "+timeinmili+" "+Isupdated);
            //System.out.println(Isupdated);
            Isupdated=false;
        }
        };
    
}
