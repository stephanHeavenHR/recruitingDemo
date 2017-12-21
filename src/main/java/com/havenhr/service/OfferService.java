package com.havenhr.service;

import com.havenhr.entity.Offer;
import com.havenhr.exception.ConstraintsViolationException;
import com.havenhr.exception.EntityNotFoundException;
import java.util.List;

public interface OfferService {


    Offer createOffer(String jobTitle) throws ConstraintsViolationException;

    Offer findById(Long offerId) throws EntityNotFoundException;

    Offer increaseNumberOfApplications(Offer offer) throws EntityNotFoundException, ConstraintsViolationException;

    List<Offer> findAll();
}
