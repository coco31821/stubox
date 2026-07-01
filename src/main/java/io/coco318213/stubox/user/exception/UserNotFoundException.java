package io.coco318213.stubox.user.exception;


import io.coco318213.stubox.common.constants.ErrorCode;
import io.coco318213.stubox.common.exception.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
