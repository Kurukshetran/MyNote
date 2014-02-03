package com.app.mynote.util;

public class MyNote 
{
    int _id;
    int month;
    String date;
    String home;
    String work;
    int readonly;
     
    public MyNote()
    {
         
    }
    
    public MyNote(int id, int month, String date, String home, String work, int readonly)
    {
        this._id = id;
        this.month = month;
        this.date = date;
        this.home = home;
        this.work = work;
        this.readonly = readonly;
    }
     
    public MyNote(int month, String date, String home, String work, int readonly)
    {
    	this.month = month;
        this.date = date;
        this.home = home;
        this.work = work;
        this.readonly = readonly;
    }
    
    public int getID()
    {
        return this._id;
    }
     
    public void setID(int id)
    {
        this._id = id;
    }
     
    public int getMonth()
    {
        return this.month;
    }
     
    public void setMonth(int month)
    {
        this.month = month;
    }
     
    public String getDate()
    {
        return this.date;
    }
     
    public void setDate(String date)
    {
        this.date = date;
    }
    
    public String getHome()
    {
        return this.home;
    }
     
    public void setHome(String home)
    {
        this.home = home;
    }
    
    public String getWork()
    {
        return this.work;
    }
     
    public void setWork(String work)
    {
        this.work = work;
    }
    
    public int getReadOnly()
    {
        return this.readonly;
    }
     
    public void setReadOnly(int readonly)
    {
        this.readonly = readonly;
    }
}