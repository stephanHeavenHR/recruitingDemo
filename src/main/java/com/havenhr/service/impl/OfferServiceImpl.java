package com.havenhr.service.impl;

import com.havenhr.entity.Offer;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import com.havenhr.repository.OfferRepository;
import com.havenhr.service.OfferService;
import java.util.Calendar;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class OfferServiceImpl implements OfferService {

    private static Logger LOG = LoggerFactory.getLogger(OfferServiceImpl.class);

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public Offer createOffer(String jobTitle) throws ConstraintsViolationException {
        Offer offer = Offer.builder().jobTitle(jobTitle).numberOfApplications(0).startDate(Calendar.getInstance()).build();
        return save(offer);
    }

    @Override
    public Offer findById(final Long offerId) throws EntityNotFoundException {
        return checkOffer(offerRepository.findOne(offerId));
    }

    @Override
    public Offer increaseNumberOfApplications(final Offer offer) throws EntityNotFoundException, ConstraintsViolationException {
        checkOffer(offer);
        offer.setNumberOfApplications(offer.getNumberOfApplications() + 1);
        return save(offer);
    }

    @Override
    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    private Offer save(Offer offer) throws ConstraintsViolationException {
        try {
            return offerRepository.save(offer);
        } catch (DataIntegrityViolationException e) {
            LOG.warn("Some constraints are thrown due to offer creation", e);
            throw new ConstraintsViolationException("Some constraints are thrown due to offer creation.");
        }
    }

    private Offer checkOffer(Offer offer) throws EntityNotFoundException {
        if (offer == null) {
            LOG.warn("Offer not found");
            throw new EntityNotFoundException("Offer not found.");
        }
        return offer;
    }
}
