package com.havenhr.controller;

import com.havenhr.HavenHRApplication;
import com.havenhr.controller.responses.OfferResponse;
import com.havenhr.controller.responses.OffersResponse;
import com.havenhr.dto.OfferDto;
import com.havenhr.entity.Offer;
import com.havenhr.repository.OfferRepository;
import java.util.HashMap;
import java.util.Map;
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
import static com.havenhr.TestDataFactory.getOffer;
import static com.havenhr.security.AuthenticationInterceptor.X_AUTH_TOKEN;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = HavenHRApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OfferControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private OfferRepository offerRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testUnauthorizedRequest() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<OfferResponse> response = restTemplate.exchange(createURLWithPort("/api/offer/1", port), HttpMethod.GET, entity, OfferResponse.class);

        assertEquals(response.getStatusCode().value(), SC_UNAUTHORIZED);
    }

    @Test
    public void testGetOfferById() {
        offerRepository.save(getOffer(null, "title"));

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<OfferResponse> response = restTemplate.exchange(createURLWithPort("/api/offer/1", port), HttpMethod.GET, entity, OfferResponse.class);

        final OfferDto offerDto = response.getBody().getOffer();
        assertThat(offerDto.getId(), is(1L));
        assertEquals(offerDto.getJobTitle(), "title");
    }

    @Test
    public void testEntityNotFoundException() {

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<OfferResponse> response = restTemplate.exchange(createURLWithPort("/api/offer/10", port), HttpMethod.GET, entity, OfferResponse.class);

        assertEquals(response.getStatusCodeValue(), SC_BAD_REQUEST);
    }

    @Test
    public void testGetOffers() {
        offerRepository.save(getOffer(null, "title1"));
        offerRepository.save(getOffer(null, "title2"));

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<OffersResponse> response = restTemplate.exchange(createURLWithPort("/api/offers", port), HttpMethod.GET, entity, OffersResponse.class);

        assertEquals(response.getBody().getOffers().size(), 2);
    }

    @Test
    public void testGetTrackNumberOfApplicationsById() {
        offerRepository.save(Offer.builder().id(1L).numberOfApplications(10).jobTitle("title1").build());

        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Map> response = restTemplate.exchange(createURLWithPort("/api/offer/1/applications", port), HttpMethod.GET, entity, Map.class);

        assertThat(response.getBody().get("numberOfApplications"), is(10));
    }


    @Test
    public void testPostCreateOffer() {

        final Map<String, String> bodyData = new HashMap<>();
        bodyData.put("jobTitle", "Java Backend");
        headers.add(X_AUTH_TOKEN, AUTH_TOKEN);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(bodyData, headers);

        ResponseEntity<OfferResponse> response = restTemplate.exchange(createURLWithPort("/api/offer", port), HttpMethod.POST, entity, OfferResponse.class);

        assertNotNull(response.getBody().getOffer());
        assertNotNull(response.getBody().getOffer().getId());
    }
}