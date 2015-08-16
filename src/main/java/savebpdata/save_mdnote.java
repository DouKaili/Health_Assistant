package savebpdata;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class save_mdnote extends Activity {
	public static void notedata(Context context ,String note){
		SharedPreferences sp =  context.getSharedPreferences("mdnote", Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("mdnote", note);
		editor.commit();	  
	}
}

