package com.test.btclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import zju.bme.bp.R;
import zju.bme.bp.add;
import zju.bme.bp.add_bp;
import zju.bme.bp.add_exercise;
import zju.bme.bp.add_weight;

import com.test.btclient.DeviceListActivity;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BTClient extends Activity {	
	public String data_num;
	public String channel_num; 
	public int[] send_num = new int[2];
	private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号	
	private InputStream is;    //输入流，用来接收蓝牙数据
    private String smsg = "00";    //显示用数据缓存
    private String fmsg = "00";    //保存用数据缓存       
    public static int tempa = 0;                //临时变量用于保存接收到的数据
    static int temp2 = 0;                //临时变量用于保存接收到的数据
    static int temp3 = 0;                //临时变量用于保存接收到的数据
    int READ = 1;                   //一个常量，用于传输数据消息队列的识别字   
    static int i=0;//用于显示时的循环计数
	final int HEIGHT=2500;   //设置画图范围高度2500
    final int WIDTH=HEIGHT*45/32;    //画图范围宽度3515.625
    final int X_OFFSET = 5;  //x轴（原点）起始位置偏移画图范围一点 
    private int cx = WIDTH/10;  //实时x的坐标    351.5625
    int centerY = 1*HEIGHT /20;  //y轴的位置   125
    private SurfaceHolder holder = null;    //画图使用，可以控制一个SurfaceView
    private Paint paint = null;      //画笔
    SurfaceView surface = null;     //
    Timer timer = new Timer();       //一个时间控制的对象，用于控制实时的x的坐标，
    //使其递增，类似于示波器从前到后扫描
    TimerTask task = null;   //时间控制对象的一个任务            
    public String filename=""; //用来保存存储的文件名
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;	
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备 
    Paint p = new Paint();
    int xtime=0;
    int lxtime=0;
    public static ArrayList<Integer> mArray=new ArrayList<Integer>();
    int j =19;	
    int count1=0;
    int count2=0;
    //byte[] buffers = new byte[2048];
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   //设置画面为主画面 main.xml        
        //Log.i("aaaaa","aaaaa");
        Button sin =(Button)findViewById(R.id.sin);         
        surface = (SurfaceView)findViewById(R.id.show);        
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
                                                      
       //添加按钮监听器 开启画图线程
        sin.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if (task != null){
					task.cancel();
				}
				//判断是否非空，否则报错（非空才取消，空不用取消）
           Intent intent= new Intent(BTClient.this,SearchActivity.class);
           startActivity(intent);
				
			}        	
        }); 
        
       //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (_bluetooth == null){
        	Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }        
        // 设置设备可以被搜索  
       new Thread(){
    	   public void run(){
    		   if(_bluetooth.isEnabled()==false){
        		_bluetooth.enable();
    		   }
    	   }   	   
       }.start(); 
       
       
    }
             
    //连接按键响应函数
    public void onConnectButtonClicked(View v){ 
    	if(_bluetooth.isEnabled()==false){  //如果蓝牙服务不可用则提示
    		Toast.makeText(this, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
    		return;
    	}
    	    	
        //如未连接设备则打开DeviceListActivity进行设备搜索
    	Button btn = (Button) findViewById(R.id.Button03);
    	
	    
    	if(_socket==null){
    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
    	}
    	else{
    		 //关闭连接socket
    	    try{    	    	
    	    	is.close(); //关闭输入流
    	    	_socket.close();
    	    	_socket = null;
    	    	bRun = false;
    	    	btn.setText("连接");//有何用？
    	    }catch(IOException e){}   
    	}
    	return;
    }
        
    //接收活动结果，响应startActivityForResult()（在上面）
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
    		// 响应返回结果
            if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                // MAC地址，由DeviceListActivity设置返回
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // 得到蓝牙设备句柄      
                _device = _bluetooth.getRemoteDevice(address); 
                // 用服务号得到socket
                try{
                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                }
                //连接socket
            	Button btn = (Button) findViewById(R.id.Button03);
                try{
                	_socket.connect();
                	Toast.makeText(this, "连接"+_device.getName()+"成功！", Toast.LENGTH_SHORT).show();
                	btn.setText("断开");
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                		_socket.close();
                		_socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                	}                	
                	return;
                }
                
//                mArray.add(0);
        	    new DrawThread().start();  //绘图线程启动 
