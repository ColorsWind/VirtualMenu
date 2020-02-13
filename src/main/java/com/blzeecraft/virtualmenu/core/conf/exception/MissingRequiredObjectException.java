package com.blzeecraft.virtualmenu.core.conf.exception;

public class MissingRequiredObjectException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MissingRequiredObjectException() {
		super();
	}

	public MissingRequiredObjectException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissingRequiredObjectException(String message) {
		super(message);	
	}

	public MissingRequiredObjectException(Throwable cause) {
		super(cause);
	}

}
