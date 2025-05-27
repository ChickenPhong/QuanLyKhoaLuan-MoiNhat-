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
@Table(name = "nguoiDungs")

@NamedQueries({
    @NamedQuery(name = "NguoiDung.findAll", query = "SELECT u FROM NguoiDung u"),
    @NamedQuery(name = "NguoiDung.findById", query = "SELECT u FROM NguoiDung u WHERE u.id = :id"),
    @NamedQuery(name = "NguoiDung.findByUsername", query = "SELECT u FROM NguoiDung u WHERE u.username = :username"),
    @NamedQuery(name = "NguoiDung.findByRole", query = "SELECT u FROM NguoiDung u WHERE u.role = :role")
})

public class NguoiDung implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "fullname")
    private String fullname;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "email")
    private String email;

    @Column(name = "khoa")
    private String khoa;

    @Column(name = "khoaHoc")
    private String khoaHoc;
    
    @Column(name = "nganh")
    private String nganh;
    // Constructors
    public NguoiDung() {}
    public NguoiDung(Integer id) { this.id = id; }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getKhoa() {
    return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getKhoaHoc() {
        return khoaHoc;
    }

    public void setKhoaHoc(String khoaHoc) {
        this.khoaHoc = khoaHoc;
    }
    
    public String getNganh() {
    return nganh;
    }

    public void setNganh(String nganh) {
        this.nganh = nganh;
    }
    // equals, hashCode, toString
    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NguoiDung)) return false;
        NguoiDung other = (NguoiDung) obj;
        return (this.id != null && this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "com.tqp.pojo.NguoiDung[ id=" + id + " ]";
    }
}
