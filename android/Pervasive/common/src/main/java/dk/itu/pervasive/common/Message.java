package dk.itu.pervasive.common;

/**
 * Created by brandt on 25/10/14.
 */

public class Message {
    private int fromUserId;
    private int toUserId;
    private int toGroupId;
    private int urgencyId;
    private String message;

    public Message(int fromUserId, int toUserId, int toGroupId, int urgencyId, String message) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.toGroupId = toGroupId;
        this.urgencyId = urgencyId;
        this.message = message;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public int getToUserId() {
        return toUserId;
    }

    public int getToGroupId() {
        return toGroupId;
    }

    public int getUrgencyId() {
        return urgencyId;
    }

    public String getMessage() {
        return message;
    }
}
