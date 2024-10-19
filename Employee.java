package application;

import java.util.Date;

public class Employee implements Comparable<Employee> {
	private String EmpID;
	private String Name;
	private int    Age;
	private String department;
	private String DOJ;
	private String gender;
	
	public Employee(String empID, String name, int age, String department, String dOJ, String gender) {
		super();
		EmpID = empID;
		Name = name;
		Age = age;
		this.department = department;
		DOJ = dOJ;
		this.gender = gender;
	}
	
	public String getEmpID() {
		return EmpID;
	}
	
	public void setEmpID(String empID) {
		EmpID = empID;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String name) {
		Name = name;
	}
	
	public int getAge() {
		return Age;
	}
	
	public void setAge(int age) {
		Age = age;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	public String getDOJ() {
		return DOJ;
	}
	
	public void setDOJ(String dOJ) {
		DOJ = dOJ;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Override
	public String toString() {
		return "Employee : \n EmployeeID = " + EmpID + "\n Name = " + Name + "\n Age = " + Age + "\n Department = "
				+ department + "\n Date Of Joining = " + DOJ + "\n Gender = " + gender;
	}
	
	@Override
	public int compareTo(Employee o) {
		return this.getEmpID().compareTo(o.getEmpID());
	}
	
}
