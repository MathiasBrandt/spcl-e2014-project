package dk.itu.pervasive.common;

/**
 * Created by brandt on 25/10/14.
 */

public class Message {
    private int fromUserId;
    private Integer toUserId;
    private Integer toGroupId;
    private int urgencyId;
    private String message;

    public Message(int fromUserId, Integer toUserId, Integer toGroupId, int urgencyId, String message) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.toGroupId = toGroupId;
        this.urgencyId = urgencyId;
        this.message = message;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public Integer getToGroupId() {
        return toGroupId;
    }

    public int getUrgencyId() {
        return urgencyId;
    }

    public String getMessage() {
        return message;
    }
}
