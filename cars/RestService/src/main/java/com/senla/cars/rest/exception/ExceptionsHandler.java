package com.senla.cars.rest.exception;

import com.senla.cars.api.dto.ResponseDto;
import com.senla.cars.serviceImpl.exception.IncorrectException;
import com.senla.cars.serviceImpl.exception.NotEmptyException;
import com.senla.cars.serviceImpl.exception.NotFoundException;
import com.senla.cars.serviceImpl.exception.ad.EmailMismatchException;
import com.senla.cars.serviceImpl.exception.ad.InvalidPhoneNumberException;
import com.senla.cars.serviceImpl.exception.ad.PromotionException;
import com.senla.cars.serviceImpl.exception.auth.BlockedEmailException;
import com.senla.cars.serviceImpl.exception.auth.DeletedUserException;
import com.senla.cars.serviceImpl.exception.user.EmailAlreadyExistsException;
import com.senla.cars.serviceImpl.exception.user.InvalidEmailException;
import com.senla.cars.serviceImpl.exception.user.InvalidPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handlerNotFound(NotFoundException ex) {
        return new ResponseEntity<>
                (new ResponseDto<>(false, ex.getMessage()),  HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            NotEmptyException.class,
            IncorrectException.class,
            InvalidPasswordException.class,
            InvalidEmailException.class,
            EmailAlreadyExistsException.class,
            DeletedUserException.class,
            PromotionException.class,
            InvalidPhoneNumberException.class,
            EmailMismatchException.class,
    })
    public ResponseEntity<Object> handlerNotEmpty(NotEmptyException ex) {
        return new ResponseEntity<>
                (new ResponseDto<>(false, ex.getMessage()),  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BlockedEmailException.class)
    public ResponseEntity<Object> handlerBlockedEmail(BlockedEmailException ex) {
        return new ResponseEntity<>
                (new ResponseDto<>(false, ex.getMessage()),  HttpStatus.LOCKED);
    }

}
