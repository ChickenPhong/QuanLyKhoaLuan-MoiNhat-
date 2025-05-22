
import React, { useState } from "react";
import { Container, Table, Button, Badge, Alert, Form } from "react-bootstrap";

const KhoaHoiDong = ({
  hoiDongs = [],
  lockedMap = {},
  message = "",
  error = "",
  onKhoaHoiDong,
}) => {
  const [alertMsg, setAlertMsg] = useState(message);
  const [alertError, setAlertError] = useState(error);

  const handleSubmit = (e, hdId) => {
    e.preventDefault();
    if (window.confirm("Bạn có chắc chắn muốn khóa hội đồng này?")) {
      if (onKhoaHoiDong) {
        onKhoaHoiDong(hdId);
      }
    }
  };

  return (
    <Container className="mt-4">
      <h2 className="text-center text-primary mb-4">Khóa Hội đồng</h2>

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
          {hoiDongs.length > 0 ? (
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
                Không có hội đồng nào
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </Container>
  );
};

export default KhoaHoiDong;