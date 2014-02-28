package ca.utoronto.ece1778.baton.screens;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.gcm.client.main.R;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;
import ca.utoronto.ece1778.baton.util.Constants;

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

	Button btnBuild;
	Button btnQuestion;
	Button btnChallenge;
	Button btnNewIdeas;

	ProgressDialog mProgress;

	public TalkTagFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_talk_tab, container,
				false);
		btnBuild = (Button) rootView.findViewById(R.id.talk_btnBuild);
		btnQuestion = (Button) rootView.findViewById(R.id.talk_btnQuestion);
		btnChallenge = (Button) rootView.findViewById(R.id.talk_btnChallenge);
		btnNewIdeas = (Button) rootView.findViewById(R.id.talk_btnNew);

		btnBuild.setOnClickListener(this);
		btnQuestion.setOnClickListener(this);
		btnChallenge.setOnClickListener(this);
		btnNewIdeas.setOnClickListener(this);

		mProgress = new ProgressDialog(getActivity());

		return rootView;
	}

	@Override
	public void onClick(View v) {
		String intentMessage = null;
		switch (v.getId()) {
		case R.id.talk_btnBuild:
			intentMessage = Constants.TALK_INTENT_BUILD;
			break;
		case R.id.talk_btnChallenge:
			intentMessage = Constants.TALK_INTENT_CHALLENGE;
			break;
		case R.id.talk_btnNew:
			intentMessage = Constants.TALK_INTENT_NEW_IDEA;
			break;
		case R.id.talk_btnQuestion:
			intentMessage = Constants.TALK_INTENT_QUESTION;
			break;
		}
		if (intentMessage == null) {
			Toast.makeText(getActivity(),
					"Please choose your paticipate intent", Toast.LENGTH_SHORT)
					.show();
		}
		new AsyncSendTalkTicketTask().execute(new String[] { intentMessage });
	}

	/*
	 * AsyncTask<Type1, Type2, Type3> 1.The type of information that is needed
	 * to process the task (e.g., messages to send) 2. The type of information
	 * that is passed within the task to indicate progress 3. The type of
	 * information that is passed when the task is completed to the post-task
	 * code
	 */
	class AsyncSendTalkTicketTask extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {
			mProgress.setMessage("Sending to teacher...");
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			mProgress.setProgress(0);
			mProgress.show();
		}

		@Override
		protected String doInBackground(String... intent) {
			// TODO: implement sendTalkIntent in BatonServerCommunicator, and assign "result" with the feedback message to student
			String result = BatonServerCommunicator.sendTalkIntent(
					getActivity(), intent[0]);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... unused) {
		}

		@Override
		protected void onPostExecute(String result) {
			mProgress.dismiss();
			Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		}
	}
}
