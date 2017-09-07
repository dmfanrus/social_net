package model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Credentials{
    long id;
    String login;
    String password;

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
