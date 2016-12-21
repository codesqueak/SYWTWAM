package com.codingrodent.microservice.template.constants;

/**
 *
 */
public class ValidationConstants {

    private ValidationConstants() {
        // Never need to make an instance of this class
    }

    public final static String UUID_V4_REGEX = "(?i)[0-9A-F]{8}-[0-9A-F]{4}-[4][0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}";

    public final static int NAME_FIELD_MIN_LENGTH = 1;
    public final static int NAME_FIELD_MAX_LENGTH = 40;
}
