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
@Table(name = "deTaiKhoaLuan_SinhVien")

public class DeTaiKhoaLuan_SinhVien implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "deTaiKhoaLuan_id")
    private Integer deTaiKhoaLuanId;

    @Column(name = "sinhVien_id")
    private Integer sinhVienId;

    public DeTaiKhoaLuan_SinhVien() {}

    public DeTaiKhoaLuan_SinhVien(Integer id, Integer deTaiKhoaLuanId, Integer sinhVienId) {
        this.id = id;
        this.deTaiKhoaLuanId = deTaiKhoaLuanId;
        this.sinhVienId = sinhVienId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getDeTaiKhoaLuanId() { return deTaiKhoaLuanId; }
    public void setDeTaiKhoaLuanId(Integer deTaiKhoaLuanId) { this.deTaiKhoaLuanId = deTaiKhoaLuanId; }

    public Integer getSinhVienId() { return sinhVienId; }
    public void setSinhVienId(Integer sinhVienId) { this.sinhVienId = sinhVienId; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeTaiKhoaLuan_SinhVien)) return false;
        DeTaiKhoaLuan_SinhVien other = (DeTaiKhoaLuan_SinhVien) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "DeTaiKhoaLuan_SinhVien[ id=" + id + " ]";
    }
}
