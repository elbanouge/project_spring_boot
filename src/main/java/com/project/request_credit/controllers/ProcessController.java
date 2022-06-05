package com.project.request_credit.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.User;
import com.project.request_credit.services.CreditService;
import com.project.request_credit.services.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

        @Autowired
        private UserDetailsServiceImpl userDetailsService;

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
        public ResponseEntity<?> startProcess(@RequestBody User user) {
                RestTemplate restTemplate = new RestTemplate();

                Map<String, HashMap<String, HashMap<String, Object>>> variables = CreateJsonDeuxArgs("nom",
                                user.getLastName(),
                                "prenom", user.getFirstName());

                HttpEntity<Object> requestBody = new HttpEntity<>(variables);
                System.out.println(requestBody);

                Object result = restTemplate.postForObject(URL_CAMUNDA +
                                "process-definition/key/credit_request/start",
                                requestBody, Object.class);

                Map<String, Object> resultMap = (Map<String, Object>) result;
                String processInstanceId = (String) resultMap.get("id");
                System.out.println(processInstanceId);

                User userConnected = userDetailsService.profile();
                List<Credit> credits = creditService.getCreditsByUser(userConnected);
                if (credits != null) {
                        for (Credit credit : credits) {

                                credit.setProcessInstanceId(processInstanceId);
                                creditService.updateCredit(credit, credit.getId());
                        }
                }

                return new ResponseEntity<>(resultMap, HttpStatus.OK);
        }

        @GetMapping({ "info-task-instance" })
        @SuppressWarnings("unchecked")
        public ResponseEntity<?> infoProcess(@RequestParam(required = true) String processInstanceId) {
                RestTemplate restTemplate = new RestTemplate();
                List<Map<String, Object>> result = restTemplate.getForObject(URL_CAMUNDA +
                                "task?processInstanceId=" + processInstanceId,
                                List.class);

                String taskId = (String) result.get(0).get("id");

                User userConnected = userDetailsService.profile();
                List<Credit> credits = creditService.getCreditsByUser(userConnected);
                if (credits != null) {
                        for (Credit credit : credits) {

                                credit.setTaskId(taskId);
                                creditService.updateCredit(credit, credit.getId());
                        }
                }
                return new ResponseEntity<>(result, HttpStatus.OK);
        }

        @PostMapping({ "complete-task-otp-motPass" })
        public ResponseEntity<?> completeTaskOTP(@RequestBody User user, @RequestParam(required = true) String taskId) {
                RestTemplate restTemplate = new RestTemplate();

                Map<String, HashMap<String, HashMap<String, Object>>> variables = CreateJsonDeuxArgs("otp",
                                user.getOtp() + "",
                                "motPass", user.getPassword());

                restTemplate.postForObject(URL_CAMUNDA + "task/" + taskId + "/complete",
                                variables, Object.class);
                return new ResponseEntity<>("Task completed successfully", HttpStatus.OK);
        }

        @PostMapping({ "complete-task-scan-docs" })
        public ResponseEntity<?> completeTaskScanDocs(
                        @RequestParam(required = true) Long fiabilite,
                        @RequestParam(required = true) String taskId) {
                RestTemplate restTemplate = new RestTemplate();

                Map<String, HashMap<String, HashMap<String, Object>>> variables = CreateJsonOneArgsLong("fiabilite",
                                fiabilite);

                restTemplate.postForObject(URL_CAMUNDA + "task/" + taskId + "/complete",
                                variables, Object.class);
                return new ResponseEntity<>("Task completed successfully", HttpStatus.OK);
        }

        @PostMapping({ "complete_Task_Ver_Man_Docs" })
        public ResponseEntity<?> completeTaskVerManDocs(
                        @RequestParam(required = true) String verifierMan,
                        @RequestParam(required = true) String taskId) {
                RestTemplate restTemplate = new RestTemplate();

                Map<String, HashMap<String, HashMap<String, Object>>> variables = CreateJsonOneArgString("verifierMan",
                                verifierMan);

                restTemplate.postForObject(URL_CAMUNDA + "task/" + taskId + "/complete",
                                variables, Object.class);
                return new ResponseEntity<>("Task completed successfully", HttpStatus.OK);
        }

        @PostMapping({ "complete_Task_Entretien_Client" })
        public ResponseEntity<?> completeTaskEntretienClient(
                        @RequestParam(required = true) String taskId) {
                RestTemplate restTemplate = new RestTemplate();

                Map<String, String> variables = new HashMap<>();

                restTemplate.postForObject(URL_CAMUNDA + "task/" + taskId + "/complete", variables, Object.class);
                return new ResponseEntity<>("Task completed successfully", HttpStatus.OK);
        }

        @PostMapping({ "complete_Task_Avis_Demande" })
        public ResponseEntity<?> completeTaskAvisDemande(
                        @RequestParam(required = true) String avis,
                        @RequestParam(required = true) String taskId) {
                RestTemplate restTemplate = new RestTemplate();

                Map<String, HashMap<String, HashMap<String, Object>>> variables = CreateJsonOneArgString("avis",
                                avis);

                restTemplate.postForObject(URL_CAMUNDA + "task/" + taskId + "/complete",
                                variables, Object.class);
                return new ResponseEntity<>("Task completed successfully", HttpStatus.OK);
        }

        @GetMapping({ "info-variables-instance" })
        public ResponseEntity<?> infoVarProcess(@RequestParam(required = true) String processInstanceId) {
                RestTemplate restTemplate = new RestTemplate();
                Object result = restTemplate.getForObject(URL_CAMUNDA +
                                "execution/" + processInstanceId + "/localVariables",
                                Object.class);

                return new ResponseEntity<>(result, HttpStatus.OK);
        }

        public Map<String, HashMap<String, HashMap<String, Object>>> CreateJsonDeuxArgs(String valueMap1, String key1,
                        String valueMap2, String key2) {
                Map<String, HashMap<String, HashMap<String, Object>>> variables = new HashMap<>();
                HashMap<String, HashMap<String, Object>> info_user = new HashMap<>();
                HashMap<String, Object> value1 = new HashMap<>();
                HashMap<String, Object> value2 = new HashMap<>();
                value1.put("value", key1);
                info_user.put(valueMap1, value1);
                variables.put("variables", info_user);

                value2.put("value", key2);
                info_user.put(valueMap2, value2);
                variables.put("variables", info_user);
                return variables;
        }

        public Map<String, HashMap<String, HashMap<String, Object>>> CreateJsonOneArgsLong(String valueMap1,
                        Long key1) {
                Map<String, HashMap<String, HashMap<String, Object>>> variables = new HashMap<>();
                HashMap<String, HashMap<String, Object>> info_user = new HashMap<>();
                HashMap<String, Object> value1 = new HashMap<>();
                value1.put("value", key1);
                info_user.put(valueMap1, value1);
                variables.put("variables", info_user);

                return variables;
        }

        public Map<String, HashMap<String, HashMap<String, Object>>> CreateJsonOneArgString(String valueMap1,
                        String key1) {
                Map<String, HashMap<String, HashMap<String, Object>>> variables = new HashMap<>();
                HashMap<String, HashMap<String, Object>> info_user = new HashMap<>();
                HashMap<String, Object> value1 = new HashMap<>();
                value1.put("value", key1);
                info_user.put(valueMap1, value1);
                variables.put("variables", info_user);

                return variables;
        }
}
