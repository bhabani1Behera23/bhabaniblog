package com.Employee.service;

import com.Employee.entity.Employee;
import com.Employee.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpSer {

    @Autowired
    private EmployeeRepo employeeRepo;

    public void add(Employee employee){
        employeeRepo.save(employee);
    }
}
