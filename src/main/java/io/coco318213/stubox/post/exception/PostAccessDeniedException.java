package io.coco318213.stubox.post.exception;

import io.coco318213.stubox.common.constants.ErrorCode;
import io.coco318213.stubox.common.exception.BusinessException;

public class PostAccessDeniedException extends BusinessException {

    public PostAccessDeniedException() {
        super(ErrorCode.FORBIDDEN);
    }
}
