package com.pao.laboratory13.exercise1;

public class ProtocolEngine {
    private SessionState state;
    private String currentUser;
    private int historyCount;

    public ProtocolEngine() {
        this.state = SessionState.INIT;
        this.currentUser = null;
        this.historyCount = 0;
    }

    public String process(String line) {
        String commandLine = line.trim();

        if (commandLine.isEmpty()) {
            return null;
        }

        String[] parts = commandLine.split("\\s+");
        String command = parts[0];

        return switch (command) {
            case "AUTH" -> handleAuth(parts);
            case "OPEN" -> handleOpen(parts);
            case "SEND" -> handleSend(commandLine, parts);
            case "BROADCAST" -> handleBroadcast(commandLine, parts);
            case "HISTORY" -> handleHistory(parts);
            case "CLOSE" -> handleClose(parts);
            default -> "ERR E_PARSE UNKNOWN_COMMAND";
        };
    }

    private String handleAuth(String[] parts) {
        if (parts.length < 2) {
            return "ERR E_PARSE AUTH";
        }

        if (state == SessionState.CLOSED) {
            return "ERR E_STATE CLOSED";
        }

        currentUser = parts[1];
        historyCount = 0;
        state = SessionState.AUTH;

        return "OK AUTH user=" + currentUser;
    }

    private String handleOpen(String[] parts) {
        if (parts.length != 1) {
            return "ERR E_PARSE OPEN";
        }

        if (state == SessionState.CLOSED) {
            return "ERR E_STATE CLOSED";
        }

        if (state == SessionState.OPEN) {
            return "ERR E_STATE ALREADY_OPEN";
        }

        if (state == SessionState.INIT) {
            return "ERR E_STATE NOT_OPEN";
        }

        state = SessionState.OPEN;
        return "OK OPEN";
    }

    private String handleSend(String commandLine, String[] parts) {
        if (parts.length < 2) {
            return "ERR E_PARSE SEND";
        }

        if (state == SessionState.CLOSED) {
            return "ERR E_STATE CLOSED";
        }

        if (state != SessionState.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        historyCount++;
        return "OK OPEN sent";
    }

    private String handleBroadcast(String commandLine, String[] parts) {
        if (parts.length < 2) {
            return "ERR E_PARSE BROADCAST";
        }

        if (state == SessionState.CLOSED) {
            return "ERR E_STATE CLOSED";
        }

        if (state != SessionState.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        historyCount++;
        return "OK OPEN broadcast";
    }

    private String handleHistory(String[] parts) {
        if (parts.length != 1) {
            return "ERR E_PARSE HISTORY";
        }

        if (state == SessionState.CLOSED) {
            return "ERR E_STATE CLOSED";
        }

        if (state != SessionState.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        return "OK OPEN history=" + historyCount;
    }

    private String handleClose(String[] parts) {
        if (parts.length != 1) {
            return "ERR E_PARSE CLOSE";
        }

        if (state == SessionState.CLOSED) {
            return "ERR E_STATE CLOSED";
        }

        if (state != SessionState.OPEN) {
            return "ERR E_STATE NOT_OPEN";
        }

        state = SessionState.CLOSED;
        return "OK CLOSED";
    }
}