package com.example.final_project.utilities;

import com.example.final_project.entities.course.State;

import java.util.ArrayList;
import java.util.List;

public class CoursesFilter {
    public enum SortBy {
        AZ, ZA, NONE
    }

    public enum Duration {
        WEEK, MONTH, YEAR, MORE
    }

    public enum Enrolled {
        TO50, TO100, MORE
    }

    SortBy sortBy = SortBy.NONE;
    List<String> topics = new ArrayList<>();
    List<Duration> durations = new ArrayList<>();
    List<Enrolled> enrolled = new ArrayList<>();
    List<String> teachers = new ArrayList<>();
    State state;
    String search;
    int offset = 0;
    int coursesPerPage = 0;

    public void addTopics(List<String> topics) {
        this.topics = topics;
    }

    public void addDurations(List<Duration> durations) {
        this.durations = durations;
    }

    public void addEnrolled(List<Enrolled> enrolled) {
        this.enrolled = enrolled;
    }

    public void addTeachers(List<String> teachers) {
        this.teachers = teachers;
    }

    public void addSortFilter(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setCoursesPerPage(int coursesPerPage) {
        this.coursesPerPage = coursesPerPage;
    }

    public void setState(State state) {
        this.state = state;
    }
    public void setSearch(String search) {
        this.search = search;
    }
    public String formCondition() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean ifFilterAvailable = checkIfFilterFilled();
        int conditionalCounter = 0;
        if (ifFilterAvailable) stringBuilder.append("WHERE ").append("( ");
        if (topics.size() != 0) ++conditionalCounter;
        for (int i = 0; i < topics.size(); ++i) {
            String topic = topics.get(i);
            stringBuilder.append("Course.topic = \'" + topic + "\'");
            if (i + 1 < topics.size()) {
                stringBuilder.append(" OR ");
            } else {
                stringBuilder.append(" )");
            }
        }

        if (ifFilterAvailable)
            if (enrolled.size() != 0 && conditionalCounter++ != 0) stringBuilder.append(" AND ").append("( ");
        for (int i = 0; i < enrolled.size(); ++i) {
            Enrolled state = enrolled.get(i);
            stringBuilder.append("Course.max_students_amount " + enrolledState(state));
            if (i + 1 < enrolled.size()) {
                stringBuilder.append(" OR ");
            } else {
                stringBuilder.append(" )");
            }
        }

        if (ifFilterAvailable)
            if (teachers.size() != 0 && conditionalCounter++ != 0) stringBuilder.append(" AND ").append("( ");
        for (int i = 0; i < teachers.size(); ++i) {
            String teacher = teachers.get(i);
            stringBuilder.append("Course.teacher = \'" + teacher + "\'");
            if (i + 1 < teachers.size()) {
                stringBuilder.append(" OR ");
            } else {
                stringBuilder.append(" )");
            }
        }

        if (ifFilterAvailable)
            if (durations.size() != 0 && conditionalCounter++ != 0) stringBuilder.append(" AND ").append("( ");
        for (int i = 0; i < durations.size(); ++i) {
            Duration duration = durations.get(i);
            stringBuilder.append("Course.duration " + durationState(duration));
            if (i + 1 < durations.size()) {
                stringBuilder.append(" OR ");
            } else {
                stringBuilder.append(" )");
            }
        }

        if (ifFilterAvailable && state != null){
            if (conditionalCounter++ != 0) stringBuilder.append(" AND ").append("( ");
            stringBuilder.append(" State_id = ").append(state.ordinal());
            stringBuilder.append(" )");
        }

        if (ifFilterAvailable && search != null){
            if (conditionalCounter++ != 0) stringBuilder.append(" AND ").append("( ");
            stringBuilder.append("Course.title LIKE '%").append(search).append("%'");
            stringBuilder.append(" )");
        }

        if (sortBy != SortBy.NONE) {
            if (sortBy == SortBy.AZ) {
                stringBuilder.append(" ORDER BY Course.title ");
            } else stringBuilder.append(" ORDER BY Course.title desc");
        }

        if (offset != 0 || coursesPerPage != 0) {
            stringBuilder.append(" LIMIT ").append(offset).append(", ").append(coursesPerPage).append(";");
        }
        return stringBuilder.toString();
    }

    private String enrolledState(Enrolled enrolled) {
        if (enrolled == Enrolled.TO50) return " < 50";
        else if (enrolled == Enrolled.TO100) return " < 100";
        else return " > 100";
    }

    private String durationState(Duration duration) {
        if (duration == Duration.WEEK) return " <= 7";
        else if (duration == Duration.MONTH) return " <= 30";
        else if (duration == Duration.YEAR) return " <= 365";
        else return " > 365";

    }

    private boolean checkIfFilterFilled() {
        if (this.durations.size() != 0 || this.enrolled.size() != 0 || this.teachers.size() != 0
                || this.topics.size() != 0 || state != null || search != null)
            return true;
        return false;
    }
}
