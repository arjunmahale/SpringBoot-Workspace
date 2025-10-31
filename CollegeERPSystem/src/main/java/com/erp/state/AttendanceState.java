package com.erp.state;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class AttendanceState {
    // Key: courseName, Value: start time
    private static Map<String, LocalDateTime> courseAttendanceMap = new ConcurrentHashMap<>();

    public static boolean isAttendanceOpen(String courseName) {
        if (courseName == null || !courseAttendanceMap.containsKey(courseName)) return false;

        LocalDateTime start = courseAttendanceMap.get(courseName);
        // Auto-close after 30 minutes
        if (start.plusMinutes(30).isBefore(LocalDateTime.now())) {
            courseAttendanceMap.remove(courseName);
            return false;
        }
        return true;
    }

    public static LocalDateTime getStartTime(String courseName) {
        return courseAttendanceMap.get(courseName);
    }

    public static void openAttendance(String courseName) {
        if (courseName != null) {
            courseAttendanceMap.put(courseName, LocalDateTime.now());
        }
    }

    public static void closeAttendance(String courseName) {
        if (courseName != null) {
            courseAttendanceMap.remove(courseName);
        }
    }
}
