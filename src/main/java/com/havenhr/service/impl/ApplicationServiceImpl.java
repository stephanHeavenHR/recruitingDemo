package com.havenhr.service.impl;

import com.havenhr.common.ApplicationStatus;
import com.havenhr.converter.ApplicationConverter;
import com.havenhr.entity.Application;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import com.havenhr.repository.ApplicationRepository;
import com.havenhr.service.ApplicationService;
import com.havenhr.service.OfferService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private static Logger LOG = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationConverter applicationConverter;

    @Autowired
    private OfferService offerService;

    @Override
    public Application createApplication(Application application) throws ConstraintsViolationException, EntityNotFoundException {
        validateApplication(application);
        final boolean isNew = application.getId() == null;
        if (isNew) {
            application.setApplicationStatus(ApplicationStatus.APPLIED);
        }

        application = save(application);

        if (isNew) {
            offerService.increaseNumberOfApplications(application.getRelated());
        }

        return application;
    }

    @Override
    public Application updateApplicationStatus(final Long applicationId,
                                               final ApplicationStatus applicationStatus) throws EntityNotFoundException, ConstraintsViolationException {
        Application application = findById(applicationId);
        application.setApplicationStatus(applicationStatus);
        application = save(application);
        notifyApplicationStatusChanged(application, applicationStatus);
        return application;
    }

    @Override
    public Application findById(final Long applicationId) throws EntityNotFoundException {
        return checkApplication(applicationRepository.findOne(applicationId));
    }

    @Override
    public List<Application> findAllByOfferId(final Long offerId) {
        return applicationRepository.findByOffer(offerId);
    }

    private Application checkApplication(Application application) throws EntityNotFoundException {
        if (application == null) {
            throw new EntityNotFoundException("Application not found.");
        }
        return application;
    }

    private Application save(final Application application) throws ConstraintsViolationException {
        try {
            return applicationRepository.save(application);
        } catch (DataIntegrityViolationException e) {
            LOG.warn("Some constraints are thrown due to application creation.", e);
            throw new ConstraintsViolationException("Some constraints are thrown due to application creation.");
        }
    }

    private void validateApplication(final Application application) throws ConstraintsViolationException {
        if (application.getRelated() == null) {
            LOG.warn("Offer not found");
            throw new ConstraintsViolationException("Offer not found.");
        }
    }

    private void notifyApplicationStatusChanged(final Application application, final ApplicationStatus applicationStatus) {
        switch (applicationStatus) {
            case APPLIED:
                onAppliedApplicant(application);
                break;
            case INVITED:
                onInvitedApplicant(application);
                break;
            case REJECTED:
                onRejectedApplicant(application);
                break;
            case HIRED:
                onHiredApplicant(application);
                break;
        }
    }

    private void onHiredApplicant(final Application application) {
        LOG.debug("Applicant " + application.getCandidate() + " has been hired for this offer: " + application.getRelated().getJobTitle() + ".");
    }

    private void onRejectedApplicant(final Application application) {
        LOG.debug("Applicant " + application.getCandidate() + " has been rejected for this offer: " + application.getRelated().getJobTitle() + ".");
    }

    private void onInvitedApplicant(final Application application) {
        LOG.debug("Applicant " + application.getCandidate() + " has been invited for this offer: " + application.getRelated().getJobTitle() + ".");
    }

    private void onAppliedApplicant(final Application application) {
        LOG.debug("Applicant: " + application.getCandidate() + " has been applied for this offer: " + application.getRelated().getJobTitle() + ".");
    }
}
