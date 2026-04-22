package it.unical.demacs.informatica.easyhomebackend.controller;

import it.unical.demacs.informatica.easyhomebackend.model.FacebookRequest;
import it.unical.demacs.informatica.easyhomebackend.service.FacebookService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/auth/facebook")
public class FacebookController {

    private final FacebookService facebookService;

    public FacebookController(FacebookService facebookService) {
        this.facebookService = facebookService;
    }

    @PostMapping("/ads")
    public Mono<String> createAd(@ModelAttribute FacebookRequest req) {
        return facebookService.createAd(req.getAdName(), req.getAdsetId(), req.getCreativeId());
    }
}
