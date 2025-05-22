/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.dto;

/**
 *
 * @author Tran Quoc Phong
 */
public class ThongKeDTO {
    private String khoa; // Ngành
    private String khoaHoc; // Khóa (năm)
    private Long soLuongDeTai; // Tổng số đề tài
    private Double diemTrungBinh; // Điểm trung bình
    private Long soLuongSinhVien; // Số lượng sinh viên làm khóa luận
    private Integer hoiDongId;

    // Constructors
    public ThongKeDTO() {}
    
    public ThongKeDTO(String khoa, String khoaHoc, Long soLuongDeTai, Double diemTrungBinh, Long soLuongSinhVien) {
        this.khoa = khoa;
        this.khoaHoc = khoaHoc;
        this.soLuongDeTai = soLuongDeTai;
        this.diemTrungBinh = diemTrungBinh;
        this.soLuongSinhVien = soLuongSinhVien;
    }

    public ThongKeDTO(String khoa, String khoaHoc, Long soLuongDeTai, Double diemTrungBinh, Long soLuongSinhVien, Integer hoiDongId) {
        this.khoa = khoa;
        this.khoaHoc = khoaHoc;
        this.soLuongDeTai = soLuongDeTai;
        this.diemTrungBinh = diemTrungBinh;
        this.soLuongSinhVien = soLuongSinhVien;
        this.hoiDongId = hoiDongId;
    }

    // Getters & setters
    public String getkhoa() { return khoa; }
    public void setkhoa(String khoa) { this.khoa = khoa; }

    public String getkhoaHoc() { return khoaHoc; }
    public void setkhoaHoc(String khoaHoc) { this.khoaHoc = khoaHoc; }

    public Long getSoLuongDeTai() { return soLuongDeTai; }
    public void setSoLuongDeTai(Long soLuongDeTai) { this.soLuongDeTai = soLuongDeTai; }

    public Double getDiemTrungBinh() { return diemTrungBinh; }
    public void setDiemTrungBinh(Double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }

    public Long getSoLuongSinhVien() { return soLuongSinhVien; }
    public void setSoLuongSinhVien(Long soLuongSinhVien) { this.soLuongSinhVien = soLuongSinhVien; }
    
    public Integer getHoiDongId() { return hoiDongId; }
    public void setHoiDongId(Integer hoiDongId) { this.hoiDongId = hoiDongId; }
}
