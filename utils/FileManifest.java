package assignment2002.utils;

import java.util.Map;

public interface FileManifest {
	public static final String WORKING_PATH = "Information/";
	public static final String APPLICANT_TXT_PATH = WORKING_PATH + "ApplicantList.txt";
	public static final String OFFICER_TXT_PATH = WORKING_PATH + "OfficerList.txt";
	public static final String MANAGER_TXT_PATH = WORKING_PATH + "ManagerList.txt";
	public static final String PROJECT_TXT_PATH = WORKING_PATH + "ProjectList.txt";
	public static final String APPLICATION_TXT_PATH = WORKING_PATH + "Application.txt";
	
	public static enum PROJECT_COLUMNS {
		PROJECT_NAME,
		NEIGHBOURHOOD,
		TWO_ROOM,
		TWO_ROOM_AMT,
		TWO_ROOM_PRICE,
		THREE_ROOM,
		THREE_ROOM_AMT,
		THREE_ROOM_PRICE,
		OPEN_DATE,
		CLOSE_DATE,
		MANAGER,
		OFFICER_SLOT,
		APPROVED_OFFICERS,
		PENDING_OFFICERS,
		REJECTED_OFFICERS,
		VISIBLE
	}
	
	public static enum USER_COLUMNS {
		NAME,
		NRIC,
		AGE,
		MARITAL_STATUS,
		PASSWORD
	}
	
	public static enum APPLICATION_COLUMNS {
		NRIC,
		NAME,
		FLATTYPE,
		PROJECTNAME,
		STATUS
	}
	
	public static final Map<String, Integer> PROJECT_COLUMNS_MAP = Map.ofEntries(
		    Map.entry(PROJECT_COLUMNS.PROJECT_NAME.toString(), 0),
		    Map.entry(PROJECT_COLUMNS.NEIGHBOURHOOD.toString(), 1),
		    Map.entry(PROJECT_COLUMNS.TWO_ROOM.toString(), 2),
		    Map.entry(PROJECT_COLUMNS.TWO_ROOM_AMT.toString(), 3),
		    Map.entry(PROJECT_COLUMNS.TWO_ROOM_PRICE.toString(), 4),
		    Map.entry(PROJECT_COLUMNS.THREE_ROOM.toString(), 5),
		    Map.entry(PROJECT_COLUMNS.THREE_ROOM_AMT.toString(), 6),
		    Map.entry(PROJECT_COLUMNS.THREE_ROOM_PRICE.toString(), 7),
		    Map.entry(PROJECT_COLUMNS.OPEN_DATE.toString(), 8),
		    Map.entry(PROJECT_COLUMNS.CLOSE_DATE.toString(), 9),
		    Map.entry(PROJECT_COLUMNS.MANAGER.toString(), 10),
		    Map.entry(PROJECT_COLUMNS.OFFICER_SLOT.toString(), 11),
		    Map.entry(PROJECT_COLUMNS.APPROVED_OFFICERS.toString(), 12),
		    Map.entry(PROJECT_COLUMNS.PENDING_OFFICERS.toString(), 13),
		    Map.entry(PROJECT_COLUMNS.REJECTED_OFFICERS.toString(), 14),
		    Map.entry(PROJECT_COLUMNS.VISIBLE.toString(), 15)
		    );
	
	public static final Map<String, Integer> USER_COLUMNS_MAP = Map.ofEntries(
		    Map.entry(USER_COLUMNS.NAME.toString(), 0),
		    Map.entry(USER_COLUMNS.NRIC.toString(), 1),
		    Map.entry(USER_COLUMNS.AGE.toString(), 2),
		    Map.entry(USER_COLUMNS.MARITAL_STATUS.toString(), 3),
		    Map.entry(USER_COLUMNS.PASSWORD.toString(), 4)
		    );

	public static final Map<String, Integer> APPLICATION_COLUMNS_MAP = Map.ofEntries(
		    Map.entry(APPLICATION_COLUMNS.NRIC.toString(), 0),
		    Map.entry(APPLICATION_COLUMNS.NAME.toString(), 1),
		    Map.entry(APPLICATION_COLUMNS.FLATTYPE.toString(), 2),
		    Map.entry(APPLICATION_COLUMNS.PROJECTNAME.toString(), 3),
		    Map.entry(APPLICATION_COLUMNS.STATUS.toString(), 4)
		    );
}
