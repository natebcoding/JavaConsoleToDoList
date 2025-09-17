public enum TaskStatus {
    NOT_STARTED, STARTED, COMPLETED;

    public static TaskStatus fromInput(String input) {
        switch (input.toLowerCase()) {
            case "ns", "not started", "not start" -> {
                return NOT_STARTED;
            }
            case "started", "s" -> {
                return STARTED;
            }
            case "complete", "c" -> {
                return COMPLETED;
            }
            default -> {
                throw new IllegalArgumentException("This is not a valid option: " + input);
            }
        }
    }

    public String displayStatus() {
        return switch (this) {
            case NOT_STARTED -> "Not Started";
            case STARTED  -> "Started";
            case COMPLETED -> "Completed";

        };
    }
}
