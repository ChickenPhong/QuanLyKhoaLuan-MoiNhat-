import React, { useState, useEffect } from "react";
import { Button, Table, Form, Alert } from "react-bootstrap";
import { authApis } from "../../config/Apis";

const DanhSachThucHien = () => {
  const [khoaHocList, setKhoaHocList] = useState([]);
  const [selectedKhoaHoc, setSelectedKhoaHoc] = useState("");
  const [sinhVienList, setSinhVienList] = useState([]);
  const [khoa, setKhoa] = useState("");
  const [msg, setMsg] = useState("");

  const [giaoVienList, setGiaoVienList] = useState([]);
  const [selectedGV, setSelectedGV] = useState({}); // Lưu GV chọn cho từng sv

  // Load danh sách khóa học
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

  // Load danh sách giảng viên để chọn
  useEffect(() => {
    const loadGiaoVien = async () => {
      try {
        const res = await authApis().get("/giaovu/danhsach_giangvien");
        setGiaoVienList(res.data || []);
      } catch (error) {
        setMsg("Lỗi tải danh sách giảng viên: " + error.message);
      }
    };
    loadGiaoVien();
  }, []);

  // Lấy danh sách sinh viên + đề tài + GV
  const handleXemDanhSach = async (e) => {
    if (e) e.preventDefault();
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

  // Thêm giảng viên thứ 2
  const handleThemGV2 = async (sinhVienId) => {
    const giaoVienId = selectedGV[sinhVienId];
    if (!giaoVienId) {
      setMsg("Vui lòng chọn giảng viên để thêm");
      return;
    }
    try {
      await authApis().post("/giaovu/them_gv2", { sinhVienId, giaoVienId });
      setMsg("Thêm giảng viên thứ 2 thành công!");
      await handleXemDanhSach();
      setSelectedGV(prev => ({ ...prev, [sinhVienId]: "" }));
    } catch (error) {
      setMsg("Thêm giảng viên thứ 2 thất bại: " + (error.response?.data || error.message));
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
                    {(() => {
                      if (!sv.giangVienHuongDan || sv.giangVienHuongDan.trim() === "" || sv.giangVienHuongDan === "Chưa có") {
                        // Chưa có giảng viên nào
                        return <span>Chưa có giảng viên</span>;
                      }
                      const gvCount = sv.giangVienHuongDan.split(",").length;
                      if (gvCount === 1) {
                        // Đã có 1 GV, hiện dropdown chọn GV thứ 2
                        return (
                          <>
                            <Form.Select
                              size="sm"
                              value={selectedGV[sv.id] || ""}
                              onChange={(e) => setSelectedGV({ ...selectedGV, [sv.id]: e.target.value })}
                              style={{ width: "150px", display: "inline-block", marginRight: "5px" }}
                            >
                              <option value="">Chọn GV thứ 2</option>
                              {giaoVienList
                                .filter((gv) => {
                                  const assignedGVNames = sv.giangVienHuongDan.split(",").map(name => name.trim());
                                  const gvName = gv.fullname || gv.name || gv.username || "";
                                  return !assignedGVNames.includes(gvName);
                                })
                                .map((gv) => (
                                  <option key={gv.id} value={gv.id}>
                                    {gv.fullname || gv.name || gv.username || "Không tên"}
                                  </option>
                                ))}
                            </Form.Select>
                            <Button
                              size="sm"
                              variant="outline-primary"
                              disabled={!selectedGV[sv.id]}
                              onClick={() => handleThemGV2(sv.id)}
                            >
                              Thêm GV thứ 2
                            </Button>
                          </>
                        );
                      }
                      // Trường hợp >= 2 GV
                      return <span>Đã có 2 GV</span>;
                    })()}
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
