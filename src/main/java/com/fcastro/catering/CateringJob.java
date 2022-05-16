package com.fcastro.catering;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CateringJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String phoneNumber;
    private String email;
    private String menu;
    private int noOfGuests;
    private Status status;

    @Override
    public String toString() {
        return "CateringJob{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", menu='" + menu + '\'' +
                ", noOfGuests=" + noOfGuests +
                ", status=" + status +
                '}';
    }
}
