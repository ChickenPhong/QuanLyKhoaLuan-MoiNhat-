/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.pojo;

/**
 *
 * @author Tran Quoc Phong
 */
import javax.persistence.*;

@Entity
@Table(name = "hoiDongs")

public class HoiDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private NguoiDung createdBy;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        active, closed
    }

    // Constructors
    public HoiDong() {}

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NguoiDung getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(NguoiDung createdBy) {
        this.createdBy = createdBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
