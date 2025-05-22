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
@Table(name = "deTaiKhoaLuan_GiangVienHuongDan")

public class DeTaiKhoaLuan_GiangVienHuongDan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "deTaiKhoaLuan_id")
    private DeTaiKhoaLuan deTaiKhoaLuan;

    @ManyToOne
    @JoinColumn(name = "giangVienHuongDan_id")
    private NguoiDung giangVien;

    // Constructors
    public DeTaiKhoaLuan_GiangVienHuongDan() {}

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

    public NguoiDung getGiangVien() {
        return giangVien;
    }

    public void setGiangVien(NguoiDung giangVien) {
        this.giangVien = giangVien;
    }
}
