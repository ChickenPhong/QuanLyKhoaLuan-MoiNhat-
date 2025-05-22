import React, { useState, useEffect } from "react";
import { Button, Table, Form, Alert } from "react-bootstrap";
import { authApis } from "../../config/Apis";

const DanhSachThucHien = () => {
  const [khoaHocList, setKhoaHocList] = useState([]);
  const [selectedKhoaHoc, setSelectedKhoaHoc] = useState("");
  const [sinhVienList, setSinhVienList] = useState([]);
  const [msg, setMsg] = useState("");
  const [khoa, setKhoa] = useState("");

  // Load danh sách khóa học khi component mount
  useEffect(() => {
    const loadKhoaHoc = async () => {
      try {
        const res = await authApis().get("/khoaluan/khoahoc"); // endpoint backend trả danh sách khóa học
        setKhoaHocList(res.data || []);
      } catch (error) {
        setMsg("Lỗi tải danh sách khóa học: " + error.message);
      }
    };
    loadKhoaHoc();
  }, []);

  // Load danh sách sinh viên khi chọn khóa học và nhấn xem danh sách
  const handleXemDanhSach = async (e) => {
    e.preventDefault();
    if (!selectedKhoaHoc) {
      setMsg("Vui lòng chọn khóa học");
      setSinhVienList([]);
      return;
    }

    try {
      setMsg("");
      // Gọi API lấy danh sách sinh viên và dữ liệu liên quan theo khóa học
      const res = await authApis().get(`/khoaluan/danhsach_thuchien?khoaHoc=${selectedKhoaHoc}`);
      setSinhVienList(res.data.sinhViens || []);
      setKhoa(res.data.khoa || "");
    } catch (error) {
      setMsg("Lỗi tải danh sách thực hiện: " + error.message);
      setSinhVienList([]);
    }
  };

  // Thêm giảng viên thứ 2 cho sinh viên
  const handleThemGV2 = async (sinhVienId) => {
    try {
      await authApis().post("/khoaluan/them_gv2", { sinhVienId });
      setMsg("Thêm giảng viên thứ 2 thành công!");
      // Có thể reload lại danh sách sau khi thêm
      handleXemDanhSach(new Event('submit'));
    } catch (error) {
      setMsg("Thêm giảng viên thứ 2 thất bại: " + error.message);
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="text-center text-info mb-4">Danh sách sinh viên đã được xếp đề tài</h2>

      {msg && <Alert variant="warning">{msg}</Alert>}

      <Form className="row mb-4" onSubmit={handleXemDanhSach}>
        <div className="col-md-4">
          <Form.Label>Chọn khóa</Form.Label>
          <Form.Select
            value={selectedKhoaHoc}
            onChange={(e) => setSelectedKhoaHoc(e.target.value)}
            required
          >
            <option value="">-- Chọn khóa học --</option>
            {khoaHocList.map((khoa) => (
              <option key={khoa} value={khoa}>
                Khóa {khoa}
              </option>
            ))}
          </Form.Select>
        </div>
        <div className="col-md-2 align-self-end">
          <Button className="w-100" type="submit">Xem danh sách</Button>
        </div>
      </Form>

      {sinhVienList.length > 0 && (
        <>
          <h5>Khóa {selectedKhoaHoc} - Khoa {khoa}</h5>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>#</th>
                <th>Tên đăng nhập</th>
                <th>Email</th>
                <th>Đề tài</th>
                <th>GV Hướng dẫn</th>
                <th>Hành động</th>
              </tr>
            </thead>
            <tbody>
              {sinhVienList.map((sv, idx) => (
                <tr key={sv.id || idx}>
                  <td>{idx + 1}</td>
                  <td>{sv.username}</td>
                  <td>{sv.email}</td>
                  <td>{sv.deTai || ""}</td>
                  <td>{sv.giangVienHuongDan || ""}</td>
                  <td>
                    <Button
                      size="sm"
                      variant="outline-primary"
                      onClick={() => handleThemGV2(sv.id)}
                    >
                      Thêm GV thứ 2
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </>
      )}
    </div>
  );
};

export default DanhSachThucHien;
