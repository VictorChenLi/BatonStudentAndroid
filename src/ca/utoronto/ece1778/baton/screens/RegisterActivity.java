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

import com.baton.publiclib.model.usermanage.UserProfile;
import com.google.android.gcm.GCMRegistrar;

//import ca.utoronto.ece1778.baton.models.StudentProfile;

/**
 * User register
 * 
 * @author Yi Zhao
 * 
 */
public class RegisterActivity extends Activity implements OnClickListener {
	static final String TAG = "RegisterActivity";

	AlertDialogManager alert = new AlertDialogManager();

	private RegisterActivity demo;

	// UI elements
	EditText txtFirstName;
	EditText txtLastName;
	EditText txtEmail;
	EditText txtLoginID;
	EditText txtPassword;
	EditText txtConfirmPwd;
	Button btnRegister;

	AsyncRegisterTask mTask = null;
	ProgressDialog mProgress = null;

	String pw = null;
	String con_pw = null;

	String gcm_id;

	UserProfile mStudentProfile = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Log.i(TAG, "RegisterActivity onCreate called");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		demo = this;
		txtFirstName = (EditText) findViewById(R.id.register_txtFirstName);
		txtLastName = (EditText) findViewById(R.id.register_txtLastName);
		txtEmail = (EditText) findViewById(R.id.register_txtEmail);
		txtLoginID = (EditText) findViewById(R.id.register_txtLoginID);
		txtPassword = (EditText) findViewById(R.id.register_txtPassword);
		txtConfirmPwd = (EditText) findViewById(R.id.register_txtConfirmPwd);
		gcm_id = GCMRegistrar.getRegistrationId(getApplicationContext());

		btnRegister = (Button) findViewById(R.id.register_btnRegister);
		btnRegister.setOnClickListener(this);
		if (mProgress == null)
			mProgress = new ProgressDialog(this);
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
				mTask = (AsyncRegisterTask) objectTask;
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
	public void onClick(View v) {
		String fName = txtFirstName.getText().toString();
		String lName = txtLastName.getText().toString();
		String loginId = txtLoginID.getText().toString();
		String email = txtEmail.getText().toString();
		pw = txtPassword.getText().toString();
		con_pw = txtConfirmPwd.getText().toString();

		mStudentProfile = new UserProfile(gcm_id, loginId, email, pw, fName,
				lName, UserProfile.USERTYPE_STUDENT);
		// Check if user filled the form
		if (!isProfileCompleted(mStudentProfile)) {
			alert.showAlertDialog(RegisterActivity.this,
					"Uncompleted information", "Please enter your details",
					false);

		} else if (!isPasswordMatch()) { // user doen't filled that data ask him
											// to fill the form
			Toast.makeText(this, "Password entered doesn't match.",
					Toast.LENGTH_LONG).show();
		} else {
			mTask = new AsyncRegisterTask(this);
			mTask.execute(new UserProfile[] { mStudentProfile });
		}

	}

	public void goToJoinPage() {
		Intent i = new Intent(this, JoinActivity.class);
		i.putExtra(UserProfile.LOGINID_WEB_STR, mStudentProfile.getLogin_id());
		// TODO dealing with MD5
		i.putExtra(UserProfile.PASSWORD_DB_STR, mStudentProfile.getPassword());
		startActivity(i);
		finish();
	}

	public void onTaskCompleted(String result) {
		if (mProgress != null && mProgress.isShowing())
			mProgress.dismiss();
		Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
		if (result.equals(BatonServerCommunicator.REPLY_MESSAGE_REGISTER_SUCCESS)) {
			goToJoinPage();
		}
	}

	private void showMyProgressDialog(ProgressDialog pd) {
		pd.setMessage("Registering...");
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setCancelable(false);
		pd.setProgress(0);
		pd.show();
	}

	private boolean isProfileCompleted(final UserProfile user) {
		if (user.getEmail().trim().length() > 0
				&& user.getF_name().trim().length() > 0
				&& user.getGcm_regid().trim().length() > 0
				&& user.getL_name().trim().length() > 0
				&& user.getPassword().trim().length() > 0)
			return true;
		return false;
	}

	private boolean isPasswordMatch() {
		if (pw.equals(con_pw))
			return true;
		return false;
	}

	/*
	 * AsyncTask<Type1, Type2, Type3> 1.The type of information that is needed
	 * to process the task (e.g., URLs to download) 2. The type of information
	 * that is passed within the task to indicate progress 3. The type of
	 * information that is passed when the task is completed to the post-task
	 * code
	 */
	class AsyncRegisterTask extends AsyncTask<UserProfile, Void, Void> {
		RegisterActivity activity;
		public boolean isCompleted = false;
		String result = null;

		private AsyncRegisterTask(RegisterActivity act) {
			this.activity = act;
		}

		@Override
		protected void onPreExecute() {
			mProgress.setMessage("Registering...");
			mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgress.setCancelable(false);
			mProgress.setProgress(0);
			mProgress.show();
		}

		@Override
		protected Void doInBackground(UserProfile... users) {
			UserProfile u = users[0];
			result = BatonServerCommunicator.register(demo, u);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			/** notify the UI thread that the process is completed */
			isCompleted = true;
			notifyActivityTaskCompleted(result);
		}

		private void notifyActivityTaskCompleted(String result2) {
			if (null != activity) {
				activity.onTaskCompleted(result);
			}
		}

		private void setActivity(RegisterActivity act) {
			this.activity = act;
			if (isCompleted) {
				notifyActivityTaskCompleted(result);
			}
		}
	}

}
