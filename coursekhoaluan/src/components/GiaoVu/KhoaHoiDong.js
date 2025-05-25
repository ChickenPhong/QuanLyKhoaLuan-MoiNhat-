import React, { useEffect, useState } from "react";
import { Container, Table, Button, Badge, Alert, Form, Spinner } from "react-bootstrap";
import { authApis } from "../../config/Apis"; // ✅ sử dụng authApis thay vì axios trực tiếp

const KhoaHoiDong = () => {
  const [hoiDongs, setHoiDongs] = useState([]);
  const [lockedMap, setLockedMap] = useState({});
  const [khoaHocList, setKhoaHocList] = useState([]);
  const [selectedKhoaHoc, setSelectedKhoaHoc] = useState("");
  const [alertMsg, setAlertMsg] = useState("");
  const [alertError, setAlertError] = useState("");
  const [loading, setLoading] = useState(false);

  // Lấy danh sách khóa học
  useEffect(() => {
    const fetchKhoaHoc = async () => {
      try {
        const res = await authApis().get("giaovu/khoahoc");
        console.log("Danh sách khóa học nhận được:", res.data);
        setKhoaHocList(res.data || []);
      } catch (error) {
        setAlertError("Không thể tải danh sách khóa học");
      }
    };
    fetchKhoaHoc();
  }, []);

  // Lấy danh sách hội đồng theo khóa học đã chọn
  useEffect(() => {
    if (selectedKhoaHoc) {
      fetchHoiDong(selectedKhoaHoc);
    } else {
      setHoiDongs([]);
      setLockedMap({});
    }
  }, [selectedKhoaHoc]);

  const fetchHoiDong = async (khoaHoc) => {
    setLoading(true);
    try {
      const res = await authApis().get(`giaovu/hoidong_by_khoahoc?khoaHoc=${khoaHoc}`);
      setHoiDongs(res.data.hoiDongs || []);
      setLockedMap(res.data.lockedMap || {});
    } catch (err) {
      setAlertError("Không thể tải danh sách hội đồng");
    } finally {
      setLoading(false);
    }
  };

  const handleKhoaHoiDong = async (hdId) => {
    try {
      const res = await authApis().post("giaovu/khoa_hoidong", {
        hoiDongId: hdId,
        khoaHoc: selectedKhoaHoc,
      });
      setAlertMsg(res.data);
      fetchHoiDong(selectedKhoaHoc); // reload lại danh sách sau khi khóa
    } catch (err) {
      setAlertError("Khóa hội đồng thất bại!");
    }
  };

  const handleSubmit = (e, hdId) => {
    e.preventDefault();
    if (window.confirm("Bạn có chắc chắn muốn khóa hội đồng này?")) {
      handleKhoaHoiDong(hdId);
    }
  };

  return (
    <Container className="mt-4">
      <h2 className="text-center text-primary mb-4">Khóa Hội đồng</h2>

      <Form.Group className="mb-4" controlId="khoaHocSelect">
        <Form.Label>Chọn khóa học</Form.Label>
        <Form.Select
          value={selectedKhoaHoc}
          onChange={(e) => setSelectedKhoaHoc(e.target.value)}
          required
        >
          <option value="">-- Chọn khóa học --</option>
          {khoaHocList
            .filter(khoa => khoa !== null)
            .map((khoa, idx) => (
              <option key={idx} value={khoa}>
                Khóa {khoa}
              </option>
            ))}
        </Form.Select>
      </Form.Group>

      {alertMsg && (
        <Alert variant="success" onClose={() => setAlertMsg("")} dismissible>
          {alertMsg}
        </Alert>
      )}
      {alertError && (
        <Alert variant="danger" onClose={() => setAlertError("")} dismissible>
          {alertError}
        </Alert>
      )}

      <Table bordered striped>
        <thead>
          <tr>
            <th>#</th>
            <th>Tên hội đồng</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {loading ? (
            <tr>
              <td colSpan={4} className="text-center">
                <Spinner animation="border" size="sm" /> Đang tải dữ liệu...
              </td>
            </tr>
          ) : hoiDongs.length > 0 ? (
            hoiDongs.map((hd, index) => {
              const isLocked = lockedMap[hd.id];
              return (
                <tr key={hd.id || index}>
                  <td>{index + 1}</td>
                  <td>{hd.name}</td>
                  <td>
                    {isLocked ? (
                      <Badge bg="danger">Đã khóa</Badge>
                    ) : (
                      <Badge bg="success">Đang mở</Badge>
                    )}
                  </td>
                  <td>
                    {!isLocked ? (
                      <Form
                        onSubmit={(e) => handleSubmit(e, hd.id)}
                        className="d-inline"
                      >
                        <Button variant="danger" size="sm" type="submit">
                          Khóa hội đồng
                        </Button>
                      </Form>
                    ) : (
                      <span className="text-muted">Không thể thao tác</span>
                    )}
                  </td>
                </tr>
              );
            })
          ) : (
            <tr>
              <td colSpan={4} className="text-center">
                {selectedKhoaHoc
                  ? "Không có hội đồng nào"
                  : "Vui lòng chọn khóa học"}
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
};

export default KhoaHoiDong;
