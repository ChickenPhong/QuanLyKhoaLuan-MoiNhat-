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
@Table(name = "bangDiems")

public class BangDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "deTaiKhoaLuan_id")
    private DeTaiKhoaLuan deTaiKhoaLuan;

    @ManyToOne
    @JoinColumn(name = "giangVienPhanBien_id")
    private NguoiDung giangVienPhanBien;

    @Column(columnDefinition = "TEXT")
    private String tieuChi;

    private Float diem;

    // Constructors
    public BangDiem() {}

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

    public NguoiDung getGiangVienPhanBien() {
        return giangVienPhanBien;
    }

    public void setGiangVienPhanBien(NguoiDung giangVienPhanBien) {
        this.giangVienPhanBien = giangVienPhanBien;
    }

    public String getTieuChi() {
        return tieuChi;
    }

    public void setTieuChi(String tieuChi) {
        this.tieuChi = tieuChi;
    }

    public Float getDiem() {
        return diem;
    }

    public void setDiem(Float diem) {
        this.diem = diem;
    }
}
