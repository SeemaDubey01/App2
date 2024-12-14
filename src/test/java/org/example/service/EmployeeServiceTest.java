package org.example.service;

import org.example.domainObjects.EmployeeResponse;
import org.example.domainObjects.Roles;
import org.example.dto.EmployeeDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.repositories.EmployeeRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.entity.Employee;
import org.example.repositories.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EntityManager entityManager;

    private EmployeeDTO employeeDTO;
    private Employee employee;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("John");
        employeeDTO.setSurname("Doe");
        employeeDTO.setRole("ADMIN");

        role = new Role();
        role.setName(Roles.ADMIN);

        employee = new Employee();
        employee.setFirstName("John");
        employee.setSurname("Doe");
        employee.setRole(role);
    }

    @Test
    void testCreateEmployee() {
        // Mock the behavior of roleRepository and employeeRepository
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Call the method under test
        EmployeeResponse createdEmployee = employeeService.createEmployee(employeeDTO);

        // Assert the result
        assertNotNull(createdEmployee);
        assertEquals("John", createdEmployee.getFirstName());
        assertEquals("Doe", createdEmployee.getSurname());

        // Verify interactions
        verify(roleRepository, times(1)).save(any(Role.class));
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testGetEmployeeById() {
        // Mock the behavior of employeeRepository
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));

        // Call the method under test
        EmployeeResponse foundEmployee = employeeService.getEmployeeById(1L);

        // Assert the result
        assertNotNull(foundEmployee);
        assertEquals("John", foundEmployee.getFirstName());
        assertEquals("Doe", foundEmployee.getSurname());

        // Verify interactions
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeByIdThrowsResourceNotFoundException() {
        // Mock the behavior of employeeRepository to return empty Optional
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Call the method under test and assert that it throws ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(1L);
        });

        assertEquals("Employee not found", exception.getMessage());

        // Verify interactions
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateEmployee() {
        // Mock the behavior of employeeRepository
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Modify the employee object
        employee.setFirstName("Updated Name");

        // Call the method under test
        EmployeeResponse updatedEmployee = employeeService.updateEmployee(1L, employee);

        // Assert the result
        assertNotNull(updatedEmployee);
        assertEquals("Updated Name", updatedEmployee.getFirstName());

        // Verify interactions
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployeeThrowsResourceNotFoundException() {
        // Mock the behavior of employeeRepository to return empty Optional
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        // Call the method under test and assert that it throws ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(1L, employee);
        });

        assertEquals("Employee not found", exception.getMessage());

        // Verify interactions
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteEmployee() {
        // Mock the behavior of employeeRepository
        doNothing().when(employeeRepository).deleteById(1L);

        // Call the method under test
        employeeService.deleteEmployee(1L);

        // Verify interactions
        verify(employeeRepository, times(1)).deleteById(1L);
    }


}
