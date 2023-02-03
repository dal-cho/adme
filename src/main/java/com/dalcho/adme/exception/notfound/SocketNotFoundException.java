package com.dalcho.adme.exception.notfound;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class SocketNotFoundException  extends CustomException {
	public SocketNotFoundException(){
		super(ErrorCode.SOCKET_NOT_FOUND);
	}
}
