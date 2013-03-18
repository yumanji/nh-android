package com.movetothebit.newholland.android.utils;

public class ServerException extends Exception {

	public String message = null;
	
	private static final long serialVersionUID = 1L;

	public ServerException(Exception e, String string) {
		this.message = string;
	
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
