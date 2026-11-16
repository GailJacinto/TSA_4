package com.mycompany.jacinto_bookbridge_tsa4;

/**
 *
 * @author Gail Jacinto
 */
public class Reservation {
    private int reservationId;
    private int bookId;
    private int memberId;
    private String reservationDate;
    private String status;

    public Reservation(int reservationId, int bookId, int memberId, String reservationDate, String status) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.reservationDate = reservationDate;
        this.status = status;
    }

    public int getReservationId() { return reservationId; }
    public int getBookId() { return bookId; }
    public int getMemberId() { return memberId; }
    public String getReservationDate() { return reservationDate; }
    public String getStatus() { return status; }
}