package chooser.view;

/*public class registerUserController {
   @Override
    public void start(Stage primaryStage) {
        // enter user data into text fields
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        Label DOBLabel = new Label("DOB (YYYY-MM-DD):");
        TextField DOBField = new TextField();

        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();

        Button registerButton = new Button("Register New Employee");
        Button listButton = new Button("List Employees");

        // show employees
        ListView<String> employeeListView = new ListView<>();

        // button for user registration
        registerButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String DOB = DOBField.getText();
            String phoneNum = phoneField.getText();
            if (!firstName.isEmpty() && !lastName.isEmpty() && !DOB.isEmpty() && !phoneNum.isEmpty()) {
                registerUser.registerEmployee(firstName, lastName, DOB, phoneNum);
                clearInputFields(firstNameField, lastNameField, DOBField, phoneField);
            } else {
                showAlert("Error", "All fields must be filled!");
            }
        });

        // list button
        listButton.setOnAction(e -> {
            employeeListView.getItems().clear();
            for (Employee emp : registerUser.getEmployees()) {
                employeeListView.getItems().add(emp.toString());
            }
        });

        // menu layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                DOBLabel, DOBField,
                phoneLabel, phoneField,
                registerButton, listButton,
                employeeListView);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setTitle("Track Bite Administrator System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void clearInputFields(TextField firstNameField, TextField lastNameField, TextField DOBField, TextField phoneField) {
        firstNameField.clear();
        lastNameField.clear();
        DOBField.clear();
        phoneField.clear();
    }

    // check for missing fields
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
*/