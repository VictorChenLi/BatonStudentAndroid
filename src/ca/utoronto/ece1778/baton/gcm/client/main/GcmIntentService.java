/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ca.utoronto.ece1778.baton.gcm.client.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import ca.utoronto.ece1778.baton.STUDENT.R;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.model.classmanage.ClassParticipate;
import com.baton.publiclib.model.ticketmanage.Ticket;
import com.baton.publiclib.utility.JsonHelper;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public GcmIntentService() {
		super(Constants.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		// CommonUtilities.displayMessage(context,
		// "Your device registred with GCM");
		// Log.d("NAME", MainActivity.name);
		// BatonServerCommunicator.registerDevice(context, MainActivity.name,
		// MainActivity.email, registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		// CommonUtilities.displayMessage(context,
		// getString(R.string.gcm_unregistered));
		// BatonServerCommunicator.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "onMessage called");
		Intent out = null;
//		List<ClassParticipate> buddies_list = JsonHelper.deserializeList(intent.getStringExtra(Ticket.TICKET_LIST_WEB_STR), ClassParticipate.class);
		String buddiesStr = intent.getStringExtra(Ticket.TICKET_LIST_WEB_STR);
		if(null!=buddiesStr&&!buddiesStr.isEmpty())
		{
			out = new Intent(Constants.DISPLAY_TALK_TICKET_ACTION);
			out.putExtra(Constants.CLASS_PARTICIPATE_IN_LESSON, buddiesStr);
		}
		if (out != null) {
			context.sendBroadcast(out);
		}
		// String message =
		// intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);

		// CommonUtilities.displayMessage(context, message);
		// notifies user
		// generateNotification(context, message);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		// CommonUtilities.displayMessage(context, message);
		// notifies user
		// generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		// CommonUtilities.displayMessage(context, getString(R.string.gcm_error,
		// errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		/*
		 * CommonUtilities.displayMessage(context,
		 * getString(R.string.gcm_recoverable_error, errorId));
		 */
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	/*
	 * private static void generateNotification(Context context, String message)
	 * { int icon = R.drawable.ic_launcher; long when =
	 * System.currentTimeMillis(); NotificationManager notificationManager =
	 * (NotificationManager)
	 * context.getSystemService(Context.NOTIFICATION_SERVICE); Notification
	 * notification = new Notification(icon, message, when);
	 * 
	 * String title = context.getString(R.string.app_name);
	 * 
	 * Intent notificationIntent = new Intent(context, MainActivity.class); //
	 * set intent so it does not start a new activity
	 * notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	 * Intent.FLAG_ACTIVITY_SINGLE_TOP); PendingIntent intent =
	 * PendingIntent.getActivity(context, 0, notificationIntent, 0);
	 * notification.setLatestEventInfo(context, title, message, intent);
	 * notification.flags |= Notification.FLAG_AUTO_CANCEL;
	 * 
	 * // Play default notification sound notification.defaults |=
	 * Notification.DEFAULT_SOUND;
	 * 
	 * // Vibrate if vibrate is enabled notification.defaults |=
	 * Notification.DEFAULT_VIBRATE; notificationManager.notify(0,
	 * notification);
	 * 
	 * }
	 */

}
