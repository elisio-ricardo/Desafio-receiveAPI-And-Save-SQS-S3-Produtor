package com.elisio.sensidia.DesafioSensidia.framework.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CustomUploadExceptionHandlerTest {


    @InjectMocks
    private CustomUploadExceptionHandler exceptionHandler;

    @Test
    void handleValidationErrorException() {
        List<String> errors = new ArrayList<>();
        errors.add("Error1");
        errors.add("Error2");
        var validationErrorException = new ValidationErrorException(errors);
        var response = exceptionHandler.handleValidationErrorException(validationErrorException);

        assertNotNull(response);
        assertEquals(2, response.getBody().getErrors().size());
    }

    @Test
    void handleJsonException() {
        var validationParseJsonException = new ValidationParseJsonException("error");
        var response = exceptionHandler.handleJsonException(validationParseJsonException);

        assertNotNull(response);
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void handleAwsExceptionException() {

        var awsException = new AwsException("error");
        var response = exceptionHandler.handleAwsExceptionException(awsException);

        assertNotNull(response);
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}