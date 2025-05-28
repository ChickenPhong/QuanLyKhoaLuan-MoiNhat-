import React, { useState, useEffect, useContext } from "react";
import { Button, Form, Table, Alert } from "react-bootstrap";
import { authApis } from "../../config/Apis";
import { MyUserContext } from "../../config/Contexts";

const AddTieuChi = () => {
  const [tieuChiList, setTieuChiList] = useState([]);
  const [newTieuChi, setNewTieuChi] = useState("");
  const [msg, setMsg] = useState("");
  const [editId, setEditId] = useState(null);
  const [editTenTieuChi, setEditTenTieuChi] = useState("");

  const user = useContext(MyUserContext);
  const userKhoa = user?.khoa || ""; // Lấy khoa từ user context

  // Load danh sách tiêu chí
  const loadTieuChi = async () => {
    try {
      const res = await authApis().get("/tieuchi");
      // Lọc danh sách tiêu chí theo khoa
      const filtered = res.data.filter(tc => tc.khoa === userKhoa);
      setTieuChiList(filtered);
      setMsg("");
    } catch (error) {
      setMsg("Lỗi tải danh sách tiêu chí: " + error.message);
    }
  };

  useEffect(() => {
    loadTieuChi();
  }, []);

  // Tạo tiêu chí mới
  const handleCreateTieuChi = async () => {
    if (!newTieuChi.trim()) {
      setMsg("Vui lòng nhập tên tiêu chí");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("tenTieuChi", newTieuChi.trim());

      await authApis().post("/tieuchi/", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      // Load lại danh sách tiêu chí sau khi thêm
      await loadTieuChi();

      setMsg("Tạo tiêu chí thành công");
      setNewTieuChi("");
    } catch (error) {
      setMsg("Tạo tiêu chí thất bại: " + (error.response?.data || error.message));
    }
  };

  // Bắt đầu sửa
  const startEdit = (tc) => {
    setEditId(tc.id);
    setEditTenTieuChi(tc.tenTieuChi);
  };

  // Hủy sửa
  const cancelEdit = () => {
    setEditId(null);
    setEditTenTieuChi("");
  };

  // Gửi cập nhật tiêu chí (chỉ sửa tên)
  const handleUpdateTieuChi = async (id) => {
    try {
      await authApis().put(`/tieuchi/${id}`, {
        tenTieuChi: editTenTieuChi
      });
      setMsg("Cập nhật tiêu chí thành công");
      setEditId(null);
      setEditTenTieuChi("");
      loadTieuChi();
    } catch (error) {
      setMsg("Cập nhật tiêu chí thất bại: " + (error.response?.data || error.message));
    }
  };

  return (
    <div className="container mt-3">
      <h3>Quản lý Tiêu chí</h3>
      {msg && <Alert variant="info">{msg}</Alert>}

      {/* Thêm mới tiêu chí */}
      <Form
        onSubmit={(e) => {
          e.preventDefault();
          handleCreateTieuChi();
        }}
        className="mb-3"
      >
        <Form.Group>
          <Form.Label>Tên tiêu chí</Form.Label>
          <Form.Control
            type="text"
            value={newTieuChi}
            onChange={(e) => setNewTieuChi(e.target.value)}
            placeholder="Nhập tên tiêu chí"
          />
        </Form.Group>
        <Button variant="primary" type="submit" className="mt-2">
          Tạo tiêu chí
        </Button>
      </Form>

      {/* Danh sách tiêu chí */}
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tên tiêu chí</th>
            <th>Trạng thái</th>
            <th>Khoa</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {tieuChiList.length > 0 ? (
            tieuChiList.map((tc) =>
              editId === tc.id ? (
                <tr key={tc.id}>
                  <td>{tc.id}</td>
                  <td>
                    <Form.Control
                      type="text"
                      value={editTenTieuChi}
                      onChange={(e) => setEditTenTieuChi(e.target.value)}
                    />
                  </td>
                  <td>{tc.status}</td>
                  <td>{tc.khoa}</td>
                  <td>
                    <Button size="sm" variant="success" onClick={() => handleUpdateTieuChi(tc.id)}>
                      Lưu
                    </Button>{" "}
                    <Button size="sm" variant="secondary" onClick={cancelEdit}>
                      Hủy
                    </Button>
                  </td>
                </tr>
              ) : (
                <tr key={tc.id}>
                  <td>{tc.id}</td>
                  <td>{tc.tenTieuChi}</td>
                  <td>{tc.status}</td>
                  <td>{tc.khoa}</td>
                  <td>
                    <Button size="sm" variant="warning" onClick={() => startEdit(tc)}>
                      Sửa
                    </Button>
                  </td>
                </tr>
              )
            )
          ) : (
            <tr>
              <td colSpan="5" className="text-center">
                Chưa có tiêu chí nào
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
};

export default AddTieuChi;
