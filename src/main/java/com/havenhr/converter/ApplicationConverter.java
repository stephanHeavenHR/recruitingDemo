package com.havenhr.converter;

import com.havenhr.dto.ApplicationDto;
import com.havenhr.entity.Application;
import com.havenhr.entity.Offer;
import com.havenhr.exception.EntityNotFoundException;
import com.havenhr.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter {
    private static Logger LOG = LoggerFactory.getLogger(ApplicationConverter.class);

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferConverter offerConverter;

    public Application externalToInternal(ApplicationDto applicationDto) {
        Offer offer = null;
        try {
            offer = applicationDto.getOfferDto() != null ? offerService.findById(applicationDto.getOfferDto().getId()) : null;
        } catch (EntityNotFoundException ignored) {
        }

        return Application.builder()
                          .id(applicationDto.getId())
                          .applicationStatus(applicationDto.getApplicationStatus())
                          .candidate(applicationDto.getCandidate())
                          .related(offer)
                          .resume(applicationDto.getResume())
                          .build();
    }

    public ApplicationDto internalToExternal(Application application) {
        return ApplicationDto.builder()
                             .id(application.getId())
                             .offerDto(offerConverter.internalToExternal(application.getRelated()))
                             .candidate(application.getCandidate())
                             .resume(application.getResume())
                             .applicationStatus(application.getApplicationStatus())
                             .build();
    }
}
