package zju.bme.bp;



import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import savebpdata.savenote;


@SuppressWarnings("deprecation")
public class add_bp  extends TabActivity{
	
	private EditText bp_high;
    private EditText bp_low;
    private EditText rate;
    private EditText text21;
	private TextView latest_bphigh;
    private TextView latest_bplow;
    private TextView latest_rate;
    private TextView latest_bptime;
    private TextView latest_cond;
    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;
    
   
	private ListView    lv;
	private TextView    tv;
	private TextView bp_cond;
	private Cursor cursor;
	private int myid;
	private String hbpString;
	private String lbpString;
	private String rtsString;
	private EditText et01;
	private EditText et02;
	private EditText et03;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_bp);
		mySQLiteHelper=new MySQLiteHelper(add_bp.this, "notepad.db", null, 1);
		db=mySQLiteHelper.getReadableDatabase();
		//显示Tab视图
		TabHost tabHost;
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("录入").setContent(R.id.text1));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("笔记").setContent(R.id.text2));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("日志").setContent(R.id.text3));
		tabHost.setCurrentTab(0);
		
		text21 = (EditText) findViewById(R.id.text21);
		bp_high = (EditText) findViewById(R.id. bp_high);
		bp_low = (EditText) findViewById(R.id.bp_low);
		rate = (EditText) findViewById(R.id.rate);
		latest_bphigh =  (TextView) findViewById(R.id.latest_bphigh);
		latest_bplow =  (TextView) findViewById(R.id.latest_bplow);
		latest_rate =  (TextView) findViewById(R.id.latest_rate);
		latest_bptime =(TextView) findViewById(R.id.latest_bptime);
		latest_cond = (TextView) findViewById(R.id.latest_cond);
		//用SharedPreference储存note
		SharedPreferences sp1 = getSharedPreferences("note",MODE_PRIVATE); 
		String notesString = sp1.getString("note", "");
		text21.setText(notesString);
		
		cursor = db.query("bptable", new String[]{"_id", "hbp","lbp","rt","tm","cond"}, null, null, null,null,null);
			if(cursor.moveToNext()!=true){
				 latest_bphigh.setText("");
			     latest_bplow.setText("");
			     latest_rate.setText("");
			     latest_bptime.setText("");
			     latest_cond.setText("");
			     //cursor.close();
			}
			else {
				
				cursor.moveToLast();
				int bphighindex = cursor.getColumnIndex("hbp");
				String bphigh = cursor.getString(bphighindex);
				int bplowindex = cursor.getColumnIndex("lbp");
				String bplow = cursor.getString(bplowindex);
				int rateindex = cursor.getColumnIndex("rt");
				String rate = cursor.getString(rateindex);
				int dateindex = cursor.getColumnIndex("tm");
				String date = cursor.getString(dateindex);
				int condindex = cursor.getColumnIndex("cond");
				String condition = cursor.getString(condindex);
				
				int bph = Integer.parseInt(bphigh);
				int bpl = Integer.parseInt(bplow);
				String color = "#000000";
				
			    if ((bph >= 180  || bpl >= 110 ) && bph > bpl) {
				color = "#cc0000";
			    }else if (((bph >= 160 && bph <= 179) || (bpl >= 100 && bpl <= 109))&& bph > bpl) {
					color = "#ff0000";
			    }else if (((bph >= 140 && bph <= 159) || (bpl >= 90 && bpl <= 99))&& bph > bpl) {
					color = "#ff6100";
			    }else if (((bph >= 120 && bph <= 139) || (bpl >= 80 && bpl <= 89))&& bph > bpl) {
					color = "#ffd700";
			    }else if (bph >= 90 && bph < 120 && bpl >= 60 && bpl < 80) {
					color = "#4db849";
				} else if (bph < 90 && bpl < 60 && bph > bpl) {
					color = "#3CB1D8";
				} else {
					color = "#000000";
				}//需改动，服从较高级
				
				latest_bphigh.setText(bphigh);
			    latest_bplow.setText(bplow);
			    latest_rate.setText(rate);
			    latest_bptime.setText(date);
			    latest_cond.setText(condition);
			    latest_cond.setTextColor(Color.parseColor(color));
			    //cursor.close();
				
			}
			
			
			
			
			//mySQLiteHelper=new MySQLiteHelper(bp_report.this, "notepad.db", null, 1);
			tv=(TextView) findViewById(R.id.TextView001a);
			lv=(ListView) findViewById(R.id.ListView01a);
			//数据库
			//db=mySQLiteHelper.getReadableDatabase();
			//cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);
			
			if(cursor.getCount()>0){
				tv.setVisibility(View.GONE);
			}
			
			MyBpCursorAdapter sca=new MyBpCursorAdapter(add_bp.this, R.layout.item, cursor, new String[]{"hbp","lbp","rt","tm","cond"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.bp_condition});
			lv.setAdapter(sca);
		
			lv.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
			
					Builder builder=new Builder(add_bp.this);
					builder.setSingleChoiceItems(new String[]{"查看","修改","删除"}, -1, new OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
						
							if(which==0&&cursor.getCount()!=0){
								int hbpindex=cursor.getColumnIndex("hbp");
								hbpString=cursor.getString(hbpindex);
								int lbpindex=cursor.getColumnIndex("lbp");
								lbpString=cursor.getString(lbpindex);
								int rtindex=cursor.getColumnIndex("rt");
								rtsString=cursor.getString(rtindex);
					
						
								Toast.makeText(add_bp.this, hbpString+"|"+lbpString+"|"+rtsString, Toast.LENGTH_SHORT).show();	
							}else if(which==2&&cursor.getCount()!=0){
						
								int myidindex=cursor.getColumnIndex("_id");
								myid=cursor.getInt(myidindex);
								Builder builder02=new Builder(add_bp.this);
								LayoutInflater inflater=LayoutInflater.from(add_bp.this);
								View view=inflater.inflate(R.layout.deleteview, null);
								builder02.setView(view);
								builder02.setPositiveButton("确定", new DialogInterface.OnClickListener(){
									public void onClick(DialogInterface dialog, int which) {
						
										

										db.delete("bptable", "_id="+myid, null);
										cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);

										MyBpCursorAdapter sca=new MyBpCursorAdapter(add_bp.this, R.layout.item, cursor, new String[]{"hbp","lbp","rt","tm","cond"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.bp_condition});
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
								Builder builder01=new Builder(add_bp.this);
								
								builder01.setTitle("编辑");
								
								LayoutInflater inflater=LayoutInflater.from(add_bp.this);
								View view=inflater.inflate(R.layout.updatedialogview, null);
								et01=(EditText) view.findViewById(R.id.ed1);
								et02=(EditText) view.findViewById(R.id.ed2);
								et03 = (EditText) view.findViewById(R.id.ed3);
								builder01.setView(view);
								builder01.setPositiveButton("确定", new DialogInterface.OnClickListener(){

									public void onClick(DialogInterface dialog, int which) {
									
									String newhbp=et01.getText().toString();
									String newlbp=et02.getText().toString();
									String newrt=et03.getText().toString();
									String condString;
									
									
									if (TextUtils.isEmpty(newhbp)||TextUtils.isEmpty(newlbp)||TextUtils.isEmpty(newrt)) {
										
									}else {
										int bph = Integer.parseInt(newhbp);
										int bpl = Integer.parseInt(newlbp);

										    if ((bph >= 180  || bpl >= 110 ) && bph > bpl) {
											   condString = "三级高";
										    }else if (((bph >= 160 && bph <= 179) || (bpl >= 100 && bpl <= 109))&& bph > bpl) {
										    	condString = "二级高";
										    }else if (((bph >= 140 && bph <= 159) || (bpl >= 90 && bpl <= 99))&& bph > bpl) {
										    	condString = "一级高";
										    }else if (((bph >= 120 && bph <= 139) || (bpl >= 80 && bpl <= 89))&& bph > bpl) {
										    	condString = "正常高";
										    }else if (bph >= 90 && bph < 120 && bpl >= 60 && bpl < 80) {
										    	condString = "正常";
											} else if (bph < 90 && bpl < 60 && bph > bpl) {
												condString = "偏低";
											} else {
												condString = "其他异常";
											}//需改动，服从较高级
									ContentValues cv=new ContentValues();
									cv.put("hbp", newhbp);
									cv.put("lbp", newlbp);
									cv.put("rt", newrt);
									cv.put("cond", condString);
							
									db.update("bptable", cv, "_id="+myid, null);
									cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);
										MyBpCursorAdapter sca=new MyBpCursorAdapter(add_bp.this, R.layout.item, cursor, new String[]{"hbp","lbp","rt","tm","cond"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.bp_condition});
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
	
	
	public void savedata(View view) {
		String high = bp_high.getText().toString();
		String low = bp_low.getText().toString();
		String rates = rate.getText().toString();
		String date = DateFormat.format("yyyy-MM-dd    HH:mm:ss", new Date()).toString();
		String cond;
		String color = "#000000";
		if (TextUtils.isEmpty(high)||TextUtils.isEmpty(low)||TextUtils.isEmpty(rates)) {
			Toast.makeText(this, "输入不能有空哦", Toast.LENGTH_SHORT).show();
			
		}else {
			int bph = Integer.parseInt(high);
			int bpl = Integer.parseInt(low);
			
			   if ((bph >= 180  || bpl >= 110 ) && bph > bpl) {
				    color = "#cc0000";
				    cond = "三级高";
			    }else if (((bph >= 160 && bph <= 179) || (bpl >= 100 && bpl <= 109))&& bph > bpl) {
					color = "#ff0000";
					cond = "二级高";
			    }else if (((bph >= 140 && bph <= 159) || (bpl >= 90 && bpl <= 99))&& bph > bpl) {
					color = "#ff6100";
					cond = "一级高";
			    }else if (((bph >= 120 && bph <= 139) || (bpl >= 80 && bpl <= 89))&& bph > bpl) {
					color = "#ffd700";
					cond = "正常高";
			    }else if (bph >= 90 && bph < 120 && bpl >= 60 && bpl < 80) {
					color = "#4db849";
					cond = "正常";
				} else if (bph < 90 && bpl < 60 && bph > bpl) {
					color = "#3CB1D8";
					cond = "偏低";
				} else {
					color = "#000000";
					cond = "其他异常";
				}

			latest_bphigh.setText(high);
			latest_bplow.setText(low);
			latest_rate.setText(rates);
			latest_bptime.setText(date);
			latest_cond.setText(cond);
			latest_cond.setTextColor(Color.parseColor(color));

			
			
		
			ContentValues cv=new ContentValues();
			cv.put("hbp", high);
			cv.put("lbp",low);
			cv.put("rt",rates);
			cv.put("rt",rates);
			cv.put("tm",date);
			cv.put("cond",cond);
			db.insert("bptable", null, cv);
					
			Toast.makeText(this, "保存数据成功啦", Toast.LENGTH_SHORT).show();
					
			}
		tv.setVisibility(View.GONE);
		cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);
		MyBpCursorAdapter sca=new MyBpCursorAdapter(add_bp.this, R.layout.item, cursor, new String[]{"hbp","lbp","rt","tm","cond"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.bp_condition});
		lv.setAdapter(sca);//为什么不实时刷新？
		
		

	}
	
	public void note(View view) {
		String noteString  = text21.getText().toString().trim();
		
		if (TextUtils.isEmpty(noteString)) {
			Toast.makeText(this, "写点什么吧亲", Toast.LENGTH_SHORT).show();
			
		}else{
				
			savenote.notedata(this, noteString);	
			Toast.makeText(this, "已经记下来了哦", Toast.LENGTH_SHORT).show();
					
			}
	
	}
	
	public void  to_bp_line(View view){
		Intent to_bp_line = new Intent(add_bp.this,bp_linechart.class); 
		startActivity(to_bp_line); 
	}
}
