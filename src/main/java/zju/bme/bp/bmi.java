package zju.bme.bp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class bmi extends Activity {
	private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bmi);
		
		TextView  your_bmidata = (TextView) findViewById(R.id.your_bmidata);
		mySQLiteHelper=new MySQLiteHelper(bmi.this, "notepad.db", null, 1);
		db=mySQLiteHelper.getReadableDatabase();
		Cursor cursor = db.query("weighttable", new String[]{"_id", "wt","ht","bm","tm"}, null, null, null,null,null);
		if(cursor.moveToNext()!=true){
			
	        your_bmidata.setText("");
		    cursor.close();
		}
		else {
			
			cursor.moveToLast();
			int bmindex = cursor.getColumnIndex("bm");
			String bm = cursor.getString(bmindex);		
	        your_bmidata.setText(bm);
		    cursor.close();
		}
	}
}