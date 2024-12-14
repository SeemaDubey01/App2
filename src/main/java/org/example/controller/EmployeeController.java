package org.example.controller;

import org.example.domainObjects.EmployeeResponse;
import org.example.dto.EmployeeDTO;
import org.example.repositories.entity.Employee;
import org.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    private static final List<String> VALID_ROLES = Arrays.asList("ADMIN", "USER", "MANAGER");

    @PostMapping
    public ResponseEntity<Object> createEmployee(
            @RequestHeader(value = "role", required = true) String role,  // Extract role from headers
            @RequestBody EmployeeDTO employee) {

        // Validate role
        if (role == null || role.trim().isEmpty()) {
            return new ResponseEntity<>("Role cannot be null or empty.", HttpStatus.BAD_REQUEST);
        }

        if (role.length() < 3 || role.length() > 50) {
            return new ResponseEntity<>("Role length must be between 3 and 50 characters.", HttpStatus.BAD_REQUEST);
        }

        if (!VALID_ROLES.contains(role.toUpperCase())) {
            return new ResponseEntity<>("Invalid role. Valid roles are: ADMIN, USER, MANAGER.", HttpStatus.BAD_REQUEST);
        }
        employee.setRole(role);
        EmployeeResponse createdEmployee = employeeService.createEmployee(employee);

        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee deleted successfully");
    }

    @DeleteMapping("role/{id}")
    public ResponseEntity<String> deletebyRoleId(@PathVariable Long id) {
        employeeService.deleteRole(id);
        return ResponseEntity.ok("Employee deleted by roleId successfully");
    }
}
