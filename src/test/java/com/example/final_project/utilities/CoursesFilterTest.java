package com.example.final_project.utilities;

import com.example.final_project.database.entities.course.State;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class CoursesFilterTest {

    @Test
    void formConditionCheckLimitCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.setCoursesPerPage(10);
        coursesFilter.setOffset(15);
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request," LIMIT 15, 10;");
    }

    @Test
    void formConditionCheckTopicsCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.addTopics(List.of("Math", "IT"));
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request, "WHERE ( Course.topic = 'Math' OR Course.topic = 'IT' )");
    }

    @Test
    void formConditionCheckEnrolledCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.addEnrolled(List.of(CoursesFilter.Enrolled.TO100, CoursesFilter.Enrolled.TO50));
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request, "WHERE ( Course.max_students_amount  < 100 OR Course.max_students_amount  < 50 )");
    }

    @Test
    void formConditionCheckDurationCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.addDurations(List.of(CoursesFilter.Duration.WEEK, CoursesFilter.Duration.MONTH));
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request, "WHERE ( Course.duration  <= 7 OR Course.duration  <= 30 )");
    }

    @Test
    void formConditionCheckTeacherCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.addTeachers(List.of("Teacher1", "Teacher2"));
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request, "WHERE ( Course.teacher = 'Teacher1' OR Course.teacher = 'Teacher2' )");
    }

    @Test
    void formConditionCheckOrderCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.addSortFilter(CoursesFilter.SortBy.AZ);
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request, " ORDER BY Course.title ");
    }

    @Test
    void formConditionCheckStateCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.setState(State.NotStarted);
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request, "WHERE (  State_id = 2 )");
    }

    @Test
    void formConditionAllFiltersCase() {
        CoursesFilter coursesFilter = new CoursesFilter();
        coursesFilter.setCoursesPerPage(10);
        coursesFilter.setOffset(15);
        coursesFilter.addTopics(List.of("Math", "IT"));
        coursesFilter.addTeachers(List.of("Teacher1", "Teacher2"));
        coursesFilter.addDurations(List.of(CoursesFilter.Duration.WEEK, CoursesFilter.Duration.MONTH));
        coursesFilter.addEnrolled(List.of(CoursesFilter.Enrolled.TO100, CoursesFilter.Enrolled.TO50));
        coursesFilter.addSortFilter(CoursesFilter.SortBy.AZ);
        coursesFilter.setState(State.NotStarted);
        String request = coursesFilter.formCondition();
        Assertions.assertEquals(request,"WHERE ( Course.topic = 'Math' OR Course.topic = 'IT' ) " +
                "AND ( Course.max_students_amount  < 100 OR Course.max_students_amount  < 50 ) " +
                "AND ( Course.teacher = 'Teacher1' OR Course.teacher = 'Teacher2' ) " +
                "AND ( Course.duration  <= 7 OR Course.duration  <= 30 ) " +
                "AND (  State_id = 2 ) ORDER BY Course.title  LIMIT 15, 10;");
    }
}