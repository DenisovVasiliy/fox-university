package com.foxminded.foxuniversity.dao.exceptions;

public class ExceptionsMessageConstants {
    private ExceptionsMessageConstants() { }

    public static final String UNABLE_GET_ALL = "Unable to get all %ss";
    public static final String ENTITY_NOT_FOUND = "%s with ID '%s' not found";
    public static final String UNABLE_GET_BY_ID = "Unable to get %s with ID '%s'";
    public static final String UNABLE_GET_BY_ENTITY = "Unable to get %s by '%s'";
    public static final String UNABLE_DELETE = "Unable to delete '%s'";
    public static final String DELETION_RESTRICTED = "Deletion was restricted: %s has relations to the %s";
    public static final String QUERY_RESTRICTED_DUPLICATE_KEY = "Operation was restricted: %s is already assigned.";
    public static final String QUERY_RESTRICTED_NO_SUCH_ID = "Operation was restricted: " +
            "There is no such %s or %s";
    public static final String UNABLE_SAVE = "Unable to save '%s'";
    public static final String UNABLE_UPDATE  = "Unable to update '%s'";
    public static final String UNABLE_ASSIGN = "Unable assign %s to %s";
    public static final String UNABLE_UPDATE_ASSIGNMENT = "Unable update assignment of %s to %s";
    public static final String UNABLE_DELETE_FROM = "Unable delete %s from %s";
}
