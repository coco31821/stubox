package io.coco318213.stubox.user.exception;

import io.coco318213.stubox.common.constants.ErrorCode;
import io.coco318213.stubox.common.exception.BusinessException;

public class DuplicateNicknameException extends BusinessException {

    public DuplicateNicknameException() {
        super(ErrorCode.USER_NICKNAME_DUPLICATED);
    }
}
