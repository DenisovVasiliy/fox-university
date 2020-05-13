package com.foxminded.foxuniversity.dao.exceptions;

public class QueryNotExecuteException extends RuntimeException {
    public QueryNotExecuteException() {
        super();
    }

    public QueryNotExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryNotExecuteException(String message) {
        super(message);
    }
}
