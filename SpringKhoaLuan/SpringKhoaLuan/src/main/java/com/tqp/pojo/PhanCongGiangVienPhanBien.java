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
@Table(name = "phanCongGiangVienPhanBiens")

public class PhanCongGiangVienPhanBien implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "giangVienPhanBien_id")
    private Integer giangVienPhanBienId;

    @Column(name = "hoiDong_id")
    private Integer hoiDongId;

    public PhanCongGiangVienPhanBien() {}

    public PhanCongGiangVienPhanBien(Integer id, Integer giangVienPhanBienId, Integer hoiDongId) {
        this.id = id; 
        this.giangVienPhanBienId = giangVienPhanBienId;
        this.hoiDongId = hoiDongId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getGiangVienPhanBienId() { return giangVienPhanBienId; }
    public void setGiangVienPhanBienId(Integer giangVienPhanBienId) { this.giangVienPhanBienId = giangVienPhanBienId; }
    
    public Integer getHoiDongId() { return hoiDongId; }
    public void setHoiDongId(Integer hoiDongId) { this.hoiDongId = hoiDongId; }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PhanCongGiangVienPhanBien)) return false;
        PhanCongGiangVienPhanBien other = (PhanCongGiangVienPhanBien) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "PhanCongGiangVienPhanBien[ id=" + id + " ]";
    }
}
