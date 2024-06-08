package com.alerts;

public class PriorityAlertDecorator extends AlertDecorator {

    public PriorityAlertDecorator(AlertInterface decoratedAlert) {
        super(decoratedAlert);
    }

    @Override
    public String getPriority() {
        return "High";
    }

    @Override
    public String toString() {
        return String.format("%s (Priority: %s)", decoratedAlert.toString(), getPriority());
    }
}
