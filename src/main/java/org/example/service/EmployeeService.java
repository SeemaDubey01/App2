package org.example.service;

import org.example.domainObjects.EmployeeResponse;
import org.example.domainObjects.Roles;
import org.example.dto.EmployeeDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.repositories.EmployeeRepository;
import org.example.repositories.RoleRepository;
import org.example.repositories.entity.Employee;
import org.example.repositories.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EntityManager entityManager;

    public EmployeeResponse createEmployee(EmployeeDTO employeeDTO) {
        Role role = new Role();
        role.setName(Roles.valueOf(employeeDTO.getRole()));
        role.setRoleId(Roles.valueOf(employeeDTO.getRole()).getId());
        roleRepository.save(role);
        Employee employee = new Employee();
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setSurname(employeeDTO.getSurname());
        employee.setRole(role);
        employeeRepository.save(employee);
        return new EmployeeResponse(employee.getId(), employee.getFirstName(), employee.getSurname(), employee.getRole().getRoleId());

    }

    public EmployeeResponse getEmployeeById(Long id) {
         var employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return new EmployeeResponse(employee.getId(), employee.getFirstName(),employee.getSurname(), employee.getRole().getRoleId());
    }

    public EmployeeResponse updateEmployee(Long id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setSurname(updatedEmployee.getSurname());
        employeeRepository.save(employee);
        return new EmployeeResponse(employee.getId(),employee.getFirstName(),employee.getFirstName(),employee.getRole().getRoleId());
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
    @Transactional

    public void deleteRole(Long roleId) {
        // Call stored procedure to delete the role and related employees/projects
        entityManager.createNativeQuery("CALL delete_role(:roleId)").setParameter("roleId", roleId).executeUpdate();

    }
}
