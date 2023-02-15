package com.example.final_project.controller.session.exceptions;


/**
 * Exception called when user try to reach page to which user does not have access
 */
public class SomethingWentWrongException
        extends RuntimeException {
    public SomethingWentWrongException() {
        super();
    }
}