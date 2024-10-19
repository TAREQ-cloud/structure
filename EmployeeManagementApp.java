package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class EmployeeManagementApp extends Application {
	
	public static ArrayList<Employee> emps            = new ArrayList<>();
	private TableView<Employee>       tableView;
	private TextArea                  searchResultsArea;
	private TextArea                  groupByResultsArea;
	private ComboBox<String>          groupByComboBox = new ComboBox<String>();
	
	public static void main(String[] args) {
		try {
			readFile();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Employee Management System");
		
		tableView = new TableView<>();
		setupTableView();
		
		TabPane tabPane = new TabPane();
		
		Tab showDataTab = new Tab("Show Data", createShowDataTab());
		Tab addTab = new Tab("Add Employee", createAddTab());
		Tab searchDeleteTab = new Tab("Search/Delete", createSearchDeleteTab());
		Tab groupByTab = new Tab("Group By", createGroupByTab());
		
		tabPane.getTabs().addAll(showDataTab, addTab, searchDeleteTab, groupByTab);
		
		Scene scene = new Scene(tabPane, 600, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		refreshTable();
	}
	
	private void setupTableView() {
		TableColumn<Employee, String> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmpID()));
		
		TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
		
		TableColumn<Employee, Integer> ageColumn = new TableColumn<>("Age");
		ageColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getAge()).asObject());
		
		TableColumn<Employee, String> depColumn = new TableColumn<>("Department");
		depColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartment()));
		
		TableColumn<Employee, String> dateColumn = new TableColumn<>("Joining Date");
		dateColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDOJ()));
		
		TableColumn<Employee, String> genderColumn = new TableColumn<>("Gender");
		genderColumn.setCellValueFactory(
				cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGender()));
		
		tableView.getColumns().addAll(idColumn, nameColumn, ageColumn, depColumn, dateColumn, genderColumn);
	}
	
	private VBox createShowDataTab() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.getChildren().add(tableView);
		return vbox;
	}
	
	private VBox createAddTab() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);
		
		TextField idField = new TextField();
		idField.setPromptText("Employee ID");
		TextField nameField = new TextField();
		nameField.setPromptText("Employee Name");
		TextField ageField = new TextField();
		ageField.setPromptText("Employee Age");
		TextField depField = new TextField();
		depField.setPromptText("Employee Department");
		TextField dateField = new TextField();
		dateField.setPromptText("Joining Date");
		TextField genderField = new TextField();
		genderField.setPromptText("Gender");
		
		Button submitButton = new Button("Add Employee");
		submitButton.setOnAction(e -> {
			String id = idField.getText();
			String name = nameField.getText();
			int age = Integer.parseInt(ageField.getText());
			String dep = depField.getText();
			String date = dateField.getText();
			String gender = genderField.getText();
			
			emps.add(new Employee(id, name, age, dep, date, gender));
			System.out.println("Employee Added");
			refreshTable();
			idField.clear();
			nameField.clear();
			ageField.clear();
			depField.clear();
			dateField.clear();
			genderField.clear();
		});
		
		grid.add(new Label("ID:"), 0, 0);
		grid.add(idField, 1, 0);
		grid.add(new Label("Name:"), 0, 1);
		grid.add(nameField, 1, 1);
		grid.add(new Label("Age:"), 0, 2);
		grid.add(ageField, 1, 2);
		grid.add(new Label("Department:"), 0, 3);
		grid.add(depField, 1, 3);
		grid.add(new Label("Joining Date:"), 0, 4);
		grid.add(dateField, 1, 4);
		grid.add(new Label("Gender:"), 0, 5);
		grid.add(genderField, 1, 5);
		grid.add(submitButton, 1, 6);
		
		vbox.getChildren().add(grid);
		return vbox;
	}
	
	private VBox createSearchDeleteTab() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		
		TextField searchField = new TextField();
		searchField.setPromptText("Enter Employee ID");
		
		Button searchButton = new Button("Search Employee");
		Button deleteButton = new Button("Delete Employee");
		
		searchResultsArea = new TextArea();
		searchResultsArea.setEditable(false);
		searchResultsArea.setPromptText("Search Results will appear here...");
		
		searchButton.setOnAction(e -> {
			String id = searchField.getText();
			search(id);
			searchField.clear();
		});
		
		deleteButton.setOnAction(e -> {
			String id = searchField.getText();
			delete(id);
			searchField.clear();
		});
		
		vbox.getChildren().addAll(searchField, searchButton, deleteButton, searchResultsArea);
		return vbox;
	}
	
	private VBox createGroupByTab() {
		VBox groupTab = new VBox();
		
		VBox groupLayout = new VBox(10);
		groupByComboBox.getItems().addAll("By Age", "By Gender", "By Department");
		
		Button groupButton = new Button("Group Employees");
		
		groupByResultsArea = new TextArea();
		groupByResultsArea.setEditable(false);
		
		groupButton.setOnAction(e -> displayGroupCounts());
		
		groupLayout.getChildren().addAll(groupByComboBox, groupButton, groupByResultsArea);
		
		groupTab.getChildren().add(groupLayout);
		
		return groupTab;
	}
	
	public static void readFile() throws FileNotFoundException {
		File file = new File("employee.txt");
		Scanner sc = new Scanner(file);
		
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] tokens = line.split(",");
			if (tokens.length == 6) {
				String id = tokens[0];
				String name = tokens[1];
				int age = Integer.parseInt(tokens[2]);
				String dep = tokens[3];
				String date = tokens[4];
				String gender = tokens[5];
				emps.add(new Employee(id, name, age, dep, date, gender));
			} else {
				System.out.println("Line not supported");
			}
		}
		sc.close();
	}
	
	private void refreshTable() {
		ObservableList<Employee> observableList = FXCollections.observableArrayList(emps);
		tableView.setItems(observableList);
	}
	
	public static void saveEmployees() {
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
			e.printStackTrace();
		}
	}
	
	private void displayGroupCounts() {
		String selectedOption = groupByComboBox.getValue();
		if (selectedOption != null) {
			switch (selectedOption) {
				case "By Age":
					displayEmployeeCountByAge();
					break;
				case "By Gender":
					displayEmployeeCountByGender();
					break;
				case "By Department":
					displayEmployeeCountByDepartment();
					break;
				default:
					groupByResultsArea.setText("Invalid selection. Please choose an option.");
			}
		} else {
			groupByResultsArea.setText("Please select a grouping option.");
		}
	}
	
	private void displayEmployeeCountByAge() {
		final int MAX_AGE = 100;
		int[] ageCount = new int[MAX_AGE + 1];
		
		for (Employee emp : emps) {
			if (emp.getAge() <= MAX_AGE) {
				ageCount[emp.getAge()]++;
			}
		}
		
		StringBuilder sb = new StringBuilder("Employee Count by Age:\n");
		for (int age = 0; age <= MAX_AGE; age++) {
			if (ageCount[age] > 0) {
				sb.append("Age ").append(age).append(": ").append(ageCount[age]).append("\n");
			}
		}
		groupByResultsArea.setText(sb.toString());
	}
	
	private void displayEmployeeCountByGender() {
		int maleCount = 0;
		int femaleCount = 0;
		
		for (Employee emp : emps) {
			switch (emp.getGender().toLowerCase()) {
				case "male":
					maleCount++;
					break;
				case "female":
					femaleCount++;
					break;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("Employee Count by Gender:\n");
		sb.append("Male: ").append(maleCount).append("\n");
		sb.append("Female: ").append(femaleCount).append("\n");
		
		groupByResultsArea.setText(sb.toString());
	}
	
	private void displayEmployeeCountByDepartment() {
		String[] departments = new String[emps.size()];
		int[] departmentCount = new int[emps.size()];
		int deptIndex = 0;
		
		for (Employee emp : emps) {
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
		
		StringBuilder sb = new StringBuilder("Employee Count by Department:\n");
		for (int i = 0; i < deptIndex; i++) {
			sb.append(departments[i]).append(": ").append(departmentCount[i]).append("\n");
		}
		groupByResultsArea.setText(sb.toString());
	}
	
	public void delete(String id) {
		boolean removed = emps.removeIf(emp -> emp.getEmpID().equals(id));
		if (removed) {
			System.out.println("Employee deleted");
			refreshTable();
		} else {
			System.out.println("Employee not found");
		}
	}
	
	public void search(String id) {
		for (Employee emp : emps) {
			if (emp.getEmpID().equals(id)) {
				searchResultsArea.setText(emp.toString());
				return;
			}
		}
		searchResultsArea.setText("No employee with the given ID was found.");
	}
}
