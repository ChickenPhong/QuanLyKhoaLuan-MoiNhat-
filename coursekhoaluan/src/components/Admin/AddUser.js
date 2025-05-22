import { useEffect, useRef, useState } from "react";
import { Button, Form, Table } from "react-bootstrap";
import Apis, { authApis, endpoints } from "../../config/Apis";

const KHOA_LIST = [
    "Công nghệ thông tin",
    "Quản trị kinh doanh",
    "Tài chính - Ngân hàng",
    "Ngôn ngữ Anh",
    "Kế toán"
];

const AddUser = () => {
    const [users, setUsers] = useState([]);
    const [form, setForm] = useState({
        fullname: "",
        username: "",
        password: "",
        email: "",
        role: "ROLE_ADMIN",
        khoa: "",
        khoaHoc: "",   
    });

    const avatar = useRef();
    const [message, setMessage] = useState("");
    const [messageType, setMessageType] = useState("");

    const loadUsers = async () => {
        try {
            const res = await authApis().get(endpoints["get-users"]);
            setUsers(res.data);
        } catch (err) {
            console.error("Lỗi tải danh sách user:", err);
        }
    };

    useEffect(() => {
        loadUsers();
    }, []);

    const handleChange = (e) => {
        const { name, value} = e.target;
        if (name === "role") {
            setForm({
                ...form,
                role: value,
                khoa: "",
                khoaHoc: ""
            });
        } else {
            setForm({ ...form, [name]: value });
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formData = new FormData();
        for (let key in form) {
            if (form[key]) formData.append(key, form[key]);
        }

        if (avatar) {
            formData.append('avatar', avatar.current.files[0]);
        }
        try {
            let res = await Apis.post(endpoints['add-user'], formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    });
            setMessage("Thêm người dùng thành công!");
            setMessageType("success");
            loadUsers();
            // Reset form nếu muốn
            // setForm({
            //     fullname: "",
            //     username: "",
            //     password: "",
            //     email: "",
            //     role: "ROLE_ADMIN",
            //     khoa: "",
            //     khoaHoc: "",
            //     avatar: null
            // });
        } catch (err) {
            console.error("Lỗi thêm user:", err);
            setMessage("Thêm người dùng thất bại, vui lòng thử lại.");
            setMessageType("error");
        }
    };

    return (
        <div className="container mt-4">
            <h2>Thêm người dùng mới</h2>

            {message && (
                <div
                    className={`alert ${messageType === "success" ? "alert-success" : "alert-danger"}`}
                    role="alert"
                >
                    {message}
                </div>
            )}

            <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-2">
                    <Form.Label>Họ và tên</Form.Label>
                    <Form.Control
                        name="fullname"
                        onChange={handleChange}
                        value={form.fullname}
                        required
                    />
                </Form.Group>

                <Form.Group className="mb-2">
                    <Form.Label>Tên đăng nhập</Form.Label>
                    <Form.Control
                        name="username"
                        onChange={handleChange}
                        value={form.username}
                        required
                    />
                </Form.Group>

                <Form.Group className="mb-2">
                    <Form.Label>Mật khẩu</Form.Label>
                    <Form.Control
                        type="password"
                        name="password"
                        onChange={handleChange}
                        value={form.password}
                        required
                    />
                </Form.Group>

                <Form.Group className="mb-2">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                        type="email"
                        name="email"
                        onChange={handleChange}
                        value={form.email}
                    />
                </Form.Group>

                <Form.Group className="mb-2">
                    <Form.Label>Vai trò</Form.Label>
                    <Form.Select
                        name="role"
                        onChange={handleChange}
                        value={form.role}
                    >
                        <option value="ROLE_ADMIN">Admin</option>
                        <option value="ROLE_GIAOVU">Giáo vụ</option>
                        <option value="ROLE_GIANGVIEN">Giảng viên</option>
                        <option value="ROLE_SINHVIEN">Sinh viên</option>
                    </Form.Select>
                </Form.Group>

                {(form.role === "ROLE_GIAOVU" || form.role === "ROLE_GIANGVIEN" || form.role === "ROLE_SINHVIEN") && (
                    <Form.Group className="mb-2">
                        <Form.Label>Khoa</Form.Label>
                        <Form.Select
                            name="khoa"
                            onChange={handleChange}
                            value={form.khoa}
                        >
                            <option value="">-- Chọn khoa --</option>
                            {KHOA_LIST.map((k, idx) => (
                                <option key={idx} value={k}>{k}</option>
                            ))}
                        </Form.Select>
                    </Form.Group>
                )}

                {form.role === "ROLE_SINHVIEN" && (
                    <Form.Group className="mb-2">
                        <Form.Label>Khóa học</Form.Label>
                        <Form.Control
                            name="khoaHoc"
                            onChange={handleChange}
                            value={form.khoaHoc}
                        />
                    </Form.Group>
                )}

                <Form.Group className="mb-2">
                    <Form.Label>Avatar</Form.Label>
                    {/* <Form.Control
                        type="file"
                        name="avatar"
                        onChange={handleChange}
                    /> */}
                    <Form.Control ref={avatar} type="file" required />
                </Form.Group>

                <Button type="submit">Thêm</Button>
            </Form>
        </div>
    );
};

export default AddUser;
