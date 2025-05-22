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
import java.util.Date;

@Entity
@Table(name = "thongBaos")

public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "nguoiDung_id")
    private NguoiDung nguoiDung;

    @Column(columnDefinition = "TEXT")
    private String tinNhan;

    @Temporal(TemporalType.TIMESTAMP)
    private Date thoiGianGui;

    // Constructors
    public ThongBao() {}

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NguoiDung getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(NguoiDung nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public String getTinNhan() {
        return tinNhan;
    }

    public void setTinNhan(String tinNhan) {
        this.tinNhan = tinNhan;
    }

    public Date getThoiGianGui() {
        return thoiGianGui;
    }

    public void setThoiGianGui(Date thoiGianGui) {
        this.thoiGianGui = thoiGianGui;
    }
}
