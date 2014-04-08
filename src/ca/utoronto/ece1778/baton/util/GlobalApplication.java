package ca.utoronto.ece1778.baton.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baton.publiclib.model.classmanage.ClassParticipate;

import android.app.Application;

public class GlobalApplication extends Application {
	private Map<String, Object> mData;  
	private List<ClassParticipate> buddiesList;
    
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
    
    public List<ClassParticipate> getBuddiesList()
    {
    	return buddiesList;
    }
    
    public void putBuddiesList(List<ClassParticipate> newBuddiesList)
    {
    	buddiesList = newBuddiesList;
    }
    
    
    @Override  
    public void onCreate() {  
        super.onCreate();  
          
        mData = new HashMap<String, Object>();  
        buddiesList = new ArrayList<ClassParticipate>();
        //synchronized the map  
        mData = Collections.synchronizedMap(mData);   
        buddiesList = Collections.synchronizedList(buddiesList);
          
        // then restore your map  
          
    }  
      
    public void onTerminate() {  
        super.onTerminate();  
          
        //save data of the map  
    }  
}
