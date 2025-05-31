
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
    NguoiDung getByUsername(String username); //apiGiaovu
    NguoiDung getById(int id); //apiAdmin, apiGiaovu
    NguoiDung addUser(NguoiDung user); //apiAdmin
    NguoiDung mergeUser(NguoiDung user);
    NguoiDung addUser(Map<String, String> params, MultipartFile avatar);
    boolean authenticate(String username, String rawPassword);
    boolean deleteUser(int id);
    List<NguoiDung> getAllUsers();  //apiAdmin
    
    List<NguoiDung> getGiangVienByKhoa(String khoa); //apiGiaovu
    // Mới gộp từ SinhVienService
    List<NguoiDung> getSinhVienByKhoaVaKhoaHoc(String khoa, String khoaHoc); //apiAdmin, apiGiaovu
    
    //api Admin
    List<String> getAllKhoaHocByKhoa(String khoa); //apiAdmin, apiGiaovu
    List<NguoiDung> getSinhVienByKhoaHoc(String khoaHoc);
    
    List<String> getAllKhoa(); 
    List<NguoiDung> getSinhVienByKhoaVaKhoaHocVaNganh(String khoa, String khoaHoc, String nganh); //apiAdmin, apiGiaovu
    List<String> getAllNganhByKhoaVaKhoaHoc(String khoa, String khoaHoc); //apiAdmin, apiGiaovu

}