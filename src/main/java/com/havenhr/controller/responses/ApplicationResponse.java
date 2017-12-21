package com.havenhr.controller.responses;

import com.havenhr.dto.ApplicationDto;
import com.havenhr.dto.OfferDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponse {

    private ApplicationDto application;
}
