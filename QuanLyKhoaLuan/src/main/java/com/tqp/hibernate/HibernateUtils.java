/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.hibernate;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.BangDiem;
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.DeTaiKhoaLuan_GiangVienHuongDan;
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.pojo.PhanCongGiangVienPhanBien;
import com.tqp.pojo.ThanhVienHoiDong;
import com.tqp.pojo.ThongBao;
import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils {
    private static final SessionFactory FACTORY;

    static {
        Configuration conf = new Configuration();

        Properties props = new Properties();
        props.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        props.put(Environment.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
        props.put(Environment.JAKARTA_JDBC_URL, "jdbc:mysql://localhost/khoaluandb?useSSL=false");
        props.put(Environment.JAKARTA_JDBC_USER, "root");
        props.put(Environment.JAKARTA_JDBC_PASSWORD, "admin@123"); // chỉnh nếu khác
        props.put(Environment.SHOW_SQL, "true");
        props.put(Environment.HBM2DDL_AUTO, "update"); // tùy chọn: validate | update | create | create-drop

        conf.setProperties(props);

        // Khai báo các lớp POJO
        conf.addAnnotatedClass(NguoiDung.class);
        conf.addAnnotatedClass(DeTaiKhoaLuan.class);
        conf.addAnnotatedClass(DeTaiKhoaLuan_SinhVien.class);
        conf.addAnnotatedClass(DeTaiKhoaLuan_GiangVienHuongDan.class);
        conf.addAnnotatedClass(PhanCongGiangVienPhanBien.class);
        conf.addAnnotatedClass(HoiDong.class);
        conf.addAnnotatedClass(ThanhVienHoiDong.class);
        conf.addAnnotatedClass(DeTaiKhoaLuan_HoiDong.class);
        conf.addAnnotatedClass(BangDiem.class);
        conf.addAnnotatedClass(ThongBao.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(conf.getProperties()).build();

        FACTORY = conf.buildSessionFactory(registry);
    }

    public static SessionFactory getFACTORY() {
        return FACTORY;
    }
}
