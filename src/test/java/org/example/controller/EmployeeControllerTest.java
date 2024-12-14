package org.example.controller;

import org.example.domainObjects.EmployeeResponse;
import org.example.domainObjects.Roles;
import org.example.dto.EmployeeDTO;
import org.example.repositories.entity.Employee;
import org.example.repositories.entity.Role;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void testCreateEmployee() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName("Raj");
        employeeDTO.setSurname("Malhotra");
        employeeDTO.setRole("ADMIN");
        Role role = new Role();
        role.setName(Roles.ADMIN);
        role.setRoleId(1L);
        EmployeeResponse createdEmployee = new EmployeeResponse(
        1L,
        "Raj",
        "Malhotra",
       1L);


        when(employeeService.createEmployee(employeeDTO)).thenReturn(createdEmployee);

        mockMvc.perform(post("/api/employees")
                        .header("role", "ADMIN")
                        .contentType("application/json")
                        .content("{\"firstName\":\"Raj\",\"surname\":\"Malhotra\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetEmployee() throws Exception {
        Role role = new Role();
        role.setName(Roles.USER);
        role.setRoleId(2L);
        EmployeeResponse employee = new EmployeeResponse(
        1L,
       "Raj",
        "Malhotra",
        2L);

        when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Raj"))
                .andExpect(jsonPath("$.surname").value("Malhotra"))
        .andExpect(jsonPath("$.roleId").value(2));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void testUpdateEmployee() throws Exception {

        Role role = new Role();
        role.setName(Roles.USER);
        role.setId(2L);
        Employee employee = new Employee();
        employee.setFirstName("Raj");
        employee.setSurname("Malhotra");
        employee.setRole(role);
        EmployeeResponse employeeResponse = new EmployeeResponse(
        1L,
        "Raj",
        "Malhotra",
        2L);

        when(employeeService.updateEmployee(1L, employee)).thenReturn(employeeResponse);

        mockMvc.perform(put("/api/employees/1")
                        .contentType("application/json")
                        .content("{\"firstName\":\"Raj\",\"surname\":\"Malhotra\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void testDeleteEmployeeByRoleId() throws Exception {
        doNothing().when(employeeService).deleteRole(1L);

        mockMvc.perform(delete("/api/employees/role/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted by roleId successfully"));

        verify(employeeService, times(1)).deleteRole(1L);
    }
}
