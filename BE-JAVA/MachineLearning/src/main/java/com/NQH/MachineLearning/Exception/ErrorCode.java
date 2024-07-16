/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.NQH.MachineLearning.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 *
 * @author nqhkt
 */
@Getter

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error",HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed",HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least 3 characters",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters",HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed",HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZE(1007,"Unauthorize",HttpStatus.FORBIDDEN),
    LABELINCORERCT(1008,"One or more specified labels are not in the train dataset columns",HttpStatus.BAD_REQUEST),
    DATASET_NOT_EXISTED(1009,"Dataset not existed",HttpStatus.NOT_FOUND),
    TRAINING_NOT_EXISTED(1010,"Training not existed",HttpStatus.NOT_FOUND),
    MODEL_NOT_EXISTED(1011,"Model not existed",HttpStatus.NOT_FOUND),
    DATA_NOT_EXISTED(1012,"Data not existed",HttpStatus.NOT_FOUND),
    DATA_USING(1013,"Data is using",HttpStatus.BAD_REQUEST),
    HANDLE_FILE_ERROR(1014,"An error occurred while processing the file",HttpStatus.BAD_REQUEST),
    DATASET_EXISTED(1015,"Dataset existed !",HttpStatus.BAD_REQUEST),
    TRAINING_EXISTED(1016,"Training existed !",HttpStatus.BAD_REQUEST)
    ;

    ErrorCode(int code, String message,HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
    private int code;
    private String message;
    private HttpStatusCode httpStatusCode;
}
