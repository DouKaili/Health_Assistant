package zju.bme.bp;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private TextView hbp;
    private TextView lbp;
    private TextView rt;
    private TextView bp_cd;
    private TextView wt;
    private TextView ht;
    private TextView bmi;
    private TextView wt_cd;
	
	private ViewFlipper allFlipper;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				//�л�����ҳ��
				allFlipper.setDisplayedChild(1);
				break;
			}
		}
	}; 
	
	protected void onResume() {//Ҫд��onResume�����ʵ�ֻ���ˢ��
		super.onResume();
		hbp = (TextView) findViewById(R.id.main_hbp);
		lbp = (TextView) findViewById(R.id.main_lbp);
		rt = (TextView) findViewById(R.id.main_rt);
		bp_cd = (TextView) findViewById(R.id.main_bpcd);
		wt = (TextView) findViewById(R.id.main_wt);
		ht = (TextView) findViewById(R.id.main_ht);
		bmi = (TextView) findViewById(R.id.main_bmi);
		wt_cd = (TextView) findViewById(R.id.main_wtcd);
		mySQLiteHelper=new MySQLiteHelper(MainActivity.this, "notepad.db", null, 1);
		db=mySQLiteHelper.getReadableDatabase();
		cursor = db.query("bptable", new String[]{"_id", "hbp","lbp","rt","tm","cond"}, null, null, null,null,null);
		if(cursor.moveToNext()!=true){
			 hbp.setText("");
		     lbp.setText("");
		     rt.setText("");
		     bp_cd.setText("");
		}
		else {
			
			cursor.moveToLast();
			int bphighindex = cursor.getColumnIndex("hbp");
			String bphigh = cursor.getString(bphighindex);
			int bplowindex = cursor.getColumnIndex("lbp");
			String bplow = cursor.getString(bplowindex);
			int rateindex = cursor.getColumnIndex("rt");
			String rate = cursor.getString(rateindex);
			int condindex = cursor.getColumnIndex("cond");
			String condition = cursor.getString(condindex);
			
			int bph = Integer.parseInt(bphigh);
			int bpl = Integer.parseInt(bplow);
			String color = "#000000";

			 if ((bph >= 180  || bpl >= 110 ) && bph > bpl) {
					color = "#cc0000";
				    }else if (((bph >= 160 && bph <= 179) || (bpl >= 100 && bpl <= 109))&& bph > bpl) {
						color = "#ff3000";
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
					}//��Ķ������ӽϸ߼�
			
			hbp.setText(bphigh);
		    lbp.setText(bplow);
		    rt.setText(rate);
		    bp_cd.setText(condition);
		    bp_cd.setTextColor(Color.parseColor(color));
			
		}
		
		
		cursor = db.query("weighttable", new String[]{"_id", "wt","ht","bm","tm","cond"}, null, null, null,null,null);
		if(cursor.moveToNext()!=true){
			wt.setText("");
			ht.setText("");
	        bmi.setText("");
	        wt_cd.setText("");
		}
		else {
			
			cursor.moveToLast();
			int wtindex = cursor.getColumnIndex("wt");
			String wtsString = cursor.getString(wtindex);
			int htindex = cursor.getColumnIndex("ht");
			String htsString = cursor.getString(htindex);
			int bmindex = cursor.getColumnIndex("bm");
			String bmisString = cursor.getString(bmindex);
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
			wt.setText(wtsString);
			ht.setText(htsString);
	        bmi.setText(bmisString);
	        wt_cd.setText(condition);
	        wt_cd.setTextColor(Color.parseColor(color));
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		allFlipper = (ViewFlipper) findViewById(R.id.allFlipper);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(1); //��UI���̷߳�����Ϣ
            }
        }, 2000); //�����ȴ�2����
		
		
        ImageView imbutton1 =  (ImageView) findViewById(R.id.imageButton1);
        imbutton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent _intent = new Intent(MainActivity.this, add.class);
				startActivity(_intent);
			}
		});
        ImageView imbutton2 =  (ImageView) findViewById(R.id.imageButton2);
        imbutton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent _intent = new Intent(MainActivity.this, stats.class);
				startActivity(_intent);
			}
		});
        
//        ImageView imbutton3 =  (ImageView) findViewById(R.id.imageButton3);
//        imbutton3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent _intent = new Intent(MainActivity.this, report.class);
//				startActivity(_intent);
//			}
//		});
        
        ImageView imbutton4 =  (ImageView) findViewById(R.id.imageButton4);
        imbutton4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent _intent = new Intent(MainActivity.this, com.test.btclient.BTClient.class);
				startActivity(_intent);
			}
		});
        
//        ImageView imbutton5 =  (ImageView) findViewById(R.id.imageButton5);
//        imbutton5.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			Intent _intent = new Intent(MainActivity.this, come.android.game2048.main_game.class);
//				startActivity(_intent);
//			}
//		});
        ImageView imbutton6 =  (ImageView) findViewById(R.id.imageButton6);
        imbutton6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			Intent _intent = new Intent(MainActivity.this, health_information.class);
				startActivity(_intent);
			}
		});
      TextView  hide_game =  (TextView) findViewById(R.id.hide_game);
       hide_game.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent _intent = new Intent(MainActivity.this, come.android.game2048.main_game.class);
				//Intent _intent = new Intent(MainActivity.this, ecg.class);
				startActivity(_intent);
			}
		});
		
	}
	
	public void send_to_doctor(View view) {
		Intent data=new Intent(Intent.ACTION_SENDTO); 
		data.setData(Uri.parse("mailto:1292552451@qq.com")); 
		data.putExtra(Intent.EXTRA_SUBJECT, "���ڽ���״̬"); 
		data.putExtra(Intent.EXTRA_TEXT, "��ѹ��"+hbp.getText()+"\n"
				+"��ѹ��"+lbp.getText()+"\n"
				+"���ʣ�"+rt.getText()+"\n"
				+"Ѫѹ״����"+bp_cd.getText()+"\n"
				+"���أ�"+wt.getText()+"\n"
				+"��ߣ�"+ht.getText()+"\n"
				+"BMI��"+bmi.getText()+"\n"
				+"����״����"+wt_cd.getText()+"\n"); 
		startActivity(data); 
	}



}
