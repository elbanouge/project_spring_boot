package com.project.request_credit.controllers;

import java.util.HashMap;
import java.util.Map;

import com.project.request_credit.entities.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/process")
public class ProcessController {
    static final String URL_CAMUNDA = "http://localhost:8090/engine-rest/";

    @GetMapping({ "all-task" })
    public ResponseEntity<?> getAllTask() {
        RestTemplate restTemplate = new RestTemplate();
        Object tasks = restTemplate.getForObject(URL_CAMUNDA + "task",
                Object.class);

        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping({ "start-process" })
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> startProcess(@RequestBody User user) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, HashMap<String, HashMap<String, Object>>> variables = new HashMap<String, HashMap<String, HashMap<String, Object>>>();
        HashMap<String, HashMap<String, Object>> info_user = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> value1 = new HashMap<String, Object>();
        HashMap<String, Object> value2 = new HashMap<String, Object>();
        value1.put("value", user.getFirstName());
        info_user.put("nom", value1);
        variables.put("variables", info_user);

        value2.put("value", user.getLastName());
        info_user.put("prenom", value2);
        variables.put("variables", info_user);

        HttpEntity<Object> requestBody = new HttpEntity<>(variables);
        System.out.println(requestBody);

        Object result = restTemplate.postForObject(URL_CAMUNDA +
                "process-definition/key/credit_request/start",
                requestBody, Object.class);

        Map<String, Object> resultMap = (Map<String, Object>) result;
        String processInstanceId = (String) resultMap.get("id");
        System.out.println(processInstanceId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping({ "info-process" })
    public ResponseEntity<?> infoProcess(@RequestParam(required = true) String processInstanceId) {
        RestTemplate restTemplate = new RestTemplate();
        Object result = restTemplate.getForObject(URL_CAMUNDA +
                "task?processInstanceId=" + processInstanceId, Object.class);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
