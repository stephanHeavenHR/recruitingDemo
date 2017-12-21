package com.havenhr.controller.responses;

import com.havenhr.dto.ApplicationDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationsResponse {

    private List<ApplicationDto> applications = new ArrayList<>();
}
