package com.fit2cloud.security.permission;

import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CeMethodSecurityExpressionRoot extends CeSecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;

    private Object returnObject;

    private Object target;

    /**
     * Creates a new instance
     *
     * @param authentication the {@link Authentication} to use. Cannot be null.
     */
    public CeMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }


    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    public void setThis(Object target) {
        this.target = target;
    }
}
