package com.test.btclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;  
import java.util.TimerTask;  

import org.achartengine.ChartFactory;   
import org.achartengine.GraphicalView;  
import org.achartengine.chart.PointStyle;   
import org.achartengine.model.XYMultipleSeriesDataset;   
import org.achartengine.model.XYSeries;   
import org.achartengine.renderer.XYMultipleSeriesRenderer;   
import org.achartengine.renderer.XYSeriesRenderer;   

import zju.bme.bp.R;
import android.R.integer;
import android.app.Activity;   
import android.content.Context;  
import android.content.Intent;
import android.graphics.Color;   
import android.graphics.Paint.Align;  
import android.os.Bundle;   
import android.os.Environment;
import android.os.Handler;  
import android.os.Message;  
import android.util.Log;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;  
import android.widget.LinearLayout;  
  
public class SearchActivity extends Activity {  
	//�����õ���achartengine����ͼ
      
    private Timer timer = new Timer();  
    private TimerTask task;  
    private Handler handler;  
    private String title = "Signal Strength";  
    private XYSeries series;  
    private XYMultipleSeriesDataset mDataset1;  
    private GraphicalView chart;  
    private XYMultipleSeriesRenderer renderer;  
    private Context context;  
    private int addX = -1, addY;  
    ArrayList<Integer> intArray =	BTClient.mArray;//������arraylist
    //ArrayList<Integer> intArray;
    int j=19;
    int x=0;
    int count1=0;
    int count2=0;  
    int[] xv = new int[100];  
    int[] yv = new int[100];  
    int z=0;
     
    byte[] buffer = new byte[2048];
	byte[] buffer_new = new byte[2048];
	int i = 0;
	int n = 0;
	int num = 0;
	
	
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_graphic);
        series = new XYSeries(title); 
        mDataset1 = new XYMultipleSeriesDataset();
        //read(); 
	    initGraph();//��������ĺ���
	    
	   
        
          
    }  
    
    public void lineView(){}

            
    private void initGraph() {
		// TODO Auto-generated method stub
    	context = getApplicationContext();  
    	mDataset1 = new XYMultipleSeriesDataset();  
        //这里获得main界面上的布局，下面会把图表画在这个布�?里面    shenmegui
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear);  
        //series = new XYSeries(title); 
        mDataset1.removeSeries(series);
        series.clear();
        /*
        if (mUserDataManager == null) {
			mUserDataManager = new UserDataManager(this);
		} 
        //这个类用来放置曲线上的所有点，是�?个点的集合，根据这些点画出曲�?         
        ArrayList<Float> weightList= new  ArrayList<Float>();
        String id= WeightDAO.getInstance().getUseId();
        weightList=mUserDataManager.getWeightData(id);
 
        mUserDataManager.openDatabase();
        ArrayList<Weight> weightList2 = new  ArrayList<Weight>();
        weightList2 =mUserDataManager.getWeightList(id);
        
        
        mUserDataManager.closeDatabase();
		//String str = date ;
		//str=str.trim();
		//String str2="";
		//if(str != null && !"".equals(str)){
		//for(int i=0;i<str.length();i++){
		//if(str.charAt(i)>=48 && str.charAt(i)<=57){
		//str2+=str.charAt(i);
		//}
		
         */  
     
