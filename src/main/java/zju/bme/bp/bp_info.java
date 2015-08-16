package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class bp_info extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bp_knowledge);
		TextView treat =   (TextView) findViewById(R.id.treat_more);
		TextView prevent =   (TextView) findViewById(R.id.prevent_more);
		treat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent _intent = new Intent(bp_info.this, bp_treat_more.class);
					startActivity(_intent);
			}
		});
		
		prevent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent _intent = new Intent(bp_info.this, bp_prevent_more.class);
					startActivity(_intent);
			}
		});

}
}
