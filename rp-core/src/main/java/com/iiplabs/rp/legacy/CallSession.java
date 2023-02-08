package com.iiplabs.rp.legacy;

import java.util.Objects;

public class CallSession {
    private int callSessionId;
    private int otherSystemId;

    public CallSession() {
        callSessionId = 0;
        otherSystemId = 0;
    }

    public CallSession(int callSessionId, int otherSystemId) {
        this();
        this.callSessionId = callSessionId;
        this.otherSystemId = otherSystemId;
    }

    public int getCallSessionId() {
        return callSessionId;
    }

    public void setCallSessionId(int callSessionId) {
        this.callSessionId = callSessionId;
    }

    public int getOtherSystemId() {
        return otherSystemId;
    }

    public void setOtherSystemId(int otherSystemId) {
        this.otherSystemId = otherSystemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallSession that = (CallSession) o;
        return callSessionId == that.callSessionId && otherSystemId == that.otherSystemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(callSessionId, otherSystemId);
    }

    @Override
    public String toString() {
        return "CallSession{" +
                "callSessionId=" + callSessionId +
                ", otherSystemId=" + otherSystemId +
                '}';
    }
}
