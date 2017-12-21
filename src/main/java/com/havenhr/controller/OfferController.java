package com.havenhr.controller;

import com.havenhr.controller.responses.OfferResponse;
import com.havenhr.controller.responses.OffersResponse;
import com.havenhr.converter.OfferConverter;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import com.havenhr.service.OfferService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Api(description = "Offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @Autowired
    private OfferConverter offerConverter;

    @PostMapping("/offer")
    @ResponseBody
    @ApiOperation(value = "Create offer")
    public OfferResponse createOffer(@Valid @RequestBody String jobTitle) throws ConstraintsViolationException {
        return new OfferResponse(offerConverter.internalToExternal(offerService.createOffer(jobTitle)));
    }

    @GetMapping("/offer/{offerId}")
    @ResponseBody
    @ApiOperation(value = "Find offer by id")
    public OfferResponse findOfferById(@Valid @PathVariable Long offerId) throws EntityNotFoundException {
        return new OfferResponse(offerConverter.internalToExternal(offerService.findById(offerId)));
    }

    @GetMapping("/offer/{offerId}/applications")
    @ResponseBody
    @ApiOperation(value = "Track the number of applications")
    public Map<String, Integer> trackNumberOfApplicationsById(@Valid @PathVariable Long offerId) throws EntityNotFoundException {
        Map<String, Integer> result = new HashMap<>();
        result.put("numberOfApplications", offerService.findById(offerId).getNumberOfApplications());
        return result;
    }

    @GetMapping("/offers")
    @ResponseBody
    @ApiOperation(value = "Find all offers")
    public OffersResponse findOffers() {
        return new OffersResponse(offerService.findAll().stream().map(offerConverter::internalToExternal).collect(Collectors.toList()));
    }
}
