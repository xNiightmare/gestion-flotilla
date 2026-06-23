package com.grandedev.gestionflotilla.exception;


import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Error de validacion");

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                400,
                "VALIDATION ERROR",
                errorMessage,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDenied(
            AccessDeniedException ex
    ){
        CustomErrorResponse errorResponse =
                new CustomErrorResponse(
                        403,
                        "Access denied",
                        "No tienes permisos para acceder a este recurso",
                        LocalDateTime.now()
                );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CustomErrorResponse> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex
    ){
        CustomErrorResponse errorResponse =
                new CustomErrorResponse(
                  413,
                  "FILE TOO LARGE",
                  "El archivo excede el tamanio permitido",
                  LocalDateTime.now()
                );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.CONTENT_TOO_LARGE
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomErrorResponse> handleBadCredentialsException(BadCredentialsException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                401,
                ex.getMessage(),
                "Credenciales invalidas",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(
                errorResponse,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleDuplicateKeyException(UsernameAlreadyExistsException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                409,
                ex.getMessage(),
                "El nombre de usuario no se encuetra disponible.",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<CustomErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                409,
                ex.getMessage(),
                "El email no se encuentra disponible.",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        CustomErrorResponse errorResponse = new CustomErrorResponse(
            404,
            ex.getMessage(),
            "El recurso solicitado no ha podido ser encontrado",
            LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAllowedRole.class)
    public ResponseEntity<CustomErrorResponse> handleNotAllowedRole(Exception ex){
        ex.printStackTrace();

        CustomErrorResponse errorResponse = new CustomErrorResponse(
                417,
                ex.getMessage(),
                "El servidor se rehúsa a intentar hacer café con una tetera.",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.EXPECTATION_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleGenericException(Exception ex) {

        ex.printStackTrace();

        CustomErrorResponse errorResponse = new CustomErrorResponse(
            500,
            "ERROR INTERNO",
            "Ha ocurrido un error inesperado",
            LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }


}
