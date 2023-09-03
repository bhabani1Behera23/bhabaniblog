package com.Employee.controller;

import com.Employee.entity.Employee;
import com.Employee.entity.TaxInfo;
import com.Employee.repository.EmployeeRepo;
import com.Employee.service.EmpSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;

@RestController
@RequestMapping("/tax")
public class TaxController {
  @Autowired
  private EmpSer empSer;
  @Autowired
  private EmployeeRepo employeeRepo;

    @GetMapping("/deduction")
    public ResponseEntity<TaxInfo> calculateTaxDeduction(
            @RequestParam int employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate doj,
            @RequestParam double salary) {


        int startingMonth = (doj.getMonthValue() >= Month.APRIL.getValue()) ? Month.APRIL.getValue() : Month.APRIL.getValue() - 12;
        int currentMonth = LocalDate.now().getMonthValue();
        int taxableMonths = (currentMonth - startingMonth) + 1;

        double yearlySalary = salary * taxableMonths;


        double taxAmount = calculateTaxAmount(yearlySalary);


        double cessAmount = (yearlySalary > 2500000) ? yearlySalary * 0.02 : 0;
          Employee e=employeeRepo.findById(employeeId).get();

        TaxInfo taxInfo = new TaxInfo();
        taxInfo.setFirstName(e.getFirstName());
        taxInfo.setLastName(e.getLastName());
        taxInfo.setEmployeeCode(e.getEmployeeId());
        taxInfo.setYearlySalary(yearlySalary);
        taxInfo.setTaxAmount(taxAmount);
        taxInfo.setCessAmount(cessAmount);

        return ResponseEntity.ok(taxInfo);
    }

    private double calculateTaxAmount(double yearlySalary) {
        if (yearlySalary <= 250000) {
            return 0;
        } else if (yearlySalary <= 500000) {
            return (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary <= 1000000) {
            return 12500 + (yearlySalary - 500000) * 0.10;
        } else {
            return 12500 + 50000 + (yearlySalary - 1000000) * 0.20;
        }
    }
    @PostMapping("/save")
    public  void saveEmployee(@RequestBody Employee employee)
    {
         empSer.add(employee);
    }
}

