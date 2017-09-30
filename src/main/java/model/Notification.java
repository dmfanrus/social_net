package model;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@Builder
public class Notification {
    long id;
    long sender_id;
    long recipient_id;
    String firstName;
    String lastName;
    int not_status;
    Timestamp ts_action;

    public long getId() {
        return id;
    }

    public long getSender_id() {
        return sender_id;
    }

    public long getRecipient_id() {
        return recipient_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNot_status() {
        return not_status;
    }

    public Timestamp getTs_action() {
        return ts_action;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", sender_id=" + sender_id +
                ", recipient_id=" + recipient_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", not_status=" + not_status +
                ", ts_action=" + ts_action +
                '}';
    }
}
