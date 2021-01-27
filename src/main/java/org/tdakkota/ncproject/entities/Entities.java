package org.tdakkota.ncproject.entities;

public final class Entities {
    public final static String BLANK_NAME_MESSAGE = "Name may not be blank";

    public final static String BLANK_USERNAME_MESSAGE = "Username may not be blank";

    public final static String BLANK_PASSWORD_MESSAGE = "Password may not be blank";

    public final static String USER_NAME_REGEXP = "^[a-zA-Z][\\sa-zA-Z]*$";

    public final static String USER_DEFAULT_ROLE = "user";

    public final static int USER_USERNAME_LENGTH_LIMIT = 20;

    public final static int NAME_LENGTH_LIMIT = 50;

    public final static int DESCRIPTION_LENGTH_LIMIT = 1000;

    public final static int INCIDENT_DESCRIPTION_LENGTH_LIMIT = 100;

    private Entities() {
    }
}
