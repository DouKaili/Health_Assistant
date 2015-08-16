package zju.bme.bp;

import java.text.DecimalFormat;
import java.util.Date;

import android.app.TabActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class key_stats extends TabActivity{
	
	private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;
    private TextView bpavr_week,bpavr_month,bpavr_season,bpavr_halfyr,bpavr_yr,bpavr_overall;
    private TextView weightavr_week,weightavr_month,weightavr_season,weightavr_halfyr,weightavr_yr,weightavr_overall;
    private TextView bp_count,weight_count;
    private Cursor cursor_bp,cursor_weight;
    
    
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.key_stats);
		TabHost tabHost;
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("血压").setContent(R.id.tab1));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("体重").setContent(R.id.tab2));
		tabHost.setCurrentTab(0);
		
		mySQLiteHelper = new MySQLiteHelper(key_stats.this, "notepad.db", null, 1);
		db = mySQLiteHelper.getReadableDatabase();
		cursor_bp = db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm"}, null, null, null, null, null);
		cursor_weight = db.query("weighttable", new String[]{"_id","wt","ht","bm","tm"}, null, null, null, null, null);
		//指定Views
		//平均值
		bpavr_week = (TextView)findViewById(R.id.bpavr_week);
		bpavr_month = (TextView)findViewById(R.id.bpavr_month);
		bpavr_season = (TextView)findViewById(R.id.bpavr_season);
		bpavr_halfyr = (TextView)findViewById(R.id.bpavr_halfyr);
		bpavr_yr = (TextView)findViewById(R.id.bpavr_yr);
		bpavr_overall = (TextView)findViewById(R.id.bpavr_overall);
		weightavr_week = (TextView)findViewById(R.id.weightavr_week);
		weightavr_month = (TextView)findViewById(R.id.weightavr_month);
		weightavr_season = (TextView)findViewById(R.id.weightavr_season);
		weightavr_halfyr = (TextView)findViewById(R.id.weightavr_halfyr);
		weightavr_yr = (TextView)findViewById(R.id.weightavr_yr);
		weightavr_overall = (TextView)findViewById(R.id.weightavr_overall);
		//条数
		weight_count = (TextView)findViewById(R.id.weight_count);
		bp_count = (TextView)findViewById(R.id.bp_count);
		int bpcount , weightcount ;
		bpcount = cursor_bp.getCount();
		weightcount = cursor_weight.getCount();
		bp_count.setText(bpcount+"");
		weight_count.setText(weightcount+"");
		
		Log.i("overall",getAverage(14600, 1)[0]+"   "+getAverage(14600, 1)[1]+"   "+getAverage(14600, 1)[2]); 
		bpavr_week.setText(getAverage(7, 1)[0]+"   "+getAverage(7, 1)[1]+"   "+getAverage(7, 1)[2]);
		bpavr_month.setText(getAverage(30, 1)[0]+"   "+getAverage(30, 1)[1]+"   "+getAverage(30, 1)[2]);
		bpavr_season.setText(getAverage(90, 1)[0]+"   "+getAverage(90, 1)[1]+"   "+getAverage(90, 1)[2]);
		bpavr_halfyr.setText(getAverage(182, 1)[0]+"   "+getAverage(182, 1)[1]+"   "+getAverage(182, 1)[2]);
		bpavr_yr.setText(getAverage(365, 1)[0]+"   "+getAverage(365, 1)[1]+"   "+getAverage(365, 1)[2]);
		bpavr_overall.setText(getAverage(14600, 1)[0]+"   "+getAverage(14600, 1)[1]+"   "+getAverage(14600, 1)[2]);
		
		weightavr_week.setText(""+getAverage(7, 2)[0]);
		weightavr_month.setText(""+getAverage(30, 2)[0]);
		weightavr_season.setText(""+getAverage(90, 2)[0]);
		weightavr_halfyr.setText(""+getAverage(182, 2)[0]);
		weightavr_yr.setText(""+getAverage(365, 2)[0]);
		weightavr_overall.setText(""+getAverage(14600, 2)[0]);
	}
	//获取一段时间内的平均值
	//timeperiod  天数
	public String[] getAverage(long timeperiod,int type){
		Date datenow = new Date();
		Date dateprev = new Date();
		mySQLiteHelper = new MySQLiteHelper(key_stats.this, "notepad.db", null, 1);
		db = mySQLiteHelper.getReadableDatabase();
		
		datenow.setTime(System.currentTimeMillis());
		String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
	    dateprev.setTime(datenow.getTime() - timeperiod*86400000);  
	    String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
	    Log.i("log",mFrom);
	    Log.i("log",mCurrent);
	   if (type==1) {
			String sql = "select * from bptable where tm Between '" +mFrom +"' and '"+mCurrent + "'"; 
			Cursor cursor = db.rawQuery(sql,null);
			double[] avr = {0,0,0};
			String[] avrbp = {"","",""};
			int count = cursor.getCount();
			if(count==0){
				avrbp[0] = " N/A  ";
				avrbp[1] = "N/A  ";
				avrbp[2] = "N/A";
				return avrbp;
			}
			else {
			   cursor.moveToFirst();
			   do {
				avr[0] = avr[0]+Double.parseDouble(cursor.getString(cursor.getColumnIndex("hbp")));
				avr[1] = avr[1]+Double.parseDouble(cursor.getString(cursor.getColumnIndex("lbp")));
				avr[2] = avr[2]+Double.parseDouble(cursor.getString(cursor.getColumnIndex("rt")));
			} while (cursor.moveToNext());
			   avr[0]=avr[0]/count;
			   avr[1]=avr[1]/count;
			   avr[2]=avr[2]/count;
			}
			DecimalFormat df = new DecimalFormat("#.#");
			avrbp[0] = df.format(avr[0]);
			avrbp[1] = df.format(avr[1]);
			avrbp[2] = df.format(avr[2]);
			return avrbp;
		} else if (type==2){
			String sql = "select * from weighttable where tm Between '" +mFrom +"' and '"+mCurrent + "'"; 
			Cursor cursor = db.rawQuery(sql, null);
			double[] avr = {0};
			String[] avrweight = {""};
			int count = cursor.getCount();
			if(count==0){
				avrweight[0] = "N/A";
				return avrweight;
			}
			else {
			   cursor.moveToFirst();
			   do {
				avr[0] = avr[0]+Double.parseDouble(cursor.getString(cursor.getColumnIndex("wt")));
			} while (cursor.moveToNext());
			   avr[0]=avr[0]/count;
			}
			DecimalFormat df = new DecimalFormat("#.##");
			avrweight[0] = df.format(avr[0]);
			return avrweight;
		}else {
			String[] avr = {""};
			return  avr;
	   }
	    
	}
	
}
