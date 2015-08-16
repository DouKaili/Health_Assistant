package zju.bme.bp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("InflateParams")
public class bp_report extends Activity {
	private MySQLiteHelper mySQLiteHelper;
	private SQLiteDatabase db;
	private ListView       lv;
	private TextView       tv;
	private TextView bp_cond;
	private Cursor cursor;
	private int myid;
	private String hbpString;
	private String lbpString;
	private String rtsString;
	private EditText et01;
	private EditText et02;
	private EditText et03;
	
	
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bp_report);
	
		mySQLiteHelper=new MySQLiteHelper(bp_report.this, "notepad.db", null, 1);
		tv=(TextView) findViewById(R.id.TextView001);
		lv=(ListView) findViewById(R.id.ListView01);
		//数据库
		db=mySQLiteHelper.getReadableDatabase();
		cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);
		
		if(cursor.getCount()>0){
			tv.setVisibility(View.GONE);
		}
		
		SimpleCursorAdapter sca=new SimpleCursorAdapter(bp_report.this, R.layout.item, cursor, new String[]{"hbp","lbp","rt","tm","cond"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.bp_condition});
		lv.setAdapter(sca);
	
		lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
		
				Builder builder=new Builder(bp_report.this);
				builder.setSingleChoiceItems(new String[]{"查看","修改","删除"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					
						if(which==0){
							int hbpindex=cursor.getColumnIndex("hbp");
							hbpString=cursor.getString(hbpindex);
							int lbpindex=cursor.getColumnIndex("lbp");
							lbpString=cursor.getString(lbpindex);
							int rtindex=cursor.getColumnIndex("rt");
							rtsString=cursor.getString(rtindex);
				
					
							Toast.makeText(bp_report.this, hbpString+"|"+lbpString+"|"+rtsString, Toast.LENGTH_SHORT).show();	
						}else if(which==2){
					
							int myidindex=cursor.getColumnIndex("_id");
							myid=cursor.getInt(myidindex);
							Builder builder02=new Builder(bp_report.this);
							LayoutInflater inflater=LayoutInflater.from(bp_report.this);
							View view=inflater.inflate(R.layout.deleteview, null);
							builder02.setView(view);
							builder02.setPositiveButton("确定", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which) {
					
									

									db.delete("bptable", "_id="+myid, null);
									cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);
					 
									SimpleCursorAdapter sca=new SimpleCursorAdapter(bp_report.this, R.layout.item, cursor, new String[]{"hbp","lbp","rt","tm","cond"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.bp_condition});
									lv.setAdapter(sca);
									}	
								});
							builder02.setNegativeButton("取消", new DialogInterface.OnClickListener(){

							public void onClick(DialogInterface dialog, int which) {

						}
						
					});builder02.show();
						}else if(which==1){	
							//取得数据
							int myidindex=cursor.getColumnIndex("_id");
							myid=cursor.getInt(myidindex);
							Builder builder01=new Builder(bp_report.this);
							
							builder01.setTitle("编辑");
							
							LayoutInflater inflater=LayoutInflater.from(bp_report.this);
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

									if (bph >= 90 && bph <= 140 && bpl >= 60 && bpl <= 90) {
										condString = "正常";
									} else if (bph > 140 || bpl > 90) {
										condString = "高压";
									} else if (bph < 90 && bpl < 60) {
										condString = "低压";
									} else {
										condString = "其他异常";
									}
								ContentValues cv=new ContentValues();
								cv.put("hbp", newhbp);
								cv.put("lbp", newlbp);
								cv.put("rt", newrt);
								cv.put("cond", condString);
						
								db.update("bptable", cv, "_id="+myid, null);
								cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);
						 
								SimpleCursorAdapter sca=new SimpleCursorAdapter(bp_report.this, R.layout.item, cursor, new String[]{"hbp","lbp","rt","tm","cond"}, new int[]{R.id.TextView01,R.id.TextView02,R.id.TextView03,R.id.TextView04,R.id.bp_condition});
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
	
}

