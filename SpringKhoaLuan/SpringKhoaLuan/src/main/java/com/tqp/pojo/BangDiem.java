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
@Table(name = "bangDiems")

public class BangDiem implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deTaiKhoaLuan_SinhVien_id")
    private Integer deTaiKhoaLuanSinhVienId;

    @Column(name = "giangVienPhanBien_id")
    private Integer giangVienPhanBienId;

    @Column(name = "tieuChi")
    private String tieuChi;

    @Column(name = "diem")
    private Float diem;

    public BangDiem() {}

    public BangDiem(Integer id, Integer deTaiKhoaLuanSinhVienId, Integer giangVienPhanBienId, String tieuChi, Float diem) {
        this.id = id;
        this.deTaiKhoaLuanSinhVienId = deTaiKhoaLuanSinhVienId;
        this.giangVienPhanBienId = giangVienPhanBienId;
        this.tieuChi = tieuChi;
        this.diem = diem;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getDeTaiKhoaLuanSinhVienId() { return deTaiKhoaLuanSinhVienId; }
    public void setDeTaiKhoaLuanSinhVienId(Integer deTaiKhoaLuanId) { this.deTaiKhoaLuanSinhVienId = deTaiKhoaLuanId; }

    public Integer getGiangVienPhanBienId() { return giangVienPhanBienId; }
    public void setGiangVienPhanBienId(Integer giangVienPhanBienId) { this.giangVienPhanBienId = giangVienPhanBienId; }

    public String getTieuChi() { return tieuChi; }
    public void setTieuChi(String tieuChi) { this.tieuChi = tieuChi; }

    public Float getDiem() { return diem; }
    public void setDiem(Float diem) { this.diem = diem; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BangDiem)) return false;
        BangDiem other = (BangDiem) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "BangDiem[ id=" + id + " ]";
    }
}
