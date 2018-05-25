package model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Conversation {

    long id;
    long otherUserID;
    String firstNameUser;
    String lastNameUser;
    long countUnread;

    public long getId() {
        return id;
    }

    public long getOtherUserID() {
        return otherUserID;
    }

    public String getFirstNameUser() {
        return firstNameUser;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public long getCountUnread() {
        return countUnread;
    }


    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", otherUserID=" + otherUserID +
                ", firstNameUser='" + firstNameUser + '\'' +
                ", lastNameUser='" + lastNameUser + '\'' +
                ", countUnread=" + countUnread +
                '}';
    }
}
