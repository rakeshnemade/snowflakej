/**
 *<p>
 *This program is confidential and proprietary to Lumiata Inc. and
 *may not be reproduced, published, or disclosed to others without
 *company authorization. Copyright 2013-2016 by Lumiata Inc.
 */

package com.lumiata.exception;

/**
 * @author rakesh
 *
 */
public class BadRequestException extends ApiException {
	private int code;

	public BadRequestException(int code, String msg) {
		super(code, msg);
		this.code = code;
	}

}
