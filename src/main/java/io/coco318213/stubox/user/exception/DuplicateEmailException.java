package io.coco318213.stubox.user.exception;


import io.coco318213.stubox.common.constants.ErrorCode;
import io.coco318213.stubox.common.exception.BusinessException;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.USER_EMAIL_DUPLICATED);
    }
}
