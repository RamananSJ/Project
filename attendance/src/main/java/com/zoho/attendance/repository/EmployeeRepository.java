package com.zoho.attendance.repository;



import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zoho.attendance.entity.EmployeeDetail;
@Repository
public interface EmployeeRepository extends CrudRepository<EmployeeDetail, Long> {
   
    EmployeeDetail findByEmployeeId(String employee_id);
    EmployeeDetail findByEmployeeIdAndShift(String employee_id,String shift);

    EmployeeDetail[] findByDepartment(String department);
    @Transactional
    void deleteByEmployeeId(String employee_id);

}	