package com.dalcho.adme.exception.bad_request;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;
import lombok.Getter;

@Getter
public class BadConstantException extends CustomException {
	public BadConstantException() {
		super(ErrorCode.BAD_CONSTANT);
	}
}
