package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class report extends Activity implements OnClickListener {
	private LinearLayout re1;
	private LinearLayout re2;
	private LinearLayout re3;
	private LinearLayout re4;
	
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);
		
		
		re1 =  (LinearLayout) findViewById(R.id.re_bpreport);
		re2 =  (LinearLayout) findViewById(R.id.re_wgreport);
		re3 =  (LinearLayout) findViewById(R.id.re_exreport);
		re4 =  (LinearLayout) findViewById(R.id.re_mdreport);
		
		re2.setOnClickListener(report.this);
		re1.setOnClickListener(report.this);
		re3.setOnClickListener(report.this);
		re4.setOnClickListener(report.this);
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.re_bpreport:
			Intent bpreport = new Intent(report.this,bp_report.class); 
			startActivity(bpreport);
			break;
			
		case R.id.re_wgreport:
			Intent wgreport = new Intent(report.this,weight_report.class); 
			startActivity(wgreport); 
			break;
			
		case R.id.re_exreport:
			Intent exreport = new Intent(report.this,ex_report.class); 
			startActivity(exreport); 
			break;
		case R.id.re_mdreport:
			Intent mdreport = new Intent(report.this,md_report.class); 
			startActivity(mdreport); 
			break;
		default:
			break;
		}
	}
}
