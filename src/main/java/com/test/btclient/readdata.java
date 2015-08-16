package com.test.btclient;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.test.btclient.BTClient.DrawThread;

import zju.bme.bp.R;
import android.R.integer;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.SurfaceHolder.Callback;

public class readdata extends Activity {
	
	    byte[] buffer = new byte[2048];
		byte[] buffer_new = new byte[2048];
		int ii = 0;
		int n = 0;
		int num = 0;
		ArrayList<Byte[]> b = new ArrayList<Byte[]>(); 
		
		static int i=0;//用于显示时的循环计数
		final int HEIGHT=2500;   //设置画图范围高度2500
	    final int WIDTH=HEIGHT*45/32;    //画图范围宽度3515.625
	    final int X_OFFSET = 5;  //x轴（原点）起始位置偏移画图范围一点 
	    int cx = WIDTH/10;  //实时x的坐标    351.5625
	    int centerY = 1*HEIGHT /20;  //y轴的位置   125
	    SurfaceHolder holder = null;    //画图使用，可以控制一个SurfaceView
	    Paint paint = null;      //画笔
	    SurfaceView surface = null;     //
	    Timer timer = new Timer();       //一个时间控制的对象，用于控制实时的x的坐标，
	    //使其递增，类似于示波器从前到后扫描
	    TimerTask task = null;   //时间控制对象的一个任务
	    
