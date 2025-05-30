
package com.tqp.controllers;

import com.tqp.pojo.*;
import com.tqp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DeTaiSinhVienService deTaiSinhVienService;

    @Autowired
    private DeTaiService deTaiService;

    @Autowired
    private DeTaiHoiDongService deTaiHoiDongService;

    @Autowired
    private HoiDongService hoiDongService;

    @Autowired
    private BangDiemService bangDiemService;

    // Trang admin danh sách người dùng
    @GetMapping("")
    public String adminView(Model model) {
        List<NguoiDung> users = nguoiDungService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";  // admin.html
    }

    // Trang thống kê điểm khóa luận
    @GetMapping("/thongke_khoaluan")
    public String thongKeKhoaLuanPage(
            @RequestParam(value = "khoa", required = false) String khoa,
            @RequestParam(value = "khoaHoc", required = false) String khoaHoc,
            @RequestParam(value = "nganh", required = false) String nganh,
            Model model) {

        // Lấy danh sách khoa (load đầy đủ từ đầu)
        List<String> allKhoa = nguoiDungService.getAllKhoa()
            .stream().filter(x -> x != null && !x.isEmpty()).distinct().collect(Collectors.toList());

        // Nếu chưa có khoa, chọn mặc định khoa đầu tiên
        if ((khoa == null || khoa.isEmpty()) && !allKhoa.isEmpty())
            khoa = allKhoa.get(0);

        List<String> allKhoaHoc = (khoa != null && !khoa.isEmpty())
                ? nguoiDungService.getAllKhoaHocByKhoa(khoa).stream()
                    .filter(x -> x != null && !x.isEmpty()).distinct().collect(Collectors.toList())
                : new ArrayList<>();

        // Nếu chưa có khóa học, chọn mặc định khóa học đầu tiên
        if ((khoaHoc == null || khoaHoc.isEmpty()) && !allKhoaHoc.isEmpty())
            khoaHoc = allKhoaHoc.get(0);

        List<String> allNganh = (khoa != null && !khoa.isEmpty() && khoaHoc != null && !khoaHoc.isEmpty())
                ? nguoiDungService.getAllNganhByKhoaVaKhoaHoc(khoa, khoaHoc).stream()
                    .filter(x -> x != null && !x.isEmpty()).distinct().collect(Collectors.toList())
                : new ArrayList<>();

        model.addAttribute("allKhoa", allKhoa);
        model.addAttribute("allKhoaHoc", allKhoaHoc);
        model.addAttribute("allNganh", allNganh);
        model.addAttribute("khoa", khoa);
        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("nganh", nganh);

        // Nếu chưa chọn đủ thông tin khoa và khóa học thì trả về trang trống
        if (khoa == null || khoa.isEmpty() || khoaHoc == null || khoaHoc.isEmpty()) {
            model.addAttribute("thongKeList", new ArrayList<>());
            model.addAttribute("tongSinhVien", 0);
            model.addAttribute("soSinhVienThamGia", 0);
            return "thongke";  // tên file html thống kê
        }

        // Lấy danh sách sinh viên theo bộ lọc có xét nganh nếu có
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

        // Đếm số đề tài khác nhau
        int soLuongDeTai = dtsvList.stream()
                .map(DeTaiKhoaLuan_SinhVien::getDeTaiKhoaLuanId)
                .collect(Collectors.toSet()).size();

        // Tính điểm trung bình chung
        double diemTong = 0;
        int countDiem = 0;
        for (DeTaiKhoaLuan_SinhVien dtsv : dtsvList) {
            List<BangDiem> diemList = bangDiemService.findByDeTaiSinhVienId(dtsv.getId());
            if (diemList != null && !diemList.isEmpty()) {
                double avg = diemList.stream().mapToDouble(BangDiem::getDiem).average().orElse(0);
                diemTong += avg;
                countDiem++;
            }
        }
        double diemTrungBinh = (countDiem > 0) ? diemTong / countDiem : 0;

        List<Map<String, Object>> thongKeList = new ArrayList<>();
        Map<String, Object> tk = new HashMap<>();
        tk.put("khoaHoc", khoaHoc);
        tk.put("khoa", khoa);
        tk.put("soLuongSinhVien", tongSinhVien);
        tk.put("soLuongDeTai", soLuongDeTai);
        tk.put("diemTrungBinh", diemTrungBinh);
        thongKeList.add(tk);

        model.addAttribute("thongKeList", thongKeList);
        model.addAttribute("tongSinhVien", tongSinhVien);
        model.addAttribute("soSinhVienThamGia", soSinhVienThamGia);

        return "thongke"; // trả về view thống kê
    }

    // API trả JSON lấy danh sách khoa
    @GetMapping("/khoa")
    @ResponseBody
    public List<String> getAllKhoa() {
        List<String> dsKhoa = nguoiDungService.getAllKhoa();
        return dsKhoa.stream().filter(x -> x != null && !x.isEmpty()).distinct().collect(Collectors.toList());
    }

    // API trả JSON lấy danh sách khóa học theo khoa
    @GetMapping("/khoahoc")
    @ResponseBody
    public List<String> getKhoaHocByKhoa(@RequestParam("khoa") String khoa) {
        List<String> dsKhoaHoc = nguoiDungService.getAllKhoaHocByKhoa(khoa);
        return dsKhoaHoc.stream().filter(x -> x != null && !x.isEmpty()).distinct().collect(Collectors.toList());
    }

    // API trả JSON lấy danh sách ngành theo khoa và khóa học
    @GetMapping("/nganh")
    @ResponseBody
    public List<String> getAllNganhByKhoaVaKhoaHoc(@RequestParam("khoa") String khoa,
            @RequestParam("khoaHoc") String khoaHoc) {
        List<String> nganhList = nguoiDungService.getAllNganhByKhoaVaKhoaHoc(khoa, khoaHoc);
        if (nganhList == null) {
            return List.of();
        }
        return nganhList.stream().filter(x -> x != null && !x.trim().isEmpty()).distinct().collect(Collectors.toList());
    }

    // Form thêm người dùng
    @GetMapping("/add-user")
    public String addUserForm() {
        return "add-user"; // add-user.html
    }

    // Xử lý thêm người dùng
    @PostMapping("/add-user")
    public String addUser(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            RedirectAttributes redirectAttrs) {

        nguoiDungService.addUser(params, avatar);
        redirectAttrs.addFlashAttribute("message", "Thêm người dùng thành công!");
        return "redirect:/admin";
    }

    // Xóa người dùng
    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") int id, RedirectAttributes redirectAttrs) {
        boolean result = nguoiDungService.deleteUser(id);
        redirectAttrs.addFlashAttribute("message", result ? "Xóa người dùng thành công!" : "Xóa người dùng thất bại!");
        return "redirect:/admin";
    }
}