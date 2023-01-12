package com.dalcho.adme.exception.notfound;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

// 댓글이 없을 때
public class CommentNotFoundException extends CustomException {
    public CommentNotFoundException(){
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
