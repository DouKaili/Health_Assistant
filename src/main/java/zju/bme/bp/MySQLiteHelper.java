package zju.bme.bp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public MySQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
		db.execSQL("create table bptable(_id integer primary key autoincrement,hbp text,lbp text,rt text,tm text,cond text);");
		db.execSQL("create table weighttable(_id integer primary key autoincrement,wt text,ht text,bm text,tm text,cond text);");
		db.execSQL("create table exercisetable(_id integer primary key autoincrement,ex text,extime text,time text,cal text);");
		db.execSQL("create table medicinetable(_id integer primary key autoincrement,md text,mdcount text,time text);");
		
	}

	
	@Override
	public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion) {
		
	}

}
