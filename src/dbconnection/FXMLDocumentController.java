/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbconnection;

import connection.ConnectionClass;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 *
 * @author Isaak
 */
public class FXMLDocumentController implements Initializable {
    ObservableList<Person> people = FXCollections.observableArrayList();
    @FXML
    TableView<Person> table;
    @FXML
    TableColumn<Person, String> colFirstName;
    @FXML
    TableColumn<Person, String> colLastName;
    @FXML
    TableColumn<Person, String> colPhone;
    @FXML
    TableColumn<Person, String> colEmail;
    @FXML
    TextField firstNameField;
    @FXML
    TextField lastNameField;
    @FXML
    TextField phoneField;
    @FXML
    TextField emailField;
    @FXML
    VBox inputBox;
    @FXML
    Button updatePerson;
    @FXML
    Button newPerson;
    @FXML
    Button addPerson;
    @FXML
    Button deletePerson;
    @FXML
    Button clonePerson;
    
    ObservableList<Person> tableSelection;
    
    private final ListChangeListener<Person> tableSelectionChanged;
    
    int currentId;   // takes care of assigning ids to newly created objects

    public FXMLDocumentController() {
        this.tableSelectionChanged = (ListChangeListener.Change<? extends Person> c) -> {
            if (tableSelection!= null && !tableSelection.isEmpty()){
                showTextFields();
                updateTextFields();
                updatePerson.setDisable(false);
                deletePerson.setDisable(false);
                clonePerson.setDisable(false);
            }
            else{
                hideTextFields();
                clearFields();
                updatePerson.setDisable(true);
                deletePerson.setDisable(true);
                clonePerson.setDisable(true);
            }
        };
    }
    void updateTextFields(){
        firstNameField.setText(tableSelection.get(0).getFirstName());
        lastNameField.setText(tableSelection.get(0).getLastName());
        phoneField.setText(tableSelection.get(0).getPhone());
        emailField.setText(tableSelection.get(0).getEmail());
        
    }
    
    void clearFields(){
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        emailField.clear();
    }
    
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        configureButtons();
        hideTextFields();

        ConnectionClass cc = new ConnectionClass();
        
