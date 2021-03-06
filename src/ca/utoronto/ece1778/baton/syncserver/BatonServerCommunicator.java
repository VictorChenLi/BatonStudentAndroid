package ca.utoronto.ece1778.baton.syncserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import ca.utoronto.ece1778.baton.STUDENT.R;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.infrastructure.exception.ErrorCode;
import com.baton.publiclib.infrastructure.exception.ServiceException;
import com.baton.publiclib.model.classmanage.ClassLesson;
import com.baton.publiclib.model.classmanage.ClassParticipate;
import com.baton.publiclib.model.classmanage.VirtualClass;
import com.baton.publiclib.model.ticketmanage.TalkTicketForDisplay;
import com.baton.publiclib.model.ticketmanage.Ticket;
import com.baton.publiclib.model.usermanage.UserProfile;
import com.baton.publiclib.utility.JsonHelper;
import com.baton.publiclib.utility.TimeHelper;
import com.google.android.gcm.GCMRegistrar;
//import ca.utoronto.ece1778.baton.models.StudentProfile;
//import ca.utoronto.ece1778.baton.models.Ticket;

/**
 * 
 * @author Yi Zhao
 * 
 */
public class BatonServerCommunicator {
	private static final int MAX_ATTEMPTS = 5;
	private static final int BACKOFF_MILLI_SECONDS = 200;
	private static final Random random = new Random();
	private static final String TAG = "BatonServerCommunicator";

	/** string for communication with sync server as a parameter name */
	private static final String POST_CLASSROOM = "classroom";

	public static final String REPLY_MESSAGE_REGISTER_SUCCESS = "register_success";
	public static final String REPLY_MESSAGE_REGISTER_FAIL = "register_fail";
	public static final String REPLY_MESSAGE_LOGIN_SUCCESS = "login_success";
	public static final String REPLY_MESSAGE_LOGIN_FAIL = "login_fail";

