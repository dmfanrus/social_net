package model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class User{
    long id;
    String fullName;
    String email;
    String login;
    String hashPassword;
    LocalDate dateOfBirth;
    Gender gender;

    public long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", hashPassword='" + hashPassword + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                '}';
    }
}
