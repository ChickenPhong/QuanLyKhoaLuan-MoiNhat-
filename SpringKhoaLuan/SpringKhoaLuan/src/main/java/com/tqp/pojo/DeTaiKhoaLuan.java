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
@Table(name = "deTaiKhoaLuans")

public class DeTaiKhoaLuan implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;
    private String khoa;

    public DeTaiKhoaLuan() {}

    public DeTaiKhoaLuan(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DeTaiKhoaLuan)) return false;
        DeTaiKhoaLuan other = (DeTaiKhoaLuan) obj;
        return this.id != null && this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "DeTaiKhoaLuan[ id=" + id + " ]";
    }
    
}
