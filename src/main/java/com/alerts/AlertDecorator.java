package com.alerts;

// Base decorator class implementing the Alert interface
public abstract class AlertDecorator implements Alert {
    protected Alert alert;

    public AlertDecorator(Alert alert) {
        this.alert = alert;
    }

    @Override
    public String getPatientId() {
        return alert.getPatientId();
    }

    @Override
    public String getCondition() {
        return alert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return alert.getTimestamp();
    }

    @Override
    public String getMessage() {
        return alert.getMessage();
    }
}
