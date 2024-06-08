package com.alerts;

public class RepeatedAlertDecorator extends AlertDecorator {
    private final int repeatCount;

    public RepeatedAlertDecorator(AlertInterface decoratedAlert, int repeatCount) {
        super(decoratedAlert);
        this.repeatCount = repeatCount;
    }

    @Override
    public String toString() {
        return String.format("%s (Repeated %d times)", decoratedAlert.toString(), repeatCount);
    }
}
