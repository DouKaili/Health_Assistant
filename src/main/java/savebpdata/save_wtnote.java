package savebpdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class save_wtnote {
	public static void notedata(Context context ,String note){
			SharedPreferences sp =  context.getSharedPreferences("wtnote", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("wtnote", note);
			editor.commit();	  
		}
}
