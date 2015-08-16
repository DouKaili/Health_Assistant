package zju.bme.bp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

@SuppressLint("InflateParams")
public class weight_report extends Activity {
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
	public void onCreate(Bundle savedInstanceState){
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weight_report);
	
		WeightSQLiteHelper=new MySQLiteHelper(weight_report.this, "notepad.db", null, 1);
		tv=(TextView) findViewById(R.id.TextView001s);
		lv=(ListView) findViewById(R.id.ListView01s);
		//���ݿ�
		db=WeightSQLiteHelper.getReadableDatabase();
		cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
		//ɾ������ʾΪ�ա���TextView
		if(cursor.getCount()>0){
			tv.setVisibility(View.GONE);
		}
		ListAdapter adapter = new MySimpleCursorAdapter(weight_report.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});
		
		lv.setAdapter(adapter);
	
		lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int which,long arg3) {
			
				Builder builder=new Builder(weight_report.this);
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
					
							Toast.makeText(weight_report.this, hbpString+"|"+lbpString+"|"+rtsString, Toast.LENGTH_SHORT).show();	
						//ɾ��
						}else if(which==2&&cursor.getCount()!=0){
							int myidindex=cursor.getColumnIndex("_id");
							myid=cursor.getInt(myidindex);
							Builder builder02=new Builder(weight_report.this);
							LayoutInflater inflater=LayoutInflater.from(weight_report.this);
							View view=inflater.inflate(R.layout.deleteview, null);
							
							//�Ƿ�ɾ����ʾ
							builder02.setView(view);
							builder02.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
								public void onClick(DialogInterface dialog, int which) {
					
									db.delete("weighttable", "_id="+myid, null);
									cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
					 
									ListAdapter adapter = new MySimpleCursorAdapter(weight_report.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});
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
					
							Builder builder01=new Builder(weight_report.this);
					
							builder01.setTitle("�༭");
					
							LayoutInflater inflater=LayoutInflater.from(weight_report.this);
							View view=inflater.inflate(R.layout.weight_updatedialogview, null);
							et01=(EditText) view.findViewById(R.id.ed1s);
							et02=(EditText) view.findViewById(R.id.ed2s);
							int lbpindex=cursor.getColumnIndex("wt");
							String wt=cursor.getString(lbpindex);
							int rtindex=cursor.getColumnIndex("ht");
							String ht=cursor.getString(rtindex);
							et01.setText(wt);
							et02.setText(ht);
					
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
												condString = "����";
											}
											ContentValues cv=new ContentValues();
											cv.put("wt", newhbp);
											cv.put("ht", newlbp);
											cv.put("bm", newrt);
											cv.put("cond", condString);
											db.update("weighttable", cv, "_id="+myid, null);
											cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
											 
											ListAdapter adapter = new MySimpleCursorAdapter(weight_report.this, R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s});
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
	}
	public void onClickBack(View view) {
		finish();
    }
   
}

