package zju.bme.bp;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

import zju.bme.bp.KCalendar.OnCalendarClickListener;
import zju.bme.bp.KCalendar.OnCalendarDateChangedListener;



public class bp_linechart extends TabActivity {
    public static MySQLiteHelper bpSQLiteHelper;
    private LineChartView chart;
    private LineChartData data;
    private PieChartView chart_pie;
    private PieChartData data_pie;
    private ColumnChartView chart_Column;
    private ColumnChartData columnData;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean hasLabels_line = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected_line = true;
    private boolean hasLabelsOutside = false;
    private boolean hasCenterCircle = false;
//各个按钮的定义
    private Button oneweek_bp ;
    private Button onemonth_bp;
 //   private Button threemonths_bp;
//    private Button oneweek1_bp ;
//    private Button onemonth1_bp;
//    private Button threemonths1_bp;
    private Drawable shape_pressed_bp;
    private Drawable shape_bp;


    private String date = null;
    private KCalendar calendar;
    private Cursor cursor;
    private ListView lv;
    private TextView tv;
    private int number=1;
    private String sql[] =new String[12];

    private int oneweek_pressed_bp = 0;
    private int onemonth_pressed_bp = 0;
 //   private int threemonths_pressed_bp = 0;
    private int j=0;
    private int q1=0;
    private int q2=0;
    private int q3=0;

    public  static SQLiteDatabase db;
    private String mCurrenttm;
    private String mCurrenttm1;
    private int count_rule;


    //@SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        bpSQLiteHelper = new MySQLiteHelper(bp_linechart.this, "notepad.db", null, 1);
        db=bpSQLiteHelper.getReadableDatabase();
        Date datenow = new Date();
        Date dateprev = new Date();
        datenow.setTime(System.currentTimeMillis());
        String mCurrent = DateFormat.format("yyyy-MM-dd", datenow).toString();
        dateprev.setTime(datenow.getTime() - 7*86400000);//ms
        String mFrom= DateFormat.format("yyyy-MM-dd", dateprev).toString();
        setContentView(R.layout.fragment_line_chart);
        TextView textView=(TextView)findViewById(R.id.title_chart);
        textView.setText(mFrom+" To "+mCurrent);

        mCurrenttm = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
        mCurrenttm1 = mCurrenttm.substring(0, 10);
        CalenderShow();
        oneweek_bp = (Button)findViewById(R.id.oneweek_bp);
        onemonth_bp=(Button)findViewById(R.id.onemonth_bp);
        onemonth_bp= (Button)findViewById(R.id.onemonth_bp);
//        threemonths_bp = (Button)findViewById(R.id.threemonths_bp);
//        oneweek1_bp = (Button)findViewById(R.id.oneweek1_bp);
//        onemonth1_bp = (Button)findViewById(R.id.onemonth1_bp);
//        threemonths1_bp = (Button)findViewById(R.id.threemonths1_bp);
        shape_pressed_bp= getResources().getDrawable(R.drawable.shape_pressed);
        shape_bp= getResources().getDrawable(R.drawable.shape);
        TabHost tabHost;
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("统计图").setContent(R.id.text1wl_bp));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("日志").setContent(R.id.text2wl_bp));
        tabHost.setCurrentTab(0);
        TabWidget tabWidget = tabHost.getTabWidget();
        for (int i =0; i < tabWidget.getChildCount(); i++) {
            //�޸�Tabhost�߶ȺͿ��
            tabWidget.getChildAt(i).getLayoutParams().height = 120;
            //tabWidget.getChildAt(i).getLayoutParams().width = 65;
            //�޸���ʾ�����С
            TextView tv = (TextView) tabWidget.getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(14);
            tv.setTextColor(Color.parseColor("#03A9F4"));
        }



        //layout = (LinearLayout)findViewById(R.id.bp_linechart);
        //onCreateView();
        //BPSQLiteHelper=new MySQLiteHelper(weight_linechart.this, "notepad.db", null, 1);

        //tv=(TextView) findViewById(R.id.TextView001wl);
        lv=(ListView) findViewById(R.id.ListView01w2);
        //���ݿ�

        MyApplication.getInstance().settime(7);
        oneweek_bp.setBackground(shape_pressed_bp);
        oneweek_bp.setTextColor(Color.WHITE);
        onemonth_bp.setBackground(shape_bp);
        onemonth_bp.setTextColor(Color.parseColor("#03A9F4"));
