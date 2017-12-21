package com.havenhr.service;

import com.havenhr.common.ApplicationStatus;
import com.havenhr.dto.ApplicationDto;
import com.havenhr.entity.Application;
import com.havenhr.entity.Offer;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import java.util.List;

public interface ApplicationService {

    Application createApplication(Application application) throws ConstraintsViolationException, EntityNotFoundException;

    Application updateApplicationStatus(Long applicationId, ApplicationStatus applicationStatus) throws EntityNotFoundException, ConstraintsViolationException;

    Application findById(Long applicationId) throws EntityNotFoundException;

    List<Application> findAllByOfferId(final Long offerId);
}
