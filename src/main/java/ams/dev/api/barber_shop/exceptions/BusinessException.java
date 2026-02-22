package ams.dev.api.barber_shop.exceptions;

import java.util.List;
import java.util.Map;

public class BusinessException extends RuntimeException {

    private Map<String, List<String>> validationErrors;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, Map<String, List<String>> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public Map<String, List<String>> getValidationErrors() {
        return validationErrors;
    }
}
