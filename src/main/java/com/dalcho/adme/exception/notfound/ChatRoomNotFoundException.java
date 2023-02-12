package com.dalcho.adme.exception.notfound;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class ChatRoomNotFoundException  extends CustomException {
	public ChatRoomNotFoundException(){
		super(ErrorCode.CHATROOM_NOT_FOUND);
	}
}
