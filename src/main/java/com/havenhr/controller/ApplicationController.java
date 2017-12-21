package com.havenhr.controller;

import com.havenhr.common.ApplicationStatus;
import com.havenhr.controller.responses.ApplicationResponse;
import com.havenhr.controller.responses.ApplicationsResponse;
import com.havenhr.converter.ApplicationConverter;
import com.havenhr.dto.ApplicationDto;
import com.havenhr.entity.Application;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import com.havenhr.service.ApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import static java.util.stream.Collectors.toList;

@RestController
@Api(description = "Applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationConverter applicationConverter;

    @PostMapping("/public/application")
    @ResponseBody
    @ApiOperation(value = "Create application")
    public ApplicationResponse createApplication(
            @Valid @RequestBody ApplicationDto applicationDto) throws ConstraintsViolationException, EntityNotFoundException {
        System.out.println(applicationDto);
        Application application = applicationConverter.externalToInternal(applicationDto);
        System.out.println(application);

        application = applicationService.createApplication(application);
        return new ApplicationResponse(applicationConverter.internalToExternal(application));
    }

    @GetMapping("/api/application/{applicationId}")
    @ResponseBody
    @ApiOperation(value = "Find application by id")
    public ApplicationResponse findApplicationById(@Valid @PathVariable Long applicationId) throws EntityNotFoundException {
        return new ApplicationResponse(applicationConverter.internalToExternal(applicationService.findById(applicationId)));
    }

    @GetMapping("/api/applications/offer/{offerId}")
    @ResponseBody
    @ApiOperation(value = "Find applications by offer id")
    public ApplicationsResponse findApplicationsByOfferId(@Valid @PathVariable Long offerId) {
        return new ApplicationsResponse(applicationService.findAllByOfferId(offerId)
                                                          .stream()
                                                          .map(applicationConverter::internalToExternal)
                                                          .collect(toList()));
    }

    @PutMapping("/api/application/{applicationId}")
    @ResponseBody
    @ApiOperation(value = "Update application status")
    public ApplicationResponse updateApplicationStatus(@Valid @PathVariable long applicationId,
                                                       @RequestParam
                                                                  ApplicationStatus applicationStatus) throws ConstraintsViolationException, EntityNotFoundException {
        return new ApplicationResponse(applicationConverter.internalToExternal(applicationService.updateApplicationStatus(applicationId,
                                                                                                                          applicationStatus)));
    }

}
