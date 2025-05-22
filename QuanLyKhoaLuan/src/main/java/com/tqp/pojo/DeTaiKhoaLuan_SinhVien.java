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
@Table(name = "deTaiKhoaLuan_SinhVien")

public class DeTaiKhoaLuan_SinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "deTaiKhoaLuan_id")
    private DeTaiKhoaLuan deTaiKhoaLuan;

    @ManyToOne
    @JoinColumn(name = "sinhVien_id")
    private NguoiDung sinhVien;

    // Constructors
    public DeTaiKhoaLuan_SinhVien() {}

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DeTaiKhoaLuan getDeTaiKhoaLuan() {
        return deTaiKhoaLuan;
    }

    public void setDeTaiKhoaLuan(DeTaiKhoaLuan deTaiKhoaLuan) {
        this.deTaiKhoaLuan = deTaiKhoaLuan;
    }

    public NguoiDung getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(NguoiDung sinhVien) {
        this.sinhVien = sinhVien;
    }
}
