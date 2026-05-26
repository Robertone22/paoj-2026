package com.pao.proiect.arenabid.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {
    private static AuditService instance;
    private static final String AUDIT_FILE = "audit.csv";

    private AuditService() {
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public synchronized void logAction(String actionName) {
        try (FileWriter fileWriter = new FileWriter(AUDIT_FILE, true)) {
            fileWriter.write(actionName + "," + LocalDateTime.now() + "\n");
        } catch (IOException e) {
            throw new RuntimeException("Could not write audit log.", e);
        }
    }
}