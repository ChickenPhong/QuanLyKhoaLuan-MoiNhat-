/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.tqp.dto.BangDiemTongHopDTO;
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.services.BangDiemService;
import com.tqp.services.DeTaiHoiDongService;
import com.tqp.services.DeTaiHuongDanService;
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import com.tqp.services.PdfExportService;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class GiaoVuController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DeTaiService deTaiService;

    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;

    @Autowired
    private DeTaiHuongDanService deTaiGVHuongDanService;
    
    @Autowired
    private HoiDongService hoiDongService;
    
    @Autowired
    private DeTaiHoiDongService deTaiHoiDongService;
    
    @Autowired
    private PhanCongGiangVienPhanBienService phanCongGiangVienPhanBienService;
    
    @Autowired
    private BangDiemService bangDiemService; 
    
    @Autowired
    private com.tqp.services.EmailService emailService;
    
    @Autowired
    private PdfExportService pdfExportService;

    @GetMapping("/khoaluan")
    public String giaoVuView(Model model, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        if ("ROLE_GIAOVU".equals(user.getRole())) {
            model.addAttribute("allDeTai", deTaiService.getByKhoaAndStatus(user.getKhoa(), "active"));
        } else {
            model.addAttribute("allDeTai", deTaiService.getAllDeTai());
        }
        model.addAttribute("khoaLuan", new DeTaiKhoaLuan());
        return "khoaluan";
    }

    @PostMapping("/khoaluan/add")
    public String addDeTai(@ModelAttribute("khoaLuan") DeTaiKhoaLuan khoaLuan, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        if ("ROLE_GIAOVU".equals(user.getRole())) {
            khoaLuan.setKhoa(user.getKhoa());
        }
        deTaiService.addDeTai(khoaLuan);
        return "redirect:/khoaluan";
    }

    @PostMapping("/khoaluan/delete")
    public String deleteDeTai(@RequestParam("id") int id) {
        deTaiService.deleteDeTai(id);
        return "redirect:/khoaluan";
    }

    @GetMapping("/khoaluan/xep_detai")
    public String viewXepDeTai(@RequestParam(value = "khoaHoc", required = false) String khoaHoc, Principal principal, Model model) {
        var user = nguoiDungService.getByUsername(principal.getName());
        if (khoaHoc != null) {
            var svList = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(user.getKhoa(), khoaHoc);
            model.addAttribute("sinhViens", svList);
        }
        // Dynamic list of years
        int currentYear = java.time.Year.now().getValue();
        List<String> years = new java.util.ArrayList<>();
        for (int year = 2020; year <= currentYear; year++) {
            years.add(String.valueOf(year));
        }
        model.addAttribute("yearOptions", years);
        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("khoa", user.getKhoa());

        return "xep_detai";
    }

    @PostMapping("/khoaluan/xep_detai/xep")
    public String xepDanhSach(@RequestParam("khoaHoc") String khoaHoc, Principal principal, RedirectAttributes redirectAttrs) {
        var user = nguoiDungService.getByUsername(principal.getName());
        var svList = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(user.getKhoa(), khoaHoc);
        var deTaiList = deTaiService.getByKhoaAndStatus(user.getKhoa(), "active");
        var giangVienList = nguoiDungService.getGiangVienByKhoa(user.getKhoa());
        
        // kiểm tra đã có sinh viên thuộc khoaHoc được phân đề tài chưa
        boolean daXep = svList.stream().anyMatch(sv -> deTaiSinhVienService.isSinhVienDaXepDeTai(sv.getId()));
        if (daXep) {
            redirectAttrs.addFlashAttribute("message", "Khóa " + khoaHoc + " đã được xếp danh sách trước đó!");
            return "redirect:/khoaluan/xep_detai?khoaHoc=" + khoaHoc;
        }

        //Tiến hành xếp nếu chưa
        for (int i = 0; i < svList.size(); i++) {
            var sv = svList.get(i);
            var dt = deTaiList.get(i % deTaiList.size());
            var gv = giangVienList.get(i % giangVienList.size());
            deTaiSinhVienService.assign(sv.getId(), dt.getId());
            deTaiGVHuongDanService.assign(dt.getId(), gv.getId());
        }

        redirectAttrs.addFlashAttribute("message", "Đã xếp danh sách thành công cho khóa " + khoaHoc);
        return "redirect:/khoaluan";
    }
    
    @GetMapping("/khoaluan/danhsach_thuchien")
    public String danhSachThucHien(@RequestParam(value = "khoaHoc", required = false) String khoaHoc,
                                   Principal principal,
                                   Model model) {
        var user = nguoiDungService.getByUsername(principal.getName());

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
                    deTaiMap.put(sv.getId(), dt != null ? dt.getTitle() : "Chưa có");
                    String tenGVs = gvs.stream()
                        .map(gv -> nguoiDungService.getById(gv.getGiangVienHuongDanId()).getUsername())
                        .collect(Collectors.joining(", "));
                    giangVienMap.put(sv.getId(), tenGVs.isEmpty() ? "Chưa có" : tenGVs);
                }
            }

            model.addAttribute("deTaiMap", deTaiMap);
            model.addAttribute("giangVienMap", giangVienMap);
        }

        model.addAttribute("khoa", user.getKhoa());
        return "danhsach_thuchien";
    }
    
    @PostMapping("/khoaluan/them_gv2")
    public String themGiangVienThuHai(@RequestParam("sinhVienId") int sinhVienId, Model model) {
        var dtsv = deTaiSinhVienService.findBySinhVienId(sinhVienId);
        var dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
        
        
        // Kiểm tra nếu đã có đủ 2 giảng viên rồi thì không thêm nữa
        var currentGVs = deTaiGVHuongDanService.findAllByDeTaiKhoaLuanSinhVienId(dtsv.getId());
        var sv = nguoiDungService.getById(sinhVienId);
        if (currentGVs.size() >= 2) {
            // Có thể hiển thị thông báo nếu muốn
            return "redirect:/khoaluan/danhsach_thuchien?khoaHoc=" + sv.getKhoaHoc(); // fallback nếu chưa có getSinhVien()
        }

        // Lấy ngẫu nhiên hoặc cố định 1 giảng viên khác (giả định)
        List<NguoiDung> giangViens = nguoiDungService.getGiangVienByKhoa(sv.getKhoa());
        for (NguoiDung gv : giangViens) {
            boolean isAlreadyAssigned = currentGVs.stream()
                .anyMatch(item -> item.getGiangVienHuongDanId().equals(gv.getId()));

            if (!isAlreadyAssigned) {
                deTaiGVHuongDanService.assign(dt.getId(), gv.getId());
                break;
            }
        }

        return "redirect:/khoaluan/danhsach_thuchien?khoaHoc=" + sv.getKhoaHoc();
    }
    
    @GetMapping("/khoaluan/giaodetai")
    public String viewGiaoDeTai(@RequestParam(value = "khoaHoc", required = false) String khoaHoc,
                                Model model,
                                Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());

        // Load danh sách năm
        int currentYear = java.time.Year.now().getValue();
        List<String> years = new ArrayList<>();
        for (int y = 2020; y <= currentYear; y++) {
            years.add(String.valueOf(y));
        }
        model.addAttribute("yearOptions", years);
        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("khoa", user.getKhoa());

        // Load danh sách hội đồng
        var hoiDongs = hoiDongService.getAllHoiDong();
        model.addAttribute("hoiDongs", hoiDongs);

        if (khoaHoc != null) {
            // Lọc những đề tài đã được gán cho sinh viên
            var deTais = deTaiService.getByKhoaAndStatus(user.getKhoa(), "active").stream()
                .filter(dt -> {
                    var dtsv = deTaiSinhVienService.findByDeTaiId(dt.getId());
                    return dtsv != null && khoaHoc.equals(nguoiDungService.getById(dtsv.getSinhVienId()).getKhoaHoc());
                })
                .collect(Collectors.toList());
            model.addAttribute("deTais", deTais);

            // Map đề tài -> sinh viên
            Map<Integer, String> svMap = new HashMap<>();
            for (var dt : deTais) {
                var dtsv = deTaiSinhVienService.findByDeTaiId(dt.getId());
                if (dtsv != null) {
                    var sv = nguoiDungService.getById(dtsv.getSinhVienId());
                    svMap.put(dt.getId(), sv.getUsername());
                }
            }

            // Map đề tài -> hội đồng
            Map<Integer, String> hdMap = new HashMap<>();
            for (var dt : deTais) {
                var hdh = deTaiHoiDongService.findByDeTaiId(dt.getId());
                if (hdh != null) {
                    var hd = hoiDongService.getById(hdh.getHoiDongId());
                    hdMap.put(dt.getId(), hd.getName());
                }
            }

            model.addAttribute("svMap", svMap);
            model.addAttribute("hdMap", hdMap);
        }

        return "giaodetai";
    }

    @PostMapping("/khoaluan/giaodetai/assign")
    public String assignDeTaiHoiDong(@RequestParam("deTaiId") int deTaiId,
                                     @RequestParam("hoiDongId") int hoiDongId,
                                     RedirectAttributes redirectAttrs) {
        if (!deTaiHoiDongService.isDeTaiAssigned(deTaiId)) {
            deTaiHoiDongService.assignHoiDong(deTaiId, hoiDongId);
            redirectAttrs.addFlashAttribute("message", "Giao đề tài thành công!");
        } else {
            redirectAttrs.addFlashAttribute("error", "Đề tài đã được giao hội đồng!");
        }

        return "redirect:/khoaluan/giaodetai";
    }
    
    @PostMapping("/khoaluan/giaodetai/giao")
    public String giaoDeTaiNgauNhien(@RequestParam("khoaHoc") String khoaHoc,
                                      Principal principal,
                                      RedirectAttributes redirectAttrs) {
        var user = nguoiDungService.getByUsername(principal.getName());

        // Lấy danh sách đề tài đã có sinh viên thực hiện theo khoa & khóa
        var deTais = deTaiService.getByKhoaAndStatus(user.getKhoa(), "active").stream()
            .filter(dt -> {
                var dtsv = deTaiSinhVienService.findByDeTaiId(dt.getId());
                return dtsv != null && khoaHoc.equals(nguoiDungService.getById(dtsv.getSinhVienId()).getKhoaHoc());
            })
            .collect(Collectors.toList());

        var hoiDongs = hoiDongService.getAllHoiDong();
        int hdIndex = 0;

        for (var dt : deTais) {
            if (deTaiHoiDongService.isDeTaiAssigned(dt.getId()))
                continue;

            boolean assigned = false;
            for (int i = 0; i < hoiDongs.size(); i++) {
                var hd = hoiDongs.get(hdIndex % hoiDongs.size());
                var soLuongDeTai = deTaiHoiDongService.countDeTaiByHoiDongId(hd.getId());

                if (soLuongDeTai < 5) {
                    deTaiHoiDongService.assignHoiDong(dt.getId(), hd.getId());

                    // Gán giảng viên phản biện từ danh sách thành viên hội đồng
                    var thanhVien = hoiDongService.getThanhVienHoiDong(hd.getId());
                    if (!thanhVien.isEmpty()) {
                        NguoiDung randomGv = thanhVien.stream()
                            .filter(tv -> "phan_bien".equals(tv.getRole()))
                            .findFirst()
                            .orElse(null);
                        // Gán đúng ID của người dùng làm giảng viên phản biện
                        if (randomGv != null) {
                            System.out.println("== DEBUG ==");  
                            System.out.println("deTaiId = " + dt.getId());  
                            System.out.println("giangVienId = " + randomGv.getId());  
                            System.out.println("hoiDongId = " + hd.getId());
                            phanCongGiangVienPhanBienService.assignPhanBien(randomGv.getId(), hd.getId());
                        }
                    }

                    assigned = true;
                    hdIndex++;
                    break;
                }
            }

            if (!assigned) {
                redirectAttrs.addFlashAttribute("error", "Không đủ hội đồng để giao tất cả đề tài.");
                break;
            }
        }

        redirectAttrs.addFlashAttribute("message", "Đã giao đề tài ngẫu nhiên cho hội đồng.");
        return "redirect:/khoaluan/giaodetai?khoaHoc=" + khoaHoc;
    }
    
    @GetMapping("/khoaluan/khoa_hoidong")
    public String viewKhoaHoiDong(Model model) {
        var hoiDongs = hoiDongService.getAllHoiDong();
        model.addAttribute("hoiDongs", hoiDongs);

        Map<Integer, Boolean> lockedMap = new HashMap<>();
        for (var hd : hoiDongs) {
            boolean locked = deTaiHoiDongService.isHoiDongLocked(hd.getId());
            lockedMap.put(hd.getId(), locked);
        }
        model.addAttribute("lockedMap", lockedMap);
        return "khoa_hoidong";
    }
    
    @PostMapping("/khoaluan/khoa_hoidong")
    public String khoaHoiDong(@RequestParam("hoiDongId") int hoiDongId, RedirectAttributes redirectAttrs) {
        deTaiHoiDongService.lockAllByHoiDongId(hoiDongId);

        // Sau khi khóa, gửi email cho sinh viên
        // Lấy danh sách đề tài thuộc hội đồng này
        var deTaiHoiDongs = deTaiHoiDongService.findByHoiDongId(hoiDongId);

        for (var dthd : deTaiHoiDongs) {
            int deTaiId = dthd.getDeTaiKhoaLuanSinhVienId();
            // Lấy sinh viên thực hiện đề tài này
            var dtsv = deTaiSinhVienService.findByDeTaiId(deTaiId);
            if (dtsv != null) {
                int sinhVienId = dtsv.getSinhVienId();
                var sinhVien = nguoiDungService.getById(sinhVienId);

                // Tính điểm trung bình hội đồng cho đề tài này
                Double diemTrungBinh = bangDiemService.tinhDiemTrungBinhByDeTaiSinhVienId(dthd.getDeTaiKhoaLuanSinhVienId());

                // Gửi email nếu có địa chỉ email
                if (sinhVien != null && sinhVien.getEmail() != null) {
                    // Log địa chỉ email và thông tin sinh viên
                    System.out.println("Đang gửi mail tới: " + sinhVien.getEmail() +
                                       " | Username: " + sinhVien.getUsername());
                    String subject = "Thông báo điểm trung bình khoá luận";
                    String content = String.format(
                        "Chào %s,\n\n" +
                        "Bạn đã hoàn thành bảo vệ khoá luận. Điểm trung bình chính thức của bạn do hội đồng chấm là: %.2f.\n" +
                        "Vui lòng kiểm tra lại kết quả trên hệ thống.\n\n" +
                        "Trân trọng.",
                        sinhVien.getUsername(), diemTrungBinh != null ? diemTrungBinh : 0.0
                    );
                    emailService.sendEmail(sinhVien.getEmail(), subject, content);
                }
            }
        }

        redirectAttrs.addFlashAttribute("message", "Đã khóa hội đồng thành công và gửi email cho sinh viên!");
        return "redirect:/khoaluan/khoa_hoidong";
    }
    
    // Export từng hội đồng (phải có tên biến!)
    @GetMapping("/khoaluan/xuat-bangdiem-hoidong/{hoiDongId}")
    public void xuatBangDiemTongHop(@PathVariable("hoiDongId") int hoiDongId, HttpServletResponse response, Principal principal) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bangdiem_hoidong_" + hoiDongId + ".pdf");

        List<BangDiemTongHopDTO> bangDiemList = bangDiemService.layBangDiemTongHopTheoHoiDong(hoiDongId);
        var user = nguoiDungService.getByUsername(principal.getName());
        String tenKhoa = user.getKhoa();
        String tenTruong = "Tên Trường của bạn"; // Bạn có thể sửa thành lấy từ config hoặc service
        String khoaHoc = "Khóa học"; 
            pdfExportService.exportBangDiemTongHop(bangDiemList, response.getOutputStream(), tenKhoa, tenTruong, khoaHoc);
        }
    
    @GetMapping("/khoaluan/xuat-bangdiem-tatca")
    public void xuatTatCaBangDiem(HttpServletResponse response, Principal principal) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=bangdiem_tatca.pdf");

            List<HoiDong> hoiDongs = hoiDongService.getAllHoiDong();
            List<BangDiemTongHopDTO> tongHop = new ArrayList<>();
            for (HoiDong hd : hoiDongs) {
                tongHop.addAll(bangDiemService.layBangDiemTongHopTheoHoiDong(hd.getId()));
            }
            var user = nguoiDungService.getByUsername(principal.getName());
            String tenKhoa = user.getKhoa();
            String tenTruong = "Tên Trường của bạn"; // thay thế logic lấy tên trường
            String khoaHoc = "Khóa học"; // logic lấy khóa học phù hợp

            pdfExportService.exportBangDiemTongHop(tongHop, response.getOutputStream(), tenKhoa, tenTruong, khoaHoc);
        } catch (Exception ex) {
            ex.printStackTrace(); // Log ra console IDE để dễ fix bug nếu có lỗi
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
