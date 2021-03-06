package br.com.cvc.banktransferscheduler.usecases.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


@RestControllerAdvice
public class ValidationHandler {

    @Autowired
    private MessageSource messageSource;  // class that helps to get error message according to message's origin (path parameter Accept-Language)

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<InputError> inputErrorExceptionHandler(MethodArgumentNotValidException exception) {
        List<InputError> errors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = e.getField() + " " + messageSource.getMessage(e, LocaleContextHolder.getLocale());
            InputError error = new InputError(e.getField(), message);
            errors.add(error);
        });
        return errors;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FeeTypeException.class)
    public Object feeTypeExceptionHandler(FeeTypeException e) {
        FeeTypeException feeTypeException = new FeeTypeException(e.getDate(), e.getValue());
        return feeTypeException.getMessage();
    }


}