        try(Connection con = cc.getConnection()){
        String sql = "Select * from people";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        while (rs.next()){
            int id = rs.getInt(1);
            String firstName = rs.getString(2);
            String lastName = rs.getString(3);
            String phone = rs.getString(4);
            String email = rs.getString(5);
            Person temp = new Person(firstName, lastName, phone, email, id);
            people.add(temp);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        currentId = people.size();
                
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        
        table.setItems(people);
        
        tableSelection = table.getSelectionModel().getSelectedItems();

        tableSelection.addListener(tableSelectionChanged);
    }    
    
    void hideTextFields(){       
        inputBox.setVisible(false);
    }
    
    void showTextFields(){
        inputBox.setVisible(true);
    }
    
    void configureButtons(){
        
    clonePerson.setDisable(true);
    addPerson.setDisable(true);
    deletePerson.setDisable(true);
    updatePerson.setDisable(true);
    
                                                                // newPerson Button
    newPerson.setOnAction((ActionEvent e) -> {
        table.getSelectionModel().clearSelection();
        showTextFields();
        //clearFields();
        addPerson.setDisable(false);
    });
                                                               //addPerson Button
    addPerson.setOnAction((ActionEvent e) -> {
        Person temp = new Person(firstNameField.getText(),
                lastNameField.getText(),
                phoneField.getText(),
                emailField.getText(),
                ++currentId
        );
        
        ConnectionClass cc = new ConnectionClass();          // Committing changes to the database
                    try(Connection con = cc.getConnection()){
                        String sql = "Insert into people(id, firstname, lastname, phonenumber, email) values("
                                + temp.getId()
                                + ", '" + temp.getFirstName()
                                + "', '" + temp.getLastName()
                                + "', '" + temp.getPhone()
                                + "', '" + temp.getEmail() + "')";
                        Statement stmt = con.createStatement();
                        stmt.execute(sql);
                        /*String sql1 = "Select count(*) from people";
                        ResultSet rs = stmt.executeQuery(sql1);
                        rs.next();
                        System.out.println("Id: " + temp.getId());
                        System.out.println(rs.getInt(1));*/
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
        
        
        
        people.add(temp);
        addPerson.setDisable(true);  
        table.getSelectionModel().clearSelection();
        clearFields();
        hideTextFields();
    });                                             //addPerson.setOnAction
    
                                                              // updatePerson Button
    updatePerson.setOnAction((ActionEvent e) -> {
        if (tableSelection!= null && !tableSelection.isEmpty()){
            Person temp = tableSelection.get(0);
            for (int i = 0; i < people.size(); i++){
                if (people.get(i).equals(temp)){
                    temp.setFirstName(firstNameField.getText());
                    temp.setLastName(lastNameField.getText());
                    temp.setPhone(phoneField.getText());
                    temp.setEmail(emailField.getText());
                    people.remove(i, i+1);
                    people.add(i, temp);
                }
            }
            
            ConnectionClass cc = new ConnectionClass();
            try(Connection con = cc.getConnection()){
                String sql = "Update people "
                        + "set firstname = '" + temp.getFirstName()
                        + "', lastname = '" + temp.getLastName()
                        + "', phonenumber = '" + temp.getPhone()
                        + "', email = '" + temp.getEmail()
                        + "' where id = " + temp.getId();
                Statement stmt = con.createStatement();
                stmt.execute(sql);
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            
            ObservableList<Person> tempPeople = FXCollections.observableArrayList(people);
            
            //int selectedRowIndex = table.getSelectionModel().getSelectedIndex();
            table.getItems().clear();
            table.setItems(tempPeople);
            //table.getSelectionModel().select(selectedRowIndex+1);
            table.getSelectionModel().clearSelection();
            
            people = tempPeople;
        }
    });                                     // updatePerson.setOnAction
    
                                                               // deletePerson Button
    deletePerson.setOnAction((ActionEvent e) -> {
        if (tableSelection!= null && !tableSelection.isEmpty()){
            Person temp = tableSelection.get(0);
            for (int i = 0; i < people.size(); i++){
                if (people.get(i).equals(temp)){
                    people.remove(i, i+1);                    
                }               
            }
            System.out.println("Id: " + temp.getId());
            ConnectionClass cc = new ConnectionClass();
            try(Connection con = cc.getConnection()){
                String sql = "Delete from people "
                        + " where id = " + temp.getId();
                Statement stmt = con.createStatement();
                stmt.execute(sql);
                String sql1 = "Select count(*) from people";
                ResultSet rs = stmt.executeQuery(sql1);
                        rs.next();
                        System.out.println(rs.getInt(1));
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            
            ObservableList<Person> tempPeople = FXCollections.observableArrayList(people);
            
            table.getSelectionModel().clearSelection();
            hideTextFields();
            
            table.getItems().clear();
            table.setItems(tempPeople);
            
            people = tempPeople;      
        }  
        
        
        
        hideTextFields();
    });                                     // deletePerson.setOnAction
    
                                                                 // clonePerson Button
    clonePerson.setOnAction((ActionEvent e) -> {
        if (tableSelection!= null && !tableSelection.isEmpty()){
            Person selected = tableSelection.get(0);
            Person temp = new Person(selected.getFirstName(),
                    selected.getLastName(),
                    selected.getPhone(),
                    selected.getEmail(),
                    ++currentId);
            people.add(temp);
                    
            ConnectionClass cc = new ConnectionClass();          // Committing changes to the database
            try(Connection con = cc.getConnection()){
                String sql = "Insert into people(id, firstname, lastname, phonenumber, email) values("
                        + temp.getId()
                        + ", '" + temp.getFirstName()
                        + "', '" + temp.getLastName()
                        + "', '" + temp.getPhone()
                        + "', '" + temp.getEmail() + "')";
                Statement stmt = con.createStatement();
                stmt.execute(sql);

                /*String sql1 = "Select count(*) from people";
                ResultSet rs = stmt.executeQuery(sql1);
                rs.next();
                System.out.println("Id: " + temp.getId());
                System.out.println(rs.getInt(1));*/
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        } 
        
        
        
        
    });                         // clonePerson.setOnAction
    
    
        
        
    }
       
}

