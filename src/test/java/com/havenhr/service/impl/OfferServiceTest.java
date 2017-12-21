package com.havenhr.service.impl;

import com.google.common.collect.Lists;
import com.havenhr.entity.Offer;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import com.havenhr.repository.OfferRepository;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import static com.havenhr.TestDataFactory.OFFER_ID;
import static com.havenhr.TestDataFactory.getOffer;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    public void shouldCreateOffer() throws ConstraintsViolationException {

        // given
        final String jobTitle = "jobTitle";
        Offer offer = getOffer(null, jobTitle);
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        //when
        offer = offerService.createOffer(jobTitle);

        //then
        assertNotNull(offer);

    }

    @Test
    public void shouldThrowConstraintsViolationExceptionIfDuplicatedJobTitle() {

        Offer offer = mock(Offer.class);
        when(offerRepository.save(offer)).thenThrow(DataIntegrityViolationException.class);

        try {
            offer = offerService.createOffer("jobTitle");
        } catch (ConstraintsViolationException e) {
            assertEquals("Some constraints are thrown due to offer creation.", e.getMessage());
            offer = null;
        }

        //then
        verify(offerRepository, never()).save(offer);
        assertNull(offer);
    }

    @Test
    public void shouldReturnOfferById() throws EntityNotFoundException {

        final Offer offer = getOffer(OFFER_ID, "test");
        when(offerRepository.findOne(OFFER_ID)).thenReturn(offer);

        final Offer offer1 = offerService.findById(OFFER_ID);

        verify(offerRepository, times(1)).findOne(OFFER_ID);
        assertNotNull(offer1);
        assertThat(offer1.getId(), is(OFFER_ID));
        assertEquals(offer1.getJobTitle(), "test");
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfNoOfferFound() {

        when(offerRepository.findOne(OFFER_ID)).thenReturn(null);

        try {
            offerService.findById(OFFER_ID);
        } catch (EntityNotFoundException e) {
            assertEquals("Offer not found.", e.getMessage());
        }
    }

    @Test
    public void shouldIncreaseNumberOfApplications() throws Exception {
        final Offer offer = getOffer(OFFER_ID, "title");

        ArgumentCaptor<Offer> offerCaptor = ArgumentCaptor.forClass(Offer.class);
        when(offerRepository.save(any(Offer.class))).thenReturn(offer);

        offerService.increaseNumberOfApplications(offer);
        verify(offerRepository, times(1)).save(offerCaptor.capture());
        assertThat(offerCaptor.getValue().getNumberOfApplications(), is(1));

    }

    @Test
    public void shouldFindAllOffers() throws Exception {
        final Offer offer1 = getOffer(OFFER_ID, "title");
        final Offer offer2 = getOffer(OFFER_ID, "title");
        final ArrayList<Offer> offerList = Lists.newArrayList(offer1, offer2);
        when(offerRepository.findAll()).thenReturn(offerList);

        final List<Offer> offers = offerService.findAll();
        assertThat(offers.size(), is(2));
        assertThat(offers, Matchers.contains(offer1, offer2));
    }

}