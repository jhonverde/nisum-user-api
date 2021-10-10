package cl.com.nisum.user.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApiControllerAdvice {

    private final String MESSAGE_GENERIC_EXCEPTION = "En estos momentos no lo podemos atender, intente nuevamente en unos minutos";

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, HttpRequestMethodNotSupportedException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(Exception ex) {
        log.error("BadRequestException: {}", ex.getMessage());

        List<FieldError> fieldErrors;
        String message;

        if(ex instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors();
            message = fieldErrors.stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("|"));
        }else if(ex instanceof HttpRequestMethodNotSupportedException) {
            message = "Path request inválido, agregar párametros o variables";
        }else if(ex instanceof HttpMessageNotReadableException) {
            message = "Body JSON de la solicitud es inválida";
        }else {
            message = MESSAGE_GENERIC_EXCEPTION;
        }

        return new ApiErrorResponse(message);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(Exception ex) {
        log.error("NotFoundException: {}", ex.getMessage(), ex);

        return new ApiErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = {UnprocessableEntity.class})
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiErrorResponse handleUnprocessableEntityException(Exception ex) {
        log.error("UnprocessableEntityException: {}", ex.getMessage(), ex);

        return new ApiErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse handleException(Exception ex) {
        log.error("Exception: {}", ex.getMessage(), ex);

        return new ApiErrorResponse(MESSAGE_GENERIC_EXCEPTION);
    }
}
