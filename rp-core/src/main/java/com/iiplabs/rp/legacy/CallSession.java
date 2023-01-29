package com.iiplabs.rp.legacy;

import java.util.Objects;

public class CallSession {
    private int callSessionId;

    public CallSession() {
        callSessionId = 0;
    }

    public int getCallSessionId() {
        return callSessionId;
    }

    public void setCallSessionId(int callSessionId) {
        this.callSessionId = callSessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallSession that = (CallSession) o;
        return callSessionId == that.callSessionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(callSessionId);
    }

    @Override
    public String toString() {
        return "CallSession{" +
                "callSessionId=" + callSessionId +
                '}';
    }
}
