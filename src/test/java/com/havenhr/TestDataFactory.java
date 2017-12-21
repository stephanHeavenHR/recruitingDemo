package com.havenhr;

import com.havenhr.common.ApplicationStatus;
import com.havenhr.dto.ApplicationDto;
import com.havenhr.dto.OfferDto;
import com.havenhr.entity.Application;
import com.havenhr.entity.Offer;
import java.util.Calendar;

public class TestDataFactory {

    public static final Long OFFER_ID = 1L;
    public static final Long APPLICATION_ID = 11L;
    public static final String AUTH_TOKEN = "fc81570e-d210-11e7-8941-cec278b6b50a";

    public static Offer getOffer(final Long id, final String jobTitle) {
        return Offer.builder().id(id).jobTitle(jobTitle).numberOfApplications(0).startDate(Calendar.getInstance()).build();
    }

    public static Application getApplication(final Long id, final Long offerId) {
        return getApplication(id, getOffer(offerId, "jobTitle"));
    }

    public static Application getApplication(final Long id, final Offer offer) {
        return Application.builder().id(id).resume("resume").candidate("test@example.com").applicationStatus(ApplicationStatus.APPLIED).related(offer).build();
    }

    public static ApplicationDto getApplicationDto(final Long id, final Long offerId) {
        return ApplicationDto.builder()
                             .id(id)
                             .resume("resume")
                             .candidate("test@example.com")
                             .applicationStatus(ApplicationStatus.APPLIED)
                             .offerDto(OfferDto.builder().id(offerId).build())
                             .build();
    }

    public static String createURLWithPort(String uri, int port) {
        return "http://localhost:" + port + uri;
    }
}
