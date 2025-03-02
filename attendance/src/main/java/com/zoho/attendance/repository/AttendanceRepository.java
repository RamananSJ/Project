package com.zoho.attendance.repository;


import javax.swing.text.StyledEditorKit.BoldAction;

import com.zoho.attendance.dto.AttendanceSummaryDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.zoho.attendance.entity.AttendanceDetail;

import java.util.List;

@Repository
public interface AttendanceRepository extends CrudRepository<AttendanceDetail, Long> {

	AttendanceDetail[] findByEmployeeId(String employeeid);
	
	 
	AttendanceDetail[] findByDepartmentId(String departmentid);
	
	AttendanceDetail findBydate(String month);
	
	AttendanceDetail findByEmployeeIdAndDepartmentId(String employeeid,String departmentid);
	
	
	Long countByEmployeeIdAndDepartmentId(String employeeid,String departmentid);
	
	Long countByEmployeeIdAndDepartmentIdAndAvailable(String employeeid,String departmentid,Boolean available);
	
	Long countByEmployeeIdAndDepartmentIdAndMonth(String employeeid,String departmentid,String month);
	
	@Query(value = "SELECT count(*) as total FROM attendancedetail where departmentId = ?1 and month = ?2 group by employeeId")
	Long countByDepartmentIdAndMonth(String departmentid,String month);

	Long countByDepartmentIdAndMonthAndAvailable(String departmentid,String month,Boolean available);

	@Transactional
	void deleteByEmployeeIdAndDepartmentId(String employeeid,String departmentid);
	
	@Transactional
	void deleteByDepartmentId(String departmentid);
	
	@Transactional
	void deleteByEmployeeId(String employeeid);

	AttendanceDetail findByEmployeeIdAndDate(String employeeid, String date);

	@Query(value = "SELECT employee_id AS employee_id, COUNT(available) AS total FROM attendancedetail WHERE available = 1 AND department_id = ?1 AND month = ?2 AND shift = ?3 GROUP BY employee_id ORDER BY COUNT(available) DESC", nativeQuery = true)
	List<AttendanceSummaryDTO> findByAttencountOrderByAttencountAsc(String departmentId, String month, String shift);

}	