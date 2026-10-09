package sk.upjs.ics;

import sk.upjs.ics.attendances.AttendanceDao;
import sk.upjs.ics.attendances.MemoryAttendanceDao;
import sk.upjs.ics.subjects.MemorySubjectDao;
import sk.upjs.ics.subjects.SubjectDao;
import sk.upjs.ics.users.MemoryUserDao;
import sk.upjs.ics.users.UserDao;

public enum Factory {
    INSTANCE;

    private UserDao userDao;

    private SubjectDao subjectDao;

    private AttendanceDao attendanceDao;

    public UserDao getUserDao() {
        if (userDao == null) {
            userDao = new MemoryUserDao();
        }

        return userDao;
    }

    public SubjectDao getSubjectDao() {
        if (subjectDao == null) {
            subjectDao = new MemorySubjectDao();
        }

        return subjectDao;
    }

    public AttendanceDao getAttendanceDao() {
        if (attendanceDao == null) {
            attendanceDao = new MemoryAttendanceDao();
        }

        return attendanceDao;
    }
}
