/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.utoronto.ece1778.baton.screens;

import java.util.Hashtable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ca.utoronto.ece1778.baton.STUDENT.R;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;
import ca.utoronto.ece1778.baton.util.AlertDialogManager;
import ca.utoronto.ece1778.baton.util.CommonUtilities;

import com.baton.publiclib.model.usermanage.UserProfile;

//import ca.utoronto.ece1778.baton.models.StudentProfile;

/**
 * Join page
 * 
 * @author Yi Zhao
 * 
 */
public class JoinActivity extends Activity implements OnClickListener {
	static final String TAG = "Student JoinActivity";
	// alert dialog manager
	AlertDialogManager alert = new AlertDialogManager();

	// UI elements
	EditText txtloginId;
	EditText txtClassroom;
	EditText txtPassword;
	EditText txtTeacherId;
	Button btnRegister;
	Button btnJoin;

	ProgressDialog mProgress = null;
	AsyncJoinTask mTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate & getLastNonConfigurationInstance called: " + this);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		if (mProgress == null)
			// only new mProgress if it's not being retained
			mProgress = new ProgressDialog(this);
		txtloginId = (EditText) findViewById(R.id.login_txtLoginId);
		txtClassroom = (EditText) findViewById(R.id.login_txtClassroomName);
		txtPassword = (EditText) findViewById(R.id.login_txtPassword);
		txtTeacherId = (EditText) findViewById(R.id.login_txtTeacherLoginId);
		btnJoin = (Button) findViewById(R.id.login_btnJoin);
		btnRegister = (Button) findViewById(R.id.login_btnRegister);

		btnJoin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);

		/* in case the previous context is RegisterActivity */
		Intent intent = this.getIntent();
		String loginId = intent.getStringExtra(UserProfile.LOGINID_WEB_STR);
		String pwd = intent.getStringExtra(UserProfile.PASSWORD_WEB_STR);
		if (loginId != null && !loginId.equals("")) {
			txtloginId.setText(loginId);
		}
		if (pwd != null && !pwd.equals("")) {
			txtPassword.setText(pwd);
		}
		/* end- in case the previous context is RegisterActivity */
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState called");
		super.onRestoreInstanceState(savedInstanceState);
		if (savedInstanceState!=null && savedInstanceState.get("savedState") != null) {
			Hashtable<String, Object> savedState = (Hashtable<String, Object>) savedInstanceState.get("savedState");
			Log.i(TAG, "Hashtable retained");
			Object objectTask = ((Hashtable<String, Object>) savedState).get("mTask");
			Object objectProgress = ((Hashtable<String, Object>) savedState).get("mProgress");
			if (objectTask != null) {
				Log.i(TAG, "mTask be retained");
				mTask = (AsyncJoinTask) objectTask;
				mTask.setActivity(this);
			}
			if (objectProgress != null) {
				Log.i(TAG, "mProgress be retained to " + this);
				mProgress = (ProgressDialog) objectProgress;
				showMyProgressDialog(mProgress);
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.i(TAG, "onSaveInstanceState called");
		Hashtable<String, Object> returnObject = new Hashtable<String, Object>();
		if (mTask != null && !mTask.isCompleted) {
			Log.i(TAG, "mTask is not finished while tilted, saved with mProgress");
			mTask.setActivity(null);
			returnObject.put("mTask", mTask);
			returnObject.put("mProgress", mProgress);
		}
		outState.putSerializable("savedState", returnObject);
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy called");
		if (mProgress != null && mProgress.isShowing()) {
			mProgress.dismiss();
			Log.i(TAG, "mProgress dismissed onDestroy called");
		}

		super.onDestroy();
	}

	public void onTaskCompleted(String result) {
		Log.i(TAG, this + " has been notified the task is complete.");
		if (mProgress != null && mProgress.isShowing())
			mProgress.dismiss();
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		if (result.equals(BatonServerCommunicator.REPLY_MESSAGE_LOGIN_SUCCESS)) {
			goToMainScreen();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btnJoin:
			String loginId = txtloginId.getText().toString();
			String classroom = txtClassroom.getText().toString();
			String password = txtPassword.getText().toString();
			String teacherId = txtTeacherId.getText().toString();
			CommonUtilities.putGlobalVar(this, UserProfile.LOGINID_WEB_STR, loginId);
			// Check if user filled the form
			if (loginId.trim().length() > 0 && password.trim().length() > 0
					&& classroom.trim().length() > 0 && teacherId.trim().length() > 0) {
				mTask = new AsyncJoinTask(this);
				mTask.execute(new String[] { loginId, classroom, password, teacherId });
			} else {
				alert.showAlertDialog(JoinActivity.this, "Incompleted Information",
						"Please fill the form", false);
			}
			break;
		case R.id.login_btnRegister:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		}
		;
	}

	public void goToMainScreen() {
		Intent i = new Intent(this, MainScreenActivity.class);
		startActivity(i);
		finish();
	}

	private void showMyProgressDialog(ProgressDialog pd) {
		pd.setMessage("Joining classroom...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setProgress(0);
		pd.show();
	}

	/*
	 * AsyncTask<Type1, Type2, Type3> 1.The type of information that is needed
	 * to process the task (e.g., URLs to download) 2. The type of information
	 * that is passed within the task to indicate progress 3. The type of
	 * information that is passed when the task is completed to the post-task
	 * code
	 */
	class AsyncJoinTask extends AsyncTask<String, Void, Void> {
		private JoinActivity activity;
		public boolean isCompleted = false;
		private String result;

		private AsyncJoinTask(JoinActivity act) {
			this.activity = act;
		}

		@Override
		protected void onPreExecute() {
			this.activity.showMyProgressDialog(mProgress);
		}

		@Override
		protected Void doInBackground(String... token) {
			result = BatonServerCommunicator.login(getApplicationContext(), token);
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... unused) {
		}

		@Override
		protected void onPostExecute(Void unused) {
			/** notify the UI thread that the process is completed */
			isCompleted = true;
			notifyActivityTaskCompleted(result);
		}

		private void setActivity(JoinActivity act) {
			this.activity = act;
			if (isCompleted) {
				notifyActivityTaskCompleted(result);
			}
		}

		/**
		 * Helper method to notify the activity that this task was completed.
		 * 
		 * @author Fiona
		 */
		private void notifyActivityTaskCompleted(String result) {
			if (null != activity) {
				activity.onTaskCompleted(result);
			}
		}
	}
}
