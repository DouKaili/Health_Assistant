package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class exercise_info extends Activity implements OnClickListener{
	private TextView q1;
	private TextView q2;
	private TextView q3;
	private TextView q4;
	private TextView q5;
	private TextView q6;
	private TextView q7;
	private TextView q8;
	private TextView q9;
	
	

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercise_info);
		
		q1 = (TextView) findViewById(R.id.q1);
		q2 = (TextView) findViewById(R.id.q2);
		q3 = (TextView) findViewById(R.id.q3);
		q4 = (TextView) findViewById(R.id.q4);
		q5 = (TextView) findViewById(R.id.q5);
		q6 = (TextView) findViewById(R.id.q6);
		q7 = (TextView) findViewById(R.id.q7);
		q8 = (TextView) findViewById(R.id.q8);
		q9 = (TextView) findViewById(R.id.q9);
		
		q1.setOnClickListener(this);
		q2.setOnClickListener(this);
		q3.setOnClickListener(this);
		q4.setOnClickListener(this);
		q5.setOnClickListener(this);
		q6.setOnClickListener(this);
		q7.setOnClickListener(this);
		q8.setOnClickListener(this);
		q9.setOnClickListener(this);
}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.q1:
			Intent q1 = new Intent(this,q1.class); 
			startActivity(q1);
			break;
			
		case R.id.q2:
			Intent q2 = new Intent(this,q2.class); 
			startActivity(q2); 
			break;
			
		case R.id.q3:
			Intent q3 = new Intent(this,q3.class); 
			startActivity(q3); 
			break;
			
		case R.id.q4:
			Intent q4 = new Intent(this,q4.class); 
			startActivity(q4); 
			break;
			
		case R.id.q5:
			Intent q5 = new Intent(this,q5.class); 
			startActivity(q5); 
			break;
			
		case R.id.q6:
			Intent q6 = new Intent(this,q6.class); 
			startActivity(q6); 
			break;
			
		case R.id.q7:
			Intent q7 = new Intent(this,q7.class); 
			startActivity(q7); 
			break;
			
		case R.id.q8:
			Intent q8 = new Intent(this,q8.class); 
			startActivity(q8); 
			break;
			
		case R.id.q9:
			Intent q9 = new Intent(this,q9.class); 
			startActivity(q9); 
			break;
		default:
			break;
		}
		
	}
}
