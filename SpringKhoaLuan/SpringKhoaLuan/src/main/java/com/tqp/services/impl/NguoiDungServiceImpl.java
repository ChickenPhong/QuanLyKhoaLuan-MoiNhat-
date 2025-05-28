
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.tqp.pojo.NguoiDung;
import com.tqp.repositories.NguoiDungRepository;
import com.tqp.services.NguoiDungService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.hibernate.Session;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
@Primary
@Transactional
public class NguoiDungServiceImpl implements NguoiDungService, UserDetailsService {

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    @Autowired
    private BCryptPasswordEncoder passEncoder;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public NguoiDung getByUsername(String username) {
        return nguoiDungRepo.getByUsername(username);
    }

    @Override
    public NguoiDung getById(int id) {
        return nguoiDungRepo.getById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung u = nguoiDungRepo.getByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Không tìm thấy user");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        System.out.println("USERNAME: " + u.getUsername());
        System.out.println("ROLE: [" + u.getRole() + "]");
        authorities.add(new SimpleGrantedAuthority(u.getRole())); // Ex: ROLE_ADMIN

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(), u.getPassword(), authorities);
    }

    @Override
    public NguoiDung addUser(NguoiDung user) {
        user.setPassword(passEncoder.encode(user.getPassword())); // mã hóa
        return nguoiDungRepo.addUser(user);
    }

    @Override
    public NguoiDung mergeUser(NguoiDung user) {
        // Nếu đã có user (tức là đã có ID), gọi merge để cập nhật
        return nguoiDungRepo.merge(user);  // Dùng merge để cập nhật thay vì persist
    }

    @Override
    public NguoiDung addUser(Map<String, String> params, MultipartFile avatar) {
        NguoiDung u = new NguoiDung();
        u.setFullname(params.get("fullname"));
        u.setUsername(params.get("username"));
        u.setPassword(passEncoder.encode(params.get("password"))); // mã hóa mật khẩu
        u.setEmail(params.get("email"));
        u.setKhoa(params.get("khoa"));
        u.setKhoaHoc(params.get("khoaHoc"));
        u.setNganh(params.get("nganh"));
        u.setRole(params.get("role"));

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = cloudinary.uploader().upload(avatar.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(NguoiDungServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return nguoiDungRepo.addUser(u);
    }

    @Override
    public boolean deleteUser(int id) {
        return nguoiDungRepo.deleteUser(id);
    }

    @Override
    public boolean authenticate(String username, String rawPassword) {
        NguoiDung u = this.getByUsername(username);
        if (u != null) {
            System.out.println(" Testing login:");
            System.out.println(" - Username: " + u.getUsername());
            System.out.println(" - Raw password: " + rawPassword);
            System.out.println(" - Stored hash: " + u.getPassword());
            System.out.println(" - Password match: " + passEncoder.matches(rawPassword, u.getPassword()));

            System.out.println("Raw: " + rawPassword);
            System.out.println("Encoded: " + u.getPassword());
            System.out.println("Match: " + passEncoder.matches(rawPassword, u.getPassword()));
        }
        return u != null && passEncoder.matches(rawPassword, u.getPassword());
    }

    @Override
    public List<NguoiDung> getAllUsers() {
        return nguoiDungRepo.getAllUsers();
    }

    @Override
    public List<NguoiDung> getGiangVienByKhoa(String khoa) {
        return nguoiDungRepo.getGiangVienByKhoa(khoa);
    }

    // Từ SinhVienService gộp vào
    @Override
    public List<NguoiDung> getSinhVienByKhoaVaKhoaHoc(String khoa, String khoaHoc) {
        return nguoiDungRepo.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
    }
    
    //api
    @Override
    public List<String> getAllKhoaHocByKhoa(String khoa) {
        return nguoiDungRepo.findDistinctKhoaHocByKhoa(khoa);
    }
    
    @Override
    public List<NguoiDung> getSinhVienByKhoaHoc(String khoaHoc) {
        return nguoiDungRepo.getSinhVienByKhoaHoc(khoaHoc);
    }
    
    @Override
    public List<String> getAllKhoa() {
        return nguoiDungRepo.getAllKhoa();
    }
    
    @Override
    public List<NguoiDung> getSinhVienByKhoaVaKhoaHocVaNganh(String khoa, String khoaHoc, String nganh) {
        return nguoiDungRepo.getSinhVienByKhoaVaKhoaHocVaNganh(khoa, khoaHoc, nganh);
    }
    
    @Override
    public List<String> getAllNganhByKhoaVaKhoaHoc(String khoa, String khoaHoc) {
        return nguoiDungRepo.getAllNganhByKhoaVaKhoaHoc(khoa, khoaHoc);
    }
}