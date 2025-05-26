/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.dto;

import java.util.List;

/**
 *
 * @author Tran Quoc Phong
 */
public class BangDiemTongHopDTO {
    private String tenHoiDong;
    private String tenGiangVienPhanBien;
    private String tenDeTai;
    private String tenSinhVien;
    private Double diemTrungBinh;

    // Constructors
    public BangDiemTongHopDTO() {
    }

    public BangDiemTongHopDTO(String tenHoiDong,  String tenGiangVienPhanBien,String tenDeTai,
                              String tenSinhVien, Double diemTrungBinh) {
        this.tenHoiDong = tenHoiDong;
        this.tenGiangVienPhanBien = tenGiangVienPhanBien;
        this.tenDeTai = tenDeTai;
        this.tenSinhVien = tenSinhVien;
        this.diemTrungBinh = diemTrungBinh;
    }

    // Getters and setters
    public String getTenHoiDong() {
        return tenHoiDong;
    }

    public void setTenHoiDong(String tenHoiDong) {
        this.tenHoiDong = tenHoiDong;
    }
    
    public String getTenGiangVienPhanBien() {
        return tenGiangVienPhanBien;
    }

    public void setTenGiangVienPhanBien(String tenGiangVienPhanBien) {
        this.tenGiangVienPhanBien = tenGiangVienPhanBien;
    }

    public String getTenDeTai() {
        return tenDeTai;
    }

    public void setTenDeTai(String tenDeTai) {
        this.tenDeTai = tenDeTai;
    }

    public String getTenSinhVien() {
        return tenSinhVien;
    }

    public void setTenSinhVien(String tenSinhVien) {
        this.tenSinhVien = tenSinhVien;
    }

    public Double getDiemTrungBinh() {
        return diemTrungBinh;
    }

    public void setDiemTrungBinh(Double diemTrungBinh) {
        this.diemTrungBinh = diemTrungBinh;
    }
}
