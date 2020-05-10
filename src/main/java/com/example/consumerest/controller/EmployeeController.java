package com.example.consumerest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.consumerest.model.Employee;
import com.example.consumerest.service.EmployeeService;

@RestController
public class EmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@RequestMapping(value = "employees",method = RequestMethod.GET)   // or use @GetMapping
    public java.util.List<Employee> listEmployee() {
        return employeeService.getAllEmployees();
    }

	@RequestMapping(value = "/employee/{empNo}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Employee getMongoEmployee(@PathVariable("empNo") String empNo) {
		return employeeService.getEmployee(empNo);
	}
	
	@RequestMapping(value = "/employee", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Employee addMongoEmployee(@ModelAttribute Employee employee) {
		return employeeService.createEmployee(employee);
	}
	
	@RequestMapping(value = "/employee", method = RequestMethod.PUT, produces = { MediaType.APPLICATION_JSON_VALUE })
	public Employee updateMongoEmployee(@ModelAttribute Employee employee) {
		return employeeService.updateEmployee(employee);
	}
	
	@RequestMapping(value = "/employee/{empNo}", method = RequestMethod.DELETE)
	public void deleteMongoEmployee(@PathVariable("empNo") String empNo) {
		employeeService.deleteEmployee(empNo);
	}
	
}
