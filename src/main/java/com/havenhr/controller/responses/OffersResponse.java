package com.havenhr.controller.responses;

import com.havenhr.dto.OfferDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OffersResponse {

    private List<OfferDto> offers = new ArrayList<>();
}
