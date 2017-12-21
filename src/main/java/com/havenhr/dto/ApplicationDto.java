package com.havenhr.dto;

import com.havenhr.common.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private Long id;

    private OfferDto offerDto;

    private String candidate;

    private String resume;

    private ApplicationStatus applicationStatus;

    @Override
    public String toString() {
        return "ApplicationDto{" +
               "id=" +
               id +
               ", offerDto=" +
               offerDto +
               ", candidate='" +
               candidate +
               '\'' +
               ", resume='" +
               resume +
               '\'' +
               ", applicationStatus=" +
               applicationStatus +
               '}';
    }
}
