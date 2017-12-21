package com.havenhr.converter;

import com.havenhr.dto.OfferDto;
import com.havenhr.entity.Offer;
import java.util.Calendar;
import org.junit.Before;
import org.junit.Test;
import static com.havenhr.TestDataFactory.OFFER_ID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OfferConverterTest {

    OfferConverter offerConverter;

    @Before
    public void setUp() {
        offerConverter = new OfferConverter();
    }

    @Test
    public void shouldConvertInternalToExternal() throws Exception {

        // given
        final Calendar startDate = Calendar.getInstance();
        final String jobTitle = "jobTitle";
        final Offer offer = Offer.builder().id(OFFER_ID).jobTitle(jobTitle).numberOfApplications(2).startDate(startDate).build();

        // when
        final OfferDto offerDto = offerConverter.internalToExternal(offer);

        // then
        assertThat(offerDto.getId(), is(OFFER_ID));
        assertEquals(offerDto.getJobTitle(), jobTitle);
        assertThat(offerDto.getNumberOfApplications(), is(2));
        assertEquals(offerDto.getStartDate(), startDate);
    }
}