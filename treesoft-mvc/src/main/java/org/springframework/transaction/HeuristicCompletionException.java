package org.springframework.transaction;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/transaction/HeuristicCompletionException.class */
public class HeuristicCompletionException extends TransactionException {
    public static final int STATE_UNKNOWN = 0;
    public static final int STATE_COMMITTED = 1;
    public static final int STATE_ROLLED_BACK = 2;
    public static final int STATE_MIXED = 3;
    private int outcomeState;

    public static String getStateString(int state) {
        switch (state) {
            case 1:
                return "committed";
            case 2:
                return "rolled back";
            case 3:
                return "mixed";
            default:
                return "unknown";
        }
    }

    public HeuristicCompletionException(int outcomeState, Throwable cause) {
        super("Heuristic completion: outcome state is " + getStateString(outcomeState), cause);
        this.outcomeState = 0;
        this.outcomeState = outcomeState;
    }

    public int getOutcomeState() {
        return this.outcomeState;
    }
}
