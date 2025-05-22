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
@Table(name = "thanhVienHoiDong")

public class ThanhVienHoiDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "hoiDong_id")
    private HoiDong hoiDong;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private NguoiDung user;

    @Enumerated(EnumType.STRING)
    private RoleHoiDong role;

    public enum RoleHoiDong {
        chu_tich, thu_ky, phan_bien, thanh_vien
    }

    // Constructors
    public ThanhVienHoiDong() {}

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HoiDong getHoiDong() {
        return hoiDong;
    }

    public void setHoiDong(HoiDong hoiDong) {
        this.hoiDong = hoiDong;
    }

    public NguoiDung getUser() {
        return user;
    }

    public void setUser(NguoiDung user) {
        this.user = user;
    }

    public RoleHoiDong getRole() {
        return role;
    }

    public void setRole(RoleHoiDong role) {
        this.role = role;
    }
}
