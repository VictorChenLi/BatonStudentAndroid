package ca.utoronto.ece1778.baton.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import ca.utoronto.ece1778.baton.STUDENT.R;
import ca.utoronto.ece1778.baton.screens.TalkParticipantArrayAdapter.TalkBuddyRowHolder;
import ca.utoronto.ece1778.baton.syncserver.BatonServerCommunicator;
import ca.utoronto.ece1778.baton.util.CommonUtilities;
import ca.utoronto.ece1778.baton.util.Constants;
import ca.utoronto.ece1778.baton.util.WakeLocker;

import com.baton.publiclib.infrastructure.exception.ServiceException;
import com.baton.publiclib.model.classmanage.ClassLesson;
import com.baton.publiclib.model.classmanage.ClassParticipate;

/**
 * 
 * @author Yi Zhao
 * 
 */
public class MainScreenActivity extends FragmentActivity implements
		ActionBar.TabListener {
	final static String TAG = "MainScreenActivity";
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	/** UI item on talk tab */
	List<ClassParticipate> talkBuddies = new ArrayList<ClassParticipate>();
	ListView talkListView;
	TalkParticipantArrayAdapter talkListAdapter;
	
	private final BroadcastReceiver mMessageReceiver_talk = new TalkTicketBroadcastReceiver();

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy called");
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_screen);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		registerReceiver(mMessageReceiver_talk, new IntentFilter(
				Constants.DISPLAY_TALK_TICKET_ACTION));

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		new GetBuddiesTask().execute();
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_profile:
			return true;
		case R.id.menu_exit_classroom:
			return true;
		case R.id.menu_about:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch (position) {
			case 0:
				fragment = new TalkTagFragment();
				break;
			/*
			 * case 1: fragment = new WorkTagFragment(); break;
			 */
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 2 total pages.
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * get current class participate status from server
	 * 
	 * @author fiona
	 * 
	 */
	protected class GetBuddiesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... unused) {
			talkListView = (ListView) mViewPager.findViewById(R.id.talk_listView_buddies);
			while (talkListView == null) {// wait until gridView is created
				talkListView = (ListView) mViewPager.findViewById(R.id.talk_listView_buddies);
				try {
					Thread.sleep(1 * 1000); // sleep for one second
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			String lid = CommonUtilities.getGlobalVar(MainScreenActivity.this, ClassLesson.LESSONID_WEB_STR);
			Log.i(TAG, "GetBuddiesTask lesson_id:" + lid);
			try {
				talkBuddies = BatonServerCommunicator.getCurrentLoginBuddies(lid);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Void... unused) {
			super.onProgressUpdate();
		}

		@Override
		protected void onPostExecute(Void unused) {
			talkListAdapter = new TalkParticipantArrayAdapter(MainScreenActivity.this, R.layout.list_item_talk_buddies,
					talkBuddies);
			talkListView.setAdapter(talkListAdapter);
		}

	}

	class TalkTicketBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("MainScreenActivity", "onReceive Called");

			WakeLocker.acquire(getApplicationContext());
			ArrayList<Integer> uid_list = intent.getIntegerArrayListExtra(Constants.GCM_TICKETS_UIDS_IN_LESSON);
			talkListView = (ListView) findViewById(R.id.talk_listView_buddies);
			int count = talkListView.getCount();
			Log.i(TAG,"count in ListView: " + count);
			for (int uid : uid_list) {
				//this listView has a header, so, should begin with the second child
				for (int i = 1; i < count; i++) {
					LinearLayout linearlayout = (LinearLayout)talkListView.getAdapter().getView(i, null, null);  
					TextView textview = (TextView)linearlayout.findViewById(R.id.talk_txtBuddyName);  
					TalkBuddyRowHolder item = (TalkBuddyRowHolder) (linearlayout.getTag());
					Log.i(TAG, "item.uid: "+ item.uid + "textview: "+textview.getText().toString());
					if (item.uid == uid) {
						//TODO: color does not change....
						Log.i(TAG,"set uid:"+uid+" to green");
						textview.setTextColor(Color.GREEN);
						item.txtName.setText("here!");
					}
				}
			}
		
			WakeLocker.release();
		}
	}

}
