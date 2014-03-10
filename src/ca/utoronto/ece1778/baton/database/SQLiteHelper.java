package ca.utoronto.ece1778.baton.database;


import com.baton.publiclib.model.classmanage.ClassLesson;
import com.baton.publiclib.model.ticketmanage.Ticket;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SQLiteHelper extends SQLiteOpenHelper {
	public static final String TB_ticket="ticket";
	public static final String TID="tid";
	
	
	public SQLiteHelper(Context context,String name,CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		//´´½¨±ítbl_task
//		Object[] bindArgsTask = { TB_Task,ID,Stage_ID,Str_Title,Str_Content,Long_AlarmTime,Int_TaskType,Int_DelayTimes,Int_TaskStatus};
		String strCreatParam="CREATE TABLE IF NOT EXISTS "+TB_ticket+" ("+TID+" INTEGER PRIMARY KEY, "+Ticket.UID_DB_STR+" INTEGER, "+Ticket.TICKETTYPE_DB_STR+" VARCHAR, "+Ticket.TICKETCONTENT_DB_STR+" VARCHAR, "+Ticket.TIMESTAMP_DB_STR+" VARCHAR, "+ClassLesson.LESSONID_DB_STR+" INTEGER, "+Ticket.TICKETSTATUS_DB_STR+" VARCHAR)";
		arg0.execSQL(strCreatParam);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP TABLE IF EXISTS "+TB_ticket);
		onCreate(arg0);
	}

}
