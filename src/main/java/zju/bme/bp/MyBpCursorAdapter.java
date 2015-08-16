package zju.bme.bp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

public class MyBpCursorAdapter extends SimpleCursorAdapter {

	private SQLiteDatabase db;
	private MySQLiteHelper BpSQLiteHelper;
	private Cursor cursor;
    private long time = MyApplication.getInstance().gettime();
    private Drawable shape_normal;
    private Drawable shape_low;
    private Drawable shape_high;
    private Drawable shape_high1;
    private Drawable shape_high2;
    private Drawable shape_high3;

    @SuppressWarnings("deprecation")
	public MyBpCursorAdapter(Context context, int layout, Cursor c,
                             String[] from, int[] to) {
        super(context, layout, c, from, to); 
        // TODO Auto-generated constructor stub 
   
    } 
   
    @Override 
    public View getView(final int position, View convertView, ViewGroup parent) { 
        // TODO Auto-generated method stub 
        // listview每次得到一个item，都要view去绘制，通过getView方法得到view 
        // position为item的序号 
        View view = null; 
        if (convertView != null) { 
            view = convertView; 
            // 使用缓存的view,节约内存 
            // 当listview的item过多时，拖动会遮住一部分item，被遮住的item的view就是convertView保存着。 
            // 当滚动条回到之前被遮住的item时，直接使用convertView，而不必再去new view() 
   
        } else { 
            view = super.getView(position, convertView, parent); 
   
        } 
       BpSQLiteHelper=new MySQLiteHelper(MyApplication.getInstance(), "notepad.db", null, 1);
		db=BpSQLiteHelper.getReadableDatabase();
        Date datenow = new Date();
        Date dateprev = new Date();
        datenow.setTime(System.currentTimeMillis());
        String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
        dateprev.setTime(datenow.getTime() - time*86400000);//ms
        String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
        String sql = "select * from bptable where tm Between '" +mFrom +"' and '"+mCurrent + "'";
        //cursor = db.rawQuery(sql, null);
    	cursor=db.query("bptable", new String[]{"_id","hbp","lbp","rt","tm","cond"}, null, null, null, null, null);
    	cursor.moveToPosition(position);
    	String cdString = cursor.getString(cursor
				.getColumnIndex("cond"));
        TextView cd = (TextView)view.findViewById(R.id.bp_condition);
        TextView hbp = (TextView)view.findViewById(R.id.TextView01);
        TextView lbp = (TextView)view.findViewById(R.id.TextView02);
        shape_normal= MyApplication.getInstance().getResources().getDrawable(R.drawable.shape_normal);
        shape_low= MyApplication.getInstance().getResources().getDrawable(R.drawable.shape_circle);
        shape_high= MyApplication.getInstance().getResources().getDrawable(R.drawable.shape_high);
        shape_high1= MyApplication.getInstance().getResources().getDrawable(R.drawable.shape_high1);
        shape_high2= MyApplication.getInstance().getResources().getDrawable(R.drawable.shape_high2);
        shape_high3= MyApplication.getInstance().getResources().getDrawable(R.drawable.shape_high3);
        if (cdString.equals("正常")) {

        	hbp.setTextColor(Color.parseColor("#8BC34A"));
            lbp.setTextColor(Color.parseColor("#8BC34A"));
            //cd.setTextColor(Color.parseColor("#8BC34A"));
            cd.setBackground(shape_normal);
		}
        else if (cdString.equals("偏低")) {
            hbp.setTextColor(Color.parseColor("#03A9F4"));
            lbp.setTextColor(Color.parseColor("#03A9F4"));
            //cd.setTextColor(Color.parseColor("#03A9F4"));
            cd.setBackground(shape_low);
		}
        else if (cdString.equals("正常高")) {
            hbp.setTextColor(Color.parseColor("#ffd700"));
            lbp.setTextColor(Color.parseColor("#ffd700"));
            //cd.setTextColor(Color.parseColor("#ffd700"));
            cd.setBackground(shape_high);
		}
        else if (cdString.equals("一级高")) {
            hbp.setTextColor(Color.parseColor("#ff6100"));
            lbp.setTextColor(Color.parseColor("#ff6100"));
            //cd.setTextColor(Color.parseColor("#ff6100"));
            cd.setBackground(shape_high1);
		}
        else if (cdString.equals("二级高")) {
            hbp.setTextColor(Color.parseColor("#ff0000"));
            lbp.setTextColor(Color.parseColor("#ff0000"));
            //cd.setTextColor(Color.parseColor("#ff0000"));
            cd.setBackground(shape_high2);
        }
        else if (cdString.equals("三级高")) {
            hbp.setTextColor(Color.parseColor("#cc0000"));
            lbp.setTextColor(Color.parseColor("#cc0000"));
            //cd.setTextColor(Color.parseColor("#cc0000"));
            cd.setBackground(shape_high3);
        }
        else  {
            hbp.setTextColor(Color.parseColor("#FFFFFF"));
            lbp.setTextColor(Color.parseColor("#FFFFFF"));
           //cd.setTextColor(Color.parseColor("#FFFFFF"));
            cd.setBackground(shape_high3);
		}

        //view.setBackgroundColor(Color.parseColor("#ffffff"));
        return super.getView(position, view, parent); 
    } 
   
}
