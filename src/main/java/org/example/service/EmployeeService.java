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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EntityManager entityManager;

    public EmployeeResponse createEmployee(EmployeeDTO employeeDTO) {
        var names = getFirstAndLastNames(employeeDTO);
        Role role = new Role();
        role.setName(Roles.fromValue(employeeDTO.getRoleId()).getName());
        role.setRoleId(employeeDTO.getRoleId());
        roleRepository.save(role);
        Employee employee = new Employee();
        employee.setFirstName(names.get(0));
        employee.setSurname(names.get(1));
        employee.setRole(role);
        employeeRepository.save(employee);
        return new EmployeeResponse(employee.getId(), employee.getFirstName()+" "+employee.getSurname(), employee.getRole().getRoleId());

    }

    public EmployeeResponse getEmployeeById(Long id) {
         var employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id "+id));
        return new EmployeeResponse(employee.getId(), employee.getFirstName()+" "+employee.getSurname(), employee.getRole().getRoleId());
    }

    public EmployeeResponse updateEmployee(Long id, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id "+id));
        employee.setFirstName(updatedEmployee.getFirstName());
        employee.setSurname(updatedEmployee.getSurname());
        employeeRepository.save(employee);
        return  new EmployeeResponse(employee.getId(), employee.getFirstName()+" "+employee.getSurname(), employee.getRole().getRoleId());
    }

    public void deleteEmployee(Long id) {
        var employee =employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with id "+id));
        employeeRepository.deleteById(employee.getId());
    }
    @Transactional

    public void deleteRole(Long roleId) {
        // Call stored procedure to delete the role and related employees/projects
        entityManager.createNativeQuery("CALL delete_role(:roleId)").setParameter("roleId", roleId).executeUpdate();

    }
    private List<String> getFirstAndLastNames(EmployeeDTO employeeDTO){
        return  Arrays.stream(employeeDTO.getName().split(" "))
                .limit(2)  // Limit to only two parts
                .collect(Collectors.toList());

    }
}
