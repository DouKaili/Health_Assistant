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
	//这里用的是achartengine画的图
      
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
    ArrayList<Integer> intArray =	BTClient.mArray;//传递来arraylist
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
	    initGraph();//调用下面的函数
	    
	   
        
          
    }  
    
    public void lineView(){}

            
    private void initGraph() {
		// TODO Auto-generated method stub
    	context = getApplicationContext();  
    	mDataset1 = new XYMultipleSeriesDataset();  
        //杩欓噷鑾峰緱main鐣岄潰涓婄殑甯冨眬锛屼笅闈細鎶婂浘琛ㄧ敾鍦ㄨ繖涓竷灞?閲岄潰    shenmegui
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear);  
        //series = new XYSeries(title); 
        mDataset1.removeSeries(series);
        series.clear();
        /*
        if (mUserDataManager == null) {
			mUserDataManager = new UserDataManager(this);
		} 
        //杩欎釜绫荤敤鏉ユ斁缃洸绾夸笂鐨勬墍鏈夌偣锛屾槸涓?涓偣鐨勯泦鍚堬紝鏍规嵁杩欎簺鐐圭敾鍑烘洸绾?         
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
	        	float y =  intArray.get(j)*256+intArray.get(j+1); //左上角
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
        
        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄  
        int color = Color.GREEN;  
        PointStyle style = PointStyle.CIRCLE;  
        renderer = buildRenderer(color, style, true);  
              
            //璁剧疆濂藉浘琛ㄧ殑鏍峰紡  
            setChartSettings(renderer, "X", "Y", 0, 100, -150, 100, Color.WHITE, Color.WHITE);  
              
            //鐢熸垚鍥捐〃  
            chart = ChartFactory.getCubeLineChartView(context, mDataset1, renderer,0.3f);  
              
            //灏嗗浘琛ㄦ坊鍔犲埌甯冨眬涓幓  
            layout.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));  
                                   
    //这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
    handler = new Handler() {
    @Override
    public void handleMessage(Message msg) 
    {
     //刷新图表
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
        //褰撶粨鏉熺▼搴忔椂鍏虫帀Timer  
        timer.cancel();  
        super.onDestroy();  
    }  
      
    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {  
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();  
          
        //璁剧疆鍥捐〃涓洸绾挎湰韬殑鏍峰紡锛屽寘鎷鑹层?佺偣鐨勫ぇ灏忎互鍙婄嚎鐨勭矖缁嗙瓑  
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
        //鏈夊叧瀵瑰浘琛ㄧ殑娓叉煋鍙弬鐪媋pi鏂囨。  
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
    
    //设置好下一个需要增加的节点
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
		addY=intArray.get(count2*1541+1519+z);//呼吸1000，1000
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
    
    //移除数据集中旧的点集
    mDataset1.removeSeries(series);
    //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
    int length = series.getItemCount();
    if (length > 100) {
     length = 100;
    }
 //将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
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
 //点集先清空，为了做成新的点集而准备
 series.clear();
 
 //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
 //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
 series.add(addX, addY);
 try {
	for (int k = 0; k < length; k++) {
	     series.add(xv[k], yv[k]);
	     }
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 //在数据集中添加新的点集
 mDataset1.addSeries(series);
 
 //视图更新，没有这一步，曲线不会呈现动态
 //如果在非UI主线程中，需要调用postInvalidate()，具体参考api
 chart.invalidate();
     }
    }