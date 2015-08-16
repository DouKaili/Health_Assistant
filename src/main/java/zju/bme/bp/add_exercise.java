package zju.bme.bp;

import java.util.Date;

import savebpdata.save_exercise_data;
import savebpdata.savenote;
import android.R.string;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class add_exercise extends TabActivity {
	private ArrayAdapter<String> adapter;
	private Spinner Spinner1;
	private TextView latest_exercise;
	private TextView llatest_exercise;
	private TextView latest_extime;
	private TextView latest_time;
	private TextView latest_cal;
	private EditText input_time;
	private EditText text21e;
	//private MySQLiteHelper exerciseSQLiteHelper;
	private SQLiteDatabase db;
	
	private MySQLiteHelper exSQLiteHelper;
	//private SQLiteDatabase db;
	private ListView   lv;
	private TextView   tv;
	private Cursor cursor;
	private int myid;
	private String hbpString;
	private String lbpString;
	private EditText et02;
    private EditText et01;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_exercise);
		
		exSQLiteHelper=new MySQLiteHelper(add_exercise.this, "notepad.db", null, 1);
		db = exSQLiteHelper.getReadableDatabase();
		text21e = (EditText) findViewById(R.id.text21e);
		latest_exercise = (TextView) findViewById(R.id.latest_exercise);
		llatest_exercise = (TextView) findViewById(R.id.llatest_exercise);
		latest_extime = (TextView) findViewById(R.id.latest_extime);
		latest_time = (TextView) findViewById(R.id.latest_time);
		input_time = (EditText) findViewById(R.id.input_time);
		latest_cal = (TextView) findViewById(R.id.latest_cal);
		
		TabHost tabHost;
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("录入").setContent(R.id.text1e));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("笔记").setContent(R.id.text2e));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("日志").setContent(R.id.text3e));
	
		tabHost.setCurrentTab(0);
		final String[] from = {"篮球","足球","乒乓球","羽毛球","网球","游泳","跑步","步行","高尔夫","滑冰","滑雪","骑车"};
		Spinner1 = (Spinner) findViewById(R.id.Spinner1);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,from);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner1.setAdapter(adapter);
		Spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
	        @Override
	        public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
	            llatest_exercise.setText(from[arg2]);
	            }

	        @Override
	        public void onNothingSelected(AdapterView<?> arg0) {
	        }
	    });
		
		//用SharedPreference储存note
				SharedPreferences sp1 = getSharedPreferences("exnote",MODE_PRIVATE); 
				String notesString = sp1.getString("exnote", "");
				text21e.setText(notesString);
		 
		 cursor = db.query("exercisetable", new String[]{"_id", "ex","extime","time","cal"}, null, null, null,null,null);
		
		if(cursor.moveToNext()!=true){
			latest_extime.setText("");
		    latest_time.setText("");
		    latest_exercise.setText("");
		    latest_cal.setText("");
		    //cursor.close();
		 }
		 else {
			
			cursor.moveToLast();
			int exindex = cursor.getColumnIndex("ex");
			String ex = cursor.getString(exindex);
			int extimeindex = cursor.getColumnIndex("extime");
			String extime = cursor.getString(extimeindex);
			int timeindex = cursor.getColumnIndex("time");
			String date = cursor.getString(timeindex);
			int calindex = cursor.getColumnIndex("cal");
			String cal = cursor.getString(calindex);
			latest_extime.setText(extime);
		    latest_time.setText(date);
		    latest_exercise.setText(ex);
		    latest_cal.setText(cal);
		    //cursor.close();
			
		 }
		
		//exSQLiteHelper=new MySQLiteHelper(add_exercise.this, "notepad.db", null, 1);
		tv=(TextView) findViewById(R.id.TextView001e);
		lv=(ListView) findViewById(R.id.ListView01e);
		//数据库
		 db=exSQLiteHelper.getReadableDatabase();
		 //cursor=db.query("exercisetable", new String[]{"_id","ex","extime","time"}, null, null, null, null, null);
		if(cursor.getCount()>0){
		tv.setVisibility(View.GONE);
		}
		SimpleCursorAdapter sca=new SimpleCursorAdapter(add_exercise.this, R.layout.exercise_item, cursor, new String[]{"ex","extime","cal","time"}, new int[]{R.id.TextView01e,R.id.TextView02e,R.id.TextView03e,R.id.TextView04e});
		lv.setAdapter(sca);
		
		lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int which,
					long arg3) {
				
				Builder builder=new Builder(add_exercise.this);
				builder.setSingleChoiceItems(new String[]{"查看","修改","删除"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
						
					if(which==0&&cursor.getCount()!=0){
						
						
						int hbpindex=cursor.getColumnIndex("ex");
						hbpString=cursor.getString(hbpindex);
						int lbpindex=cursor.getColumnIndex("extime");
						lbpString=cursor.getString(lbpindex);
						
						
						Toast.makeText(add_exercise.this, hbpString+"|"+lbpString, Toast.LENGTH_SHORT).show();	
					}else if(which==2&&cursor.getCount()!=0){
						int myidindex=cursor.getColumnIndex("_id");
						myid=cursor.getInt(myidindex);
						Builder builder02=new Builder(add_exercise.this);
						LayoutInflater inflater=LayoutInflater.from(add_exercise.this);
						View view=inflater.inflate(R.layout.deleteview, null);
						builder02.setView(view);
						builder02.setPositiveButton("确定", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int which) {
						
						

						db.delete("exercisetable", "_id="+myid, null);
						cursor=db.query("exercisetable", new String[]{"_id","ex","extime","time","cal"}, null, null, null, null, null);
						 
						SimpleCursorAdapter sca=new SimpleCursorAdapter(add_exercise.this, R.layout.exercise_item, cursor, new String[]{"ex","extime","cal","time"}, new int[]{R.id.TextView01e,R.id.TextView02e,R.id.TextView03e,R.id.TextView04e});
						lv.setAdapter(sca);}	
							});
						builder02.setNegativeButton("取消", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog, int which) {

								
							}
							
						});builder02.show();
					}else if(which==1&&cursor.getCount()!=0){	
						
						int idindex=cursor.getColumnIndex("_id");
					      myid=cursor.getInt(idindex);
						
						Builder builder01=new Builder(add_exercise.this);
						
						builder01.setTitle("编辑");
						
						LayoutInflater inflater=LayoutInflater.from(add_exercise.this);
						View view=inflater.inflate(R.layout.exercise_updatedialogview, null);
						
						et01=(EditText) view.findViewById(R.id.ed1e);
						et02=(EditText) view.findViewById(R.id.ed2e);
						
						builder01.setView(view);
						builder01.setPositiveButton("确定", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog, int which) {
							
								
								
							String newhbp=et01.getText().toString();
							String newlbp=et02.getText().toString();
							double change_cal=0;
							double new_time = Double.parseDouble(newlbp);
							if (TextUtils.isEmpty(newhbp)||TextUtils.isEmpty(newlbp)) {
								
							}else {
							
							
							ContentValues cv=new ContentValues();
							cv.put("ex", newhbp);
							cv.put("extime", newlbp);
							
							if(newhbp.equals("篮球")) change_cal=new_time*550/60;
							else if(newhbp.equals("足球")) change_cal=new_time*750/60;
							else if(newhbp.equals("乒乓球")) change_cal=new_time*320/60;
							else if(newhbp.equals("羽毛球")) change_cal=new_time*306/60;
							else if(newhbp.equals("网球")) change_cal=new_time*352/60;
							else if(newhbp.equals("游泳")) change_cal=new_time*1036/60;
							else if(newhbp.equals("跑步")) change_cal=new_time*700/60;
							else if(newhbp.equals("步行")) change_cal=new_time*255/60;
							else if(newhbp.equals("高尔夫")) change_cal=new_time*186/60;
							else if(newhbp.equals("滑冰")) change_cal=new_time*480/60;
							else if(newhbp.equals("滑雪")) change_cal=new_time*354/60;
							else if(newhbp.equals("骑车")) change_cal=new_time*184/60;
							String change_cal_string = change_cal+"";
							cv.put("cal", change_cal_string);
							db.update("exercisetable", cv, "_id="+myid, null);
							cursor=db.query("exercisetable", new String[]{"_id","ex","extime","time","cal"}, null, null, null, null, null);
							 
							SimpleCursorAdapter sca=new SimpleCursorAdapter(add_exercise.this, R.layout.exercise_item, cursor, new String[]{"ex","extime","cal","time"}, new int[]{R.id.TextView01e,R.id.TextView02e,R.id.TextView03e,R.id.TextView04e});
							lv.setAdapter(sca);
							  }
							
							}
							
						});
						builder01.setNegativeButton("取消", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog, int which) {

								
							}
							
						});
						builder01.show();
					}

					}
					
				});
				builder.show();
			}
			
		});
		 
	}
	
	public void save_exercise(View view) {
		String extime = input_time.getText().toString();
		String ex = llatest_exercise.getText().toString();
		String date = DateFormat.format("yyyy-MM-dd    HH:mm:ss", new Date()).toString();
		double ex_time = Double.parseDouble(extime);
		double cal = 0;
		if (TextUtils.isEmpty(extime)) {
		
			Toast.makeText(this, "时间不能为空哦", Toast.LENGTH_SHORT).show();
			
		}else {
			
			if(ex.equals("篮球")) cal=ex_time*550/60;
			else if(ex.equals("足球")) cal=ex_time*750/60;
			else if(ex.equals("乒乓球")) cal=ex_time*320/60;
			else if(ex.equals("羽毛球")) cal=ex_time*306/60;
			else if(ex.equals("网球")) cal=ex_time*352/60;
			else if(ex.equals("游泳")) cal=ex_time*1036/60;
			else if(ex.equals("跑步")) cal=ex_time*700/60;
			else if(ex.equals("步行")) cal=ex_time*255/60;
			else if(ex.equals("高尔夫")) cal=ex_time*186/60;
			else if(ex.equals("滑冰")) cal=ex_time*480/60;
			else if(ex.equals("滑雪")) cal=ex_time*354/60;
			else if(ex.equals("骑车")) cal=ex_time*184/60; 
			String calString = cal+"";
			latest_extime.setText(extime);
			latest_time.setText(date);
			latest_exercise.setText(ex);
			latest_cal.setText(calString);
				
			Toast.makeText(this, "保存数据成功啦", Toast.LENGTH_SHORT).show();
			
			ContentValues cv=new ContentValues();
			cv.put("ex", ex);
			cv.put("extime",extime);
			cv.put("time",date);
			cv.put("cal",calString);
			db.insert("exercisetable", null, cv);
			tv.setVisibility(View.GONE);
			cursor=db.query("exercisetable", new String[]{"_id","ex","extime","time","cal"}, null, null, null, null, null);
			SimpleCursorAdapter sca=new SimpleCursorAdapter(add_exercise.this, R.layout.exercise_item, cursor, new String[]{"ex","extime","cal","time"}, new int[]{R.id.TextView01e,R.id.TextView02e,R.id.TextView03e,R.id.TextView04e});
			lv.setAdapter(sca);
			
			}
		
	}
	
	public void exnote(View view) {
		String noteString  = text21e.getText().toString().trim();
		
		if (TextUtils.isEmpty(noteString)) {
			Toast.makeText(this, "写点什么吧亲", Toast.LENGTH_SHORT).show();
			
		}else{
				
			save_exercise_data.notedata(this, noteString);	
			Toast.makeText(this, "已经记下来了哦", Toast.LENGTH_SHORT).show();
					
			}
	
	}
	public void to_cal_line(View view){
		Intent to_cal_line = new Intent(add_exercise.this,cal_linechart.class); 
		startActivity(to_cal_line); 
	}
}