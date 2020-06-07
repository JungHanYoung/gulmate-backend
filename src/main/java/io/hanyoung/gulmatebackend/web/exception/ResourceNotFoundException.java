package io.hanyoung.gulmatebackend.web.exception;

import lombok.Getter;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class ResourceNotFoundException extends Throwable {

    private static final String messageTemplate = "Resource Not Found Error";

    public ResourceNotFoundException(Class<?> entity) {
        super(messageTemplate + ": " + entity.getTypeName());
    }

}