//        threemonths_bp.setBackground(shape_bp);
 //       threemonths_bp.setTextColor(Color.parseColor("#03A9F4"));
        oneweek_pressed_bp = 1;
//        oneweek1_bp.setBackground(shape_pressed_bp);
//        oneweek1_bp.setTextColor(Color.WHITE);
//        onemonth1_bp.setBackground(shape_bp);
//        onemonth1_bp.setTextColor(Color.parseColor("#03A9F4"));
//        threemonths1_bp.setBackground(shape_bp);
//        threemonths1_bp.setTextColor(Color.parseColor("#03A9F4"));
        //mLineChart = (LineChart) findViewById(R.id.spread_line_chart);
        chart = (LineChartView)findViewById(R.id.chart);
        number=1;
        showLineChart(getLineData(1,7));
        chart_Column =(ColumnChartView )findViewById(R.id.chart_column);
        showColumnChart(generateColumnData(4,7) );
        //mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        //PieData mPieData = getPieData(7);
        //showpieChart(mChart, mPieData);

        listview(mCurrenttm1);


    }

    public void onClickBack(View view) {
        finish();
    }
    public void OneWeek_BP(View view) {
        number=4;
        MyApplication.getInstance().settime(7);

        oneweek_bp.setBackground(shape_pressed_bp);
        oneweek_bp.setTextColor(Color.WHITE);
        onemonth_bp.setBackground(shape_bp);
        onemonth_bp.setTextColor(Color.parseColor("#03A9F4"));
//        threemonths_bp.setBackground(shape_bp);
//        threemonths_bp.setTextColor(Color.parseColor("#03A9F4"));//�Ժ��ټӸ�������֤�ظ���û��Ӱ��
//        oneweek1_bp.setBackground(shape_pressed_bp);
//        oneweek1_bp.setTextColor(Color.WHITE);
//        onemonth1_bp.setBackground(shape_bp);
//        onemonth1_bp.setTextColor(Color.parseColor("#03A9F4"));
//        threemonths1_bp.setBackground(shape_bp);
//        threemonths1_bp.setTextColor(Color.parseColor("#03A9F4"));
        if(oneweek_pressed_bp==0){

            showLineChart(getLineData(1,7));
            showColumnChart(generateColumnData(number,7) );
            TextView textView1=(TextView)findViewById(R.id.title_column);
            textView1.setText("近四周的血压平均值");
            //listview(7);
        }
        onemonth_pressed_bp=0;
        //threemonths_pressed_bp=0;
        oneweek_pressed_bp=1;
    }
    public void OneMonth_BP(View view) {
        number=12;

        MyApplication.getInstance().settime(30);
        onemonth_bp.setBackground(shape_pressed_bp);
        onemonth_bp.setTextColor(Color.WHITE);
        oneweek_bp.setBackground(shape_bp);
        oneweek_bp.setTextColor(Color.parseColor("#03A9F4"));
       // threemonths_bp.setBackground(shape_bp);
        //threemonths_bp.setTextColor(Color.parseColor("#03A9F4"));
//        onemonth1_bp.setBackground(shape_pressed_bp);
//        onemonth1_bp.setTextColor(Color.WHITE);
//        oneweek1_bp.setBackground(shape_bp);
//        oneweek1_bp.setTextColor(Color.parseColor("#03A9F4"));
//        threemonths1_bp.setBackground(shape_bp);
//        threemonths1_bp.setTextColor(Color.parseColor("#03A9F4"));
        if(onemonth_pressed_bp==0){
            ;
            showLineChart(getLineData(1,30));
            showColumnChart(generateColumnData(number,30) );
            TextView textView=(TextView)findViewById(R.id.title_column);
            textView.setText("近一年的血压平均值");


            TextView textView2=(TextView)findViewById(R.id.title_chart);

            String title_chart = sql[0].substring(40, 50)+" To "+sql[0].substring(69, 79);
            textView2.setText(title_chart);
            //PieData mPieData = getPieData(30);
            //showpieChart(mChart, mPieData);
            //listview(30);
        }
        onemonth_pressed_bp=1;
        //threemonths_pressed_bp=0;
        oneweek_pressed_bp=0;
    }
