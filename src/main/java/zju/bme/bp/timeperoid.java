package zju.bme.bp;

import android.app.Application;

/**
 * Created by zheyu on 2015/6/19.
 */
public class timeperoid extends Application {
    private long time;
    public long gettime(){
        return  time;
    }
    public void settime(long time){
        this.time = time;
    }
}
