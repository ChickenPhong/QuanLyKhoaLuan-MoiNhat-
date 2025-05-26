import React, { useState, useEffect } from "react";
import { Button, Table, Form, Alert } from "react-bootstrap";
import { authApis } from "../../config/Apis";

const DanhSachThucHien = () => {
  const [khoaHocList, setKhoaHocList] = useState([]);
  const [selectedKhoaHoc, setSelectedKhoaHoc] = useState("");
  const [sinhVienList, setSinhVienList] = useState([]);
  const [khoa, setKhoa] = useState("");
  const [msg, setMsg] = useState("");

  // Load danh sách khóa học từ backend
  useEffect(() => {
    const loadKhoaHoc = async () => {
      try {
        const res = await authApis().get("/giaovu/khoahoc");
        setKhoaHocList(res.data || []);
      } catch (error) {
        setMsg("Lỗi tải danh sách khóa học: " + error.message);
      }
    };
    loadKhoaHoc();
  }, []);

  // Gọi API lấy danh sách sinh viên + đề tài + GV
  const handleXemDanhSach = async (e) => {
    e.preventDefault();
    if (!selectedKhoaHoc) {
      setMsg("Vui lòng chọn khóa học");
      setSinhVienList([]);
      return;
    }

    try {
      setMsg("");
      const res = await authApis().get(`/giaovu/danhsach_thuchien?khoaHoc=${selectedKhoaHoc}`);
      setSinhVienList(res.data.sinhViens || []);
      setKhoa(res.data.khoa || "");
    } catch (error) {
      setMsg("Lỗi tải danh sách thực hiện: " + error.message);
      setSinhVienList([]);
    }
  };

  // Gọi API thêm giảng viên hướng dẫn thứ 2
  const handleThemGV2 = async (sinhVienId) => {
    try {
      await authApis().post("/giaovu/them_gv2", { sinhVienId });
      setMsg("Thêm giảng viên thứ 2 thành công!");
      handleXemDanhSach(new Event("submit"));
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
            {khoaHocList.map((k) => (
              <option key={k} value={k}>
                Khóa {k}
              </option>
            ))}
          </Form.Select>
        </div>
        <div className="col-md-2 align-self-end">
          <Button className="w-100" type="submit">
            Xem danh sách
          </Button>
        </div>
      </Form>

      {sinhVienList.length > 0 && (
        <>
          <h5>Khóa {selectedKhoaHoc} - Khoa {khoa}</h5>
          <Button
            className="mb-3"
            variant="success"
            onClick={async () => {
              if (!selectedKhoaHoc) {
                setMsg("Vui lòng chọn khóa học!");
                return;
              }
              try {
                await authApis().post("/giaovu/them_gv_1toanbo", { khoaHoc: selectedKhoaHoc });
                setMsg("Đã thêm GV hướng dẫn cho toàn bộ sinh viên.");
                handleXemDanhSach(new Event("submit"));
              } catch (error) {
                setMsg("Lỗi khi thêm GV hướng dẫn toàn bộ: " + error.message);
              }
            }}
          >
            Thêm toàn bộ GV hướng dẫn cho sinh viên
          </Button>
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
                  <td>{sv.fullname}</td>
                  <td>{sv.email}</td>
                  <td>{sv.deTai || "Chưa có"}</td>
                  <td>{sv.giangVienHuongDan || "Chưa có"}</td>
                  <td>
                    {sv.giangVienHuongDan && sv.giangVienHuongDan !== "Chưa có" && sv.giangVienHuongDan.split(",").length === 1 ? (
                      <Button
                        size="sm"
                        variant="outline-primary"
                        onClick={() => handleThemGV2(sv.id)}
                      >
                        Thêm GV thứ 2
                      </Button>
                    ) : null}

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
