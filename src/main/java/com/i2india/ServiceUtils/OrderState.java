package com.i2india.ServiceUtils;

public interface OrderState {
	final static String SUCCESS = "SUCCESS";
	final static String FAIL = "FAIL";
	final static String REFUND = "REFUND";
	final static String INITIATED = "INITIATED";
	final static String DELIVERED = "DELIVERED";
	final static String CLOSED = "CLOSED";
	final static String UPDATE = "UPDATE";
	final static String DISPUTE = "DISPUTE";
}
