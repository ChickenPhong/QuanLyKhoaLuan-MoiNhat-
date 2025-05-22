/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.pojo;

/**
 *
 * @author Tran Quoc Phong
 */
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "thanhVienHoiDong")

public class ThanhVienHoiDong implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "hoiDong_id")
    private Integer hoiDongId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role")
    private String role;

    public ThanhVienHoiDong() {}

    public ThanhVienHoiDong(Integer id, Integer hoiDongId, Integer userId, String role) {
        this.id = id;
        this.hoiDongId = hoiDongId;
        this.userId = userId;
        this.role = role;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getHoiDongId() { return hoiDongId; }
    public void setHoiDongId(Integer hoiDongId) { this.hoiDongId = hoiDongId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ThanhVienHoiDong)) return false;
        ThanhVienHoiDong other = (ThanhVienHoiDong) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "ThanhVienHoiDong[ id=" + id + " ]";
    }
}
