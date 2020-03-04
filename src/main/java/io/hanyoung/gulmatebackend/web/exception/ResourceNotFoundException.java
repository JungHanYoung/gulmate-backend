package io.hanyoung.gulmatebackend.web.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends Throwable {

    private static final String messageTemplate = "Resource Not Found Error";

    public ResourceNotFoundException(Class<?> entity) {
        super(messageTemplate + ": " + entity.getTypeName());
    }

}
