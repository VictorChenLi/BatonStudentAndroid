package ca.utoronto.ece1778.baton.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;

public class GlobalApplication extends Application {
	private Map<String, Object> mData;  
    
    public Map<String, Object> getmData() {  
        return mData;  
    }  
  
    public void put(String key, String value)
    {
    	mData.put(key, value);
    }
    
    public String get(String key)
    {
    	return mData.get(key).toString();
    }
    
    @Override  
    public void onCreate() {  
        super.onCreate();  
          
        mData = new HashMap<String, Object>();  
        //synchronized the map  
        mData = Collections.synchronizedMap(mData);   
          
        // then restore your map  
          
    }  
      
    public void onTerminate() {  
        super.onTerminate();  
          
        //save data of the map  
    }  
}