//        	    Log.i("drawthread","start"); 
        	    
                //打开接收线程
                try{
            		is = _socket.getInputStream();   //得到蓝牙数据输入流
            		}catch(IOException e){
            			Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
            			return;
            		}
            		if(bThread==false){
            			ReadThread.start();
            			bThread=true;
            		}else{
            			bRun = true;
            		}
            }
    		break;
    	default:break;
    	}
    }
     
    //接收数据线程
    Thread ReadThread=new Thread(){
  	    	
    	public void run(){
    		int num = 0;
    		byte[] buffer = new byte[2048];
    		byte[] buffer_new = new byte[2048];
    		int i = 0;
    		int n = 0;
    		bRun = true;

    		//接收线程
    		while(true){
    			try{
    				while(is.available()==0){
    					while(bRun == false){}
    				}
    				while(true){
    					num = is.read(buffer);	//读入数据
    					//buffers = buffer;
    					n=0;    					
    					String s0 = new String(buffer,0,num);
    					fmsg+=s0;    //保存收到数据(+=把数据合起来了，如何分离？）
//    					Log.i("fmsg",fmsg);
   					   					    					
							for(i=0;i<num;i++){
								if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){  //Windows下在txt文件中敲下回车键，然后16进制观察，你会发现获得了2个字符，0x0d和0x0a
									buffer_new[n] = 0x0a;   //改这个有什么用？似乎并没有用
									i++;
								}else{
									buffer_new[n] = buffer[i];
								}
								n++;
							 }
							
    					String s = new String(buffer_new,0,n);    					

							for(int k=0; k<n; k++){
								mArray.add((int)buffer_new[k]);// 将buffer_new的值赋给Arraylist
//    						Log.i("k",String.valueOf(k));
							}
							
    					smsg+=s;   //写入接收缓存
//    					Log.i("smsg",smsg);
    					if(is.available()==0)break;  //短时间没有数据才跳出进行显示,否则一直循环
    				}
      	    		
    	    		}catch(IOException e){
    	    		}
    		}
    	}
    	

    }; 
        
    public static int byteToInt(byte[] b){
     	  return (((int)b[0])+((int)b[1])*256);
     }
             
	//绘图线程，实时获取temp 数值即是y值
	public class DrawThread extends Thread {
		public void run() {
			// TODO Auto-generated method stub
			drawBack(holder);    //画出背景和坐标轴
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
                    
//                    System.out.print(mArray);
                    Log.i("mArraysize",String.valueOf(mArray.size()));
                    Log.i("tempa",String.valueOf(tempa));  
                    Log.i("temp2",String.valueOf(temp2));
                    Log.i("temp3",String.valueOf(temp3));
                    Log.i("j",String.valueOf(j));
                    
                	int cy1 = 4*HEIGHT/10 - tempa ; //实时获取的temp数值，对于画布来说最左上角是原点
                	int cy2 = 4*HEIGHT/10 - temp2 ; //实时获取的temp数值，对于画布来说最左上角是原点
                	int cy3 = 4*HEIGHT/10 - temp3 ; //实时获取的temp数值，对于画布来说最左上角是原点
                	
                    Canvas canvas = holder.lockCanvas(new Rect(cx-2,cy1-2,cx+2,cy1+2)); 
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
                    
//                    canvas = holder.lockCanvas(new Rect(cx+WIDTH/2-2,cy1+HEIGHT/2-2,cx+WIDTH/2+2,cy1+HEIGHT/2+2));
//            		paint.setColor(Color.BLUE);  //画波形的颜色是绿色的，区别于坐标轴黑色
//                    canvas.drawPoint(cx+WIDTH/2, cy1+HEIGHT/2, paint); //打点
//                    holder.unlockCanvasAndPost(canvas);  //解锁画布
                    
                    cx++;    //cx 自增， 就类似于随时间轴的图形  
                                                                              
                    
/*                    //动态改变x时间轴的坐标
                    if (xtime<=100){       //每隔1ms进行xtime的递增，每隔0.1s进行lxtime递增，用于坐标绘制
                    xtime++;
                    }else{
                    	xtime=0;
                    	lxtime++;
                    }                    
                    String lx1=String.valueOf(lxtime);
                    String lx2=String.valueOf(lxtime+1);
                    String lx3=String.valueOf(lxtime+2);
                                     
                    canvas.drawText(lx1,2*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //绘制坐标轴的数据
                    canvas.drawText(lx2,3*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //绘制坐标轴的数据       
                    canvas.drawText(lx3,4*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //绘制坐标轴的数据  
                    canvas.drawText("时间/0.1s",5*WIDTH/10,19*HEIGHT/20, p); //绘制坐标轴的数据
                    canvas.drawText(lx1,7*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //绘制坐标轴的数据
                    canvas.drawText(lx2,8*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //绘制坐标轴的数据       
                    canvas.drawText(lx3,9*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //绘制坐标轴的数据  
                    canvas.drawText("时间/0.1s",9*WIDTH/10-X_OFFSET,19*HEIGHT/20, p); //绘制坐标轴的数据                    
                    canvas.drawText(lx1,2*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //绘制坐标轴的数据
                    canvas.drawText(lx2,3*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //绘制坐标轴的数据       
                    canvas.drawText(lx3,4*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //绘制坐标轴的数据  
                    canvas.drawText("时间/0.1s",5*WIDTH/10,9*HEIGHT/20, p); //绘制坐标轴的数据
                    canvas.drawText(lx1,7*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //绘制坐标轴的数据
                    canvas.drawText(lx2,8*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //绘制坐标轴的数据       
                    canvas.drawText(lx3,9*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //绘制坐标轴的数据  
                    canvas.drawText("时间/0.1s",9*WIDTH/10-X_OFFSET,9*HEIGHT/20, p); //绘制坐标轴的数据  
*/                    
                    
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
             
    //关闭程序调用处理部分
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null)  //关闭连接socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
    //	_bluetooth.disable();  //关闭蓝牙服务
    }
            
    //保存按键响应函数
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //清除按键响应函数
    public void onClearButtonClicked(View v){
    	smsg="00";
    	fmsg="00";
//    	timer.cancel();
    	return;
    }
    
    //退出按键响应函数
    public void onQuitButtonClicked(View v){
    	  final String[] arrayFruit = new String[] { "数据一", "数据二", "数据三", "数据四" };

    	  Dialog alertDialog = new AlertDialog.Builder(this).
    	    setTitle("请选择要读取的数据")
    	    .setItems(arrayFruit, new DialogInterface.OnClickListener() {
    	 
    	     @Override
    	     public void onClick(DialogInterface dialog, int which) {
                 send_num[0]=which;
    	    	 final String[] arrayFruit = new String[] { "通道1", "通道2", "通道3" };

    	    	  Dialog alertDialog = new AlertDialog.Builder(BTClient.this).
    	    	    setTitle("请选择通道")
    	    	    .setItems(arrayFruit, new DialogInterface.OnClickListener() {
    	    	 
    	    	     @Override
    	    	     public void onClick(DialogInterface dialog, int which) {
    	    	    	 send_num[1]=which;
    	    	    	 Intent readdata = new Intent(BTClient.this,zju.bme.bp.ecg.class); 
    	    	    	readdata.putExtra("send_num", send_num);
    	                 startActivity(readdata);     
    	    	     }
    	    	    }).
    	    	    setNegativeButton("取消", new DialogInterface.OnClickListener() {

    	    	     @Override
    	    	     public void onClick(DialogInterface dialog, int which) {
    	    	      // TODO Auto-generated method stub
    	    	     }
    	    	    }).
    	    	    create();
    	    	  alertDialog.show();
    	    	     
    	     }
    	    }).
    	    setNegativeButton("取消", new DialogInterface.OnClickListener() {

    	     @Override
    	     public void onClick(DialogInterface dialog, int which) {
    	      // TODO Auto-generated method stub
    	     }
    	    }).
    	    create();
    	  alertDialog.show();
//    	Intent readdata = new Intent(this,zju.bme.bp.ecg.class); 
//		startActivity(readdata);
    }
    
    //保存功能实现
	private void Save() {
		
		//显示对话框输入文件名		
		LayoutInflater factory = LayoutInflater.from(BTClient.this);  //图层模板生成器句柄
		final View DialogView =  factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
		new AlertDialog.Builder(BTClient.this)
								.setTitle("保存数据")
								.setView(DialogView)   //设置视图模板
								.setPositiveButton("确定",
								new DialogInterface.OnClickListener() //确定按键响应函数
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //得到文件名输入框句柄
										filename = text1.getText().toString();  //得到文件名
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
												
												filename =filename+".txt";   //在文件名末尾加上.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
												File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
												FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
												stream.write(fmsg.getBytes());//此处用到fmsg（只要保存不乱码就可以）
												stream.close();
												Toast.makeText(BTClient.this, "存储成功！", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(BTClient.this, "没有存储卡！", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}																														
									}
								})
								.setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //显示对话框
	} 
	
//	public void  read(byte[] buffer){
//		try {
//			File file = new File("/sdcard/ff.txt");
//			FileInputStream fis = new FileInputStream(file);
//			//int length = fis.available();
//			//byte[] buffer = new byte[length];
//			fis.read(buffer);
//			fis.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//	}
}