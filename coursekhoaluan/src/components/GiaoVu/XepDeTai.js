import React, { useState, useEffect, useContext } from "react";
import { Button, Table, Form, Alert } from "react-bootstrap";
import Apis, { authApis } from "../../config/Apis";
import { MyUserContext } from "../../config/Contexts";

const XepDeTai = () => {
  const [khoaHocList, setKhoaHocList] = useState([]);
  const [selectedKhoaHoc, setSelectedKhoaHoc] = useState("");
  const [sinhVienList, setSinhVienList] = useState([]);
  const [msg, setMsg] = useState("");
  const [msgType, setMsgType] = useState(""); // 'success' hoặc 'danger'
  const current_user = useContext(MyUserContext);
  useEffect(() => {
    const loadKhoaHoc = async () => {
      try {
        const res = await authApis().get("/giaovu/khoahoc");
        setKhoaHocList(res.data || []);
      } catch (error) {
        setMsg("Lỗi tải danh sách khóa học: " + error.message);
        setMsgType("danger");
      }
    };
    loadKhoaHoc();
  }, []);

  const handleLocDanhSach = async (e) => {
    e.preventDefault();
    if (!selectedKhoaHoc) {
      setMsg("Vui lòng chọn khóa học");
      setMsgType("warning");
      setSinhVienList([]);
      return;
    }
    setMsg("");
    try {
      let form = new FormData();
      form.append('username', current_user.username);
      form.append('khoaHoc', selectedKhoaHoc);
      const res = await authApis().get(`/giaovu/danhsach_thuchien?khoaHoc=${selectedKhoaHoc}`);
      setSinhVienList(res.data.sinhViens || []);
    } catch (error) {
      setMsg("Lỗi tải danh sách sinh viên: " + error.message);
      setMsgType("danger");
      setSinhVienList([]);
    }
  };

  // Hàm gọi API xếp đề tài
  const handleXepDeTai = async () => {
    if (!selectedKhoaHoc) {
      setMsg("Vui lòng chọn khóa học trước khi xếp đề tài");
      setMsgType("warning");
      return;
    }
    setMsg("");
    try {
      const res = await authApis().post(`giaovu/xepdetai?khoaHoc=${selectedKhoaHoc}`);
      setMsg(res.data.message || "Xếp đề tài thành công");
      setMsgType("success");
    } catch (error) {
      const errMsg = error.response?.data?.error || error.message;
      setMsg("Lỗi khi xếp đề tài: " + errMsg);
      setMsgType("danger");
    }
  };

  return (
    <div className="container mt-4">
      <h2 className="text-center text-info mb-4">
        Xếp danh sách sinh viên thực hiện khóa luận
      </h2>

      {msg && <Alert variant={msgType}>{msg}</Alert>}

      <Form className="row mb-4" onSubmit={handleLocDanhSach}>
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
          <Button className="btn btn-primary w-100" type="submit">
            Lọc danh sách
          </Button>
        </div>
        <div className="col-md-2 align-self-end">
          <Button
            variant="success"
            className="w-100"
            type="button"
            onClick={handleXepDeTai}
          >
            Xếp đề tài
          </Button>
        </div>
      </Form>

      {sinhVienList.length > 0 && (
        <div>
          <h5 className="mb-3">
            Danh sách sinh viên thuộc khoa Công nghệ thông tin - Khóa {selectedKhoaHoc}:
          </h5>
          <Table striped bordered hover>
            <thead>
              <tr>
                <th>#</th>
                <th>Họ và tên sinh viên</th>
                <th>Email</th>
                <th>Đề tài</th>
              </tr>
            </thead>
            <tbody>
              {sinhVienList.map((sv, idx) => (
                <tr key={sv.id || idx}>
                  <td>{idx + 1}</td>
                  <td>{sv.fullname}</td>
                  <td>{sv.email}</td>
                  <td>{sv.deTai || "Chưa có"}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      )}
    </div>
  );
};

export default XepDeTai;
