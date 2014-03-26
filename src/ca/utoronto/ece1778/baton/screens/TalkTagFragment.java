package ca.utoronto.ece1778.baton.screens;

import java.util.Hashtable;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.STUDENT.R;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;

import com.baton.publiclib.model.ticketmanage.Ticket;
import com.baton.publiclib.model.usermanage.UserProfile;
//import ca.utoronto.ece1778.baton.models.StudentProfile;

/**
 * 
 * @author Yi Zhao
 * 
 */
public class TalkTagFragment extends Fragment implements OnClickListener {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "0";
	static final String TAG = "TalkTagFragment";

	Button btnBuild;
	Button btnQuestion;
	Button btnChallenge;
	Button btnNewIdeas;

	ProgressDialog mProgress = null;
	AsyncSendTalkTicketTask mTask = null;

	public TalkTagFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG,"onCreateView called");
		View rootView = inflater.inflate(R.layout.fragment_talk_tab, container,
				false);
		btnBuild = (Button) rootView.findViewById(R.id.talk_btnBuild);
		btnQuestion = (Button) rootView.findViewById(R.id.talk_btnQuestion);
		btnChallenge = (Button) rootView.findViewById(R.id.talk_btnChallenge);
		btnNewIdeas = (Button) rootView.findViewById(R.id.talk_btnNew);
		
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), Constants.TYPEFACE_ACTION_MAN_BOLD);
		btnBuild.setTypeface(tf1);
		Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), Constants.TYPEFACE_ACTION_MAN_BOLD);
		btnQuestion.setTypeface(tf2);
		Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(), Constants.TYPEFACE_ACTION_MAN_BOLD);
		btnChallenge.setTypeface(tf3);
		Typeface tf4 = Typeface.createFromAsset(getActivity().getAssets(), Constants.TYPEFACE_ACTION_MAN_BOLD);
		btnNewIdeas.setTypeface(tf4);

		btnBuild.setOnClickListener(this);
		btnQuestion.setOnClickListener(this);
		btnChallenge.setOnClickListener(this);
		btnNewIdeas.setOnClickListener(this);

		if(mProgress==null)
		    mProgress = new ProgressDialog(getActivity());

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    Log.i(TAG,"onSaveInstanceState called");
		super.onSaveInstanceState(outState);
		Hashtable<String, Object> returnObject = new Hashtable<String, Object>();
		if (mTask != null && !mTask.isCompleted) {
			Log.i(TAG, "mTask is not finished while tilted, saved with mProgress");
			mTask.setFragment(null);
			returnObject.put("mTask", mTask);
			returnObject.put("mProgress", mProgress);
		}
		outState.putSerializable("savedState",returnObject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		Log.i(TAG,"onViewStateRestored called");
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState!=null && savedInstanceState.get("savedState") != null) {
			Hashtable<String, Object> savedState = (Hashtable<String, Object>) savedInstanceState.get("savedState");
			Log.i(TAG, "Hashtable retained");
			Object objectTask = ((Hashtable<String, Object>) savedState).get("mTask");
			Object objectProgress = ((Hashtable<String, Object>) savedState).get("mProgress");
			if (objectTask != null) {
				Log.i(TAG, "mTask be retained");
				mTask = (AsyncSendTalkTicketTask) objectTask;
				mTask.setFragment(this);
			}
			if (objectProgress != null) {
				Log.i(TAG, "mProgress be retained to " + this);
				mProgress = (ProgressDialog) objectProgress;
				showMyProgressDialog(mProgress);
			}
		}
	}

	private void showMyProgressDialog(ProgressDialog mProgress2) {
		mProgress.setMessage("Sending to teacher...");
		mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgress.setCancelable(false);
		mProgress.setProgress(0);
		mProgress.show();		
	}

	@Override
	public void onDestroy() {
		Log.i(TAG,"onDestroy called");
		if (mProgress != null && mProgress.isShowing()) {
			mProgress.dismiss();
			Log.i(TAG, "mProgress dismissed onDestroy called");
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		String intentMessage = null;
		String loginId = CommonUtilities.getGlobalVar(getActivity(), UserProfile.LOGINID_WEB_STR);
		Log.i("TalkTagFragment","send ticket with loginId:"+loginId);
		switch (v.getId()) {
		case R.id.talk_btnBuild:
			intentMessage = Ticket.TALK_INTENT_BUILD_WEB_STR;
			break;
		case R.id.talk_btnChallenge:
			intentMessage = Ticket.TALK_INTENT_CHALLENGE_WEB_STR;
			break;
		case R.id.talk_btnNew:
			intentMessage = Ticket.TALK_INTENT_NEWIDEA_WEB_STR;
			break;
		case R.id.talk_btnQuestion:
			intentMessage = Ticket.TALK_INTENT_QUESTION_WEB_STR;
			break;
		}
		if (intentMessage == null) {
			Toast.makeText(getActivity(),
					"Please choose your paticipate intent", Toast.LENGTH_SHORT)
					.show();
		}
		mTask = new AsyncSendTalkTicketTask(this);
		mTask.execute(new String[] { intentMessage,loginId });
	}

	public void onTaskCompleted(boolean result) {
		if(mProgress!=null && mProgress.isShowing())
		    mProgress.dismiss();
		if(result)
			Toast.makeText(getActivity(), R.string.server_sendticket_success, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getActivity(), R.string.server_sendticket_error, Toast.LENGTH_SHORT).show();
	}

	/*
	 * AsyncTask<Type1, Type2, Type3> 1.The type of information that is needed
	 * to process the task (e.g., messages to send) 2. The type of information
	 * that is passed within the task to indicate progress 3. The type of
	 * information that is passed when the task is completed to the post-task
	 * code
	 */
	class AsyncSendTalkTicketTask extends AsyncTask<String, Void, Void> {
		public boolean isCompleted = false;
        TalkTagFragment fragment = null;
        boolean result = false;
        
        private AsyncSendTalkTicketTask(TalkTagFragment frag){
        	this.fragment = frag;
        }
        
		@Override
		protected void onPreExecute() {
			mProgress.setMessage("Sending to teacher...");
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			mProgress.setProgress(0);
			mProgress.show();
		}

		
		@Override
		protected Void doInBackground(String... intent) {
			// TODO: implement sendTalkIntent in BatonServerCommunicator, and assign "result" with the feedback message to student
			result = BatonServerCommunicator.sendTalkIntent(getActivity(), intent);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			isCompleted = true;
			notifyFragmentTaskCompleted(result);
			
		}
		
		private void notifyFragmentTaskCompleted(boolean result2) {
			if (null != fragment) {
				fragment.onTaskCompleted(result2);
			}			
		}
		
		private void setFragment(TalkTagFragment frag) {
			this.fragment = frag;
			if (isCompleted) {
				notifyFragmentTaskCompleted(result);
			}
		}
	}
}
