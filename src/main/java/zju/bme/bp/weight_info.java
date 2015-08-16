package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class weight_info extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weight_info);
		TextView bmi =   (TextView) findViewById(R.id.bmi_more);
		TextView sports_more =   (TextView) findViewById(R.id.sports_more);
		TextView food_more =   (TextView) findViewById(R.id.food_more);
		bmi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent _intent = new Intent(weight_info.this, bmi.class);
					startActivity(_intent);
			}
		});
		sports_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent_ = new Intent(weight_info.this, weight_more2.class);
					startActivity(intent_);
			}
		});
		food_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent_ = new Intent(weight_info.this, weight_more1.class);
					startActivity(intent_);
			}
		});
}
}
