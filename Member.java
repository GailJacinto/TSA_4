package com.mycompany.jacinto_bookbridge_tsa4;

/**
 *
 * @author Gail Jacinto
 */
public class Member {
    private int memberId;
    private String name;
    private String email;
    private String phone;
    private String registeredDate;

    public Member(int memberId, String name, String email, String phone, String registeredDate) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.registeredDate = registeredDate;
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRegisteredDate() { return registeredDate; }
}
