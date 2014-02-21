package ca.utoronto.ece1778.baton.util;

public final class Constants {
	public final static int TALK_INTENT_BUILD = 0x0001;
	public final static int TALK_INTENT_CHALLENGE = 0x0002;
	public final static int TALK_INTENT_QUESTION = 0x0003;
	public final static int TALK_INTENT_NEW_IDEA = 0x0004;

	public final static String TALK_TICKET_TIMESTAMP = "talk_ticket_timestamp";
	public final static String TALK_TICKET_INTENT = "talk_ticket_intent";

	public final static String SQLLITE_STUDENT_DATABASE_NAME = "baton_student";
	public final static String SQLLITE_TABLE_USER_PROFILE = "GCM_USER_PROFILE";
	public final static String USER_COLUMN_KEY = "id";
}
