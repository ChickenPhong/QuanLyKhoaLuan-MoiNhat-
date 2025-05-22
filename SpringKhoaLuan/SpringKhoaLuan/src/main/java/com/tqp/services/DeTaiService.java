/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import java.util.List;
import com.tqp.pojo.DeTaiKhoaLuan;

public interface DeTaiService {
    List<DeTaiKhoaLuan> getAllDeTai();
    List<DeTaiKhoaLuan> getByKhoa(String khoa);
    DeTaiKhoaLuan getDeTaiById(int id);
    DeTaiKhoaLuan addDeTai(DeTaiKhoaLuan deTai);
    boolean deleteDeTai(int id);
}
