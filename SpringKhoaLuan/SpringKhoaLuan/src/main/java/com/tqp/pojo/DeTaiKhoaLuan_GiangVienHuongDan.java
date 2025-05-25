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
@Table(name = "deTaiKhoaLuan_GiangVienHuongDan")

public class DeTaiKhoaLuan_GiangVienHuongDan implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "detaikhoaluan_sinhvien_id")
    private Integer deTaiKhoaLuanSinhVienId;

    @Column(name = "giangVienHuongDan_id")
    private Integer giangVienHuongDanId;

    public DeTaiKhoaLuan_GiangVienHuongDan() {}

    public DeTaiKhoaLuan_GiangVienHuongDan(Integer id, Integer deTaiKhoaLuanSinhVienId, Integer giangVienHuongDanId) {
        this.id = id;
        this.deTaiKhoaLuanSinhVienId = deTaiKhoaLuanSinhVienId;
        this.giangVienHuongDanId = giangVienHuongDanId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getDeTaiKhoaLuanSinhVienId() { return deTaiKhoaLuanSinhVienId; }
    public void setDeTaiKhoaLuanSinhVienId(Integer deTaiKhoaLuanSinhVienId) { this.deTaiKhoaLuanSinhVienId = deTaiKhoaLuanSinhVienId; }

    public Integer getGiangVienHuongDanId() { return giangVienHuongDanId; }
    public void setGiangVienHuongDanId(Integer giangVienHuongDanId) { this.giangVienHuongDanId = giangVienHuongDanId; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeTaiKhoaLuan_GiangVienHuongDan)) return false;
        DeTaiKhoaLuan_GiangVienHuongDan other = (DeTaiKhoaLuan_GiangVienHuongDan) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "DeTaiKhoaLuan_GiangVienHuongDan[ id=" + id + " ]";
    }
}
