package io.coco318213.stubox.comment.exception;

import io.coco318213.stubox.common.constants.ErrorCode;
import io.coco318213.stubox.common.exception.BusinessException;

public class CommentNotFoundException extends BusinessException {

    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND);
    }
}
