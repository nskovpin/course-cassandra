package ru.dataart.courses.cassandra.web.entities;

public class BookingRequest {

    private String guestName;

    private String comment;


    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
