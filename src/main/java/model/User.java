package model;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;
import java.time.LocalDate;

@Value
@Builder
public class User{
    long id;
    String firstName;
    String lastName;
    String email;
    String login;
    String password;
    LocalDate dateOfBirth;
    Gender gender;
    RelationStatus relationStatus;
    Timestamp timeCreate;

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public RelationStatus getRelationship() {
        return relationStatus;
    }

    public Timestamp getTimeCreate() {
        return timeCreate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", relationStatus=" + relationStatus +
                ", ts_create=" + timeCreate +
                '}';
    }
}