	    Paint p = new Paint();
	    int xtime=0;
	    int lxtime=0;
	    public static ArrayList<Integer> mArray=new ArrayList<Integer>();
	    int j =19;	
	    int count1=0;
	    int count2=0;
	    public static int tempa = 0;                //临时变量用于保存接收到的数据
	    static int temp2 = 0;                //临时变量用于保存接收到的数据
	    static int temp3 = 0;                //临时变量用于保存接收到的数据
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data);
		
		surface = (SurfaceView)findViewById(R.id.read_show);        
        //初始化SurfaceHolder对象
        holder = surface.getHolder();  
        holder.setFixedSize(WIDTH+5, HEIGHT+5);  //设置画布大小，要比实际的绘图位置大一点，画布和绘图区域并不一致
        paint = new Paint();  

        paint.setStrokeWidth(50);  //画笔宽度？ 

        
        //添加按钮监听器  清除TextView内容（什么意思？）
        holder.addCallback(new Callback() {  //按照上面注释，添加回调函数
            public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){ 
                drawBack(holder); 
                //如果没有这句话，会使得在开始运行程序，整个屏幕没有白色的画布出现
                //直到按下按键，因为在按键中有对drawBack(SurfaceHolder holder)的调用
            } 
 
            public void surfaceCreated(SurfaceHolder holder) { 
                // TODO Auto-generated method stub 
            } 
 
            public void surfaceDestroyed(SurfaceHolder holder) { 
                // TODO Auto-generated method stub 
           
            }
        });  

        read();
       // draw(); //这里有问题,估计还是读不到数据，导致nullpointexception
        //new DrawThread().start();
}
	public void onSelectButtonClicked(View v){
		
		new DrawThread().start();
	}
	
	public void  read(){
		try {
			File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
			File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
			if(BuildDir.exists()==false) BuildDir.mkdirs();
			File File =new File(BuildDir, "1.01.txt");  //新建文件句柄，如已存在仍新建文档
			FileInputStream fis = new FileInputStream(File);
			//int length = fis.available();
			//byte[] buffer = new byte[length];
			num = fis.read(buffer);//需要分离吗
//			String s0 = new String(buffer,0,num);
//			String[] str = s0.split(" ");
//			int len = str.length;
//			for(int m = 0;m<len;m++){
//				
//			}
			fis.close();
			for(ii=0;ii<num;ii++){
				if((buffer[ii] == 0x0d)&&(buffer[ii+1]==0x0a)){  
					buffer_new[n] = 0x0a;
					ii++;
				}else{
					buffer_new[n] = buffer[ii];
				}
				n++;
			 }
			for(int k=0; k<n; k++){
				mArray.add((int)buffer_new[k]);// 将buffer_new的值赋给Arraylist

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	 public static int byteToInt(byte[] b){
    	  return (((int)b[0])+((int)b[1])*256);
    }
            
	//绘图线程，实时获取temp 数值即是y值
	 public class DrawThread extends Thread {
		public void draw() {
			// TODO Auto-generated method stub
			//drawBack(holder);    //画出背景和坐标轴
           if(task != null){ 
               task.cancel(); 
           } 
           task = new TimerTask() { //新建任务
               
               @Override 
               public void run() {                 	
               	//获取每一次实时的y坐标值                   	                	                   	
                   if(j<mArray.size()-12){
                   	
                    	tempa =  mArray.get(j)*256+mArray.get(j+1); //左上角
                    	temp2 =  mArray.get(j+2)*256+mArray.get(j+3);//左下角
                    	temp3 =  mArray.get(j+4)*256+mArray.get(j+5);//右上角
                    	
                    	int temp11=mArray.get(j);
                    	Log.i("temp11",String.valueOf(temp11));
                    	int temp12=mArray.get(j+1);
                    	Log.i("temp12",String.valueOf(temp12));
                    	int temp13=mArray.get(j+2);
                    	Log.i("temp13",String.valueOf(temp13));
                    	int temp14=mArray.get(j+3);
                    	Log.i("temp14",String.valueOf(temp14));
                    	int temp15=mArray.get(j+4);
                    	Log.i("temp15",String.valueOf(temp15));
                    	int temp16=mArray.get(j+5);
                    	Log.i("temp16",String.valueOf(temp16));//log？
                    	
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
                   
//                   System.out.print(mArray);
                   Log.i("mArraysize",String.valueOf(mArray.size()));
                   Log.i("tempa",String.valueOf(tempa));  
                   Log.i("temp2",String.valueOf(temp2));
                   Log.i("temp3",String.valueOf(temp3));
                   Log.i("j",String.valueOf(j));
                   
               	int cy1 = 4*HEIGHT/10 - tempa ; //实时获取的temp数值，对于画布来说最左上角是原点
               	int cy2 = 4*HEIGHT/10 - temp2 ; //实时获取的temp数值，对于画布来说最左上角是原点
               	int cy3 = 4*HEIGHT/10 - temp3 ; //实时获取的temp数值，对于画布来说最左上角是原点
               	
//               	int cy1 = 100 ; //实时获取的temp数值，对于画布来说最左上角是原点
//               	int cy2 = 150 ; //实时获取的temp数值，对于画布来说最左上角是原点
//               	int cy3 = 200 ;
               	
                   Canvas canvas = holder.lockCanvas(new Rect(cx-2,cy1-2,cx+2,cy1+2)); //似乎不写作线程就会变成null。。但还是画不出图
                   //锁定画布，只对其中Rect这块区域做改变，减小工程量
           		   paint.setColor(Color.RED);  //画波形的颜色是红色的，区别于坐标轴黑色
                   canvas.drawPoint(cx, cy1, paint); //打点
                   holder.unlockCanvasAndPost(canvas);  //解锁画布
                   
                   canvas = holder.lockCanvas(new Rect(cx-2,cy2+HEIGHT/2-2,cx+2,cy2+HEIGHT/2+2)); 
           		   paint.setColor(Color.GREEN);  //画波形的颜色是绿色的，区别于坐标轴黑色
                   canvas.drawPoint(cx, cy2+HEIGHT/2, paint); //打点
                   holder.unlockCanvasAndPost(canvas);  //解锁画布
                   
                   canvas = holder.lockCanvas(new Rect(cx+WIDTH/2-2,cy3-2,cx+WIDTH/2+2,cy3+2));
           		   paint.setColor(Color.BLUE);  //画波形的颜色是蓝色的，区别于坐标轴黑色
                   canvas.drawPoint(cx+WIDTH/2, cy3, paint); //打点
                   holder.unlockCanvasAndPost(canvas);  //解锁画布
                   
                   
                   cx++;    //cx 自增， 就类似于随时间轴的图形  
                                                                             
                   
                   if(cx >=5*WIDTH/10){                       
                       cx=WIDTH/10;     //如果画满则从头开始画                   
                       drawBack(holder);  //画满之后，清除原来的图像，从新开始    
                   }                    
               }
           }; 
           timer.schedule(task, 0, 1); //隔1ms被执行一次该循环任务，画出图形， 
           //简单一点就是1ms画出一个点，然后依次下去 
           
		}	 
	 }
	
	
   //设置画布背景色，设置XY轴的位置
   private void drawBack(SurfaceHolder holder){ 
       Canvas canvas= holder.lockCanvas(); //锁定画布
       //绘制白色背景 
       canvas.drawColor(Color.WHITE); 
       p.setColor(Color.BLACK); 
       p.setStrokeWidth(2);          
       //绘制坐标轴 
      canvas.drawLine(X_OFFSET, HEIGHT*2/5, WIDTH, HEIGHT*2/5, p); //绘制X轴 前四个参数是坐标
      canvas.drawLine(WIDTH/10, X_OFFSET, WIDTH/10, HEIGHT, p); //绘制Y轴 前四个参数是起始坐标
      canvas.drawLine(X_OFFSET, HEIGHT*9/10, WIDTH, HEIGHT*9/10, p); //绘制X轴 前四个参数是坐标
      canvas.drawLine(WIDTH*3/5, X_OFFSET, WIDTH*3/5, HEIGHT, p); //绘制Y轴 前四个参数是起始坐标
      
      p.setStrokeWidth(100);
      canvas.drawText("0",WIDTH/20,HEIGHT*4/10, p); //绘制坐标轴的数据
      canvas.drawText("100",WIDTH/20,HEIGHT*3/10, p); //绘制坐标轴的数据
      canvas.drawText("200",WIDTH/20,HEIGHT*2/10, p); //绘制坐标轴的数据
      canvas.drawText("300",WIDTH/20,HEIGHT*1/10, p); //绘制坐标轴的数据
      canvas.drawText("电压/mV temp",WIDTH/20,HEIGHT*1/20, p); //绘制坐标轴的数据      
      
      canvas.drawText("0",WIDTH/20,HEIGHT*9/10, p); //绘制坐标轴的数据
      canvas.drawText("100",WIDTH/20,HEIGHT*8/10, p); //绘制坐标轴的数据
      canvas.drawText("200",WIDTH/20,HEIGHT*7/10, p); //绘制坐标轴的数据
      canvas.drawText("300",WIDTH/20,HEIGHT*6/10, p); //绘制坐标轴的数据
      canvas.drawText("电压/mV temp2",WIDTH/20,HEIGHT*10/20, p); //绘制坐标轴的数据   
      
      canvas.drawText("0",11*WIDTH/20,HEIGHT*4/10, p); //绘制坐标轴的数据
      canvas.drawText("100",11*WIDTH/20,HEIGHT*3/10, p); //绘制坐标轴的数据
      canvas.drawText("200",11*WIDTH/20,HEIGHT*2/10, p); //绘制坐标轴的数据
      canvas.drawText("300",11*WIDTH/20,HEIGHT*1/10, p); //绘制坐标轴的数据
      canvas.drawText("电压/mV temp3",11*WIDTH/20,HEIGHT*1/20, p); //绘制坐标轴的数据          
      
      canvas.drawText("0",11*WIDTH/20,HEIGHT*9/10, p); //绘制坐标轴的数据
      canvas.drawText("100",11*WIDTH/20,HEIGHT*8/10, p); //绘制坐标轴的数据
      canvas.drawText("200",11*WIDTH/20,HEIGHT*7/10, p); //绘制坐标轴的数据
      canvas.drawText("300",11*WIDTH/20,HEIGHT*6/10, p); //绘制坐标轴的数据
      canvas.drawText("电压/mV",11*WIDTH/20,HEIGHT*10/20, p); //绘制坐标轴的数据  //最后一个有何用？
      
       holder.unlockCanvasAndPost(canvas);  //结束锁定 显示在屏幕上
       holder.lockCanvas(new Rect(0,0,0,0)); //锁定局部区域，其余地方不做改变
       holder.unlockCanvasAndPost(canvas);          
   }
}

