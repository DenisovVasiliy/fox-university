package com.foxminded.foxuniversity.dao.exceptions;

public class QueryRestrictedException extends QueryNotExecuteException {
    public QueryRestrictedException() {
        super();
    }

    public QueryRestrictedException(String message, Throwable cause) {
        super(message, cause);
    }

    public QueryRestrictedException(String message) {
        super(message);
    }
}
