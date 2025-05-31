import React, { useEffect, useState } from "react";
import { authApis, endpoints } from "../../config/Apis";

export default function SinhVienDeTai() {
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [data, setData] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError("");
        const res = await authApis().get(endpoints.detaiSinhVien);
        setData(res.data);
      } catch (err) {
        setError("Lỗi khi lấy dữ liệu: " + (err.response?.data || err.message));
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  if (loading) return <p>Đang tải dữ liệu...</p>;
  if (error) return <p style={{ color: "red" }}>{error}</p>;

  if (!data)
    return (
      <div className="alert alert-warning">
        Bạn chưa được phân công đề tài hoặc giảng viên hướng dẫn.
      </div>
    );

  return (
    <div className="container mt-4">
      <h2 className="text-primary mb-4">Thông tin đề tài và giảng viên hướng dẫn của bạn</h2>

      <div className="card border-info mb-4">
        <div className="card-header font-weight-bold">
          Đề tài khóa luận của bạn
          <br />
          Khóa: {data.khoaHoc}
        </div>
        <div className="card-body">
          <strong>Tiêu đề đề tài:</strong> {data.deTai?.title || "Chưa có đề tài"}
          <br />
          <strong>Khoa:</strong> {data.deTai?.khoa || "Chưa có thông tin"}
        </div>
      </div>

      <div className="card border-info">
        <div className="card-header font-weight-bold">Danh sách giảng viên hướng dẫn</div>
        <table className="table table-bordered mb-0">
          <thead>
            <tr>
              <th style={{ width: "5%" }}>#</th>
              <th>Họ tên giảng viên</th>
              <th>Email</th>
            </tr>
          </thead>
          <tbody>
            {data.giangVienHuongDan && data.giangVienHuongDan.length > 0 ? (
              data.giangVienHuongDan.map((gv, index) => (
                <tr key={gv.id}>
                  <td>{index + 1}</td>
                  <td>{gv.fullname || "Tên giảng viên"}</td>
                  <td>{gv.email || "email@example.com"}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={3} className="text-center">
                  Bạn chưa có giảng viên hướng dẫn.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
