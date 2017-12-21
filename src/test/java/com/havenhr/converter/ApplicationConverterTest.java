package com.havenhr.converter;

import com.havenhr.common.ApplicationStatus;
import com.havenhr.dto.ApplicationDto;
import com.havenhr.dto.OfferDto;
import com.havenhr.entity.Application;
import com.havenhr.entity.Offer;
import com.havenhr.service.OfferService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static com.havenhr.TestDataFactory.APPLICATION_ID;
import static com.havenhr.TestDataFactory.OFFER_ID;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationConverterTest {

    @Mock
    private OfferService offerService;

    @Mock
    private OfferConverter offerConverter;

    @InjectMocks
    private ApplicationConverter applicationConverter;

    @Test
    public void shouldConvertExternalToInternal() throws Exception {

        // given
        final String resume = "resume";
        final String candidate = "test@email.com";
        final ApplicationDto applicationDto = ApplicationDto.builder()
                                                            .id(APPLICATION_ID)
                                                            .offerDto(OfferDto.builder().id(OFFER_ID).build())
                                                            .resume(resume)
                                                            .candidate(candidate)
                                                            .applicationStatus(ApplicationStatus.APPLIED)
                                                            .build();

        when(offerService.findById(OFFER_ID)).thenReturn(mock(Offer.class));

        // when
        final Application application = applicationConverter.externalToInternal(applicationDto);

        // then
        assertThat(application.getId(), is(APPLICATION_ID));
        assertEquals(application.getResume(), resume);
        assertEquals(application.getCandidate(), candidate);
        assertEquals(application.getApplicationStatus(), ApplicationStatus.APPLIED);
        assertNotNull(application.getRelated());

    }

    @Test
    public void shouldConvertInternalToExternal() throws Exception {

        // given
        final String resume = "resume";
        final String candidate = "test@email.com";
        final Offer offer = Offer.builder().id(OFFER_ID).build();
        final Application application = Application.builder()
                                                   .id(APPLICATION_ID)
                                                   .related(offer)
                                                   .resume(resume)
                                                   .candidate(candidate)
                                                   .applicationStatus(ApplicationStatus.APPLIED)
                                                   .build();

        when(offerConverter.internalToExternal(offer)).thenReturn(mock(OfferDto.class));

        // when
        final ApplicationDto applicationDto = applicationConverter.internalToExternal(application);

        // then
        assertThat(applicationDto.getId(), is(APPLICATION_ID));
        assertEquals(applicationDto.getResume(), resume);
        assertEquals(applicationDto.getCandidate(), candidate);
        assertEquals(applicationDto.getApplicationStatus(), ApplicationStatus.APPLIED);
        assertNotNull(applicationDto.getOfferDto());

    }
}