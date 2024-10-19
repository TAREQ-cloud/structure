package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class main {
	public static ArrayList<Employee> emps = new ArrayList<Employee>();

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		try {
			readFile();
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
		}
		while (true) {
			System.out.println("1.Add Emplyee");
			System.out.println("2.Delete Employee");
			System.out.println("3.search for Employee");
			System.out.println("4.Print all Employees");
			System.out.println("5.Save to file");
			System.out.println("6.Display Employee Count by Age, Gender, and Department");
			System.out.println("Choode an option: ");
			int choise = input.nextInt();

			switch (choise) {
			case 1:
				add();
				break;
			case 2:
				System.out.println("Enter Id to delete: ");
				String idtodelete = input.nextLine();
				input.nextLine();
				delete(idtodelete);
				break;
			case 3:
				System.out.println("Enter Id to search: ");
				input.nextLine();
				String idtosearch = input.nextLine();
				search(idtosearch);
				break;
			case 4:
				emps.sort(null);
				for (Employee emp : emps) {

					System.out.println(emp.toString());

				}
				System.out.println();
				break;
			case 5:
				save();
				break;
			case 6:
				displayEmployeeCounts();
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + choise);
			}
		}
	}

	public static void add() {
		Scanner addsc = new Scanner(System.in);
		System.out.println("Enter the Employee ID: ");
		String id = addsc.nextLine();

		System.out.println("Enter the Employee Name: ");
		String name = addsc.nextLine();

		System.out.println("Enter the Employee Age: ");
		int age = addsc.nextInt();

		System.out.println("Enter the Employee Departmant: ");
		addsc.nextLine();
		String dep = addsc.nextLine();

		System.out.println("Enter the Employee Joining Date: ");
		String date = addsc.nextLine();

		System.out.println("Enter the Employee gender: ");
		String gender = addsc.nextLine();

		emps.add(new Employee(id, name, age, dep, date, gender));
		System.out.println("Employee Added");
	}

	public static void delete(String id) {
		for (Employee emp : emps) {
			if (emp.getEmpID().equals(id)) {
				emps.remove(emp);
				System.out.println("Employee deleted");
				return;
			}
		}
		System.out.println("Employee not found");
	}

	public static void search(String id) {
		for (Employee emp : emps) {
			if (emp.getEmpID().equals(id))
				System.out.println(emp.toString());
		}
		System.out.println("Employee not found");

	}

	public static void readFile() throws FileNotFoundException {
		File file = new File("employee.txt");
		Scanner sc = new Scanner(file);

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.length() != 6) {
				String[] tokens = line.split(",");
				String id = tokens[0];
				String name = tokens[1];
				int age = Integer.parseInt(tokens[2]);
				String dep = tokens[3];
				String date = tokens[4];
				String gender = tokens[5];
				emps.add(new Employee(id, name, age, dep, date, gender));

			} else
				System.out.println("line not supportedS");
		}

	}

	public static void save() {
		File file = new File("updatedEmployee.txt");
		try {
			FileWriter write = new FileWriter(file);
			for (Employee emp : emps) {
				write.write(emp.toString());
				write.write("\n");
			}
			write.close();
			System.out.println("Data Saved");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void displayEmployeeCounts() {

		final int MAX_AGE = 100;
		int[] ageCount = new int[MAX_AGE + 1];
		int maleCount = 0;
		int femaleCount = 0;
		String[] departments = new String[emps.size()];
		int[] departmentCount = new int[emps.size()];
		int deptIndex = 0;

		for (Employee emp : emps) {
			if (emp.getAge() <= MAX_AGE) {
				ageCount[emp.getAge()]++;
			}

			switch (emp.getGender().toLowerCase()) {
			case "male":
				maleCount++;
				break;
			case "female":
				femaleCount++;
				break;

			}

			boolean found = false;
			for (int i = 0; i < deptIndex; i++) {
				if (departments[i].equals(emp.getDepartment())) {
					found = true;
					departmentCount[i]++;
					break;
				}
			}
			if (!found) {
				departments[deptIndex] = emp.getDepartment();
				departmentCount[deptIndex] = 1;
				deptIndex++;
			}
		}

		System.out.println("Employee Count by Age:");
		for (int age = 0; age <= MAX_AGE; age++) {
			if (ageCount[age] > 0) {
				System.out.println("Age " + age + ": " + ageCount[age]);
			}
		}

		System.out.println("\nEmployee Count by Gender:");
		System.out.println("Male: " + maleCount);
		System.out.println("Female: " + femaleCount);

		System.out.println("\nEmployee Count by Department:");
		for (int i = 0; i < deptIndex; i++) {
			System.out.println(departments[i] + ": " + departmentCount[i]);
		}
	}

}