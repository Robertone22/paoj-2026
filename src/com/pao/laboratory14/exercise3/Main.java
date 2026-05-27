package com.pao.laboratory14.exercise3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        List<Event> events = List.of(
                new Event("Concert Rock", "09:00", "11:00"),
                new Event("Stand-up Comedy", "09:30", "10:30"),
                new Event("Conferinta Tech", "10:00", "12:00"),
                new Event("Workshop Foto", "11:00", "13:00"),
                new Event("Piesa Teatru", "11:30", "14:00"),
                new Event("Festival Jazz", "13:00", "15:00"),
                new Event("Gala Film", "14:00", "16:00"),
                new Event("Concert Simfonic", "15:30", "17:00"),
                new Event("Lansare Carte", "16:00", "17:30")
        );

        List<Event> sortedEvents = new ArrayList<>(events);
        sortedEvents.sort(
                Comparator.comparingInt(Event::startMin)
                        .thenComparingInt(Event::endMin)
                        .thenComparing(Event::name)
        );

        System.out.println("=== Evenimente sortate dupa ora de start ===");
        for (Event event : sortedEvents) {
            System.out.println(event);
        }

        System.out.println();
        System.out.println("=== Varianta 1: Greedy simplu O(N^2) ===");
        List<RoomAssignment> simpleAssignments = assignRoomsSimple(sortedEvents);

        for (RoomAssignment assignment : simpleAssignments) {
            System.out.println(assignment);
        }

        int simpleRoomCount = simpleAssignments.stream()
                .mapToInt(RoomAssignment::roomNumber)
                .max()
                .orElse(0);

        System.out.println("Numar minim de sali folosite: " + simpleRoomCount);

        System.out.println();
        System.out.println("=== Varianta 2: PriorityQueue O(N log N) ===");
        int priorityQueueRoomCount = minimumRoomsWithPriorityQueue(sortedEvents);

        System.out.println("Numar minim de sali confirmat cu PriorityQueue: " + priorityQueueRoomCount);
    }

    private static List<RoomAssignment> assignRoomsSimple(List<Event> events) {
        List<Integer> roomEndTimes = new ArrayList<>();
        List<RoomAssignment> assignments = new ArrayList<>();

        for (Event event : events) {
            int assignedRoom = -1;

            for (int i = 0; i < roomEndTimes.size(); i++) {
                if (roomEndTimes.get(i) <= event.startMin()) {
                    assignedRoom = i + 1;
                    roomEndTimes.set(i, event.endMin());
                    break;
                }
            }

            if (assignedRoom == -1) {
                roomEndTimes.add(event.endMin());
                assignedRoom = roomEndTimes.size();
            }

            assignments.add(new RoomAssignment(event, assignedRoom));
        }

        return assignments;
    }

    private static int minimumRoomsWithPriorityQueue(List<Event> events) {
        PriorityQueue<Integer> occupiedRooms = new PriorityQueue<>();

        int maxRooms = 0;

        for (Event event : events) {
            if (!occupiedRooms.isEmpty() && occupiedRooms.peek() <= event.startMin()) {
                occupiedRooms.poll();
            }

            occupiedRooms.offer(event.endMin());
            maxRooms = Math.max(maxRooms, occupiedRooms.size());

            System.out.println(
                    event.name() + " (" + event.start() + " - " + event.end() + ")" +
                            " -> sali ocupate acum: " + occupiedRooms.size()
            );
        }

        return maxRooms;
    }

    private static int toMinutes(String time) {
        String[] parts = time.split(":");

        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        return hours * 60 + minutes;
    }

    private record Event(String name, String start, String end) {
        public int startMin() {
            return toMinutes(start);
        }

        public int endMin() {
            return toMinutes(end);
        }

        @Override
        public String toString() {
            return name + " (" + start + " - " + end + ")";
        }
    }

    private record RoomAssignment(Event event, int roomNumber) {
        @Override
        public String toString() {
            return event.name() + " (" + event.start() + " - " + event.end() + ") -> Sala #" + roomNumber;
        }
    }
}