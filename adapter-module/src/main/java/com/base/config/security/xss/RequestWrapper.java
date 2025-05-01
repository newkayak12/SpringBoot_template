package com.base.config.security.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

final class RequestWrapper extends HttpServletRequestWrapper {

    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = this.manipulateRequest(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return this.manipulateRequest(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return this.manipulateRequest(value);

    }

    private String manipulateRequest(String value) {
        return value.replaceAll("<", "& lt;").replaceAll(">", "& gt;")
            .replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;")
            .replaceAll("'", "& #39;")
            .replaceAll("eval\\((.*)\\)", "")
            .replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"")
            .replaceAll("script", "");
    }
}
