package zju.bme.bp;
import android.app.Application;


public class MyApplication extends Application {
	private static MyApplication instance;


    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
    }

    private long time=7;
    public long gettime(){
        return  time;
    }
    public void settime(long time){
        this.time = time;
    }
}
