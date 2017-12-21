package com.havenhr.controller;

import com.google.common.collect.Lists;
import com.havenhr.HavenHRApplication;
import com.havenhr.common.ApplicationStatus;
import com.havenhr.controller.responses.ApplicationResponse;
import com.havenhr.controller.responses.ApplicationsResponse;
import com.havenhr.dto.ApplicationDto;
import com.havenhr.entity.Application;
import com.havenhr.entity.Offer;
import com.havenhr.repository.ApplicationRepository;
import com.havenhr.repository.OfferRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import static com.havenhr.TestDataFactory.AUTH_TOKEN;
import static com.havenhr.TestDataFactory.createURLWithPort;
import static com.havenhr.TestDataFactory.getApplication;
import static com.havenhr.TestDataFactory.getApplicationDto;
import static com.havenhr.TestDataFactory.getOffer;
import static com.havenhr.security.AuthenticationInterceptor.X_AUTH_TOKEN;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HavenHRApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ApplicationControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testPostCreateApplication() {

        final Offer offer = offerRepository.save(getOffer(null, "Java Backend"));
        final ApplicationDto applicationDto = getApplicationDto(null, offer.getId());
        System.out.println(applicationDto);
        HttpEntity<ApplicationDto> entity = new HttpEntity<>(applicationDto, headers);

        ResponseEntity<ApplicationResponse> response = restTemplate.exchange(createURLWithPort("/public/application", port),
                                                                             HttpMethod.POST,
                                                                             entity,
                                                                             ApplicationResponse.class);

        assertNotNull(response.getBody().getApplication());
        assertNotNull(response.getBody().getApplication().getId());
    }

    @Test
    public void testThrowEntityNotFoundIfNoOfferWhenPostCreateApplication() {

        final Application application = getApplication(null, 12L);

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<Application> entity = new HttpEntity<>(application, headers);

        ResponseEntity<ApplicationResponse> response = restTemplate.exchange(createURLWithPort("/public/application", port),
                                                                             HttpMethod.POST,
                                                                             entity,
                                                                             ApplicationResponse.class);

        assertEquals(response.getStatusCodeValue(), SC_BAD_REQUEST);
    }

    @Test
    public void testThrowConstraintsViolationExceptionIfDuplicatedCandidateForSameOfferWhenPostCreateApplication() {
        final Offer offer = offerRepository.save(getOffer(null, "Java Backend"));
        applicationRepository.save(getApplication(null, offer));

        final Application application = getApplication(null, offer);

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<Application> entity = new HttpEntity<>(application, headers);

        ResponseEntity<ApplicationResponse> response = restTemplate.exchange(createURLWithPort("/public/application", port),
                                                                             HttpMethod.POST,
                                                                             entity,
                                                                             ApplicationResponse.class);

        assertEquals(response.getStatusCodeValue(), SC_BAD_REQUEST);
    }

    @Test
    public void testGetApplicationById() {
        final Offer offer = offerRepository.save(getOffer(null, "Java Backend"));
        applicationRepository.save(getApplication(null, offer));

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ApplicationResponse> response = restTemplate.exchange(createURLWithPort("/api/application/1", port),
                                                                             HttpMethod.GET,
                                                                             entity,
                                                                             ApplicationResponse.class);

        assertNotNull(response.getBody().getApplication());
    }

    @Test
    public void testThrowEntityNotFoundIfNoApplicationGetApplicationById() {

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ApplicationResponse> response = restTemplate.exchange(createURLWithPort("/api/application/11", port),
                                                                             HttpMethod.GET,
                                                                             entity,
                                                                             ApplicationResponse.class);

        assertEquals(response.getStatusCode().value(), SC_BAD_REQUEST);
    }

    @Test
    public void testGetApplicationsByOfferId() {
        final Offer offer1 = offerRepository.save(getOffer(null, "Java Backend"));
        final Offer offer2 = offerRepository.save(getOffer(null, "Frontend"));

        final Application application1 = getApplication(null, offer1);
        final Application application2 = getApplication(null, offer1);
        application2.setCandidate("test2@example.com");
        final Application application3 = getApplication(null, offer2);

        applicationRepository.save(Lists.newArrayList(application1, application2, application3));

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<ApplicationsResponse> response = restTemplate.exchange(createURLWithPort("/api/applications/offer/" + offer1.getId(), port),
                                                                              HttpMethod.GET,
                                                                              entity,
                                                                              ApplicationsResponse.class);

        assertEquals(response.getBody().getApplications().size(), 2);
    }

    @Test
    public void testPutUpdateApplicationStatus() {
        final Offer offer = offerRepository.save(getOffer(null, "Java Backend"));
        final Application application = getApplication(null, offer);
        applicationRepository.save(application);

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        final ApplicationStatus invited = ApplicationStatus.INVITED;

        ResponseEntity<ApplicationResponse> response = restTemplate.exchange(createURLWithPort("/api/application/" +
                                                                                               application.getId() +
                                                                                               "?applicationStatus=" +
                                                                                               invited, port),
                                                                             HttpMethod.PUT,
                                                                             entity,
                                                                             ApplicationResponse.class);

        assertEquals(response.getBody().getApplication().getApplicationStatus(), invited);
    }

}