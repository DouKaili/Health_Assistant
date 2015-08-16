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
public class md_report extends Activity {
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
	
	
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.md_report);
	
		mySQLiteHelper=new MySQLiteHelper(md_report.this, "notepad.db", null, 1);
		tv=(TextView) findViewById(R.id.TextView001m);
		lv=(ListView) findViewById(R.id.ListView01m);
		//数据库
		db=mySQLiteHelper.getReadableDatabase();
		cursor=db.query("medicinetable", new String[]{"_id","md","mdcount","time"}, null, null, null, null, null);
		
		if(cursor.getCount()>0){
			tv.setVisibility(View.GONE);
		}
		
		SimpleCursorAdapter sca=new SimpleCursorAdapter(md_report.this, R.layout.md_item, cursor, new String[]{"md","mdcount","time"}, new int[]{R.id.TextView01m,R.id.TextView02m,R.id.TextView03m});
		lv.setAdapter(sca);
	
		lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
		
				Builder builder=new Builder(md_report.this);
				builder.setSingleChoiceItems(new String[]{"查看","修改","删除"}, -1, new OnClickListener(){

					public void onClick(DialogInterface dialog, int which) {
					
						if(which==0){
							int mdindex=cursor.getColumnIndex("md");
							mdString=cursor.getString(mdindex);
							int ctindex=cursor.getColumnIndex("mdcount");
							ctString=cursor.getString(ctindex);
							
				
					
							Toast.makeText(md_report.this, mdString+"|"+ctString+"片", Toast.LENGTH_SHORT).show();	
						}else if(which==2){
					
							int myidindex=cursor.getColumnIndex("_id");
							myid=cursor.getInt(myidindex);
							Builder builder02=new Builder(md_report.this);
							LayoutInflater inflater=LayoutInflater.from(md_report.this);
							View view=inflater.inflate(R.layout.deleteview, null);
							builder02.setView(view);
							builder02.setPositiveButton("确定", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which) {
					
									

									db.delete("medicinetable", "_id="+myid, null);
									cursor=db.query("medicinetable", new String[]{"_id","md","mdcount","time"}, null, null, null, null, null);
					 
									SimpleCursorAdapter sca=new SimpleCursorAdapter(md_report.this, R.layout.md_item, cursor, new String[]{"md","mdcount","time"}, new int[]{R.id.TextView01m,R.id.TextView02m,R.id.TextView03m});
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
							Builder builder01=new Builder(md_report.this);
							
							builder01.setTitle("编辑");
							
							LayoutInflater inflater=LayoutInflater.from(md_report.this);
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
						 
								SimpleCursorAdapter sca=new SimpleCursorAdapter(md_report.this, R.layout.md_item, cursor, new String[]{"md","mdcount","time"}, new int[]{R.id.TextView01m,R.id.TextView02m,R.id.TextView03m});
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

