package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.core.annotation.Primary;
import com.w1sh.aperture.example.controller.CalculatorController;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Primary
@Path("/calculator")
public class CalculatorControllerImpl implements CalculatorController {

    @GET
    public Integer calculate(int first, int second) {
        return first + second;
    }
}
