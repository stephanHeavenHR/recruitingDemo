package com.havenhr.converter;

import com.havenhr.dto.OfferDto;
import com.havenhr.entity.Offer;
import org.springframework.stereotype.Component;

@Component
public class OfferConverter {

    public Offer externalToInternal(OfferDto offerDto) {
        throw new UnsupportedOperationException();
    }

    public OfferDto internalToExternal(Offer offer) {
        return OfferDto.builder()
                       .id(offer.getId())
                       .jobTitle(offer.getJobTitle())
                       .numberOfApplications(offer.getNumberOfApplications())
                       .startDate(offer.getStartDate())
                       .build();
    }
}
