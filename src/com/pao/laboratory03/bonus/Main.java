package com.pao.laboratory03.bonus;

import java.util.List;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        // TODO: implementează toți cei 10 pași de mai sus
        // Creează TOATE clasele necesare în acest pachet (bonus/)
        // Nu ai subpachete impuse — organizează cum consideri

        TaskService service = TaskService.getInstance();

        System.out.println("=== Adăugare task-uri ===");
        Task t1 = service.addTask("Fix login bug", Priority.CRITICAL);
        Task t2 = service.addTask("Add dark mode", Priority.LOW);
        Task t3 = service.addTask("Update docs", Priority.MEDIUM);
        Task t4 = service.addTask("Fix memory leak", Priority.HIGH);
        Task t5 = service.addTask("Refactor DB layer", Priority.HIGH);

        System.out.println("Adăugat: " + t1);
        System.out.println("Adăugat: " + t2);
        System.out.println("Adăugat: " + t3);
        System.out.println("Adăugat: " + t4);
        System.out.println("Adăugat: " + t5);

        System.out.println();
        System.out.println("=== Asignare ===");
        service.assignTask("T001", "Ana");
        service.assignTask("T003", "Mihai");
        service.assignTask("T004", "Elena");
        System.out.println("T001 → Ana");
        System.out.println("T003 → Mihai");
        System.out.println("T004 → Elena");

        System.out.println();
        System.out.println("=== Schimbări status ===");
        service.changeStatus("T001", Status.IN_PROGRESS);
        System.out.println("T001: TODO → IN_PROGRESS ✓");

        service.changeStatus("T001", Status.DONE);
        System.out.println("T001: IN_PROGRESS → DONE ✓");

        service.changeStatus("T003", Status.IN_PROGRESS);
        System.out.println("T003: TODO → IN_PROGRESS ✓");

        try {
            service.changeStatus("T001", Status.TODO);
        } catch (InvalidTransitionException e) {
            System.out.println("T001: DONE → TODO → InvalidTransitionException: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== Task-uri HIGH ===");
        List<Task> highTasks = service.getTasksByPriority(Priority.HIGH);
        for (Task task : highTasks) {
            System.out.println(task);
        }

        System.out.println();
        System.out.println("=== Sumar status ===");
        Map<Status, Long> summary = service.getStatusSummary();
        for (Status status : Status.values()) {
            System.out.println(status + ": " + summary.get(status));
        }

        System.out.println();
        System.out.println("=== Task-uri neasignate ===");
        List<Task> unassigned = service.getUnassignedTasks();
        for (Task task : unassigned) {
            System.out.println(task.getId() + ": " + task.getTitle());
        }

        System.out.println();
        System.out.println("=== Scor urgență (baseDays=5) ===");
        System.out.println("Total: " + service.getTotalUrgencyScore(5));

        System.out.println();
        System.out.println("=== Audit Log ===");
        service.printAuditLog();

        System.out.println();
        System.out.println("=== Excepții ===");

        try {
            service.addTaskWithId("T001", "Duplicate task", Priority.LOW);
        } catch (DuplicateTaskException e) {
            System.out.println("DuplicateTaskException: " + e.getMessage());
        }

        try {
            service.findTaskById("T999");
        } catch (TaskNotFoundException e) {
            System.out.println("TaskNotFoundException: " + e.getMessage());
        }
    }
}