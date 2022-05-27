package com.project.request_credit.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.User;
import com.project.request_credit.services.AccountService;
import com.project.request_credit.services.UserDetailsServiceImpl;
import com.project.request_credit.services.CreditService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CreditService creditService;

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
    public ResponseEntity<?> startProcess(@RequestBody Credit credit) {
        if(accountService.findUserByEmail(credit.getUser().getEmail())==null)  return new ResponseEntity<>("NOTFOUND", HttpStatus.NOT_FOUND);

        RestTemplate restTemplate = new RestTemplate();

        Map<String, HashMap<String, HashMap<String, Object>>> variables = CreateJson
                ("name", credit.getUser().getFirstName(),
                "prenom", credit.getUser().getLastName());

        HttpEntity<Object> requestBody = new HttpEntity<>(variables);
        System.out.println(requestBody);

        Object result = restTemplate.postForObject(URL_CAMUNDA +
                "process-definition/key/credit_request/start",
                requestBody, Object.class);

        Map<String, Object> resultMap = (Map<String, Object>) result;
        String processInstanceId = (String) resultMap.get("id");
        System.out.println(processInstanceId);

        //User
        User userConnected = accountService.findUserByEmail(credit.getUser().getEmail());
        Credit c=creditService.getCreditById(credit.getId());
        System.out.println(credit.getId());
        c.setProcessInstanceId(processInstanceId);
        creditService.updateCredit(c,credit.getId());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping({ "info-task-instance/{id}" })
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> infoProcess(@PathVariable long id, @RequestParam(required = true) String processInstanceId) {
        RestTemplate restTemplate = new RestTemplate();
        List<Map<String, Object>> result = restTemplate.getForObject(URL_CAMUNDA +
                "task?processInstanceId=" + processInstanceId,
                List.class);

        String taskId = (String) result.get(0).get("id");
        String taskName = (String) result.get(0).get("name");

        //User userConnected = accountService.findUserByEmail(credit.getUser().getEmail());
        Credit c=creditService.getCreditById(id);
        //c.setProcessInstanceId(processInstanceId);


        //User userConnected = accountService.findUserByEmail(email);
        c.setTaskId(taskId);
        c.setTaskName(taskName);
        creditService.updateCredit(c,id);


        return new ResponseEntity<>(creditService.updateCredit(c,id), HttpStatus.OK);
    }

    @PostMapping({ "complete-task-otp" })
    public ResponseEntity<?> completeTask(@RequestBody User user, @RequestParam(required = true) String taskId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(URL_CAMUNDA + "task/" + taskId + "/complete",
                CreateJson("otp", user.getFirstName(), "motPass", user.getLastName()),
                Object.class);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Map<String, HashMap<String, HashMap<String, Object>>> CreateJson(String valueMap1, String key2,
            String valueMap2, String key1) {
        Map<String, HashMap<String, HashMap<String, Object>>> variables = new HashMap<String, HashMap<String, HashMap<String, Object>>>();
        HashMap<String, HashMap<String, Object>> info_user = new HashMap<String, HashMap<String, Object>>();
        HashMap<String, Object> value1 = new HashMap<String, Object>();
        HashMap<String, Object> value2 = new HashMap<String, Object>();
        value1.put("value", key1);
        info_user.put(valueMap1, value1);
        variables.put("variables", info_user);

        value2.put("value", key2);
        info_user.put(valueMap2, value2);
        variables.put("variables", info_user);
        return variables;
    }

}
