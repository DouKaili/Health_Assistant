package zju.bme.bp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class add extends Activity implements OnClickListener {
	private LinearLayout re2;
	private LinearLayout re1;
	private LinearLayout re4;
	private LinearLayout re3;
	
		protected void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.add);
			
			
			re1 =   (LinearLayout) findViewById(R.id.re1);
			re2 =  (LinearLayout) findViewById(R.id.re2);
			re3 =  (LinearLayout) findViewById(R.id.re3);
			re4 =   (LinearLayout) findViewById(R.id.re4);
			
			re2.setOnClickListener(add.this);
			re1.setOnClickListener(add.this);
			re4.setOnClickListener(add.this);
			re3.setOnClickListener(add.this);

		}
		public void onClick(View v){
			switch (v.getId()) {
			case R.id.re2:
				Intent add_weight = new Intent(add.this,add_weight.class); 
				startActivity(add_weight);
				break;
				
			case R.id.re1:
				Intent add_bp = new Intent(add.this,add_bp.class); 
				startActivity(add_bp); 
				break;
				
			case R.id.re4:
				Intent add_exercise = new Intent(add.this,add_exercise.class); 
				startActivity(add_exercise); 

               //startActivity(new Intent(AlarmClock.ACTION_SET_ALARM));
				break;
			case R.id.re3:
				Intent add_md = new Intent(add.this,add_medicine.class); 
				startActivity(add_md); 
				break;
			default:
				break;
			}
		}

}
