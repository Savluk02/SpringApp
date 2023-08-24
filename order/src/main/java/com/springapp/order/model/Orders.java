package com.springapp.order.model;

import org.springframework.security.core.userdetails.User;

import javax.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "orders")
public class Orders {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "specialization")
    private String specialization;
    @Column(name = "creation_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp creationTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person userId;

    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private Person assignedTo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Person getUserId() {
        return userId;
    }

    public void setUserId(Person userId) {
        this.userId = userId;
    }

    public Person getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Person assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getUserName() {
        return userId.getUserName();
    }
}
