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
public class ex_report extends Activity {
	private MySQLiteHelper exSQLiteHelper;
	private SQLiteDatabase db;
	private ListView       lv;
	private TextView       tv;
	private Cursor cursor;
	private int myid;
	private String hbpString;
	private String lbpString;
	
	private EditText et02;
    private EditText et01;
	
	
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_report);
	
	exSQLiteHelper=new MySQLiteHelper(ex_report.this, "notepad.db", null, 1);
	tv=(TextView) findViewById(R.id.TextView001e);
	lv=(ListView) findViewById(R.id.ListView01e);
	//数据库
	 db=exSQLiteHelper.getReadableDatabase();
	 cursor=db.query("exercisetable", new String[]{"_id","ex","extime","time"}, null, null, null, null, null);
	if(cursor.getCount()>0){
	tv.setVisibility(View.GONE);
	}
	SimpleCursorAdapter sca=new SimpleCursorAdapter(ex_report.this, R.layout.exercise_item, cursor, new String[]{"ex","extime","time"}, new int[]{R.id.TextView01e,R.id.TextView02e,R.id.TextView03e});
	lv.setAdapter(sca);
	
	lv.setOnItemClickListener(new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View arg1, int which,
				long arg3) {
			
			Builder builder=new Builder(ex_report.this);
			builder.setSingleChoiceItems(new String[]{"查看","修改","删除"}, -1, new OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					
				if(which==0){
					
					
					int hbpindex=cursor.getColumnIndex("ex");
					hbpString=cursor.getString(hbpindex);
					int lbpindex=cursor.getColumnIndex("extime");
					lbpString=cursor.getString(lbpindex);
					
					
					Toast.makeText(ex_report.this, hbpString+"|"+lbpString, Toast.LENGTH_SHORT).show();	
				}else if(which==2){
					int myidindex=cursor.getColumnIndex("_id");
					myid=cursor.getInt(myidindex);
					Builder builder02=new Builder(ex_report.this);
					LayoutInflater inflater=LayoutInflater.from(ex_report.this);
					View view=inflater.inflate(R.layout.deleteview, null);
					builder02.setView(view);
					builder02.setPositiveButton("确定", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
					
					

					db.delete("exercisetable", "_id="+myid, null);
					cursor=db.query("exercisetable", new String[]{"_id","ex","extime","time"}, null, null, null, null, null);
					 
					SimpleCursorAdapter sca=new SimpleCursorAdapter(ex_report.this, R.layout.exercise_item, cursor, new String[]{"ex","extime","time"}, new int[]{R.id.TextView01e,R.id.TextView02e,R.id.TextView03e});
					lv.setAdapter(sca);}	
						});
					builder02.setNegativeButton("取消", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {

							
						}
						
					});builder02.show();
				}else if(which==1){	
					
					int idindex=cursor.getColumnIndex("_id");
				      myid=cursor.getInt(idindex);
					
					Builder builder01=new Builder(ex_report.this);
					
					builder01.setTitle("编辑");
					
					LayoutInflater inflater=LayoutInflater.from(ex_report.this);
					View view=inflater.inflate(R.layout.exercise_updatedialogview, null);
					
					et01=(EditText) view.findViewById(R.id.ed1e);
					et02=(EditText) view.findViewById(R.id.ed2e);
					
					builder01.setView(view);
					builder01.setPositiveButton("确定", new DialogInterface.OnClickListener(){

						public void onClick(DialogInterface dialog, int which) {
						
							
							
						String newhbp=et01.getText().toString();
						String newlbp=et02.getText().toString();
						
						
						if (TextUtils.isEmpty(newhbp)||TextUtils.isEmpty(newlbp)) {
							
						}else {
						
						
						ContentValues cv=new ContentValues();
						cv.put("ex", newhbp);
						cv.put("extime", newlbp);
						
						
						db.update("exercisetable", cv, "_id="+myid, null);
						cursor=db.query("exercisetable", new String[]{"_id","ex","extime","time"}, null, null, null, null, null);
						 
						SimpleCursorAdapter sca=new SimpleCursorAdapter(ex_report.this, R.layout.exercise_item, cursor, new String[]{"ex","extime","time"}, new int[]{R.id.TextView01e,R.id.TextView02e,R.id.TextView03e});
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



