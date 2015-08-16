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
	private final static int REQUEST_CONNECT_DEVICE = 1;    //�궨���ѯ�豸���	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP����UUID��	
	private InputStream is;    //������������������������
    private String smsg = "00";    //��ʾ�����ݻ���
    private String fmsg = "00";    //���������ݻ���       
    public static int tempa = 0;                //��ʱ�������ڱ�����յ�������
    static int temp2 = 0;                //��ʱ�������ڱ�����յ�������
    static int temp3 = 0;                //��ʱ�������ڱ�����յ�������
    int READ = 1;                   //һ�����������ڴ���������Ϣ���е�ʶ����   
    static int i=0;//������ʾʱ��ѭ������
	final int HEIGHT=2500;   //���û�ͼ��Χ�߶�2500
    final int WIDTH=HEIGHT*45/32;    //��ͼ��Χ���3515.625
    final int X_OFFSET = 5;  //x�ᣨԭ�㣩��ʼλ��ƫ�ƻ�ͼ��Χһ�� 
    private int cx = WIDTH/10;  //ʵʱx������    351.5625
    int centerY = 1*HEIGHT /20;  //y���λ��   125
    private SurfaceHolder holder = null;    //��ͼʹ�ã����Կ���һ��SurfaceView
    private Paint paint = null;      //����
    SurfaceView surface = null;     //
    Timer timer = new Timer();       //һ��ʱ����ƵĶ������ڿ���ʵʱ��x�����꣬
    //ʹ�������������ʾ������ǰ����ɨ��
    TimerTask task = null;   //ʱ����ƶ����һ������            
    public String filename=""; //��������洢���ļ���
    BluetoothDevice _device = null;     //�����豸
    BluetoothSocket _socket = null;      //����ͨ��socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;	
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //��ȡ�����������������������豸 
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
        setContentView(R.layout.main);   //���û���Ϊ������ main.xml        
        //Log.i("aaaaa","aaaaa");
        Button sin =(Button)findViewById(R.id.sin);         
        surface = (SurfaceView)findViewById(R.id.show);        
        //��ʼ��SurfaceHolder����
        holder = surface.getHolder();  
        holder.setFixedSize(WIDTH+5, HEIGHT+5);  //���û�����С��Ҫ��ʵ�ʵĻ�ͼλ�ô�һ�㣬�����ͻ�ͼ���򲢲�һ��
        paint = new Paint();  

        paint.setStrokeWidth(50);  //���ʿ�ȣ� 

        
        //��Ӱ�ť������  ���TextView���ݣ�ʲô��˼����
        holder.addCallback(new Callback() {  //��������ע�ͣ���ӻص�����
            public void surfaceChanged(SurfaceHolder holder,int format,int width,int height){ 
                drawBack(holder); 
                //���û����仰����ʹ���ڿ�ʼ���г���������Ļû�а�ɫ�Ļ�������
                //ֱ�����°�������Ϊ�ڰ������ж�drawBack(SurfaceHolder holder)�ĵ���
            } 
 
            public void surfaceCreated(SurfaceHolder holder) { 
                // TODO Auto-generated method stub 
            } 
 
            public void surfaceDestroyed(SurfaceHolder holder) { 
                // TODO Auto-generated method stub 
           
            }
        });  
                                                      
       //��Ӱ�ť������ ������ͼ�߳�
        sin.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if (task != null){
					task.cancel();
				}
				//�ж��Ƿ�ǿգ����򱨴��ǿղ�ȡ�����ղ���ȡ����
           Intent intent= new Intent(BTClient.this,SearchActivity.class);
           startActivity(intent);
				
			}        	
        }); 
        
       //����򿪱��������豸���ɹ�����ʾ��Ϣ����������
        if (_bluetooth == null){
        	Toast.makeText(this, "�޷����ֻ���������ȷ���ֻ��Ƿ����������ܣ�", Toast.LENGTH_LONG).show();
            finish();
            return;
        }        
        // �����豸���Ա�����  
       new Thread(){
    	   public void run(){
    		   if(_bluetooth.isEnabled()==false){
        		_bluetooth.enable();
    		   }
    	   }   	   
       }.start(); 
       
       
    }
             
    //���Ӱ�����Ӧ����
    public void onConnectButtonClicked(View v){ 
    	if(_bluetooth.isEnabled()==false){  //����������񲻿�������ʾ
    		Toast.makeText(this, " ��������...", Toast.LENGTH_LONG).show();
    		return;
    	}
    	    	
        //��δ�����豸���DeviceListActivity�����豸����
    	Button btn = (Button) findViewById(R.id.Button03);
    	
	    
    	if(_socket==null){
    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //��ת��������
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //���÷��غ궨��
    	}
    	else{
    		 //�ر�����socket
    	    try{    	    	
    	    	is.close(); //�ر�������
    	    	_socket.close();
    	    	_socket = null;
    	    	bRun = false;
    	    	btn.setText("����");//�к��ã�
    	    }catch(IOException e){}   
    	}
    	return;
    }
        
    //���ջ�������ӦstartActivityForResult()�������棩
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //���ӽ������DeviceListActivity���÷���
    		// ��Ӧ���ؽ��
            if (resultCode == Activity.RESULT_OK) {   //���ӳɹ�����DeviceListActivity���÷���
                // MAC��ַ����DeviceListActivity���÷���
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // �õ������豸���      
                _device = _bluetooth.getRemoteDevice(address); 
                // �÷���ŵõ�socket
                try{
                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                }
                //����socket
            	Button btn = (Button) findViewById(R.id.Button03);
                try{
                	_socket.connect();
                	Toast.makeText(this, "����"+_device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
                	btn.setText("�Ͽ�");
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                		_socket.close();
                		_socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                	}                	
                	return;
                }
                
//                mArray.add(0);
        	    new DrawThread().start();  //��ͼ�߳����� 
//        	    Log.i("drawthread","start"); 
        	    
                //�򿪽����߳�
                try{
            		is = _socket.getInputStream();   //�õ���������������
            		}catch(IOException e){
            			Toast.makeText(this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
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
     
    //���������߳�
    Thread ReadThread=new Thread(){
  	    	
    	public void run(){
    		int num = 0;
    		byte[] buffer = new byte[2048];
    		byte[] buffer_new = new byte[2048];
    		int i = 0;
    		int n = 0;
    		bRun = true;

    		//�����߳�
    		while(true){
    			try{
    				while(is.available()==0){
    					while(bRun == false){}
    				}
    				while(true){
    					num = is.read(buffer);	//��������
    					//buffers = buffer;
    					n=0;    					
    					String s0 = new String(buffer,0,num);
    					fmsg+=s0;    //�����յ�����(+=�����ݺ������ˣ���η��룿��
//    					Log.i("fmsg",fmsg);
   					   					    					
							for(i=0;i<num;i++){
								if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){  //Windows����txt�ļ������»س�����Ȼ��16���ƹ۲죬��ᷢ�ֻ����2���ַ���0x0d��0x0a
									buffer_new[n] = 0x0a;   //�������ʲô�ã��ƺ���û����
									i++;
								}else{
									buffer_new[n] = buffer[i];
								}
								n++;
							 }
							
    					String s = new String(buffer_new,0,n);    					

							for(int k=0; k<n; k++){
								mArray.add((int)buffer_new[k]);// ��buffer_new��ֵ����Arraylist
//    						Log.i("k",String.valueOf(k));
							}
							
    					smsg+=s;   //д����ջ���
//    					Log.i("smsg",smsg);
    					if(is.available()==0)break;  //��ʱ��û�����ݲ�����������ʾ,����һֱѭ��
    				}
      	    		
    	    		}catch(IOException e){
    	    		}
    		}
    	}
    	

    }; 
        
    public static int byteToInt(byte[] b){
     	  return (((int)b[0])+((int)b[1])*256);
     }
             
	//��ͼ�̣߳�ʵʱ��ȡtemp ��ֵ����yֵ
	public class DrawThread extends Thread {
		public void run() {
			// TODO Auto-generated method stub
			drawBack(holder);    //����������������
            if(task != null){ 
                task.cancel(); 
            } 
            task = new TimerTask() { //�½�����
                
                @Override 
                public void run() {                 	
                	//��ȡÿһ��ʵʱ��y����ֵ                   	                	                   	
                    if(j<mArray.size()-12){
                    	
                     	tempa =  mArray.get(j)*256+mArray.get(j+1); //���Ͻ�
                     	temp2 =  mArray.get(j+2)*256+mArray.get(j+3);//���½�
                     	temp3 =  mArray.get(j+4)*256+mArray.get(j+5);//���Ͻ�
                     	
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
                     	Log.i("temp16",String.valueOf(temp16));//log��
                     	
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
                    
                	int cy1 = 4*HEIGHT/10 - tempa ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
                	int cy2 = 4*HEIGHT/10 - temp2 ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
                	int cy3 = 4*HEIGHT/10 - temp3 ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
                	
                    Canvas canvas = holder.lockCanvas(new Rect(cx-2,cy1-2,cx+2,cy1+2)); 
                    //����������ֻ������Rect����������ı䣬��С������
            		paint.setColor(Color.RED);  //�����ε���ɫ�Ǻ�ɫ�ģ��������������ɫ
                    canvas.drawPoint(cx, cy1, paint); //���
                    holder.unlockCanvasAndPost(canvas);  //��������
                    
                    canvas = holder.lockCanvas(new Rect(cx-2,cy2+HEIGHT/2-2,cx+2,cy2+HEIGHT/2+2)); 
            		paint.setColor(Color.GREEN);  //�����ε���ɫ����ɫ�ģ��������������ɫ
                    canvas.drawPoint(cx, cy2+HEIGHT/2, paint); //���
                    holder.unlockCanvasAndPost(canvas);  //��������
                    
                    canvas = holder.lockCanvas(new Rect(cx+WIDTH/2-2,cy3-2,cx+WIDTH/2+2,cy3+2));
            		paint.setColor(Color.BLUE);  //�����ε���ɫ����ɫ�ģ��������������ɫ
                    canvas.drawPoint(cx+WIDTH/2, cy3, paint); //���
                    holder.unlockCanvasAndPost(canvas);  //��������
                    
//                    canvas = holder.lockCanvas(new Rect(cx+WIDTH/2-2,cy1+HEIGHT/2-2,cx+WIDTH/2+2,cy1+HEIGHT/2+2));
//            		paint.setColor(Color.BLUE);  //�����ε���ɫ����ɫ�ģ��������������ɫ
//                    canvas.drawPoint(cx+WIDTH/2, cy1+HEIGHT/2, paint); //���
//                    holder.unlockCanvasAndPost(canvas);  //��������
                    
                    cx++;    //cx ������ ����������ʱ�����ͼ��  
                                                                              
                    
/*                    //��̬�ı�xʱ���������
                    if (xtime<=100){       //ÿ��1ms����xtime�ĵ�����ÿ��0.1s����lxtime�����������������
                    xtime++;
                    }else{
                    	xtime=0;
                    	lxtime++;
                    }                    
                    String lx1=String.valueOf(lxtime);
                    String lx2=String.valueOf(lxtime+1);
                    String lx3=String.valueOf(lxtime+2);
                                     
                    canvas.drawText(lx1,2*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //���������������
                    canvas.drawText(lx2,3*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //���������������       
                    canvas.drawText(lx3,4*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //���������������  
                    canvas.drawText("ʱ��/0.1s",5*WIDTH/10,19*HEIGHT/20, p); //���������������
                    canvas.drawText(lx1,7*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //���������������
                    canvas.drawText(lx2,8*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //���������������       
                    canvas.drawText(lx3,9*WIDTH/10+X_OFFSET,19*HEIGHT/20, p); //���������������  
                    canvas.drawText("ʱ��/0.1s",9*WIDTH/10-X_OFFSET,19*HEIGHT/20, p); //���������������                    
                    canvas.drawText(lx1,2*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //���������������
                    canvas.drawText(lx2,3*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //���������������       
                    canvas.drawText(lx3,4*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //���������������  
                    canvas.drawText("ʱ��/0.1s",5*WIDTH/10,9*HEIGHT/20, p); //���������������
                    canvas.drawText(lx1,7*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //���������������
                    canvas.drawText(lx2,8*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //���������������       
                    canvas.drawText(lx3,9*WIDTH/10+X_OFFSET,9*HEIGHT/20, p); //���������������  
                    canvas.drawText("ʱ��/0.1s",9*WIDTH/10-X_OFFSET,9*HEIGHT/20, p); //���������������  
*/                    
                    
                    if(cx >=5*WIDTH/10){                       
                        cx=WIDTH/10;     //����������ͷ��ʼ��                   
                        drawBack(holder);  //����֮�����ԭ����ͼ�񣬴��¿�ʼ    
                    }                    
                } 
            }; 
            timer.schedule(task, 0, 1); //��1ms��ִ��һ�θ�ѭ�����񣬻���ͼ�Σ� 
            //��һ�����1ms����һ���㣬Ȼ��������ȥ 
            
		}	 
	}
	
    //���û�������ɫ������XY���λ��
    private void drawBack(SurfaceHolder holder){ 
        Canvas canvas= holder.lockCanvas(); //��������
        //���ư�ɫ���� 
        canvas.drawColor(Color.WHITE); 
        p.setColor(Color.BLACK); 
        p.setStrokeWidth(2);          
        //���������� 
       canvas.drawLine(X_OFFSET, HEIGHT*2/5, WIDTH, HEIGHT*2/5, p); //����X�� ǰ�ĸ�����������
       canvas.drawLine(WIDTH/10, X_OFFSET, WIDTH/10, HEIGHT, p); //����Y�� ǰ�ĸ���������ʼ����
       canvas.drawLine(X_OFFSET, HEIGHT*9/10, WIDTH, HEIGHT*9/10, p); //����X�� ǰ�ĸ�����������
       canvas.drawLine(WIDTH*3/5, X_OFFSET, WIDTH*3/5, HEIGHT, p); //����Y�� ǰ�ĸ���������ʼ����
       
       p.setStrokeWidth(100);
       canvas.drawText("0",WIDTH/20,HEIGHT*4/10, p); //���������������
       canvas.drawText("100",WIDTH/20,HEIGHT*3/10, p); //���������������
       canvas.drawText("200",WIDTH/20,HEIGHT*2/10, p); //���������������
       canvas.drawText("300",WIDTH/20,HEIGHT*1/10, p); //���������������
       canvas.drawText("��ѹ/mV temp",WIDTH/20,HEIGHT*1/20, p); //���������������      
       
       canvas.drawText("0",WIDTH/20,HEIGHT*9/10, p); //���������������
       canvas.drawText("100",WIDTH/20,HEIGHT*8/10, p); //���������������
       canvas.drawText("200",WIDTH/20,HEIGHT*7/10, p); //���������������
       canvas.drawText("300",WIDTH/20,HEIGHT*6/10, p); //���������������
       canvas.drawText("��ѹ/mV temp2",WIDTH/20,HEIGHT*10/20, p); //���������������   
       
       canvas.drawText("0",11*WIDTH/20,HEIGHT*4/10, p); //���������������
       canvas.drawText("100",11*WIDTH/20,HEIGHT*3/10, p); //���������������
       canvas.drawText("200",11*WIDTH/20,HEIGHT*2/10, p); //���������������
       canvas.drawText("300",11*WIDTH/20,HEIGHT*1/10, p); //���������������
       canvas.drawText("��ѹ/mV temp3",11*WIDTH/20,HEIGHT*1/20, p); //���������������          
       
       canvas.drawText("0",11*WIDTH/20,HEIGHT*9/10, p); //���������������
       canvas.drawText("100",11*WIDTH/20,HEIGHT*8/10, p); //���������������
       canvas.drawText("200",11*WIDTH/20,HEIGHT*7/10, p); //���������������
       canvas.drawText("300",11*WIDTH/20,HEIGHT*6/10, p); //���������������
       canvas.drawText("��ѹ/mV",11*WIDTH/20,HEIGHT*10/20, p); //���������������  //���һ���к��ã�
       
        holder.unlockCanvasAndPost(canvas);  //�������� ��ʾ����Ļ��
        holder.lockCanvas(new Rect(0,0,0,0)); //�����ֲ���������ط������ı�
        holder.unlockCanvasAndPost(canvas);          
    }
             
    //�رճ�����ô�����
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null)  //�ر�����socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
    //	_bluetooth.disable();  //�ر���������
    }
            
    //���水����Ӧ����
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //���������Ӧ����
    public void onClearButtonClicked(View v){
    	smsg="00";
    	fmsg="00";
//    	timer.cancel();
    	return;
    }
    
    //�˳�������Ӧ����
    public void onQuitButtonClicked(View v){
    	  final String[] arrayFruit = new String[] { "����һ", "���ݶ�", "������", "������" };

    	  Dialog alertDialog = new AlertDialog.Builder(this).
    	    setTitle("��ѡ��Ҫ��ȡ������")
    	    .setItems(arrayFruit, new DialogInterface.OnClickListener() {
    	 
    	     @Override
    	     public void onClick(DialogInterface dialog, int which) {
                 send_num[0]=which;
    	    	 final String[] arrayFruit = new String[] { "ͨ��1", "ͨ��2", "ͨ��3" };

    	    	  Dialog alertDialog = new AlertDialog.Builder(BTClient.this).
    	    	    setTitle("��ѡ��ͨ��")
    	    	    .setItems(arrayFruit, new DialogInterface.OnClickListener() {
    	    	 
    	    	     @Override
    	    	     public void onClick(DialogInterface dialog, int which) {
    	    	    	 send_num[1]=which;
    	    	    	 Intent readdata = new Intent(BTClient.this,zju.bme.bp.ecg.class); 
    	    	    	readdata.putExtra("send_num", send_num);
    	                 startActivity(readdata);     
    	    	     }
    	    	    }).
    	    	    setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

    	    	     @Override
    	    	     public void onClick(DialogInterface dialog, int which) {
    	    	      // TODO Auto-generated method stub
    	    	     }
    	    	    }).
    	    	    create();
    	    	  alertDialog.show();
    	    	     
    	     }
    	    }).
    	    setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

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
    
    //���湦��ʵ��
	private void Save() {
		
		//��ʾ�Ի��������ļ���		
		LayoutInflater factory = LayoutInflater.from(BTClient.this);  //ͼ��ģ�����������
		final View DialogView =  factory.inflate(R.layout.sname, null);  //��sname.xmlģ��������ͼģ��
		new AlertDialog.Builder(BTClient.this)
								.setTitle("��������")
								.setView(DialogView)   //������ͼģ��
								.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() //ȷ��������Ӧ����
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //�õ��ļ����������
										filename = text1.getText().toString();  //�õ��ļ���
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����
												
												filename =filename+".txt";   //���ļ���ĩβ����.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
												File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
												FileOutputStream stream = new FileOutputStream(saveFile);  //���ļ�������
												stream.write(fmsg.getBytes());//�˴��õ�fmsg��ֻҪ���治����Ϳ��ԣ�
												stream.close();
												Toast.makeText(BTClient.this, "�洢�ɹ���", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(BTClient.this, "û�д洢����", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}																														
									}
								})
								.setNegativeButton("ȡ��",   //ȡ��������Ӧ����,ֱ���˳��Ի������κδ��� 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //��ʾ�Ի���
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