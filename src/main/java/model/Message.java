package model;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;

@Value
@Builder
public class Message {
    long id;
    long conv_id;
    long sender_id;
    Timestamp ts_action;
    String message;
    String firstName;
    String lastName;

    public long getId() {
        return id;
    }

    public long getConv_id() {
        return conv_id;
    }

    public long getSender_id() {
        return sender_id;
    }

    public Timestamp getTs_action() {
        return ts_action;
    }

    public String getMessage() {
        return message;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                "conv_id=" + conv_id +
                ", sender_id=" + sender_id +
                ", ts_action=" + ts_action +
                ", message='" + message + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}

