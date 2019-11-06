/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbconnection;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Isaak
 */
public class Person{
    private  SimpleStringProperty firstName;
    private  SimpleStringProperty lastName;
    private  SimpleStringProperty phone;
    private  SimpleStringProperty email;
    //private  Date birthDate
    final private SimpleIntegerProperty id;
    
    
    Person(String firstName, String lastName, String phone, String email, int id){
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.id = new SimpleIntegerProperty(id);
    }
    
    public String getFirstName(){
        return firstName.get();
    }
    public String getLastName(){
        return lastName.get();
    }
    public String getPhone(){
        return phone.get();
    }
    public String getEmail(){
        return email.get();
    }
    public int getId(){
        return id.get();
    }

    public void setFirstName(String fn){
        this.firstName = new SimpleStringProperty(fn);
    }
    public void setLastName(String ln){
        this.lastName = new SimpleStringProperty(ln);
    }
    public void setPhone(String phone){
        this.phone = new SimpleStringProperty(phone);
    }
    public void setEmail(String email){
        this.email = new SimpleStringProperty(email);
    }
    
    
}
    


    
