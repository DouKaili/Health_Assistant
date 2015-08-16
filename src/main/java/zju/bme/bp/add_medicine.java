package zju.bme.bp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import savebpdata.save_exercise_data;
import savebpdata.save_mdnote;
import savebpdata.savenote;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
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
public class add_medicine extends TabActivity {
	//private ArrayAdapter<String> adapter;
	//private Spinner Spinner1;
	private TextView latest_medicine;
	private TextView latest_mdcount;
	private TextView latest_time;
	private EditText input_count;
	private EditText text21m;
	private EditText medicine_name;
	//private MySQLiteHelper exerciseSQLiteHelper;
	//private SQLiteDatabase db;
	
	private MySQLiteHelper mySQLiteHelper;
	private SQLiteDatabase db;
	private ListView       lv;
	private TextView       tv;
	private Cursor cursor;
	private int myid;
	private String mdString;
	private String ctString;
	private EditText et01;
	private EditText et02;
	
	public Map<String, Object> item;

	private ArrayList<String> pagList  = new ArrayList<String>();;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_medicine);
		
		mySQLiteHelper=new MySQLiteHelper(add_medicine.this, "notepad.db", null, 1);
		db = mySQLiteHelper.getReadableDatabase();
		text21m = (EditText) findViewById(R.id.text21m);
		latest_medicine = (TextView) findViewById(R.id.latest_medicine);
		latest_mdcount = (TextView) findViewById(R.id.latest_mdcount);
		latest_time = (TextView) findViewById(R.id.latest_truetime);
		input_count = (EditText) findViewById(R.id.input_count);
		medicine_name = (EditText) findViewById(R.id.medicine_name);
		
		TabHost tabHost;
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("录入").setContent(R.id.text1m));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("笔记").setContent(R.id.text2m));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("日志").setContent(R.id.text3m));
	
		tabHost.setCurrentTab(0);
		
		//用SharedPreference储存note
				SharedPreferences sp1 = getSharedPreferences("mdnote",MODE_PRIVATE); 
				String notesString = sp1.getString("mdnote", "");
				text21m.setText(notesString);
		 
		cursor = db.query("medicinetable", new String[]{"_id", "md","mdcount","time"}, null, null, null,null,null);
		
		if(cursor.moveToNext()!=true){
			latest_medicine.setText("");
		    latest_time.setText("");
		    latest_mdcount.setText("");
		    //cursor.close();
		 }
		 else {
			
			cursor.moveToLast();
			int mdindex = cursor.getColumnIndex("md");
			String md = cursor.getString(mdindex);
			int mdcountindex = cursor.getColumnIndex("mdcount");
			String mdcount = cursor.getString(mdcountindex);
			int timeindex = cursor.getColumnIndex("time");
			String date = cursor.getString(timeindex);
			latest_medicine.setText(md);
		    latest_time.setText(date);
		    latest_mdcount.setText(mdcount);
		    //cursor.close();
			
		 }
		
		tv=(TextView) findViewById(R.id.TextView001m);
		lv=(ListView) findViewById(R.id.ListView01m);
		//数据库
		//db=mySQLiteHelper.getReadableDatabase();
		//cursor=db.query("medicinetable", new String[]{"_id","md","mdcount","time"}, null, null, null, null, null);
		
		if(cursor.getCount()>0){
			tv.setVisibility(View.GONE);
		}
		
		SimpleCursorAdapter sca=new SimpleCursorAdapter(add_medicine.this, R.layout.md_item, cursor, new String[]{"md","mdcount","time"}, new int[]{R.id.TextView01m,R.id.TextView02m,R.id.TextView03m});
		lv.setAdapter(sca);
	
		lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
		
				Builder builder=new Builder(add_medicine.this);
				builder.setSingleChoiceItems(new String[]{"查看","修改","删除"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					
						if(which==0&&cursor.getCount()!=0){
							int mdindex=cursor.getColumnIndex("md");
							mdString=cursor.getString(mdindex);
							int ctindex=cursor.getColumnIndex("mdcount");
							ctString=cursor.getString(ctindex);
							
				
					
							Toast.makeText(add_medicine.this, mdString+"|"+ctString+"片", Toast.LENGTH_SHORT).show();	
						}else if(which==2&&cursor.getCount()!=0){
					
							int myidindex=cursor.getColumnIndex("_id");
							myid=cursor.getInt(myidindex);
							Builder builder02=new Builder(add_medicine.this);
							LayoutInflater inflater=LayoutInflater.from(add_medicine.this);
							View view=inflater.inflate(R.layout.deleteview, null);
							builder02.setView(view);
							builder02.setPositiveButton("确定", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which) {
					
									

									db.delete("medicinetable", "_id="+myid, null);
									cursor=db.query("medicinetable", new String[]{"_id","md","mdcount","time"}, null, null, null, null, null);
					 
									SimpleCursorAdapter sca=new SimpleCursorAdapter(add_medicine.this, R.layout.md_item, cursor, new String[]{"md","mdcount","time"}, new int[]{R.id.TextView01m,R.id.TextView02m,R.id.TextView03m});
									lv.setAdapter(sca);
									}	
								});
							builder02.setNegativeButton("取消", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog, int which) {

						}
						
					});builder02.show();
						}else if(which==1&&cursor.getCount()!=0){	
							//取得数据
							int myidindex=cursor.getColumnIndex("_id");
							myid=cursor.getInt(myidindex);
							Builder builder01=new Builder(add_medicine.this);
							
							builder01.setTitle("编辑");
							
							LayoutInflater inflater=LayoutInflater.from(add_medicine.this);
							View view=inflater.inflate(R.layout.md_updatedialogview, null);
							et01=(EditText) view.findViewById(R.id.ed1m);
							et02=(EditText) view.findViewById(R.id.ed2m);
							builder01.setView(view);
							builder01.setPositiveButton("确定", new DialogInterface.OnClickListener(){

								public void onClick(DialogInterface dialog, int which) {
								
								String md=et01.getText().toString();
								String ct=et02.getText().toString();
								
								if (TextUtils.isEmpty(md)||TextUtils.isEmpty(ct)) {
									
								}else {
								ContentValues cv=new ContentValues();
								cv.put("md", md);
								cv.put("mdcount", ct);
						
								db.update("medicinetable", cv, "_id="+myid, null);
								cursor=db.query("medicinetable", new String[]{"_id","md","mdcount","time"}, null, null, null, null, null);
						 
								SimpleCursorAdapter sca=new SimpleCursorAdapter(add_medicine.this, R.layout.md_item, cursor, new String[]{"md","mdcount","time"}, new int[]{R.id.TextView01m,R.id.TextView02m,R.id.TextView03m});
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
	
	public void save_medicine(View view) {
		String medicine = medicine_name.getText().toString();
		String count = input_count.getText().toString();
		String date = DateFormat.format("yyyy-MM-dd    HH:mm:ss", new Date()).toString();
		if (TextUtils.isEmpty(count)||TextUtils.isEmpty(medicine)) {
		
			Toast.makeText(this, "输入不能为空哦", Toast.LENGTH_SHORT).show();
			
		}else {
		      
			latest_medicine.setText(medicine);
			latest_time.setText(date);
			latest_mdcount.setText(count);
				
			Toast.makeText(this, "保存数据成功啦", Toast.LENGTH_SHORT).show();
			
			ContentValues cv=new ContentValues();
			cv.put("md", medicine);
			cv.put("mdcount",count);
			cv.put("time",date);
			db.insert("medicinetable", null, cv);
			
			tv.setVisibility(View.GONE);
			cursor=db.query("medicinetable", new String[]{"_id","md","mdcount","time"}, null, null, null, null, null);
			SimpleCursorAdapter sca=new SimpleCursorAdapter(add_medicine.this, R.layout.md_item, cursor, new String[]{"md","mdcount","time"}, new int[]{R.id.TextView01m,R.id.TextView02m,R.id.TextView03m});
			lv.setAdapter(sca);
			
			}
		
	}
	
	public void mdnote(View view) {
		String noteString  = text21m.getText().toString().trim();
		
		if (TextUtils.isEmpty(noteString)) {
			Toast.makeText(this, "写点什么吧亲", Toast.LENGTH_SHORT).show();
			
		}else{
				
			save_mdnote.notedata(this, noteString);	
			Toast.makeText(this, "已经记下来了哦", Toast.LENGTH_SHORT).show();
					
			}
	
	}
	
	public void medicine_inform(View view){
		listPackages();
		Log.d("mxt", "paglist的大小：" + pagList.size());
		for (int i = 0; i < pagList.size(); i++) {
			Log.d("mxt", pagList.get(i));
		}
		
		PackageManager pm = getPackageManager();   

		 Intent i = pm.getLaunchIntentForPackage(pagList.get(0));   
            //如果该程序不可启动（像系统自带的包，有很多是没有入口的）会返回NULL   
            if (i != null) {
            	startActivity(i);  
            }  else{
            	Intent i2= new Intent(Settings.ACTION_DATE_SETTINGS);
            	startActivity(i);
            }
	}
	
	class PInfo {
		private String appname = "";
		private String pname = "";
		private String versionName = "";
		private int versionCode = 0;
		private Drawable icon;

		private void prettyPrint() {
			Log.i("taskmanger", appname + "\t" + pname + "\t" + versionName
					+ "\t" + versionCode + "\t");
		}
	}

	private void listPackages() {
		ArrayList<PInfo> apps = getInstalledApps(false); /*
														 * false = no system
														 * packages
														 */
		final int max = apps.size();
		for (int i = 0; i < max; i++) {
			apps.get(i).prettyPrint();
			item = new HashMap<String, Object>();

			int aa = apps.get(i).pname.length();
			// String
			// bb=apps.get(i).pname.substring(apps.get(i).pname.length()-11);
			// Log.d("mxt", bb);

			if (aa > 11) {
				Log.d("lxf", "进来了11");
				if (apps.get(i).pname.indexOf("clock") != -1) {
					if (!(apps.get(i).pname.indexOf("widget") != -1)) {
						try {
							PackageInfo pInfo = getPackageManager().getPackageInfo(apps.get(i).pname, 0); 
					    	   if (isSystemApp(pInfo) || isSystemUpdateApp(pInfo)) {  
					    		   Log.d("mxt","是系统自带的");
					    		   Log.d("mxt","找到了"
													+ apps.get(i).pname.substring(apps
															.get(i).pname.length() - 5)
													+ "  全名：" + apps.get(i).pname + " "
													+ apps.get(i).appname);
									item.put("pname", apps.get(i).pname);
									item.put("appname", apps.get(i).appname);
									pagList.add(apps.get(i).pname); 
					    	   }
						} catch (Exception e) {
							// TODO: handle exception
						}
						
					}
				}
			}

			/*
			 * if(apps.get(i).pname.subSequence(apps.get(i).pname.length()-11,
			 * apps.get(i).pname.length()) != null){
			 * 
			 * }
			 */
		}
	}

	private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
		ArrayList<PInfo> res = new ArrayList<PInfo>();
		List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if ((!getSysPackages) && (p.versionName == null)) {
				continue;
			}
			PInfo newInfo = new PInfo();
			newInfo.appname = p.applicationInfo.loadLabel(getPackageManager())
					.toString();
			newInfo.pname = p.packageName;
			newInfo.versionName = p.versionName;
			newInfo.versionCode = p.versionCode;
			newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
			res.add(newInfo);
		}
		return res;
	}

	public boolean isSystemApp(PackageInfo pInfo) {
		return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}

	public boolean isSystemUpdateApp(PackageInfo pInfo) {
		return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
	}
}