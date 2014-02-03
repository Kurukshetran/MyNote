package com.app.mynote;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow;

import com.app.mynote.adapter.ListViewAdapter;
import com.app.mynote.db.DatabaseHandler;
import com.app.mynote.util.Constants;
import com.app.mynote.util.MyNote;
import com.app.mynote.util.PopupHelper;

public class MainActivity extends Activity 
{	
	public static ListView list;
	public static ListViewAdapter adapter;
	
	public static int image;
    public static int counter;
    public static int curr_month;
    public static int selected_day;
 
    public static String[] datelist;
	public static String dateString;
	public static String selectedItem;
	public static String curr_dateString;
    
    public static TextView Month_Year, Selected_Day;
    public static EditText home, work;
    
    public static MyNote note;
    public static Context context;    
    public static DatabaseHandler db;
    public static Calendar cal;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.example);
        
        db = new DatabaseHandler(this);
        
        generateDateList();
 
        context = this;
        
        image = R.drawable.open;
        
        Month_Year = (TextView) findViewById(R.id.month_year);
        Selected_Day = (TextView) findViewById(R.id.currentDate);
        
        home = (EditText) findViewById(R.id.editHome);
        work = (EditText) findViewById(R.id.editWork);
 
        cal = Calendar.getInstance();
        curr_month = cal.get(Calendar.MONTH);
        Month_Year.setText(Constants.month[curr_month]+ " " +Constants.year);
        
        counter = cal.get(Calendar.DAY_OF_MONTH) - 1;        
    
        //Gets the value of first highlighted value and set day for it
        String day = getDayOfWeek(datelist[cal.get(Calendar.DAY_OF_MONTH) - 1]);
    	
        Selected_Day.setText(day);
        
        setHomeWork(datelist[cal.get(Calendar.DAY_OF_MONTH) - 1]);
        
        disableEditBox();
        
        //Listview
        list = (ListView) findViewById(R.id.listview);
 
        adapter = new ListViewAdapter(this, datelist, image);

        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
            {            	
            	counter = position;            	
            	
            	TextView textview1 = (TextView) view.findViewById(R.id.date);
            	
            	selectedItem = textview1.getText().toString();            	
            	
            	String day = getDayOfWeek(selectedItem);
            	
            	disableEditBox();
            	
            	Selected_Day.setText(day);
            	
            	setHomeWork(selectedItem);
            } 
        });
    }
    
    public static void generateDateList()
    {
    	int inc;
    	
    	Calendar cale = Calendar.getInstance();
		
		int cur_month = cale.get(Calendar.MONTH);
		
		datelist = new String[Constants.num_of_days[cur_month]];
		
		for(int i = 0; i < Constants.num_of_days[cur_month]; i++)
		{
			inc = i + 1;
			
			if(inc <= 9)
			{
				dateString = "  "+ inc + " " + Constants.month[cur_month].substring(0, 3) + " " + Constants.year;
				datelist[i] = dateString.toString();
			}
			else
			{
				dateString = inc + " " + Constants.month[cur_month].substring(0, 3) + " " + Constants.year;
				datelist[i] = dateString.toString();
			}
		}
    }
    
    //Disable EditText below Current date 
    public static void disableEditBox()
    {    	
    	Calendar cale = Calendar.getInstance();
		
    	int cur_day = cale.get(Calendar.DATE);
		
		if(cur_day <= selected_day)
		{
			home.setEnabled(true);
			work.setEnabled(true);
		}
		else
		{
			home.setEnabled(false);
			work.setEnabled(false);
		}
    }
    
    public static String getDayOfWeek(String date)
    {
    	String[] dd = date.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
		
    	int d = 0;
    	String m = "";
    	int y = 0;
    	int j = 0;

    	if(dd.length == 3)
    	{
	    	d = Integer.parseInt(dd[0]);
	    	m = dd[1].trim();
	    	y = Integer.parseInt(dd[2].trim());
    	}
    	else
    	{
    		d = Integer.parseInt(dd[1]);
	    	m = dd[2].trim();
	    	y = Integer.parseInt(dd[3].trim());
    	}
    	
    	selected_day = d;

    	for(int i = 0; i < Constants.month.length; i++)
    	{
    		if(m.equals(Constants.month[i].substring(0, 3)))
    		{
    			j = i;
    		}
    	}

    	Date dat= (new GregorianCalendar(y, j, d)).getTime();
    	SimpleDateFormat f = new SimpleDateFormat("EEEE");
    	String day = f.format(dat);

    	return day;
    }
    
    public static void setHomeWork(String selectedItem)
    {
    	//Fetch Home and Work values from DB and set in EditText
    	int c = db.getNoteCount();
    	
    	if(c > 0)
    	{
    		note = db.getMyNotes(selectedItem);
        	
        	if(note != null)
        	{
        		home.setText(note.getHome());
        		work.setText(note.getWork());
        		
        		if(note.getReadOnly() == 0) // 1
        		{
        			home.setEnabled(false);
        			work.setEnabled(false);
        		}
        	}
        	else
        	{
        		Calendar cale = Calendar.getInstance();
        		
            	int cur_day = cale.get(Calendar.DATE);
        		
        		if(cur_day <= selected_day)
        		{
        			home.setText(" ");
            		work.setText(" ");
        			home.setEnabled(true);
        			work.setEnabled(true);
        		}
        		else
        		{
        			home.setText(" ");
            		work.setText(" ");
        			home.setEnabled(false);
        			work.setEnabled(false);
        		}
        	}
    	}
    }
    
    public static void onCancelBtnClicked(View v)
    {
    	if(v.getId() == R.id.cancel_btn)
    	{
    		if(selectedItem == null)
    		{
    			setHomeWork(datelist[cal.get(Calendar.DAY_OF_MONTH) - 1]);
    		}
    		else
    		{
    			setHomeWork(selectedItem);
    		}    		
    	}
    }
    
    public void onCloneBtnClicked(View v)
    {
    	if(v.getId() == R.id.clone_btn)
    	{
    		PopupWindow window = PopupHelper.newBasicPopupWindow(context);
    		
    		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		
    		View popupView = inflater.inflate(R.layout.popup, null);
    		
    		window.setContentView(popupView);
    		int totalHeight = getWindowManager().getDefaultDisplay().getHeight();
    		int[] location = new int[2];
    		v.getLocationOnScreen(location);
    		
    		if (location[1] < (totalHeight / 2.0)) 
    		{
    			// top half of screen
    			window.setAnimationStyle(R.style.Animations_PopDownMenuRight);
    			window.showAsDropDown(v);
    		} 
    		else 
    		{ 
    			// bottom half
    			PopupHelper.showLikeQuickAction(window, popupView, v, getWindowManager(), 0, 0);
    		}
    	}
    }
    
    public static void onDeleteBtnClicked(View v)
    {
    	if(v.getId() == R.id.delete_btn)
    	{
    		if(selectedItem == null)
    		{
    			Toast.makeText(context, "Delete"+" for "+datelist[cal.get(Calendar.DAY_OF_MONTH) - 1], Toast.LENGTH_SHORT).show();
    		}
    		else
    		{
    			Toast.makeText(context, "Delete"+" for "+selectedItem, Toast.LENGTH_SHORT).show();
    		}
    	}
    }
    
    public static void onSaveBtnClicked(View v)
    {
    	if(v.getId() == R.id.save_btn)
    	{
    		if(selectedItem == null)
    		{
    			note = db.getMyNotes(datelist[cal.get(Calendar.DAY_OF_MONTH) - 1]);
            	
            	if(note == null)
            	{
            		if(home.isEnabled() || work.isEnabled())
    				{
		    			if(home.getText().toString().matches("") || work.getText().toString().matches(""))
		    			{
		    				Toast.makeText(context, "Home and Work field Empty", Toast.LENGTH_SHORT).show();    				
		    			}
		    			else
		    			{	    				
		    				insertNotesToDB(curr_month, datelist[cal.get(Calendar.DAY_OF_MONTH) - 1], home.getText().toString(), work.getText().toString(), 0);
		    				
		    				Toast.makeText(context, "Saved Sucessfully - datelist[0]", Toast.LENGTH_SHORT).show();
		    			}
    				}
            	}
            	else
            	{
            		Toast.makeText(context, "Already Saved", Toast.LENGTH_SHORT).show();
            	}
    		}
    		else
    		{
    			note = db.getMyNotes(selectedItem);
            	
            	if(note == null)
            	{
            		if(home.isEnabled() || work.isEnabled())
    				{
            			if(home.getText().toString().matches("") || work.getText().toString().matches(""))
		    			{
		    				Toast.makeText(context, "Home and Work field Empty", Toast.LENGTH_SHORT).show();    				
		    			}
		    			else
		    			{
		    				insertNotesToDB(curr_month, selectedItem, home.getText().toString(), work.getText().toString(), 0);
		    				
		    				Toast.makeText(context, "Saved Sucessfully - "+selectedItem, Toast.LENGTH_SHORT).show();
		    			}
    				}
            	}
            	else
            	{
            		Toast.makeText(context, "Already Saved", Toast.LENGTH_SHORT).show();
            	}
    		}
    	}
    }
    
    public static void insertNotesToDB(int month, String date, String home, String work, int readonly)
    {
        Log.d("Insert: ", "Inserting ..");
        db.addContact(new MyNote(month, date, home, work, readonly));
    }
}