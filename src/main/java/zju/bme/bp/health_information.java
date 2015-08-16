package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class health_information extends Activity implements OnClickListener {
	private LinearLayout re1;
	private LinearLayout re2;
	private LinearLayout re3;
	private LinearLayout re4;
	
	
		protected void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.onCreate(savedInstanceState);
			setContentView(R.layout.health_information);
					
			re1 =   (LinearLayout) findViewById(R.id.re_infor1);
			re2 =  (LinearLayout) findViewById(R.id.re_infor2);
			re3 =  (LinearLayout) findViewById(R.id.re_infor3);
			re4 =   (LinearLayout) findViewById(R.id.re_infor4);
				
			re1.setOnClickListener(this);
			re2.setOnClickListener(this);
			re3.setOnClickListener(this);
			re4.setOnClickListener(this);

		}
		public void onClick(View v){
			switch (v.getId()) {
			case R.id.re_infor1:
				Intent bp_info = new Intent(this,bp_info.class); 
				startActivity(bp_info);
				break;
				
			case R.id.re_infor2:
				Intent weight_info = new Intent(this,weight_info.class); 
				startActivity(weight_info); 
				break;
				
			case R.id.re_infor3:
				Intent drug_info = new Intent(this,save.class); 
				startActivity(drug_info); 
				break;
				
			case R.id.re_infor4:
				Intent exercise_info = new Intent(this,exercise_info.class); 
				startActivity(exercise_info); 
				break;
			default:
				break;
			}
		}
}
