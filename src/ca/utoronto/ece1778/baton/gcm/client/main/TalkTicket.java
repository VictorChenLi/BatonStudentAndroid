package ca.utoronto.ece1778.baton.gcm.client.main;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import ca.utoronto.ece1778.baton.util.Constants;

public class TalkTicket{
   private Timestamp mTimestamp;
   private int mIntent;

   public TalkTicket(int intent){
	   mTimestamp = new Timestamp(System.currentTimeMillis());
	   mIntent = intent;
   }
   
   public String toJSON(){
	   Map<String, Object> message = new HashMap<String, Object>();
	   message.put(Constants.TALK_TICKET_TIMESTAMP, mTimestamp);
	   message.put(Constants.TALK_TICKET_INTENT, mIntent);
	   return JSONObject.toJSONString(message);
   }
}