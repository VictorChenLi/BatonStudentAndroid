package ca.utoronto.ece1778.baton.models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import ca.utoronto.ece1778.baton.util.Constants;

/**
 * 
 * @author Yi Zhao
 * 
 */
public class Ticket {
	/** string for communication with sync server as a parameter name */
	public static final String POST_TICKET_TYPE = "ticketType";
	/** string for communication with sync server as a parameter name */
	public static final String POST_TICKET_CONTENT = "ticketContent";
	/** string for communication with sync server as a parameter name */
	public static final String POST_TIME_STAMP = "timeStamp";
	
	private String ticketType;
	private String ticketContent;
	private String timeStamp;

	

	public Ticket(String ticketType, String ticketContent, String timeStamp) {
		super();
		this.ticketType = ticketType;
		this.ticketContent = ticketContent;
		this.timeStamp = timeStamp;
	}



	public String getTicketType() {
		return ticketType;
	}



	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}



	public String getTicketContent() {
		return ticketContent;
	}



	public void setTicketContent(String ticketContent) {
		this.ticketContent = ticketContent;
	}



	public String getTimeStamp() {
		return timeStamp;
	}



	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}



	public static String getPostTicketType() {
		return POST_TICKET_TYPE;
	}



	public static String getPostTicketContent() {
		return POST_TICKET_CONTENT;
	}



	public static String getPostTimeStamp() {
		return POST_TIME_STAMP;
	}
}
