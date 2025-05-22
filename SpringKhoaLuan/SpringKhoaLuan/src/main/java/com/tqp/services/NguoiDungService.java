
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.NguoiDung;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface NguoiDungService extends UserDetailsService{
    NguoiDung getByUsername(String username);
    NguoiDung getById(int id);
    NguoiDung addUser(NguoiDung user);
    NguoiDung mergeUser(NguoiDung user);
    NguoiDung addUser(Map<String, String> params, MultipartFile avatar);
    boolean authenticate(String username, String rawPassword);
    boolean deleteUser(int id);
    List<NguoiDung> getAllUsers();
    
    List<NguoiDung> getGiangVienByKhoa(String khoa);
    // Mới gộp từ SinhVienService
    List<NguoiDung> getSinhVienByKhoaVaKhoaHoc(String khoa, String khoaHoc);
    
    //api
    List<String> getAllKhoaHocByKhoa(String khoa);
}