/*	  final long timeInterval = 100;
	  Runnable runnable = new Runnable() {
	  public void run() {
	    while (true) {
	        int j=19;
	        int x=0;
	        int count1=0;
	        int count2=0;        
	 
	        ArrayList<Integer> intArray =	BTClient.mArray;
	        
	        for(int i=0; i<(1541); i++){
	        	
	        	if(j<intArray.size()-12){
	        	float y =  intArray.get(j)*256+intArray.get(j+1); //���Ͻ�
	        	x++;
	        	series.add(x, y);
	        	Log.i("Sx",String.valueOf(x));
	        	Log.i("Sy",String.valueOf(y));
	        	Log.i("Sj",String.valueOf(j));
	         	count1++;
	         	if(count1<250){
	         		j=j+6;
	         	}else{
	         		count1=0;
	       		    count2++;
	         		j=count2*1541+19;
	         	}                     	                    	
	        	
	        }else{
	        	j=19;
	        	count1=0;
	        	count2=0;
	        }
	            }

	      try {
	       Thread.sleep(timeInterval);
	      } catch (InterruptedException e) {
	        e.printStackTrace();
	      }
	      }
	    }
	  };
	  Thread thread = new Thread(runnable);
	  thread.start();
	  
*/
        mDataset1.addSeries(series);  
        
        //���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��  
        int color = Color.GREEN;  
        PointStyle style = PointStyle.CIRCLE;  
        renderer = buildRenderer(color, style, true);  
              
            //设置好图表的样式  
            setChartSettings(renderer, "X", "Y", 0, 100, -150, 100, Color.WHITE, Color.WHITE);  
              
            //生成图表  
            chart = ChartFactory.getCubeLineChartView(context, mDataset1, renderer,0.3f);  
              
            //将图表添加到布局中去  
            layout.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
                                   
    //�����Handlerʵ������������Timerʵ������ɶ�ʱ����ͼ��Ĺ���
    handler = new Handler() {
    @Override
    public void handleMessage(Message msg) 
    {
     //ˢ��ͼ��
     try {
		updateChart();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     super.handleMessage(msg);
    }
    };
    
    task = new TimerTask() {
    @Override
    public void run() {
    Message message = new Message();
        message.what = 1;
        handler.sendMessage(message);
    }
    };
    
    timer.schedule(task, 1000, 1000);
    
}
         
        //mDataset1 = new XYMultipleSeriesDataset();             
         
	@Override
    protected void onResume(){
    	super.onResume();
    	Log.d("onResume", "onResume Method is executed");
    	initGraph();
    }
            
	@Override  
    public void onDestroy() {  
        //当结束程序时关掉Timer  
        timer.cancel();  
        super.onDestroy();  
    }  
      
    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {  
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
          
        //设置图表中曲线本身的样式，包括颜色�?�点的大小以及线的粗细等  
        XYSeriesRenderer r = new XYSeriesRenderer();  
        r.setColor(color);  
        r.setPointStyle(style);  
        r.setFillPoints(fill);  
        r.setLineWidth(3);  
        renderer.addSeriesRenderer(r);  
          
        return renderer;  
    }  
      
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,  
                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {  
        //有关对图表的渲染可参看api文档  
        renderer.setChartTitle(title);  
        renderer.setXTitle(xTitle);  
        renderer.setYTitle(yTitle);  
        renderer.setXAxisMin(xMin);  
        renderer.setXAxisMax(xMax);  
        renderer.setYAxisMin(yMin);  
        renderer.setYAxisMax(yMax);  
        renderer.setAxesColor(axesColor);  
        renderer.setLabelsColor(labelsColor);  
        renderer.setShowGrid(true);  
        renderer.setGridColor(Color.RED);  
        renderer.setXLabels(20);  
        renderer.setYLabels(10);  
        renderer.setXTitle("Time");  
        renderer.setYTitle("dBm");  
        renderer.setYLabelsAlign(Align.RIGHT);  
        renderer.setPointSize((float) 2);  
        renderer.setShowLegend(false);  
    }  

    private void updateChart() {
    
    //���ú���һ����Ҫ���ӵĽڵ�
/*	if(j<intArray.size()-12){
    addX = 0;
//    addY = intArray.get(j+4)*256+intArray.get(j+5);
    
    addY =-( intArray.get(j+4)*256+intArray.get(j+5));
    count1++;
	Log.i("addY",String.valueOf(addY));
	Log.i("Sj",String.valueOf(j));
 	if(count1<250){
 		j=j+6;
 	}else{
 		count1=0;
		    count2++;
 		j=count2*1541+19;
 	}
	}*/
    	
    try {
		addX=0;
		addY=intArray.get(count2*1541+1519+z);//����1000��1000
		Log.i("addY",String.valueOf(addY));
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    if(z<19){
    z++;
    }else{
    	count2++;
    	z=0;
    }
    
    //�Ƴ����ݼ��оɵĵ㼯
    mDataset1.removeSeries(series);
    //�жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������100�������Ե���������100ʱ��������Զ��100
    int length = series.getItemCount();
    if (length > 100) {
     length = 100;
    }
 //���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
 try {
	for (int i = 0; i < length; i++) {
		Log.i("JYM",Integer.toString(yv[i]));
	 xv[i] = (int) series.getX(i) + 1;
	 yv[i] = (int) series.getY(i);
	 }
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 //�㼯����գ�Ϊ�������µĵ㼯��׼��
 series.clear();
 
 //���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
 //�����������һ�°�˳��ߵ�������ʲôЧ������������ѭ���壬������²����ĵ�
 series.add(addX, addY);
 try {
	for (int k = 0; k < length; k++) {
	     series.add(xv[k], yv[k]);
	     }
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 //�����ݼ�������µĵ㼯
 mDataset1.addSeries(series);
 
 //��ͼ���£�û����һ�������߲�����ֶ�̬
 //����ڷ�UI���߳��У���Ҫ����postInvalidate()������ο�api
 chart.invalidate();
     }
    }