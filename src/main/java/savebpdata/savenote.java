package savebpdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class savenote {
	public static void notedata(Context context ,String note){
			SharedPreferences sp =  context.getSharedPreferences("note", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("note", note);
			editor.commit();	  
		}
}
