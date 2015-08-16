package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class save extends Activity implements OnClickListener{
	private TextView s1;
	private TextView s2;
	private TextView s3;
	private TextView s4;
	private TextView s5;
	private TextView s6;
	private TextView s7;
	private TextView s8;
	private TextView s9;
	private TextView s10;
	private TextView s11;
	private TextView s12;
	private TextView s13;
	private TextView s14;
	private TextView s15;
	private TextView s16;
	private TextView s17;
	private TextView s18;
	private TextView s19;
	private TextView s20;
	private TextView s21;


	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.save_info);
		s1 = (TextView) findViewById(R.id.s1);
		s2 = (TextView) findViewById(R.id.s2);
		s3 = (TextView) findViewById(R.id.s3);
		s4 = (TextView) findViewById(R.id.s4);
		s5 = (TextView) findViewById(R.id.s5);
		s6 = (TextView) findViewById(R.id.s6);
		s7 = (TextView) findViewById(R.id.s7);
		s8 = (TextView) findViewById(R.id.s8);
		s9 = (TextView) findViewById(R.id.s9);
		s10 = (TextView) findViewById(R.id.s10);
		s11 = (TextView) findViewById(R.id.s11);
		s12 = (TextView) findViewById(R.id.s12);
		s13 = (TextView) findViewById(R.id.s13);
		s14 = (TextView) findViewById(R.id.s14);
		s15 = (TextView) findViewById(R.id.s15);
		s16 = (TextView) findViewById(R.id.s16);
		s17 = (TextView) findViewById(R.id.s17);
		s18 = (TextView) findViewById(R.id.s18);
		s19 = (TextView) findViewById(R.id.s19);
		s20 = (TextView) findViewById(R.id.s20);
		s21 = (TextView) findViewById(R.id.s21);
		
		s1.setOnClickListener(this);
		s2.setOnClickListener(this);
		s3.setOnClickListener(this);
		s4.setOnClickListener(this);
		s5.setOnClickListener(this);
		s6.setOnClickListener(this);
		s7.setOnClickListener(this);
		s8.setOnClickListener(this);
		s9.setOnClickListener(this);
		s10.setOnClickListener(this);
		s11.setOnClickListener(this);
		s12.setOnClickListener(this);
		s13.setOnClickListener(this);
		s14.setOnClickListener(this);
		s15.setOnClickListener(this);
		s16.setOnClickListener(this);
		s17.setOnClickListener(this);
		s18.setOnClickListener(this);
		s19.setOnClickListener(this);
		s20.setOnClickListener(this);
		s21.setOnClickListener(this);
		
		
}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.s1:
			Intent s1 = new Intent(this,s1.class); 
			startActivity(s1);
			break;
		case R.id.s2:
			Intent s2 = new Intent(this,s2.class); 
			startActivity(s2);
			break;
		case R.id.s3:
			Intent s3 = new Intent(this,s3.class); 
			startActivity(s3);
			break;
		case R.id.s4:
			Intent s4 = new Intent(this,s4.class); 
			startActivity(s4);
			break;	
		case R.id.s5:
			Intent s5 = new Intent(this,s5.class); 
			startActivity(s5);
			break;
		case R.id.s6:
			Intent s6 = new Intent(this,s6.class); 
			startActivity(s6);
			break;
		case R.id.s7:
			Intent s7 = new Intent(this,s7.class); 
			startActivity(s7);
			break;
		case R.id.s8:
			Intent s8 = new Intent(this,s8.class); 
			startActivity(s8);
			break;
		case R.id.s9:
			Intent s9 = new Intent(this,s9.class); 
			startActivity(s9);
			break;	
		case R.id.s10:
			Intent s10 = new Intent(this,s10.class); 
			startActivity(s10);
			break;	
		case R.id.s11:
			Intent s11 = new Intent(this,s11.class); 
			startActivity(s11);
			break;
		case R.id.s12:
			Intent s12 = new Intent(this,s12.class); 
			startActivity(s12);
			break;
		case R.id.s13:
			Intent s13 = new Intent(this,s13.class); 
			startActivity(s13);
			break;
		case R.id.s14:
			Intent s14 = new Intent(this,s14.class); 
			startActivity(s14);
			break;	
		case R.id.s15:
			Intent s15 = new Intent(this,s15.class); 
			startActivity(s15);
			break;
		case R.id.s16:
			Intent s16 = new Intent(this,s16.class); 
			startActivity(s16);
			break;	
		case R.id.s17:
			Intent s17 = new Intent(this,s17.class); 
			startActivity(s17);
			break;
		case R.id.s18:
			Intent s18 = new Intent(this,s18.class); 
			startActivity(s18);
			break;
		case R.id.s19:
			Intent s19 = new Intent(this,s19.class); 
			startActivity(s19);
			break;
		case R.id.s20:
			Intent s20 = new Intent(this,s20.class); 
			startActivity(s20);
			break;	
		case R.id.s21:
			Intent s21 = new Intent(this,s21.class); 
			startActivity(s21);
			break;
			
			
			
		default:
			break;
		}
	}
}
