package model;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;

@Value
@Builder
public class Relationship {
    long firstUserID;
    long secondUserID;
    RelationStatus relationStatus;
    Timestamp ts_action;

    public long getFirstUserID() {
        return firstUserID;
    }

    public long getSecondUserID() {
        return secondUserID;
    }

    public RelationStatus getRelationStatus() {
        return relationStatus;
    }

    public Timestamp getTimeAction() {
        return ts_action;
    }

    @Override
    public String toString() {
        return "Relationship{" +
                "firstUserID=" + firstUserID +
                ", secondUserID=" + secondUserID +
                ", relationStatus=" + relationStatus +
                ", ts_action=" + ts_action +
                '}';
    }
}
