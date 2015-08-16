package zju.bme.bp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class weight_more2 extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weight_more2);
	    TextView cal = (TextView) findViewById(R.id.cal);
	    cal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent _intent = new Intent(weight_more2.this, add_exercise.class);
					startActivity(_intent);
			}
		});
}
}