//    public void ThreeMonths_BP(View view) {
//        MyApplication.getInstance().settime(90);
//        //threemonths_bp.setBackground(shape_pressed_bp);
//      //  threemonths_bp.setTextColor(Color.WHITE);
//        onemonth_bp.setBackground(shape_bp);
//        onemonth_bp.setTextColor(Color.parseColor("#03A9F4"));
//        oneweek_bp.setBackground(shape_bp);
//        oneweek_bp.setTextColor(Color.parseColor("#03A9F4"));
////        threemonths1_bp.setBackground(shape_pressed_bp);
////        threemonths1_bp.setTextColor(Color.WHITE);
////        onemonth1_bp.setBackground(shape_bp);
////        onemonth1_bp.setTextColor(Color.parseColor("#03A9F4"));
////        oneweek1_bp.setBackground(shape_bp);
////        oneweek1_bp.setTextColor(Color.parseColor("#03A9F4"));
//       / if(threemonths_pressed_bp==0){
//
//            showLineChart(getLineData(90));
//            //PieData mPieData = getPieData(90);
//            //showpieChart(mChart, mPieData);
//            //listview(90);
//        }
//        onemonth_pressed_bp=0;
//        threemonths_pressed_bp=1;
//        oneweek_pressed_bp=0;
//    }

    private void showLineChart(LineChartData data){
        toggleLabelForSelected();
        chart.setLineChartData(data);
        chart.setOnValueTouchListener(new BP_ValueTouchListener());
        //chart = (LineChartView) findViewById(R.id.chart);
        chart.setViewportCalculationEnabled(false);
        chart.setValueSelectionEnabled(true);
        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 200, j, 0);
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }
    private void toggleLabelForSelected() {
        //hasLabelForSelected_line = !hasLabelForSelected_line;

        chart.setValueSelectionEnabled(hasLabelForSelected_line);

        if (hasLabelForSelected_line) {
            hasLabels_line = false;
        }
    }
    private void showPieChart(PieChartData data_pie){
        chart_pie.setPieChartData(data_pie);
        chart_pie.setOnValueTouchListener(new Pie_ValueTouchListener());
    }
    private void showColumnChart(ColumnChartData data_column){
        chart_Column.setColumnChartData(data_column);

        // Set value touch listener that will trigger changes for chartTop.
        chart_Column.setOnValueTouchListener(new ColumnValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chart_Column.setValueSelectionEnabled(true);

        chart_Column.setZoomType(ZoomType.HORIZONTAL);
    }
    private PieChartData getPieData(long count){
        List<SliceValue> values_pie = new ArrayList<SliceValue>();
        SliceValue sliceValue = new SliceValue((float) q1 /count* 30 + 15, ChartUtils.pickColor());
        values_pie.add(sliceValue);
        SliceValue sliceValue1 = new SliceValue((float) q2 /count* 30 + 15, ChartUtils.pickColor());
        values_pie.add(sliceValue1);
        SliceValue sliceValue2 = new SliceValue((float) q3 /count* 30 + 15, ChartUtils.pickColor());
        values_pie.add(sliceValue2);
        data_pie = new PieChartData(values_pie);
        data_pie.setHasLabels(hasLabels);
        //data_pie.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data_pie.setHasLabelsOutside(hasLabelsOutside);
        data_pie.setHasCenterCircle(hasCenterCircle);
        return data_pie;

    }
    private float[][] getbpData(String sql_buffer,long count) {//countһ��Ҫ��long����
        int bph_1=0;
        int bpl_1=0;
        int equal_count=1;
        int date_number=0;
        j=0;

        bpSQLiteHelper = new MySQLiteHelper(bp_linechart.this, "notepad.db", null, 1);
        db = bpSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql_buffer, null);//�½�һ��cursor����listview���ֿ�
        int k=0;
        String[] date_last = new String[cursor.getCount()];
        float[][] data_bp=new float[2][(int)count];
        if (cursor.getCount()!=0) {

            cursor.moveToFirst();
            do {

                //String tmString = cursor.getString(cursor.getColumnIndex("tm"));
                String tmString = cursor.getString(cursor
                        .getColumnIndex("tm"));
                String tmString1 = tmString.substring(0, 10);
                date_last[date_number]=tmString1;
                date_number++;
            }while (cursor.moveToNext());
        }
        if (cursor.getCount()!=0) {
            cursor.moveToFirst();
            do {
                //String tmString = cursor.getString(cursor.getColumnIndex("tm"));
                String tmString = cursor.getString(cursor
                        .getColumnIndex("tm"));
                String tmString1=tmString.substring(0,10);
                //StringTokenizer token = new StringTokenizer(tmString, " ");//���տո�Ͷ��Ž��н�ȡ
                String bphString = cursor.getString(cursor
                        .getColumnIndex("hbp"));
                int bph = Integer.parseInt(bphString);

                String bplString = cursor.getString(cursor
                        .getColumnIndex("lbp"));
                int bpl = Integer.parseInt(bplString);

                boolean result=false;
                if(k!=date_number-1)result=tmString1.equals(date_last[k+1]);
                if (result){
                    equal_count++;
                    bph_1=bph_1+bph;
                    bpl_1=bpl_1+bpl;
                }
                if(!result){
                    if (equal_count==1){
                        data_bp[0][j]=bph;
                        data_bp[1][j]=bpl;
                    }
                    else {
                        bph=(bph_1+bph)/equal_count;
                        bpl=(bpl_1+bpl)/equal_count;
                        data_bp[0][j]=bph;
                        data_bp[1][j]=bpl;
                        bpl_1=0;bph_1=0;equal_count=1;
                    }
                    j++;
                }
                k++;
                count--;

            } while (cursor.moveToNext());
        }
        return data_bp;
    }
    private LineChartData getLineData(int number,long count) {//countһ��Ҫ��long����
        //ArrayList<String> xValues = new ArrayList<String>();
       // ArrayList<Entry> yValues = new ArrayList<Entry>();
       // ArrayList<Integer> colors = new ArrayList<Integer>();
       //String date_last="0123456789";
        getsql(number,count);
        String sqlall = sql[number-1];
        float[][] Linedata=getbpData(sqlall,count);
        int Line_lengh=Linedata[0].length;
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Line> lines = new ArrayList<Line>();
        List<PointValue> values = new ArrayList<PointValue>();
        List<PointValue> values1 = new ArrayList<PointValue>();

        for (int i = 0; i < Line_lengh; ++i) {
            if(Linedata[0][i]!=0&&Linedata[1][i]!=0) {
                values.add(new PointValue((float) i, Linedata[0][i]));
                values1.add(new PointValue((float) i, Linedata[1][i]));
            }
        }
        Line line = new Line(values);
        Line line1 = new Line(values1);
        line.setColor(ChartUtils.COLOR_BLUE).setCubic(true);
        line1.setColor(ChartUtils.COLOR_GREEN).setCubic(true);
        //line.setColor(ChartUtils.COLORS[i]);
               /* line.setShape(shape);*/
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels_line);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected_line);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);

        line1.setCubic(isCubic);
        line1.setFilled(isFilled);
        line1.setHasLabels(hasLabels_line);
        line1.setHasLabelsOnlyForSelected(hasLabelForSelected_line);
        line1.setHasLines(hasLines);
        line1.setHasPoints(hasPoints);

        lines.add(line);
        lines.add(line1);
        data = new LineChartData(lines);
        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("日期");
                axisY.setName("mmHg");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);

        }
        data.setLines(lines);

        return data;
    }
    private void getsql(int number,long count){
        Date datenow = new Date();
        Date dateprev = new Date();

        datenow.setTime(System.currentTimeMillis());
        String mCurrent = DateFormat.format("yyyy-MM-dd    HH:mm:ss", datenow).toString();
        dateprev.setTime(datenow.getTime() - count*86400000);//ms
        String mFrom[]=new String[number+1];
        mFrom[0]=mCurrent;
        for(int i = 1; i <= number; ++i){
            dateprev.setTime(datenow.getTime() - i*count*86400000);
            mFrom[i] = DateFormat.format("yyyy-MM-dd    HH:mm:ss", dateprev).toString();
            sql[i-1]= "select * from bptable where tm Between '" +mFrom[i] +"' and '"+mFrom[i-1] + "'";
        }

    }
    private ColumnChartData generateColumnData(int numColumns,long count) {

        getsql(numColumns,count);
        int numSubcolumns = 2;
        float Columndata_bp[][][]=new float[numColumns][numSubcolumns][(int)count];
        float Columndata[][]=new float[numColumns][numSubcolumns];

            for (int i = 0; i < numColumns; ++i) {
                Columndata_bp[i]=getbpData(sql[i], count);
            }
        float count_real=0;
        float bph_av=0;
        float bpl_av=0;
        for (int k = 0; k < numColumns; ++k) {
            for (int i = 0; i < count; ++i) {
                bph_av=bph_av+Columndata_bp[k][0][i];
                bpl_av=bpl_av+Columndata_bp[k][1][i];
                if (Columndata_bp[k][0][i] == 0) {
                    count_real = i;
                    if(i!=0) {
                        Columndata[k][0] = bph_av / count_real;
                        Columndata[k][1] = bpl_av / count_real;
                    }
                    bph_av=0;bpl_av=0;
                    break;
                }
            }
        }

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();

            values.add(new SubcolumnValue(Columndata[numColumns-1-i][0], ChartUtils.COLOR_BLUE));
            values.add(new SubcolumnValue(Columndata[numColumns-1-i][1], ChartUtils.COLOR_GREEN));


            //axisValues.add(new AxisValue(i).setLabel(months[i]));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
//        for (int i = 0; i < Line_lengh; ++i) {
//            if(Columndata[0][i]!=0&&Columndata[1][i]!=0) {
//                values.add(new PointValue((float) i, Linedata[0][i], 120, 100));
//                values1.add(new PointValue((float) i, Linedata[1][i], 90, 40));
//            }
//        }

        columnData = new ColumnChartData(columns);

        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));



        columnData = new ColumnChartData(columns);
        if (hasAxes) {
            Axis axisX = new Axis(axisValues);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("日期");
                axisY.setName("mmHg");
            }
            columnData.setAxisXBottom(axisX);
            columnData.setAxisYLeft(axisY);
        } else {
            columnData.setAxisXBottom(null);
            columnData.setAxisYLeft(null);

        }
        chart_Column.setColumnChartData(columnData);
        return columnData;

    }
    private class BP_ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            //Toast.makeText(getApplication(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            hasLabels=true;
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub
            hasLabels=false;

        }

    }
    private class Pie_ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getApplication(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
    private class ColumnValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            TextView textView1=(TextView)findViewById(R.id.title_chart);


            if(oneweek_pressed_bp==1) {
                showLineChart(getLineData(4-columnIndex, 7));
                String title_chart = sql[3-columnIndex].substring(40, 50)+" To "+sql[3-columnIndex].substring(69, 79);
                textView1.setText(title_chart);
            }

            if(onemonth_pressed_bp==1){
            showLineChart(getLineData(12-columnIndex,30));
            String title_chart = sql[11-columnIndex].substring(40, 50)+" To "+sql[11-columnIndex].substring(69, 79);
            textView1.setText(title_chart);
            }


        }

        @Override
        public void onValueDeselected() {
        }
    }
    private void listview(String dateFormat){
        String dataFormat_start=dateFormat+"    00:00:00";
        String dataFormat_end=dateFormat+"    23:59:59";
        if (dateFormat==mCurrenttm1){ dataFormat_end=mCurrenttm;}
        final String sql_oneday = "select * from bptable where tm Between '" +dataFormat_start +"' and '"+dataFormat_end + "'";
        Cursor cursor = db.rawQuery(sql_oneday, null);
        int count1=cursor.getCount();
//        if (cursor.getCount()!=0) {
//        String tmString3 = cursor.getString(cursor
//                .getColumnIndex("wt"));}
        //Cursor cursor = db.query("bptable", new String[]{"_id", "hbp", "lbp", "rt", "tm"}, null, null, null, null, null);
        //cursor=db.query("weighttable", new String[]{"_id","wt","ht","bm","tm","cond"}, null, null, null, null, null);
        //ɾ������ʾΪ�ա���TextView
//        if(cursor.getCount()>0){
//            tv.setVisibility(View.GONE);
//        }
        //ListAdapter adapter = new MySimpleCursorAdapter(bp_linechart.this,R.layout.bp_item, cursor, new String[]{"hbp", "lbp", "rt"}, new int[]{R.id.TextView01_bp,R.id.TextView02_bp,R.id.TextView03_bp});

            ListAdapter adapter = new MySimpleCursorAdapter(bp_linechart.this, R.layout.weight_item, cursor, new String[]{"hbp", "lbp", "rt","tm"}, new int[]{R.id.TextView01s, R.id.TextView02s, R.id.TextView03s});
            //ListAdapter adapter = new MySimpleCursorAdapter(bp_linechart.this,R.layout.weight_item, cursor, new String[]{"wt","ht","bm","tm","cond"}, new int[]{R.id.TextView01s,R.id.TextView02s,R.id.TextView03s,R.id.TextView04s,R.id.wt_condition});
            lv.setAdapter(adapter);

    }

    private void CalenderShow(){
       // View view = findViewById(R.id.text2wl_bp);
        calendar = (KCalendar)findViewById(R.id.popupwindow_calendar);
        SimpleDateFormat ft1=new SimpleDateFormat("yyyy-MM-dd");
        //最大可以选日期
        Date maxDate=null;
        try {
            maxDate = ft1.parse(mCurrenttm1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        calendar.setMaxDay(maxDate);
        // calendar.setThisday(new Date(2011, 12, 22));
        calendar.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translucent_zoom_in));
        LinearLayout ll_popup = (LinearLayout)findViewById(R.id.text2wl_bp);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_bottom_in_1));
