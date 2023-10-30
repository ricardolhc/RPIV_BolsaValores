package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/invista")
public class InvistaController {
    
    @GetMapping
    public ModelAndView invistaPagina() {
        return new ModelAndView("/invista");
    }

}
