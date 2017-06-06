package ua.metro.mobileapp;

public interface OnTaskCompleted {
	
	void onTaskCompleted(int id_request);
	
	void onTaskCompletedJSON(String json, int id_request);
}
