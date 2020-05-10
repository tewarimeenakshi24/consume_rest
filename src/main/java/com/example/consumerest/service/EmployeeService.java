package com.example.consumerest.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.consumerest.model.Employee;

@Service
public class EmployeeService {
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	public List<Employee> getAllEmployees() {
		RestTemplate restTemplate = new RestTemplate();
		List<Employee> employeeList = new ArrayList<Employee>();
		ResponseEntity<Employee[]> response = restTemplate.getForEntity("http://localhost:8080/mongoemployees", Employee[].class);
		Employee[] employees = response.getBody();
		employeeList = Arrays.asList(employees);
		for(Employee emp : employees) {
			log.info(emp.toString());
		}
		return employeeList;
	}

	public Employee getEmployee(String empNo) {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Employee> response = restTemplate.getForEntity("http://localhost:8080/mongoemployee/" + empNo, Employee.class);
		Employee employee = response.getBody();
		return employee;
	}

	public Employee createEmployee(Employee employee) {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("empNo", employee.getEmployeeId());
		map.add("empName", employee.getEmployeeName());
		map.add("position", employee.getEmployeeRole());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
		ResponseEntity<Employee> response = restTemplate.postForEntity("http://localhost:8080/mongoemployee", httpEntity, Employee.class);
		if(response != null) {
			employee = response.getBody();
		} else {
			employee = null;
		}
		return employee;
	}
	
	public Employee updateEmployee(Employee employee) {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("empNo", employee.getEmployeeId());
			map.add("empName", employee.getEmployeeName());
			map.add("position", employee.getEmployeeRole());
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Employee> response = restTemplate.exchange("http://localhost:8080/mongoemployee", HttpMethod.PUT, requestEntity, Employee.class);
			if(response != null) {
				employee = response.getBody();
			} else {
				employee = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			employee = null;
		} 
		return employee;
	}

	public void deleteEmployee(String empNo) {
		try {
			RequestEntity<Void> requestEntity = RequestEntity.delete(new URI("http://localhost:8080/mongoemployee/" + empNo)).build();
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.exchange(requestEntity, Void.class);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
