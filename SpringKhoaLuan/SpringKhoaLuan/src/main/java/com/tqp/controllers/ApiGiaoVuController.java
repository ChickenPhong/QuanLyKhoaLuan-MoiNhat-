/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tqp.controllers;

/**
 *
 * @author Tran Quoc Phong
 */
import com.itextpdf.text.pdf.PdfWriter;
import com.tqp.dto.BangDiemTongHopDTO;
import com.tqp.pojo.BangDiem;
import com.tqp.pojo.DeTaiKhoaLuan;
import com.tqp.pojo.DeTaiKhoaLuan_HoiDong;
import com.tqp.pojo.DeTaiKhoaLuan_SinhVien;
import com.tqp.pojo.HoiDong;
import com.tqp.pojo.NguoiDung;
import com.tqp.services.BangDiemService;
import com.tqp.services.DeTaiHoiDongService;
import com.tqp.services.DeTaiService;
import com.tqp.services.DeTaiSinhVienService;
import com.tqp.services.DeTaiHuongDanService;
import com.tqp.services.EmailService;
import com.tqp.services.HoiDongService;
import com.tqp.services.NguoiDungService;
import com.tqp.services.PdfExportService;
import com.tqp.services.PhanCongGiangVienPhanBienService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/giaovu")
public class ApiGiaoVuController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DeTaiService deTaiService;

    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;

    @Autowired
    private DeTaiHuongDanService deTaiGVHuongDanService;

    @Autowired
    private DeTaiHoiDongService deTaiHoiDongService;

    @Autowired
    private HoiDongService hoiDongService;

    @Autowired
    private BangDiemService bangDiemService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PdfExportService pdfExportService;

    @Autowired
    private PhanCongGiangVienPhanBienService phanCongGiangVienPhanBienService;

    @GetMapping("/khoahoc")
    public ResponseEntity<?> getKhoaHocList(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }
        var user = nguoiDungService.getByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }
        String khoa = user.getKhoa();
        if (khoa == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Người dùng chưa khai báo khoa");
        }
        List<String> khoaHocList = nguoiDungService.getAllKhoaHocByKhoa(khoa);
        if (khoaHocList == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không tìm thấy khóa học nào");
        }
        // Lọc bỏ null
        khoaHocList = khoaHocList.stream().filter(x -> x != null).collect(Collectors.toList());
        return ResponseEntity.ok(khoaHocList);
    }
    
    @GetMapping("/sinhvien_by_khoahoc")
    public ResponseEntity<?> getSinhVienByKhoaHoc(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // Lấy toàn bộ sinh viên theo khoa và khóa học
        List<NguoiDung> svList = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        List<Map<String, Object>> result = new ArrayList<>();

        for (var sv : svList) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", sv.getId());
            item.put("fullname", sv.getFullname());
            item.put("username", sv.getUsername());
            item.put("email", sv.getEmail());

            // Kiểm tra đã được xếp đề tài hay chưa
            var dtsv = deTaiSinhVienService.findBySinhVienId(sv.getId());
            if (dtsv != null) {
                var dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
                item.put("deTai", dt != null ? dt.getTitle() : "Chưa có");
            } else {
                item.put("deTai", "Chưa có");
            }
            result.add(item);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("sinhViens", result);
        res.put("khoa", khoa);
        return ResponseEntity.ok(res);
    }

    // 2. Xếp đề tài cho sinh viên khóa học (theo khoa của người dùng đăng nhập)
    @PostMapping("/xepdetai")
    public ResponseEntity<?> xepDeTaiChoSinhVien(
            @RequestParam("khoaHoc") String khoaHoc,
            Principal principal) {
        
        System.out.println("PRINCIPAL = " + principal);

        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        List<NguoiDung> svList = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        List<DeTaiKhoaLuan> deTaiList = deTaiService.getByKhoaAndStatus(khoa, "active");
        List<NguoiDung> giangVienList = nguoiDungService.getGiangVienByKhoa(khoa);

        // Kiểm tra sinh viên đã được xếp đề tài chưa
        boolean daXep = svList.stream()
                .anyMatch(sv -> deTaiSinhVienService.isSinhVienDaXepDeTai(sv.getId()));

        if (daXep) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Khóa " + khoaHoc + " đã được xếp danh sách trước đó!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }
        
        // BỔ SUNG KIỂM TRA:
        if (deTaiList.isEmpty()) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Không có đề tài nào thuộc khoa này đang hoạt động!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
        if (giangVienList.isEmpty()) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Không có giảng viên nào thuộc khoa này!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        // Thực hiện xếp đề tài
        for (int i = 0; i < svList.size(); i++) {
            var sv = svList.get(i);
            var dt = deTaiList.get(i % deTaiList.size());

            deTaiSinhVienService.assign(sv.getId(), dt.getId());

        }

        Map<String, String> res = new HashMap<>();
        res.put("message", "Đã xếp danh sách thành công cho khóa " + khoaHoc);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/giaodetai")
    public ResponseEntity<?> getGiaoDeTai(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // Lấy danh sách detaikhoaluan_sinhvien ứng với khoa và khóa học này
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.getByKhoaVaKhoaHoc(khoa, khoaHoc);

        // Lấy danh sách đề tài
        List<DeTaiKhoaLuan> deTais = dtsvList.stream()
                .map(dtsv -> deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId()))
                .collect(Collectors.toList());

        // Map đề tài id -> username sinh viên
        Map<Integer, String> svMap = new HashMap<>();
        for (var dtsv : dtsvList) {
            var sv = nguoiDungService.getById(dtsv.getSinhVienId());
            svMap.put(dtsv.getDeTaiKhoaLuanId(), sv.getFullname());
        }

        // Map đề tài id -> tên hội đồng (nếu đã giao)
        Map<Integer, String> hdMap = new HashMap<>();
        for (var dtsv : dtsvList) {
            var dthd = deTaiHoiDongService.findByDtsvId(dtsv.getId());
            if (dthd != null) {
                var hd = hoiDongService.getById(dthd.getHoiDongId());
                hdMap.put(dtsv.getDeTaiKhoaLuanId(), hd.getName());
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("deTais", deTais);
        res.put("svMap", svMap);
        res.put("hdMap", hdMap);

        return ResponseEntity.ok(res);
    }

    @PostMapping("/giaodetai/giao")
    public ResponseEntity<?> giaoDeTaiNgauNhien(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        System.out.println("GIAODETAI: principal = " + principal);
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // Lấy danh sách detaikhoaluan_sinhvien ứng với khóa này
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.getByKhoaVaKhoaHoc(khoa, khoaHoc);

        // Kiểm tra nếu tất cả dtsvId này đã có mặt trong detaikhoaluan_hoidong thì báo đã giao
        boolean daGiao = dtsvList.stream()
                .allMatch(dtsv -> deTaiHoiDongService.isDeTaiAssigned(dtsv.getId()));

        if (daGiao) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Khóa " + khoaHoc + " đã được giao đề tài cho hội đồng trước đó!");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        // Lấy danh sách hội đồng
        List<HoiDong> hoiDongs = hoiDongService.getHoiDongByKhoa(khoa);
        if (hoiDongs.isEmpty()) {
            Map<String, String> res = new HashMap<>();
            res.put("error", "Chưa có hội đồng nào để giao đề tài");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }

        // Tính tổng slot tối đa có thể giao (mỗi hội đồng tối đa 5 đề tài)
        int maxSlot = hoiDongs.size() * 5;

        // Đếm số đề tài chưa được giao
        long soDeTaiChuaGiao = dtsvList.stream()
                .filter(dtsv -> !deTaiHoiDongService.isDeTaiAssigned(dtsv.getId()))
                .count();

        // Nếu số đề tài vượt quá tổng slot, trả lỗi
        if (soDeTaiChuaGiao > maxSlot) {
            Map<String, String> res = new HashMap<>();
            res.put("error", String.format(
                "Số đề tài chưa được giao (%d) vượt quá tối đa (%d) khóa luận mà một hội đồng cần chấm. Vui lòng tăng số hội đồng lên để giao khóa luận.",
                soDeTaiChuaGiao, maxSlot));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        // Đếm số đề tài đã giao cho mỗi hội đồng hiện tại (để tránh vượt quá 5)
        Map<Integer, Integer> hoiDongCount = new HashMap<>();
        for (HoiDong hd : hoiDongs) {
            hoiDongCount.put(hd.getId(), 0);
        }

        List<DeTaiKhoaLuan_SinhVien> deTaiChuaGiaoList = dtsvList.stream()
                .filter(dtsv -> !deTaiHoiDongService.isDeTaiAssigned(dtsv.getId()))
                .collect(Collectors.toList());

        // Cập nhật số đề tài đã giao cho từng hội đồng hiện tại
        List<DeTaiKhoaLuan_HoiDong> dthds = deTaiHoiDongService.findByDtsvIds(
                dtsvList.stream().map(DeTaiKhoaLuan_SinhVien::getId).collect(Collectors.toList())
        );
        for (DeTaiKhoaLuan_HoiDong dthd : dthds) {
            hoiDongCount.put(dthd.getHoiDongId(), hoiDongCount.getOrDefault(dthd.getHoiDongId(), 0) + 1);
        }

        // Bắt đầu giao đề tài cho hội đồng, đảm bảo không vượt quá 5 đề tài mỗi hội đồng
        int hdIndex = 0;
        for (var dtsv : deTaiChuaGiaoList) {
            boolean assigned = false;
            int loopCount = 0;
            while (!assigned && loopCount < hoiDongs.size()) {
                HoiDong hd = hoiDongs.get(hdIndex % hoiDongs.size());
                int currentCount = hoiDongCount.getOrDefault(hd.getId(), 0);
                if (currentCount < 5) {
                    deTaiHoiDongService.assignHoiDong(dtsv.getId(), hd.getId());
                    hoiDongCount.put(hd.getId(), currentCount + 1);
                    assigned = true;
                }
                hdIndex++;
                loopCount++;
            }
            if (!assigned) {
                // Trường hợp không thể giao đề tài (dù đã kiểm tra ở trên, đây là phòng ngừa)
                Map<String, String> res = new HashMap<>();
                res.put("error", "Không thể giao đề tài vì giới hạn số lượng đề tài cho hội đồng đã đạt tối đa.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
            }
        }

        Map<String, String> res = new HashMap<>();
        res.put("message", "Đã giao đề tài ngẫu nhiên cho hội đồng thành công!");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/danhsach_thuchien")
    public ResponseEntity<?> getDanhSachThucHien(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // ✅ Lấy các bản ghi đã gán sinh viên - đề tài theo đúng khoa và khóa học
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.getByKhoaVaKhoaHoc(khoa, khoaHoc);
        List<Map<String, Object>> result = new ArrayList<>();

        for (var dtsv : dtsvList) {
            Map<String, Object> item = new HashMap<>();

            var sv = nguoiDungService.getById(dtsv.getSinhVienId());
            item.put("id", sv.getId());
            item.put("fullname", sv.getFullname());
            item.put("username", sv.getUsername());
            item.put("email", sv.getEmail());

            var dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
            item.put("deTai", dt != null ? dt.getTitle() : "Chưa có");

            var gvs = deTaiGVHuongDanService.findAllByDeTaiKhoaLuanSinhVienId(dtsv.getId());
            String tenGVs = gvs.stream()
                    .map(gv -> nguoiDungService.getById(gv.getGiangVienHuongDanId()).getFullname())
                    .collect(Collectors.joining(", "));
            item.put("giangVienHuongDan", tenGVs.isEmpty() ? "Chưa có" : tenGVs);

            result.add(item);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("sinhViens", result);
        res.put("khoa", khoa);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/them_gv_1toanbo")
    public ResponseEntity<?> themGiangVien1ToanBo(@RequestBody Map<String, String> body, Principal principal) {
        String khoaHoc = body.get("khoaHoc");
        if (khoaHoc == null) {
            return ResponseEntity.badRequest().body("Thiếu khóa học");
        }

        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();
        var sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        var giangViens = nguoiDungService.getGiangVienByKhoa(khoa);

        java.util.Random rand = new java.util.Random();
        int count = 0;

        for (var sv : sinhViens) {
            var dtsv = deTaiSinhVienService.findBySinhVienId(sv.getId());
            if (dtsv == null) {
                continue;
            }
            var currentGVs = deTaiGVHuongDanService.findAllByDeTaiKhoaLuanSinhVienId(dtsv.getId());
            if (currentGVs.size() == 0 && !giangViens.isEmpty()) {
                // Random GV
                var gv = giangViens.get(rand.nextInt(giangViens.size()));
                deTaiGVHuongDanService.assign(dtsv.getId(), gv.getId());
                count++;
            }
        }
        return ResponseEntity.ok("Đã thêm giảng viên hướng dẫn cho " + count + " sinh viên.");
    }

    @GetMapping("/danhsach_giangvien")
    public ResponseEntity<?> getDanhSachGiangVien(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập");
        }

        var user = nguoiDungService.getByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng");
        }

        String khoa = user.getKhoa();
        if (khoa == null) {
            return ResponseEntity.badRequest().body("Người dùng chưa khai báo khoa");
        }

        var giangVienList = nguoiDungService.getGiangVienByKhoa(khoa);
        return ResponseEntity.ok(giangVienList);
    }

    @PostMapping("/them_gv2")
    public ResponseEntity<?> themGiangVienThuHai(@RequestBody Map<String, Integer> body) {
        Integer sinhVienId = body.get("sinhVienId");
        Integer giaoVienId = body.get("giaoVienId");

        if (sinhVienId == null) {
            return ResponseEntity.badRequest().body("Thiếu sinhVienId");
        }
        if (giaoVienId == null) {
            return ResponseEntity.badRequest().body("Thiếu giaoVienId");
        }

        var dtsv = deTaiSinhVienService.findBySinhVienId(sinhVienId);
        if (dtsv == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sinh viên chưa được xếp đề tài");
        }

        var currentGVs = deTaiGVHuongDanService.findAllByDeTaiKhoaLuanSinhVienId(dtsv.getId());
        if (currentGVs.size() >= 2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Đề tài đã có đủ 2 giảng viên hướng dẫn");
        }

        // Kiểm tra GV đã được gán chưa
        boolean daGiao = currentGVs.stream()
                .anyMatch(gv -> gv.getGiangVienHuongDanId().equals(giaoVienId));
        if (daGiao) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Giảng viên này đã được gán cho đề tài");
        }

        // Gán giảng viên thứ 2 theo giaoVienId do frontend gửi
        deTaiGVHuongDanService.assign(dtsv.getId(), giaoVienId);

        return ResponseEntity.ok("Đã thêm giảng viên thứ 2 thành công");
    }

    @GetMapping("/hoidong_by_khoahoc")
    public ResponseEntity<?> getHoiDongByKhoaHoc(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        // Xác định khoa dựa vào user hiện tại
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoaUser = user.getKhoa();
        // 1. Lấy danh sách sinh viên id thuộc khóa này
        List<NguoiDung> sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoaUser, khoaHoc);
        List<Integer> sinhVienIds = sinhViens.stream().map(NguoiDung::getId).collect(Collectors.toList());

        // 2. Lấy các id detaikhoaluan_sinhvien thuộc sinh viên trên
        List<DeTaiKhoaLuan_SinhVien> dtsvs = deTaiSinhVienService.findBySinhVienIds(sinhVienIds);
        List<Integer> dtsvIds = dtsvs.stream().map(DeTaiKhoaLuan_SinhVien::getId).collect(Collectors.toList());

        // 3. Lấy các detaikhoaluan_hoidong theo dtsvIds
        List<DeTaiKhoaLuan_HoiDong> dthds = deTaiHoiDongService.findByDtsvIds(dtsvIds);

        // 4. Lấy danh sách hội đồng (unique) + trạng thái locked
        Map<Integer, Boolean> lockedMap = new HashMap<>();
        Map<Integer, HoiDong> hoiDongMap = new HashMap<>();
        for (var dthd : dthds) {
            HoiDong hd = hoiDongService.getById(dthd.getHoiDongId());
            hoiDongMap.put(hd.getId(), hd);
            if (dthd.getLocked()) {
                lockedMap.put(hd.getId(), true);
            } else {
                lockedMap.putIfAbsent(hd.getId(), false);
            }
        }

        List<HoiDong> hoiDongs = new ArrayList<>(hoiDongMap.values());

        Map<String, Object> res = new HashMap<>();
        res.put("hoiDongs", hoiDongs);
        res.put("lockedMap", lockedMap);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/khoa_hoidong")
    public ResponseEntity<?> khoaHoiDong(@RequestBody Map<String, Object> body, Principal principal) {
        Integer hdId = (Integer) body.get("hoiDongId");
        String khoaHoc = (String) body.get("khoaHoc");

        if (hdId == null || khoaHoc == null || khoaHoc.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Thiếu thông tin hội đồng hoặc khóa học.");
        }

        var user = nguoiDungService.getByUsername(principal.getName());
        String khoaUser = user.getKhoa();

        if (khoaUser == null) {
            return ResponseEntity.badRequest().body("Tài khoản chưa có thông tin khoa.");
        }

        List<NguoiDung> sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoaUser, khoaHoc);
        if (sinhViens.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy sinh viên nào thuộc khóa này.");
        }

        List<Integer> sinhVienIds = sinhViens.stream().map(NguoiDung::getId).collect(Collectors.toList());
        List<DeTaiKhoaLuan_SinhVien> dtsvs = deTaiSinhVienService.findBySinhVienIds(sinhVienIds);
        if (dtsvs.isEmpty()) {
            return ResponseEntity.ok("Không có đề tài nào để khóa cho hội đồng.");
        }

        List<Integer> dtsvIds = dtsvs.stream().map(DeTaiKhoaLuan_SinhVien::getId).collect(Collectors.toList());
        int count = deTaiHoiDongService.lockAllByHoiDongIdAndDtsvIds(hdId, dtsvIds);

        // Sau khi khóa, gửi email thông báo điểm cho từng sinh viên
        for (Integer dtsvId : dtsvIds) {
            DeTaiKhoaLuan_SinhVien dtsv = deTaiSinhVienService.getById(dtsvId);
            NguoiDung sv = nguoiDungService.getById(dtsv.getSinhVienId());
            if (sv == null || sv.getEmail() == null || sv.getEmail().isEmpty()) {
                continue;
            }

            // Lấy danh sách bảng điểm của sinh viên này
            List<BangDiem> diemList = bangDiemService.findByDeTaiSinhVienId(dtsvId);
            if (diemList == null || diemList.isEmpty()) {
                continue;
            }

            // Tính điểm trung bình
            double avg = diemList.stream()
                    .mapToDouble(BangDiem::getDiem)
                    .average()
                    .orElse(0);

            String content = String.format(
                    "Chào bạn %s,\n\nHội đồng đã khóa điểm. Điểm trung bình cuối cùng của bạn là: %.2f.\n\nTrân trọng.",
                    sv.getFullname(), avg
            );
            emailService.sendEmail(sv.getEmail(), "Thông báo điểm trung bình hội đồng", content);
        }

        return ResponseEntity.ok("Đã khóa " + count + " đề tài của hội đồng " + hdId);
    }

    @GetMapping("/thongke_khoahoc")
    public ResponseEntity<?> thongKeKhoaHoc(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        // Lấy danh sách sinh viên và đề tài-sinh viên theo khoa + khóa học
        List<NguoiDung> sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        int soSinhVien = sinhViens.size();

        List<Integer> sinhVienIds = sinhViens.stream().map(NguoiDung::getId).collect(Collectors.toList());
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.findBySinhVienIds(sinhVienIds);
        int soDeTai = dtsvList.size();

        // Tính điểm trung bình cho từng sinh viên
        double diemTrungBinh = 0;
        int countSvCoDiem = 0;
        for (DeTaiKhoaLuan_SinhVien dtsv : dtsvList) {
            List<BangDiem> diemList = bangDiemService.findByDeTaiSinhVienId(dtsv.getId());
            if (diemList != null && !diemList.isEmpty()) {
                double diemTB = diemList.stream().mapToDouble(BangDiem::getDiem).average().orElse(0);
                diemTrungBinh += diemTB;
                countSvCoDiem++;
            }
        }
        diemTrungBinh = countSvCoDiem > 0 ? diemTrungBinh / countSvCoDiem : 0;
        Map<String, Object> result = new HashMap<>();
        result.put("khoaHoc", khoaHoc);
        result.put("soSinhVien", soSinhVien);
        result.put("soDeTai", soDeTai);
        result.put("diemTrungBinh", Math.round(diemTrungBinh * 100.0) / 100.0);

        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/nganh")
    public ResponseEntity<?> getNganhList(@RequestParam("khoaHoc") String khoaHoc, Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();
        List<String> nganhList = nguoiDungService.getAllNganhByKhoaVaKhoaHoc(khoa, khoaHoc); // Bạn phải implement hàm này
        if (nganhList == null) return ResponseEntity.ok(List.of());
        nganhList = nganhList.stream().filter(x -> x != null && !x.trim().isEmpty()).distinct().collect(Collectors.toList());
        return ResponseEntity.ok(nganhList);
    }

    @GetMapping("/thongke_sinhvien")
    public ResponseEntity<?> thongKeSinhVienTheoKhoaHoc(
            @RequestParam("khoaHoc") String khoaHoc,
            @RequestParam(value = "nganh", required = false) String nganh,
            Principal principal) {
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

         // Lọc theo khoa + khoá học + ngành (nếu có)
        List<NguoiDung> sinhViens;
        if (nganh == null || nganh.isEmpty()) {
            sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        } else {
            sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHocVaNganh(khoa, khoaHoc, nganh);
        }
        int tongSinhVien = sinhViens.size();
        
        List<Integer> sinhVienIds = sinhViens.stream().map(NguoiDung::getId).collect(Collectors.toList());
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.findBySinhVienIds(sinhVienIds);

        int soSinhVienThamGia = dtsvList.size();

        List<Map<String, Object>> result = new ArrayList<>();
        for (DeTaiKhoaLuan_SinhVien dtsv : dtsvList) {
            NguoiDung sv = nguoiDungService.getById(dtsv.getSinhVienId());
            DeTaiKhoaLuan dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
            DeTaiKhoaLuan_HoiDong dthd = deTaiHoiDongService.findByDtsvId(dtsv.getId());
            HoiDong hd = dthd != null ? hoiDongService.getById(dthd.getHoiDongId()) : null;

            List<BangDiem> diemList = bangDiemService.findByDeTaiSinhVienId(dtsv.getId());
            Double diemTB = (diemList != null && !diemList.isEmpty())
                    ? diemList.stream().mapToDouble(BangDiem::getDiem).average().orElse(0.0) : null;

            Map<String, Object> map = new HashMap<>();
            map.put("tenSinhVien", sv != null ? sv.getFullname() : "");
            map.put("tenDeTai", dt != null ? dt.getTitle() : "");
            map.put("tenHoiDong", hd != null ? hd.getName() : "");
            map.put("diemTrungBinh", diemTB);

            result.add(map);
        }

        // Trả về thêm tổng số sinh viên và số SV tham gia khóa luận
        Map<String, Object> res = new HashMap<>();
        res.put("tongSinhVien", tongSinhVien);
        res.put("soSinhVienThamGia", soSinhVienThamGia);
        res.put("dsSinhVien", result);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/xuat_pdf_diem")
    public void xuatPdfDiem(@RequestBody Map<String, String> body, HttpServletResponse response, Principal principal) throws Exception {
        String khoaHoc = body.get("khoaHoc");
        var user = nguoiDungService.getByUsername(principal.getName());
        String khoa = user.getKhoa();

        List<NguoiDung> sinhViens = nguoiDungService.getSinhVienByKhoaVaKhoaHoc(khoa, khoaHoc);
        List<Integer> sinhVienIds = sinhViens.stream().map(NguoiDung::getId).collect(Collectors.toList());
        List<DeTaiKhoaLuan_SinhVien> dtsvList = deTaiSinhVienService.findBySinhVienIds(sinhVienIds);

        List<BangDiemTongHopDTO> bangDiemTongHopList = new ArrayList<>();
        for (DeTaiKhoaLuan_SinhVien dtsv : dtsvList) {
            NguoiDung sv = nguoiDungService.getById(dtsv.getSinhVienId());
            DeTaiKhoaLuan dt = deTaiService.getDeTaiById(dtsv.getDeTaiKhoaLuanId());
            DeTaiKhoaLuan_HoiDong dthd = deTaiHoiDongService.findByDtsvId(dtsv.getId());
            HoiDong hd = dthd != null ? hoiDongService.getById(dthd.getHoiDongId()) : null;

            String tenGVPhanBien = "";
            if (hd != null) {
                // Lấy danh sách giảng viên phản biện từ bảng phanconggiangvienphanbiens
                var dsGVPhanBien = phanCongGiangVienPhanBienService.findByHoiDongId(hd.getId());
                if (dsGVPhanBien != null && !dsGVPhanBien.isEmpty()) {
                    List<String> tenGVs = new ArrayList<>();
                    for (var p : dsGVPhanBien) {
                        NguoiDung gv = nguoiDungService.getById(p.getGiangVienPhanBienId());
                        if (gv != null) {
                            tenGVs.add(gv.getFullname());
                        }
                    }
                    tenGVPhanBien = String.join(", ", tenGVs);
                }
            }
            List<BangDiem> diemList = bangDiemService.findByDeTaiSinhVienId(dtsv.getId());
            Double diemTB = (diemList != null && !diemList.isEmpty())
                    ? diemList.stream().mapToDouble(BangDiem::getDiem).average().orElse(0.0) : null;

            bangDiemTongHopList.add(new BangDiemTongHopDTO(
                    hd != null ? hd.getName() : "",
                    tenGVPhanBien,
                    dt != null ? dt.getTitle() : "",
                    sv != null ? sv.getFullname() : "",
                    diemTB
            ));
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=bang_diem_khoahoc.pdf");
        
        String tenKhoa = khoa;
        String tenTruong = "Tên Trường Của Bạn";
        pdfExportService.exportBangDiemTongHop(bangDiemTongHopList, response.getOutputStream(), tenKhoa, tenTruong, khoaHoc);
    }

}
