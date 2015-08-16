
package zju.bme.bp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.Utils;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * Custom implementation of the MarkerView.
 * 
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent;
    private SQLiteDatabase db;
	private MySQLiteHelper WeightSQLiteHelper;
	private Cursor cursor;
    private long time = MyApplication.getInstance().gettime();//getApplicationContext�����ã�û��������,����ʱ�������
	

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
 
        tvContent = (TextView) findViewById(R.id.tvContent);
        
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, int dataSetIndex) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
        	int i = e.getXIndex();
        	WeightSQLiteHelper=new MySQLiteHelper(MyApplication.getInstance(), "notepad.db", null, 1);
			db=WeightSQLiteHelper.getReadableDatabase();
            Date datenow = new Date();
            Date dateprev = new Date();
            datenow.setTime(System.currentTimeMillis());
            String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
            dateprev.setTime(datenow.getTime() - time*86400000);//ms
            Log.i("timee",time+"");
            String mFrom = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
            String sql = "select * from weighttable where tm Between '" +mFrom +"' and '"+mCurrent + "'";
            cursor = db.rawQuery(sql, null);
        	//cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
        	cursor.moveToPosition(i);
        	String cdString = cursor.getString(cursor
					.getColumnIndex("cond"));
            String wtString = cursor.getString(cursor
                    .getColumnIndex("wt"));
        	String tmString = cursor.getString(cursor
					.getColumnIndex("tm"));
        	StringTokenizer token = new StringTokenizer(tmString, " ");//���տո�Ͷ��Ž��н�ȡ 
     		
     		 String tm = token.nextToken();
            //tvContent.setText("����ֵ" + Utils.formatNumber(e.getXIndex(), 0, true));
            tvContent.setText(wtString+"���� "+cdString);
            //cursor.close();
            //db.close();������в������ݵ��߳�Ψһ�� �����̲߳������ݿ⡣ ���鲻Ҫclose�� �������������ܡ���������ж��̲߳������ǻ��ǽ���close�ɡ�
        }
    }

    @Override
    public int getXOffset() {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset() {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
