package ca.utoronto.ece1778.baton.util;

public final class Constants {
	/**Baton sync server url on Amazon*/
	public static final String SERVER_URL = "http://54.213.105.123:8080/BatonSyncServer";

	/**Baton sync server url on localhost*/
//	public static final String SERVER_URL = "http://192.168.1.147:8080/BatonSyncServer";
	/**Baton sync server url Victor */
	//public static final String SERVER_URL = "http://138.51.220.99:8080/BatonSyncServer";
	/**Baton Google project id*/
	public static final String SENDER_ID = "553157495789";
	
	// For universal usage of time format
	public static String DATE_FORMAT_LONG = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	
	/**path for Typeface assets*/
	public final static String TYPEFACE_COMIC_RELIEF = "fonts/ComicRelief.ttf";
	public final static String TYPEFACE_ACTION_MAN_BOLD = "fonts/Action_Man_Bold.ttf";

	public final static String SQLLITE_STUDENT_DATABASE_NAME = "baton_student";
	public final static String SQLLITE_TABLE_USER_PROFILE = "GCM_USER_PROFILE";
	public final static String USER_COLUMN_KEY = "id";
	
	public static final String CLASS_PARTICIPATE_IN_LESSON = "class_participate_in_lesson";
	public static final String GCM_TICKETS_UIDS_IN_LESSON = "tickets_uids_in_lesson" ;
	public static final String DISPLAY_TALK_TICKET_ACTION = "/ca.utoronto.ece1778.baton.student.DISPLAY_TALK_TICKET";
}
