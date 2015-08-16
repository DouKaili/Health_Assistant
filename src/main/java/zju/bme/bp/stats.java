package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import lecho.lib.hellocharts.view.LineChartView;

public class stats extends Activity implements OnClickListener {

	//private LinearLayout md_report;
	private LinearLayout bplinechart;
	private LinearLayout weightlinechart;
	private LinearLayout ex_report;
	private LinearLayout keystats;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);
		
		//re_stat3 =   (LinearLayout) findViewById(R.id.re_stat3);
		//md_report =   (LinearLayout) findViewById(R.id.re_mdreport);
		bplinechart = (LinearLayout) findViewById(R.id.rltlayout_bplinechart);
		weightlinechart = (LinearLayout) findViewById(R.id.rltlayout_weightlinechart);
		keystats = (LinearLayout) findViewById(R.id.rltlayout_keystats);
		ex_report = (LinearLayout) findViewById(R.id.re_exreport);
		
		//md_report.setOnClickListener(stats.this);
		bplinechart.setOnClickListener(stats.this);
		weightlinechart.setOnClickListener(stats.this);
		ex_report.setOnClickListener(stats.this);
		keystats.setOnClickListener(stats.this);

	}
	public void onClick(View v){
		switch (v.getId()) {		
			
		case R.id.rltlayout_bplinechart:
			Intent bplinechart = new Intent(stats.this,bp_linechart.class);
			startActivity(bplinechart); 
			break;
			
		case R.id.rltlayout_weightlinechart:
			Intent weightlinechart = new Intent(stats.this,weight_linechart.class); 
			startActivity(weightlinechart); 
			break;
			
		case R.id.re_exreport:
			Intent ex = new Intent(stats.this,cal_linechart.class); 
			startActivity(ex); 
			break;
			
		case R.id.rltlayout_keystats:
			Intent key = new Intent(stats.this,key_stats.class); 
			startActivity(key);
			break;
		default:
			break;
		}

	}
}
