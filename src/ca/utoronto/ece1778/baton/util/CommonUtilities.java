package ca.utoronto.ece1778.baton.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.baton.publiclib.model.classmanage.ClassParticipate;

//import ca.utoronto.ece1778.baton.models.StudentProfile;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

/**
 *
 */
public class CommonUtilities {
	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "CommonUtilities";

	//public static final String DISPLAY_MESSAGE_ACTION = "/ca.utoronto.ece1778.baton.DISPLAY_MESSAGE";

	//public static final String EXTRA_MESSAGE = "TalkTicket";

	/**
	 * Notifies UI to display a message.
	 * <p>
	 * This method is defined in the common helper because it's used both by the
	 * UI and the background service.
	 * 
	 * @param context
	 *            application's context.
	 * @param message
	 *            message to be displayed.
	 */
	/*public static void displayMessage(Context context, String message) {
		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
		intent.putExtra(EXTRA_MESSAGE, message);
		context.sendBroadcast(intent);
	}*/

	/**
	 * MD5
	 * 
	 * @param str
	 * @return MD5 of str
	 */
	public static String getMD5Str(String str) {
		//TODO password MD5
		return str;
	}
	
	public static String getGlobalVar(Activity context,String key)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		return global.get(key);
	}
	
	public static void putGlobalVar(Activity context,String key, String value)
	{
		GlobalApplication global = (GlobalApplication) context.getApplication();
		global.put(key, value);
	}
	
	public static String getGlobalVar(Application application,String key)
	{
		GlobalApplication global = (GlobalApplication) application;
		return global.get(key);
	}
	
	public static void putGlobalVar(Application application,String key, String value)
	{
		GlobalApplication global = (GlobalApplication) application;
		global.put(key, value);
	}
	
	public static List<ClassParticipate> getGlobalBuddiesList(Application application)
	{
		GlobalApplication global = (GlobalApplication) application;
		return global.getBuddiesList();
	}
	
	public static void putGlobalBuddiesList(Application application, List<ClassParticipate> newBuddiesList)
	{
		GlobalApplication global = (GlobalApplication) application;
		global.putBuddiesList(newBuddiesList);
	}
}
