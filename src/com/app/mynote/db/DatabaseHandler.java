package com.app.mynote.db;

import java.util.ArrayList;
import java.util.List;

import com.app.mynote.util.MyNote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper
{
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "MyNoteStore";
 
    // Contacts table name
    private static final String TABLE_NOTES = "apptable";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MONTH = "month";
    private static final String KEY_DATE = "date";
    private static final String KEY_HOME = "home";
    private static final String KEY_WORK = "work";
    private static final String KEY_READONLY = "readonly";
 
    public DatabaseHandler(Context context) 
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) 
    {
        String CREATE_MYNOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ KEY_MONTH + " INTEGER,"
        		+ KEY_DATE + " TEXT," 
        		+ KEY_HOME + " TEXT,"
        		+ KEY_WORK + " TEXT,"
                + KEY_READONLY + " INTEGER" + ")";
        db.execSQL(CREATE_MYNOTES_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
 
        // Create tables again
        onCreate(db);
    }
    
    // Adding new Note
    public void addContact(MyNote myNote) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_MONTH, myNote.getMonth());
        values.put(KEY_DATE, myNote.getDate());
        values.put(KEY_HOME, myNote.getHome());
        values.put(KEY_WORK, myNote.getWork());
        values.put(KEY_READONLY, myNote.getReadOnly());
 
        // Inserting Row
        db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single Note
    public MyNote getMyNotes(String date)
    {
    	int id = 0, m = 0, ro = 0;
    	String h = " ", w = " ";
    	
    	String data = " ";
    	MyNote myNotes;
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_NOTES, new String[] { KEY_ID,
                KEY_MONTH, KEY_DATE, KEY_HOME, KEY_WORK, KEY_READONLY }, KEY_DATE + "=?",
                new String[] { date }, null, null, null, null);
        
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
            	id = Integer.parseInt(cursor.getString(0));
            	m = Integer.parseInt(cursor.getString(1));
            	data = cursor.getString(2);
            	h = cursor.getString(3);
            	w = cursor.getString(4);
            	ro = Integer.parseInt(cursor.getString(5));
            } 
        }
        
        if(data.equals(" ") || data == null)
        {
        	myNotes = null;
        }
        else
        {        	
        	myNotes = new MyNote(id, m, data, h, w, ro);
        }
        
        return myNotes;
    }
     
    // Getting All Notes
    public List<MyNote> getAllNotes() 
    {
        List<MyNote> myNotesList = new ArrayList<MyNote>();
        
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) 
        {
            do 
            {
            	MyNote myNote = new MyNote();
            	
            	myNote.setID(Integer.parseInt(cursor.getString(0)));
            	myNote.setMonth(Integer.parseInt(cursor.getString(1)));
            	myNote.setDate(cursor.getString(2));
            	myNote.setHome(cursor.getString(3));
            	myNote.setWork(cursor.getString(4));
            	myNote.setReadOnly(Integer.parseInt(cursor.getString(5)));
                
                myNotesList.add(myNote);
                
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return myNotesList;
    }
 
    // Updating single Note
    public int updateNote(MyNote myNote) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_MONTH, myNote.getMonth());
        values.put(KEY_DATE, myNote.getDate());
        values.put(KEY_HOME, myNote.getHome());
        values.put(KEY_WORK, myNote.getWork());
        values.put(KEY_READONLY, myNote.getReadOnly());
 
        // updating row
        return db.update(TABLE_NOTES, values, KEY_ID + " = ?", new String[] { String.valueOf(myNote.getID()) });
    }
 
    // Deleting single Note
    public void deleteNote(MyNote myNote) 
    {
        SQLiteDatabase db = this.getWritableDatabase();
        
        db.delete(TABLE_NOTES, KEY_ID + " = ?", new String[] { String.valueOf(myNote.getID()) });
        db.close();
    }
 
    // Getting Notes Count
    public int getNoteCount() 
    {
    	int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_NOTES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        
        if (cursor != null && cursor.getCount() > 0)
        {
            count = cursor.getCount();
        }
        
        cursor.close();
 
        // return count
        return count;
    }
}