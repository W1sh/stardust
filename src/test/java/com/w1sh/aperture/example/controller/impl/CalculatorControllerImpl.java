package com.w1sh.aperture.example.controller.impl;

import com.w1sh.aperture.example.controller.CalculatorController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/calculator")
public class CalculatorControllerImpl implements CalculatorController {

    @GET
    public Integer calculate(int first, int second) {
        return first + second;
    }
}