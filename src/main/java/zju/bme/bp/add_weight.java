package zju.bme.bp;

import android.app.AlertDialog.Builder;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Date;

import savebpdata.save_wtnote;


public class add_weight extends TabActivity {
	
	private EditText input_weight;
    private EditText input_height;
    private TextView latest_weight;
    private TextView latest_height;
    private TextView latest_BMI;
    private TextView latest_wttime;
    private TextView latest_cond;
    private EditText text21w;
    
    private MySQLiteHelper WeightSQLiteHelper;
	private SQLiteDatabase db;
	private ListView       lv;
	private TextView       tv;
	private Cursor cursor;
	private int myid;
	private String hbpString;
	private String lbpString;
	private String rtsString;
	private EditText et01;
	private EditText et02;
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_weight);
		
		WeightSQLiteHelper=new MySQLiteHelper(add_weight.this, "notepad.db", null, 1);
		db=WeightSQLiteHelper.getReadableDatabase();
		
		TabHost tabHost;
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("¼��").setContent(R.id.text1w));
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("�ʼ�").setContent(R.id.text2w));
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("��־").setContent(R.id.text3w));
		tabHost.setCurrentTab(0);
		
		input_weight = (EditText) findViewById(R.id.input_weight);
		input_height = (EditText) findViewById(R.id.input_height);
		latest_weight =  (TextView) findViewById(R.id.latest_weight);
		latest_height =  (TextView) findViewById(R.id.latest_height);
		latest_BMI =  (TextView) findViewById(R.id.latest_BMI);
		latest_wttime =  (TextView) findViewById(R.id.latest_wttime);
		latest_cond = (TextView) findViewById(R.id.latest_wcond);
		text21w = (EditText) findViewById(R.id.text21w);
	
		SharedPreferences sp1 = getSharedPreferences("wtnote",MODE_PRIVATE); 
		String notesString = sp1.getString("wtnote", "");
		text21w.setText(notesString);
		
		 cursor = db.query("weighttable", new String[]{"_id", "wt","ht","bm","tm","cond"}, null, null, null,null,null);
		if(cursor.moveToNext()!=true){
			latest_weight.setText("");
			latest_height.setText("");
	        latest_BMI.setText("");
	        latest_wttime.setText("");
	        latest_cond.setText("");
		    //cursor.close();
		}
		else {
			
			cursor.moveToLast();
			int wtindex = cursor.getColumnIndex("wt");
			String wt = cursor.getString(wtindex);
			int htindex = cursor.getColumnIndex("ht");
			String ht = cursor.getString(htindex);
			int bmindex = cursor.getColumnIndex("bm");
			String bm = cursor.getString(bmindex);
			int dateindex = cursor.getColumnIndex("tm");
			String date = cursor.getString(dateindex);
			int condindex = cursor.getColumnIndex("cond");
			String condition = cursor.getString(condindex);
			String color = "#000000";
			if(condition.equals("ƫ��")){
				color = "#3CB1D8";
			}else if (condition.equals("��׼")) {
				color = "#4db849";
			}
			else if (condition.equals("ƫ��")) {
				color = "#FF9F26";
			}
			else if (condition.equals("����")) {
				color = "#ff0000";
			}

			latest_weight.setText(wt);
			latest_height.setText(ht);
	        latest_BMI.setText(bm);
	        latest_wttime.setText(date);
	        latest_cond.setText(condition);
	        latest_cond.setTextColor(Color.parseColor(color));
		    //cursor.close();
		}
		
		//WeightSQLiteHelper=new MySQLiteHelper(weight_report.this, "notepad.db", null, 1);
				tv=(TextView) findViewById(R.id.TextView001w);
				lv=(ListView) findViewById(R.id.ListView01w);
				//���ݿ�
				//db=WeightSQLiteHelper.getReadableDatabase();
				//cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm"}, null, null, null, null, null);
				//ɾ������ʾΪ�ա���TextView
				if(cursor.getCount()>0){
					tv.setVisibility(View.GONE);
				}
				ListAdapter adapter = new MySimpleCursorAdapter(add_weight.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});
				lv.setAdapter(adapter);
			
				lv.setOnItemClickListener(new OnItemClickListener(){

					public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
					
						Builder builder=new Builder(add_weight.this);
						builder.setSingleChoiceItems(new String[]{"�鿴","�޸�","ɾ��"}, -1, new DialogInterface.OnClickListener(){ //��������OnClickListenerʱ��ĳ�����

							public void onClick(DialogInterface dialog, int which) {
								//�鿴
								if(which==0&&cursor.getCount()!=0){
							
							
									int hbpindex=cursor.getColumnIndex("wt");
									hbpString=cursor.getString(hbpindex);
									int lbpindex=cursor.getColumnIndex("ht");
									lbpString=cursor.getString(lbpindex);
									int rtindex=cursor.getColumnIndex("bm");
									rtsString=cursor.getString(rtindex);
							
									Toast.makeText(add_weight.this, hbpString+"|"+lbpString+"|"+rtsString, Toast.LENGTH_SHORT).show();	
								//ɾ��
								}else if(which==2&&cursor.getCount()!=0){
									int myidindex=cursor.getColumnIndex("_id");
									myid=cursor.getInt(myidindex);
									Builder builder02=new Builder(add_weight.this);
									LayoutInflater inflater=LayoutInflater.from(add_weight.this);
									View view=inflater.inflate(R.layout.deleteview, null);
									
									//�Ƿ�ɾ����ʾ
									builder02.setView(view);
									builder02.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
										public void onClick(DialogInterface dialog, int which) {
							
											db.delete("weighttable", "_id="+myid, null);
											cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
							 
											ListAdapter adapter = new MySimpleCursorAdapter(add_weight.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});
											lv.setAdapter(adapter);
											}	
									});
									builder02.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){

										public void onClick(DialogInterface dialog, int which) {
										
										}
								
									});
									builder02.show();
								//�޸�
								}else if(which==1&&cursor.getCount()!=0){	
							
									int idindex=cursor.getColumnIndex("_id");
									myid=cursor.getInt(idindex);
							
									Builder builder01=new Builder(add_weight.this);
							
									builder01.setTitle("�༭");
							
									LayoutInflater inflater=LayoutInflater.from(add_weight.this);
									View view=inflater.inflate(R.layout.weight_updatedialogview, null);
									et01=(EditText) view.findViewById(R.id.ed1s);
									et02=(EditText) view.findViewById(R.id.ed2s);
							
									builder01.setView(view);
									builder01.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

										public void onClick(DialogInterface dialog, int which) {
								
											String newhbp=et01.getText().toString();
											String newlbp=et02.getText().toString();
											String newrt = null;
											String condString = null;
								
											if (TextUtils.isEmpty(newhbp)||TextUtils.isEmpty(newlbp)) {
									
											}else {
												double number1;
												double number2;
												double result;
												number1=Double.parseDouble(newhbp);
												number2=Double.parseDouble(newlbp);
												if(number2==0){
								
												}else{
													result = (number1/(number2*number2))*10000;
													BigDecimal b = new BigDecimal(result);
													double true_result = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
									
													newrt = true_result+"";		
													if(true_result<18.5){
														condString="ƫ��";
													}else if (true_result<24&&true_result>=18.5) {
														condString="��׼";
													}
													else if (true_result<28&&true_result>=24) {
														condString="ƫ��";
													}
													else if (true_result>=28) {
														condString="����";
													}

													
													ContentValues cv=new ContentValues();
													cv.put("wt", newhbp);
													cv.put("ht", newlbp);
													cv.put("bm", newrt);
													cv.put("cond", condString);
													db.update("weighttable", cv, "_id="+myid, null);
													cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
													 
													ListAdapter adapter = new MySimpleCursorAdapter(add_weight.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});
													lv.setAdapter(adapter);
												}
								
											}}
								
									});
									builder01.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){

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
		
		TextView bmiintro =    (TextView) findViewById(R.id.bmi_introduction);
		bmiintro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent _intent = new Intent(add_weight.this, bmi.class);
					startActivity(_intent);
			}
		});
	
		
    }
	
	
	@SuppressWarnings("deprecation")
	public void save_weight(View view) {
		String weight = input_weight.getText().toString();
		String height = input_height.getText().toString();
		String bmi =null ;
		String date = DateFormat.format("yyyy-MM-dd    HH:mm:ss", new Date()).toString();  
		String cond = null;
		String color = "#000000";
		
		if (TextUtils.isEmpty(weight)||TextUtils.isEmpty(height)) {
			Toast.makeText(this, "���벻���п���", Toast.LENGTH_SHORT).show();
		}else {
				double number1;
				double number2;
				double result;
				number1=Double.parseDouble(weight);
				number2=Double.parseDouble(height);
			if(number2==0){
				Toast.makeText(this, "��߲�����0��", Toast.LENGTH_SHORT).show();
			}else{
				result = (number1/(number2*number2))*10000;
				BigDecimal b = new BigDecimal(result);
				double true_result = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
				// ������λС����ĩβ0����ʾ
				bmi = true_result+"";	
				if(true_result<18.5){
					cond="ƫ��";
					color = "#3CB1D8";
				}else if (true_result<24&&true_result>=18.5) {
					cond="��׼";
					color = "#4db849";
				}
				else if (true_result<28&&true_result>=24) {
					cond="ƫ��";
					color = "#FF9F26";
				}
				else if (true_result>=28) {
					cond="����";
					color = "#ff0000";
				}
				
				latest_weight.setText(weight);
				latest_height.setText(height);
				latest_BMI.setText(bmi);
				latest_wttime.setText(date);
				latest_cond.setText(cond);
				latest_cond.setTextColor(Color.parseColor(color));

				ContentValues cv=new ContentValues();
				cv.put("wt", weight);
				cv.put("ht",height);
				cv.put("bm",bmi);
				cv.put("tm",date);
				cv.put("cond", cond);
				db.insert("weighttable", null, cv);

				Toast.makeText(this, "�������ݳɹ���", Toast.LENGTH_SHORT).show();
//				Toast.makeText(this, "����BMI��"+bmi, Toast.LENGTH_SHORT).show();
				
				tv.setVisibility(View.GONE); //��ֹ�״�¼��ʱ�������⣨�ظ����޹�ϵ��
				cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null); 
				ListAdapter adapter = new MySimpleCursorAdapter(add_weight.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});
				lv.setAdapter(adapter);
						 
			}

		}
    }
	
	public void wtnote(View view) {
		String noteString  = text21w.getText().toString().trim();
		
		if (TextUtils.isEmpty(noteString)) {
			Toast.makeText(this, "д��ʲô����", Toast.LENGTH_SHORT).show();
			
		}else{
				
			save_wtnote.notedata(this, noteString);	
			Toast.makeText(this, "�Ѿ���������Ŷ", Toast.LENGTH_SHORT).show();
					
			}
	
	}
	
	public void  to_wt_line(View view){
		Intent to_wt_line = new Intent(add_weight.this,weight_linechart.class); 
		startActivity(to_wt_line); 
	}
	
}