//			setWidth(LayoutParams.FILL_PARENT);
//			setHeight(LayoutParams.FILL_PARENT);
//			setBackgroundDrawable(new BitmapDrawable());
//			setFocusable(true);
//			setOutsideTouchable(true);
        //setContentView(view);
        //showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //update();

        final TextView popupwindow_calendar_month = (TextView)findViewById(R.id.popupwindow_calendar_month);
//			Button popupwindow_calendar_bt_enter = (Button) view
//					.findViewById(R.id.popupwindow_calendar_bt_enter);

        popupwindow_calendar_month.setText(calendar.getCalendarYear() + "年"
                + calendar.getCalendarMonth() + "月");

        if (null != date) {

            int years = Integer.parseInt(date.substring(0,
                    date.indexOf("-")));
            int month = Integer.parseInt(date.substring(
                    date.indexOf("-") + 1, date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            calendar.showCalendar(years, month);
            calendar.setCalendarDayBgColor(date,
                    R.drawable.calendar_date_focused);
        }

        List<String> list = new ArrayList<String>(); // 设置标记列表
        Date d=new Date(System.currentTimeMillis());
        SimpleDateFormat ft=new SimpleDateFormat("yyyy-MM-dd");
        list.add(ft.format(d));
        list.add("2014-10-02");
        list.add("2014-10-31");
        calendar.addMarks(list, 0);


        // 监听所选中的日期
        calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));

                if (calendar.getCalendarMonth() - month == 1// 跨年跳转
                        || calendar.getCalendarMonth() - month == -11) {
                    calendar.lastMonth();

                } else if (month - calendar.getCalendarMonth() == 1 // 跨年跳转
                        || month - calendar.getCalendarMonth() == -11) {
                    calendar.nextMonth();

                } else {
                    calendar.removeAllBgColor();
                    calendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_focused);
                    date = dateFormat;// 最后返回给全局 date
                    Log.e("Date", date);
                    //Toast.makeText(this, "可选"+date,Toast.LENGTH_SHORT).show();
                    listview(dateFormat);
                }
            }

            @Override
            public void onCalendarNorClick() {
                //Toast.makeText(CalenderShow.this, "不可选"+date,Toast.LENGTH_SHORT).show();
            }
        });

        // 监听当前月份
        calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month
                        .setText(year + "年" + month + "月");
            }
        });
        //设置标记



        // 上月监听按钮
        LinearLayout popupwindow_calendar_last_month = (LinearLayout)findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        calendar.lastMonth();
                    }

                });

        // 下月监听按钮
        LinearLayout popupwindow_calendar_next_month = (LinearLayout) findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        calendar.nextMonth();
                    }
                });
        // 当天监听按钮
        TextView popupwindow_calendar_now = (TextView) findViewById(R.id.popupwindow_calendar_now);
        popupwindow_calendar_now.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                calendar.toNow();
            }
        });
        // 关闭窗口
//			popupwindow_calendar_bt_enter
//					.setOnClickListener(new OnClickListener() {
//						public void onClick(View v) {
//							dismiss();
//						}
//					});
    }



}

