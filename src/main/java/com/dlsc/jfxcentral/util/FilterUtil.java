package com.dlsc.jfxcentral.util;

import com.dlsc.gemsfx.FilterView.Filter;
import com.dlsc.gemsfx.FilterView.FilterGroup;
import javafx.util.Callback;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class FilterUtil {

    public static final <T> void createFilters(FilterGroup<T> group, String title, Callback<T, ZonedDateTime> timeCallback) {
        Filter<T> createdToday = new Filter<>("Today") {
            @Override
            public boolean test(T item) {
                ZonedDateTime zonedDateTime = timeCallback.call(item);
                if (zonedDateTime != null) {
                    return zonedDateTime.toLocalDate().equals(LocalDate.now());
                }
                return false;
            }
        };

        Filter<T> createdThisWeek = new Filter<>("This Week") {
            @Override
            public boolean test(T item) {
                DayOfWeek firstDayOfWeek = WeekFields.of(Locale.UK).getFirstDayOfWeek();
                ZonedDateTime st = ZonedDateTime.now().with(firstDayOfWeek).with(LocalTime.MIN);
                ZonedDateTime et = st.plusDays(6).with(LocalTime.MAX);
                ZonedDateTime date = timeCallback.call(item);
                return date != null && !date.isBefore(st) && !date.isAfter(et);
            }
        };

        Filter<T> createdThisMonth = new Filter<>("This Month") {
            @Override
            public boolean test(T item) {
                ZonedDateTime st = ZonedDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                ZonedDateTime et = ZonedDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
                ZonedDateTime date = timeCallback.call(item);
                return date != null && !date.isBefore(st) && !date.isAfter(et);
            }
        };

        Filter<T> createdLastMonth = new Filter<>("Last Month") {
            @Override
            public boolean test(T item) {
                ZonedDateTime st = ZonedDateTime.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                ZonedDateTime et = ZonedDateTime.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
                ZonedDateTime date = timeCallback.call(item);
                return date != null && !date.isBefore(st) && !date.isAfter(et);
            }
        };

        Filter<T> createdThisYear = new Filter<>("This Year") {
            @Override
            public boolean test(T item) {
                ZonedDateTime st = ZonedDateTime.now().with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
                ZonedDateTime et = ZonedDateTime.now().with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
                ZonedDateTime date = timeCallback.call(item);
                return date != null && !date.isBefore(st) && !date.isAfter(et);
            }
        };

        Filter<T> createdLastYear = new Filter<>("Last Year") {
            @Override
            public boolean test(T item) {
                ZonedDateTime st = ZonedDateTime.now().minusYears(1).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
                ZonedDateTime et = ZonedDateTime.now().minusYears(1).with(TemporalAdjusters.lastDayOfYear()).with(LocalTime.MAX);
                ZonedDateTime date = timeCallback.call(item);
                return date != null && !date.isBefore(st) && !date.isAfter(et);
            }
        };

        group.getFilters().setAll(createdToday, createdThisWeek, createdThisMonth, createdLastMonth, createdThisYear, createdLastYear);
    }
}
