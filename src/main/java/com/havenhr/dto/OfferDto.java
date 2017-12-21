package com.havenhr.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
import java.util.Calendar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDto {
    private static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private Long id;

    private String jobTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_PATTERN)
    @JsonSerialize(using = CalendarSerializer.class)
    private Calendar startDate;

    private Integer numberOfApplications;

    @Override
    public String toString() {
        return "OfferDto{" +
               "id=" +
               id +
               ", jobTitle='" +
               jobTitle +
               '\'' +
               ", startDate=" +
               startDate +
               ", numberOfApplications=" +
               numberOfApplications +
               '}';
    }
}
