package com.havenhr.service.impl;

import com.google.common.collect.Lists;
import com.havenhr.common.ApplicationStatus;
import com.havenhr.converter.ApplicationConverter;
import com.havenhr.entity.Application;
import com.havenhr.entity.Offer;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import com.havenhr.repository.ApplicationRepository;
import com.havenhr.service.OfferService;
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
import static com.havenhr.TestDataFactory.APPLICATION_ID;
import static com.havenhr.TestDataFactory.OFFER_ID;
import static com.havenhr.TestDataFactory.getApplication;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ApplicationConverter applicationConverter;

    @Mock
    private OfferService offerService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    public void shouldCreateApplication() throws Exception {
        final Application application = getApplication(null, OFFER_ID);

        when(applicationRepository.save(application)).thenReturn(application);
        when(offerService.increaseNumberOfApplications(application.getRelated())).thenReturn(application.getRelated());

        final Application result = applicationService.createApplication(application);
        verify(applicationRepository, times(1)).save(application);
        verify(offerService, times(1)).increaseNumberOfApplications(application.getRelated());
        assertNotNull(result);
    }

    @Test
    public void shouldThrowConstraintsViolationExceptionIfNoOffer() {
        final Application application = getApplication(null, (Offer) null);

        when(applicationRepository.save(application)).thenReturn(application);

        Application result = null;
        try {
            result = applicationService.createApplication(application);
        } catch (ConstraintsViolationException e) {
            assertEquals("Offer not found.", e.getMessage());
        } catch (EntityNotFoundException ignored) {
        }
        assertNull(result);
    }

    @Test
    public void shouldThrowEntityNotFoundException() {
        final Application application = getApplication(null, OFFER_ID);

        when(applicationRepository.save(application)).thenThrow(DataIntegrityViolationException.class);

        Application result = null;
        try {
            result = applicationService.createApplication(application);
        } catch (ConstraintsViolationException ignored) {
        } catch (EntityNotFoundException e) {
            assertEquals("Some constraints are thrown due to application creation.", e.getMessage());
        }
        assertNull(result);
    }

    @Test
    public void shouldReturnApplicationById() throws EntityNotFoundException {

        final Application application = getApplication(APPLICATION_ID, OFFER_ID);
        when(applicationRepository.findOne(APPLICATION_ID)).thenReturn(application);

        final Application result = applicationService.findById(APPLICATION_ID);

        verify(applicationRepository, times(1)).findOne(APPLICATION_ID);
        assertNotNull(result);
        assertThat(result.getId(), is(APPLICATION_ID));
    }

    @Test
    public void shouldThrowEntityNotFoundExceptionIfNoOfferFound() {

        when(applicationRepository.findOne(APPLICATION_ID)).thenReturn(null);

        try {
            applicationService.findById(APPLICATION_ID);
        } catch (EntityNotFoundException e) {
            assertEquals("Application not found.", e.getMessage());
        }
    }

    @Test
    public void shouldFindAllByOfferId() throws Exception {
        final Application application1 = getApplication(1L, OFFER_ID);
        final Application application2 = getApplication(2L, OFFER_ID);

        final ArrayList<Application> offerList = Lists.newArrayList(application1, application2);
        when(applicationRepository.findByOffer(OFFER_ID)).thenReturn(offerList);

        final List<Application> applications = applicationService.findAllByOfferId(OFFER_ID);
        assertThat(applications.size(), is(2));
        assertThat(applications, Matchers.contains(application1, application2));
    }

    @Test
    public void shouldUpdateApplicationStatus() throws Exception {
        final Application application = getApplication(APPLICATION_ID, OFFER_ID);
        when(applicationRepository.findOne(APPLICATION_ID)).thenReturn(application);

        ArgumentCaptor<Application> applicationArgumentCaptor = ArgumentCaptor.forClass(Application.class);
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        applicationService.updateApplicationStatus(APPLICATION_ID, ApplicationStatus.HIRED);
        verify(applicationRepository, times(1)).save(applicationArgumentCaptor.capture());
        assertEquals(applicationArgumentCaptor.getValue().getApplicationStatus(), ApplicationStatus.HIRED);

    }
}