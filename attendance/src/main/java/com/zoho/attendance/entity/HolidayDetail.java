package com.zoho.attendance.entity;

import jakarta.persistence.*;

@Entity(name="holidaydetail")
public class HolidayDetail {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
		@Column(name = "date")
		long date;
		
		String reason;
		
		public long getDate() {
			return date;
		}
		public void setDate(long date) {
			
			this.date = date;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
}
