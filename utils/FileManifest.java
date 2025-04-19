package assignment2002.utils;

import java.util.Map;

public interface FileManifest {
	public static final String APPLICANT_TXT_PATH = "Information/ApplicantList.txt";
	public static final String OFFICER_TXT_PATH = "Information/OfficerList.txt";
	public static final String MANAGER_TXT_PATH = "Information/ManagerList.txt";
	public static final String PROJECT_TXT_PATH = "Information/ProjectList.txt";
	public static final String APPLICATION_TXT_PATH = "Information/Application.txt";
	
	public static enum PROPERTY_COLUMNS {
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
	
	public static final Map<String, Integer> PROPERTY_COLUMNS_MAP = Map.ofEntries(
		    Map.entry(PROPERTY_COLUMNS.PROJECT_NAME.toString(), 0),
		    Map.entry(PROPERTY_COLUMNS.NEIGHBOURHOOD.toString(), 1),
		    Map.entry(PROPERTY_COLUMNS.TWO_ROOM.toString(), 2),
		    Map.entry(PROPERTY_COLUMNS.TWO_ROOM_AMT.toString(), 3),
		    Map.entry(PROPERTY_COLUMNS.TWO_ROOM_PRICE.toString(), 4),
		    Map.entry(PROPERTY_COLUMNS.THREE_ROOM.toString(), 5),
		    Map.entry(PROPERTY_COLUMNS.THREE_ROOM_AMT.toString(), 6),
		    Map.entry(PROPERTY_COLUMNS.THREE_ROOM_PRICE.toString(), 7),
		    Map.entry(PROPERTY_COLUMNS.OPEN_DATE.toString(), 8),
		    Map.entry(PROPERTY_COLUMNS.CLOSE_DATE.toString(), 9),
		    Map.entry(PROPERTY_COLUMNS.MANAGER.toString(), 10),
		    Map.entry(PROPERTY_COLUMNS.OFFICER_SLOT.toString(), 11),
		    Map.entry(PROPERTY_COLUMNS.APPROVED_OFFICERS.toString(), 12),
		    Map.entry(PROPERTY_COLUMNS.PENDING_OFFICERS.toString(), 13),
		    Map.entry(PROPERTY_COLUMNS.REJECTED_OFFICERS.toString(), 14),
		    Map.entry(PROPERTY_COLUMNS.VISIBLE.toString(), 15)
		    );

	public static int getCol(PROPERTY_COLUMNS col) {
		return PROPERTY_COLUMNS_MAP.get(col.toString());
	}
	
}
