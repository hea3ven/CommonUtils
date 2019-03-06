package com.hea3ven.tools.commonutils.mod;

public class ModuleLoadingException extends RuntimeException {

    public ModuleLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleLoadingException(Throwable cause) {
        super(cause);
    }
}
