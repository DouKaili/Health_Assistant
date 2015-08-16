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
		
		static int i=0;//������ʾʱ��ѭ������
		final int HEIGHT=2500;   //���û�ͼ��Χ�߶�2500
	    final int WIDTH=HEIGHT*45/32;    //��ͼ��Χ���3515.625
	    final int X_OFFSET = 5;  //x�ᣨԭ�㣩��ʼλ��ƫ�ƻ�ͼ��Χһ�� 
	    int cx = WIDTH/10;  //ʵʱx������    351.5625
	    int centerY = 1*HEIGHT /20;  //y���λ��   125
	    SurfaceHolder holder = null;    //��ͼʹ�ã����Կ���һ��SurfaceView
	    Paint paint = null;      //����
	    SurfaceView surface = null;     //
	    Timer timer = new Timer();       //һ��ʱ����ƵĶ������ڿ���ʵʱ��x�����꣬
	    //ʹ�������������ʾ������ǰ����ɨ��
	    TimerTask task = null;   //ʱ����ƶ����һ������
	    
	    Paint p = new Paint();
	    int xtime=0;
	    int lxtime=0;
	    public static ArrayList<Integer> mArray=new ArrayList<Integer>();
	    int j =19;	
	    int count1=0;
	    int count2=0;
	    public static int tempa = 0;                //��ʱ�������ڱ�����յ�������
	    static int temp2 = 0;                //��ʱ�������ڱ�����յ�������
	    static int temp3 = 0;                //��ʱ�������ڱ�����յ�������
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data);
		
		surface = (SurfaceView)findViewById(R.id.read_show);        
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

        read();
       // draw(); //����������,���ƻ��Ƕ��������ݣ�����nullpointexception
        //new DrawThread().start();
}
	public void onSelectButtonClicked(View v){
		
		new DrawThread().start();
	}
	
	public void  read(){
		try {
			File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
			File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
			if(BuildDir.exists()==false) BuildDir.mkdirs();
			File File =new File(BuildDir, "1.01.txt");  //�½��ļ���������Ѵ������½��ĵ�
			FileInputStream fis = new FileInputStream(File);
			//int length = fis.available();
			//byte[] buffer = new byte[length];
			num = fis.read(buffer);//��Ҫ������
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
				mArray.add((int)buffer_new[k]);// ��buffer_new��ֵ����Arraylist

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	 public static int byteToInt(byte[] b){
    	  return (((int)b[0])+((int)b[1])*256);
    }
            
	//��ͼ�̣߳�ʵʱ��ȡtemp ��ֵ����yֵ
	 public class DrawThread extends Thread {
		public void draw() {
			// TODO Auto-generated method stub
			//drawBack(holder);    //����������������
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
                   
//                   System.out.print(mArray);
                   Log.i("mArraysize",String.valueOf(mArray.size()));
                   Log.i("tempa",String.valueOf(tempa));  
                   Log.i("temp2",String.valueOf(temp2));
                   Log.i("temp3",String.valueOf(temp3));
                   Log.i("j",String.valueOf(j));
                   
               	int cy1 = 4*HEIGHT/10 - tempa ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
               	int cy2 = 4*HEIGHT/10 - temp2 ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
               	int cy3 = 4*HEIGHT/10 - temp3 ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
               	
//               	int cy1 = 100 ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
//               	int cy2 = 150 ; //ʵʱ��ȡ��temp��ֵ�����ڻ�����˵�����Ͻ���ԭ��
//               	int cy3 = 200 ;
               	
                   Canvas canvas = holder.lockCanvas(new Rect(cx-2,cy1-2,cx+2,cy1+2)); //�ƺ���д���߳̾ͻ���null���������ǻ�����ͼ
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
                   
                   
                   cx++;    //cx ������ ����������ʱ�����ͼ��  
                                                                             
                   
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
}

