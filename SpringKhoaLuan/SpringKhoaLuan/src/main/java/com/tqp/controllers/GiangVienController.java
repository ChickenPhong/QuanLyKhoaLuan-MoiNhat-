/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.pojo.BangDiem;
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.services.BangDiemService;
import com.tqp.services.DeTaiHoiDongService;
import com.tqp.services.DeTaiHuongDanService;
import com.tqp.services.DeTaiService;
import com.tqp.services.NguoiDungService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import com.tqp.services.TieuChiService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GiangVienController {
    
    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DeTaiService deTaiService;

    @Autowired
    private DeTaiHuongDanService deTaiGVHuongDanService;

    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;
    
    @Autowired
    private PhanCongGiangVienPhanBienService phanCongGiangVienPhanBienService;

    @Autowired
    private DeTaiHoiDongService deTaiHoiDongService;

    @Autowired
    private TieuChiService tieuChiService;

    @Autowired
    private BangDiemService bangDiemService;

    
    // Giao diện dành cho giảng viên
    @GetMapping("/giangvien")
    public String danhSachThucHien(@RequestParam(value = "khoaHoc", required = false) String khoaHoc,
            Principal principal,
            Model model) {
        var user = nguoiDungService.getByUsername(principal.getName());
        // Giả sử bạn có thể lấy giangVienPhanBienId từ user hoặc service
        int giangVienPhanBienId = user.getId();
        model.addAttribute("giangVienPhanBienId", giangVienPhanBienId);
        

        // Nạp danh sách năm
        int currentYear = java.time.Year.now().getValue();
        List<String> years = new ArrayList<>();
        for (int y = 2020; y <= currentYear; y++) {
            years.add(String.valueOf(y));
        }
        model.addAttribute("yearOptions", years);
        model.addAttribute("khoaHoc", khoaHoc);

        if (khoaHoc != null) {
            var sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(user.getKhoa(), khoaHoc);
            model.addAttribute("sinhViens", sinhViens);

            Map<Integer, String> deTaiMap = new HashMap<>();
            Map<Integer, String> giangVienMap = new HashMap<>();

            for (var sv : sinhViens) {
                var dtsv = deTaiSinhVienService.findBySinhVienId(sv.getId());
                if (dtsv != null) {
                    var dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
                    var gvs = deTaiGVHuongDanService.findAllByDeTaiKhoaLuanSinhVienId(dtsv.getId());

                    // Thêm vào map đề tài
                    deTaiMap.put(sv.getId(), dt != null ? dt.getTitle() : "Chưa có");

                    // Lấy giảng viên từ danh sách giảng viên hướng dẫn
                    String tenGVs = gvs.stream()
                            .map(gv -> {
                                var giangVien = nguoiDungService.getById(gv.getGiangVienHuongDanId());
                                return giangVien != null ? giangVien.getUsername() : "Chưa có";
                            })
                            .collect(Collectors.joining(", "));

                    // Thêm vào map giảng viên
                    giangVienMap.put(sv.getId(), tenGVs.isEmpty() ? "Chưa có" : tenGVs);
                }
            }

            model.addAttribute("deTaiMap", deTaiMap);
            model.addAttribute("giangVienMap", giangVienMap);
            model.addAttribute("khoa", user.getKhoa());
        }

        model.addAttribute("khoa", user.getKhoa());
        return "giangvien";
    }
    
    @GetMapping("/giangvien/chamdiem")
    public String hienThiChamDiem(@RequestParam("giangVienPhanBienId") int giangVienPhanBienId,
                                  @RequestParam(value = "hoiDongId", required = false) Integer hoiDongId, // lấy từ query param nếu có
                                  Model model) {
        var phanCongList = phanCongGiangVienPhanBienService.findByGiangVienPhanBienId(giangVienPhanBienId);

        if (phanCongList == null || phanCongList.isEmpty()) {
            model.addAttribute("error", "Bạn chưa được phân công vào hội đồng nào.");
            return "chamdiem";
        }

        if (hoiDongId == null) {
            hoiDongId = phanCongList.get(0).getHoiDongId(); // Lấy hội đồng đầu tiên nếu không truyền vào
        }

        var deTaiHoiDongs = deTaiHoiDongService.findByHoiDongId(hoiDongId);

        List<DeTaiKhoaLuan> deTais = new ArrayList<>();
        for (var dthd : deTaiHoiDongs) {
            var dt = deTaiService.getDeTaiById(dthd.getDeTaiKhoaLuanSinhVienId());
            if (dt != null)
                deTais.add(dt);
        }

        var tieuchiList = tieuChiService.getAll();

        // Map điểm đã cho
        Map<String, Float> diemDaCho = new HashMap<>();
        for (var dthd : deTaiHoiDongs) {
            for (var tc : tieuchiList) {
                var bangDiem = bangDiemService.findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(
                    dthd.getDeTaiKhoaLuanSinhVienId(), giangVienPhanBienId, tc.getTenTieuChi());
                if (bangDiem != null) {
                    diemDaCho.put(dthd.getDeTaiKhoaLuanSinhVienId() + "_" + tc.getTenTieuChi(), bangDiem.getDiem());
                }
            }
        }
        model.addAttribute("diemDaCho", diemDaCho);

        // Truyền biến khóa hội đồng
        boolean isLocked = deTaiHoiDongService.isHoiDongLocked(hoiDongId);
        model.addAttribute("isLocked", isLocked);

        model.addAttribute("deTais", deTais);
        model.addAttribute("tieuchiList", tieuchiList);
        model.addAttribute("giangVienPhanBienId", giangVienPhanBienId);
        model.addAttribute("hoiDongId", hoiDongId);

        return "chamdiem";
    }


    
    @PostMapping("/giangvien/chamdiem/save")
    public String luuDiemCham(@RequestParam Map<String, String> params,
                              @RequestParam("giangVienPhanBienId") int giangVienPhanBienId,
                              @RequestParam("hoiDongId") int hoiDongId,
                              RedirectAttributes redirectAttributes) {

        // Nếu hội đồng đã khóa thì không cho lưu nữa
        if (deTaiHoiDongService.isHoiDongLocked(hoiDongId)) {
            redirectAttributes.addFlashAttribute("message", "Hội đồng đã khóa. Bạn không thể sửa điểm nữa.");
            return "redirect:/giangvien/chamdiem?hoiDongId=" + hoiDongId + "&giangVienPhanBienId=" + giangVienPhanBienId;
        }

        var tieuchis = tieuChiService.getAll();
        var deTaiHoiDongs = deTaiHoiDongService.findByHoiDongId(hoiDongId);

        for (var dthd : deTaiHoiDongs) {
            for (var tc : tieuchis) {
                String paramKey = "diem_" + dthd.getDeTaiKhoaLuanSinhVienId() + "_" + tc.getTenTieuChi();
                if (params.containsKey(paramKey) && !params.get(paramKey).isEmpty()) {
                    float diem = Float.parseFloat(params.get(paramKey));
                    var existingScore = bangDiemService.findByDeTaiSinhVienIdAndGiangVienIdAndTieuChi(
                        dthd.getDeTaiKhoaLuanSinhVienId(), giangVienPhanBienId, tc.getTenTieuChi());

                    if (existingScore != null) {
                        existingScore.setDiem(diem);
                        bangDiemService.update(existingScore); // hoặc save(existingScore);
                    } else {
                        bangDiemService.add(new BangDiem(null, dthd.getDeTaiKhoaLuanSinhVienId(), giangVienPhanBienId, tc.getTenTieuChi(), diem));
                    }
                }
            }
        }

        redirectAttributes.addFlashAttribute("message", "Đã lưu điểm thành công.");

        return "redirect:/giangvien/chamdiem?hoiDongId=" + hoiDongId + "&giangVienPhanBienId=" + giangVienPhanBienId;
    }


}
