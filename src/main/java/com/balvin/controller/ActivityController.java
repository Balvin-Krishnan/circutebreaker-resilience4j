package com.balvin.controller;

import com.balvin.model.Activity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/activity")
@RestController
@Slf4j
public class ActivityController {


    private RestTemplate restTemplate;

    private final String BORED_API="https://www.boredapi.com/api/activity";

    public ActivityController(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @GetMapping
    @CircuitBreaker(name="randomActivity", fallbackMethod = "fallbackRandomActivity")
    public String getRandomActivity(){
        ResponseEntity<Activity> responseEntity = restTemplate.getForEntity(BORED_API, Activity.class);
        Activity activity = responseEntity.getBody();
        log.info("Activity received: "+activity.getActivity());
        return  activity.getActivity();
    }

    public String fallbackRandomActivity(Throwable throwable){
        return "Looks like it failed!";
    }
}