	/**
	 * Register user on the server.
	 */
	public static String register(final Context context, final UserProfile user) {
		Log.i(TAG, "registering user:");
		Log.i(TAG, user.toString());

		String serverUrl = Constants.SERVER_URL + "/register?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(UserProfile.GCMID_WEB_STR, user.getGcm_regid());
		params.put(UserProfile.EMAIL_WEB_STR, user.getEmail());
		params.put(UserProfile.FNAME_WEB_STR, user.getF_name());
		params.put(UserProfile.LNAME_WEB_STR, user.getL_name());
		params.put(UserProfile.LOGINID_WEB_STR, user.getLogin_id());
		params.put(UserProfile.PASSWORD_WEB_STR, user.getPassword());
		params.put(UserProfile.USERTYPE_WEB_STR, UserProfile.USERTYPE_STUDENT);

		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, true);
			Log.i(TAG, "register success");
			return REPLY_MESSAGE_REGISTER_SUCCESS;
		} catch (ServiceException e) {
			e.printStackTrace();
			return REPLY_MESSAGE_REGISTER_FAIL;
		}

	}

	/**
	 * Login user on the server.
	 */
	public static String login(final Context context, final String[] token) {
		Log.i(TAG, "Login user:");
		Log.i(TAG, Arrays.toString(token));

		String serverUrl = Constants.SERVER_URL + "/login?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(UserProfile.LOGINID_WEB_STR, token[0]);
		params.put(VirtualClass.CLASSROOM_NAME_WEB_STR, token[1]);
		params.put(UserProfile.PASSWORD_WEB_STR, token[2]);
		params.put(UserProfile.GCMID_WEB_STR,
				GCMRegistrar.getRegistrationId(context));
		params.put(UserProfile.TEACHER_LOGINID_WEB_STR, token[3]);

		try {

			String returnStr = post(serverUrl, params);
			ClassLesson lesson = JsonHelper.deserialize(returnStr,
					ClassLesson.class);
			CommonUtilities.putGlobalVar((Application) context,
					ClassLesson.LESSONID_WEB_STR,
					String.valueOf(lesson.getLid()));

			Log.i(TAG, "login success");
			return REPLY_MESSAGE_LOGIN_SUCCESS;
		} catch (ServiceException e) {
			e.printStackTrace();
			return REPLY_MESSAGE_LOGIN_FAIL;
		}

	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void unregister(final Context context, final UserProfile user) {
		Log.i(TAG, "unregistering user");
		String serverUrl = Constants.SERVER_URL + "/unregister?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(UserProfile.GCMID_WEB_STR, user.getGcm_regid());
		params.put(UserProfile.EMAIL_WEB_STR, user.getEmail());
		try {
			post(serverUrl, params);
			GCMRegistrar.setRegisteredOnServer(context, false);
		} catch (ServiceException e) {
			// At this point the device is unregistered from GCM, but still
			// registered in the server.
			// We could try to unregister again, but it is not necessary:
			// if the server tries to send a message to the device, it will get
			// a "NotRegistered" error message and should unregister the device.
			e.printStackTrace();
		}
	}

	public static Boolean sendTalkIntent(Activity context, String[] intent) {
		Log.i(TAG, "Send Ticket:");
		Log.i(TAG, intent[0]);
		Log.i(TAG, intent[1]);
		String serverUrl = Constants.SERVER_URL + "/sendTicket?";
		Map<String, String> params = new HashMap<String, String>();
		String gcm_regId = CommonUtilities.getGlobalVar(context,
				UserProfile.GCMID_WEB_STR);
		params.put(UserProfile.GCMID_WEB_STR, gcm_regId);
		params.put(UserProfile.LOGINID_WEB_STR, intent[1]);
		params.put(Ticket.TICKETCONTENT_WEB_STR, intent[0]);
		params.put(Ticket.TICKETTYPE_WEB_STR, Ticket.TICKET_TYPE_TALK);
		params.put(Ticket.TIMESTAMP_WEB_STR,
				TimeHelper.getStrTimeFromMillis(System.currentTimeMillis()));
		params.put(ClassLesson.LESSONID_WEB_STR, CommonUtilities.getGlobalVar(
				context, ClassLesson.LESSONID_WEB_STR));

		try {
			post(serverUrl, params);
			return true;
		} catch (ServiceException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Issue a POST request to the server.
	 * 
	 * @param endpoint
	 *            POST address.
	 * @param params
	 *            request parameters.
	 * 
	 * @throws IOException
	 *             propagated from POST.
	 * @throws ServiceException
	 */
	private static String post(String endpoint, Map<String, String> params)
			throws ServiceException {

		URL url;
		String retStr = null;
		try {
			url = new URL(endpoint);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("invalid url: " + endpoint);
		}
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		Log.v(TAG, "Posting '" + body + "' to " + url);
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(100);
		// As the server might be down, we will retry it a couple times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {

			Log.d(TAG, "Attempt #" + i);
			try {
				Log.e("URL", "> " + url);
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setUseCaches(false);
				conn.setFixedLengthStreamingMode(bytes.length);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				// post the request
				OutputStream out = conn.getOutputStream();
				out.write(bytes);
				out.close();
				// handle the response
				int status = conn.getResponseCode();
				System.out.println("######status:"+status);
				if (status != 200) {
					if (status == 400)
					{
						// this is the specific exception
						String strException = readHttpResponseMsg(conn);
						ServiceException se = JsonHelper.deserialize(strException, ServiceException.class);
						throw se;
					}
					else
					{
						// throw the network exception
						throw new ServiceException(ErrorCode.Network_connection_Error_Msg, ErrorCode.LoginId_Not_Exist);
					}
				} else {
					return readHttpResponseMsg(conn);
				}
			} catch (ServiceException se) {
				// we handle the 400 error, which is type of exception we
				// already customized
				// if the errorcode equal to network error, we will try it again
				// otherwise, we will throw this exception to the upper layer to
				// handle
				if (!se.getErrorCode().equals(ErrorCode.Network_connection_Error))
					throw se;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
			Log.e(TAG, "Failed on attempt " + i);
			if (i == MAX_ATTEMPTS) {
				break;
			}
			try {
				Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
				Thread.sleep(backoff);
			} catch (InterruptedException e1) {
				// Activity finished before we complete - exit.
				Log.d(TAG, "Thread interrupted: abort remaining retries!");
				Thread.currentThread().interrupt();
				throw new ServiceException(ErrorCode.System_Unknow_Error_Msg, ErrorCode.System_Unknow_Error);
			}
			// increase backoff exponentially
			backoff *= 2;
		}
		throw new ServiceException(ErrorCode.Network_connection_Error_Msg, ErrorCode.Network_connection_Error);
	}

	public static String readHttpResponseMsg(HttpURLConnection conn) throws IOException
	{
		InputStream in = new BufferedInputStream(
				conn.getInputStream());
		StringBuffer sb = new StringBuffer("");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(in));
		String inputLine = "";
		while ((inputLine = reader.readLine()) != null) {
			sb.append(inputLine);
			// sb.append("\n");
		}
		in.close();
		return sb.toString();
	}

	/**
	 * get current login students list of the current lesson
	 * 
	 * @param lid
	 *            current lesson id
	 * @return
	 */
	public static List<ClassParticipate> getCurrentLoginBuddies(String lid) throws ServiceException {

		Log.i(TAG, "getCurrentLoginBuddies");
		List<ClassParticipate> cpList = new ArrayList<ClassParticipate>();
		String serverUrl = Constants.SERVER_URL + "/getLessonBuddies?";
		Map<String, String> params = new HashMap<String, String>();
		params.put(ClassLesson.LESSONID_WEB_STR, lid);
		String retStr = post(serverUrl, params);
		Log.i(TAG, "getCurrentLoginBuddies result:" + retStr);
		cpList = JsonHelper.deserializeList(retStr, ClassParticipate.class);
		Log.i(TAG, cpList.size() + " login buddies got from server");
		/*for (ClassParticipate cp : cpList) {
			Log.i(TAG, "#Buddy#:");
			Log.i(TAG, cp.toString());
		}*/

		return cpList;

	}
}
