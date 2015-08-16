package zju.bme.bp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

public class MySimpleCursorAdapter extends SimpleCursorAdapter { 
	
	private SQLiteDatabase db;
	private MySQLiteHelper WeightSQLiteHelper;
	private Cursor cursor;
    private long time = MyApplication.getInstance().gettime();
	   
    @SuppressWarnings("deprecation")
	public MySimpleCursorAdapter(Context context, int layout, Cursor c, 
            String[] from, int[] to) { 
        super(context, layout, c, from, to); 
        // TODO Auto-generated constructor stub 
   
    } 
   
    @Override 
    public View getView(final int position, View convertView, ViewGroup parent) { 
        // TODO Auto-generated method stub 
        // listviewÿ�εõ�һ��item����Ҫviewȥ���ƣ�ͨ��getView�����õ�view 
        // positionΪitem����� 
        View view = null; 
        if (convertView != null) { 
            view = convertView; 
            // ʹ�û����view,��Լ�ڴ� 
            // ��listview��item����ʱ���϶�����סһ����item������ס��item��view����convertView�����š� 
            // ���������ص�֮ǰ����ס��itemʱ��ֱ��ʹ��convertView����������ȥnew view() 
   
        } else { 
            view = super.getView(position, convertView, parent); 
   
        } 
        WeightSQLiteHelper=new MySQLiteHelper(MyApplication.getInstance(), "notepad.db", null, 1);
		db=WeightSQLiteHelper.getReadableDatabase();
        Date datenow = new Date();
        Date dateprev = new Date();
        datenow.setTime(System.currentTimeMillis());
        String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
        dateprev.setTime(datenow.getTime() - time*86400000);//ms
        String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
        String sql = "select * from weighttable where tm Between '" +mFrom +"' and '"+mCurrent + "'";
        cursor = db.rawQuery(sql, null);
    	//cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
    	cursor.moveToPosition(position);
    	String tm = cursor.getString(cursor
				.getColumnIndex("tm"));
        TextView time = (TextView)view.findViewById(R.id.TextView04s);
        time.setText(tm.substring(14,19));
//        if (cdString.equals("��׼")) {
//
//        	cd.setTextColor(Color.parseColor("#8BC34A"));
//		}
//        else if (cdString.equals("ƫ��")) {
//            cd.setTextColor(Color.parseColor("#03A9F4"));
//		}
//        else if (cdString.equals("ƫ��")) {
//            cd.setTextColor(Color.parseColor("#ff6100"));
//		}
//        else if (cdString.equals("����")) {
//            cd.setTextColor(Color.parseColor("#ff0000"));
//		}
//        else  {
//            cd.setTextColor(Color.parseColor("#FFFFFF"));
//		}

        //view.setBackgroundColor(Color.parseColor("#ffffff"));
        return super.getView(position, view, parent); 
    } 
   
}
