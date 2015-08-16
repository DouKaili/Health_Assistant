package savebpdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class save_exercise_data {
	public static void notedata(Context context ,String note){
			SharedPreferences sp =  context.getSharedPreferences("exnote", Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("exnote", note);
			editor.commit();	  
		}
}
