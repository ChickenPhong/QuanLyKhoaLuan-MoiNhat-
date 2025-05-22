/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.pojo;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 *
 * @author Tran Quoc Phong
 */
@Entity
@Table(name = "thongBaos")

public class ThongBao implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nguoiDung_id")
    private Integer nguoiDungId;

    @Column(name = "tinNhan")
    private String tinNhan;

    @Column(name = "thoiGianGui")
    private LocalDateTime thoiGianGui;

    public ThongBao() {}

    public ThongBao(Integer id, Integer nguoiDungId, String tinNhan, LocalDateTime thoiGianGui) {
        this.id = id;
        this.nguoiDungId = nguoiDungId;
        this.tinNhan = tinNhan;
        this.thoiGianGui = thoiGianGui;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getNguoiDungId() { return nguoiDungId; }
    public void setNguoiDungId(Integer nguoiDungId) { this.nguoiDungId = nguoiDungId; }

    public String getTinNhan() { return tinNhan; }
    public void setTinNhan(String tinNhan) { this.tinNhan = tinNhan; }

    public LocalDateTime getThoiGianGui() { return thoiGianGui; }
    public void setThoiGianGui(LocalDateTime thoiGianGui) { this.thoiGianGui = thoiGianGui; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ThongBao)) return false;
        ThongBao other = (ThongBao) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "ThongBao[ id=" + id + " ]";
    }
}
