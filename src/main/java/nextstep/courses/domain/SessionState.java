package nextstep.courses.domain;

public enum SessionState {
    READY("READY"), START("START"), END("END");

    public String getState() {
        return state;
    }

    public String state;

    SessionState(String state) {
        this.state = state;
    }

    public boolean isRequestSession() {
        return START.state.equals(this.state);
    }
}
