package assignment2002.utils;

public interface Status {
	public enum REGISTRATION { PENDING, REJECTED, SUCCESSFUL };
	public enum APPLICATION_STATUS {
        SUCCESSFUL,
        UNSUCCESSFUL,
        PENDING,
        BOOKED,
        NOTAPPLIED,
        PENDINGWITHDRAWN,
        WITHDRAWN
    }
}
