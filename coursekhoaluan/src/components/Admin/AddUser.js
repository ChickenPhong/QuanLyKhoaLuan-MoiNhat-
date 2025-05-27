import { useEffect, useRef, useState } from "react";
import { Button, Form } from "react-bootstrap";
import Apis, { authApis, endpoints } from "../../config/Apis";

const KHOA_LIST = [
    "Công nghệ thông tin",
    "Quản trị kinh doanh",
    "Tài chính - Ngân hàng",
    "Ngôn ngữ",
    "Công nghệ sinh học"
];

const NGANH_THEO_KHOA = {
    "Công nghệ thông tin": [
        "Khoa học máy tính",
        "Hệ thống thông tin quản lý",
        "Công nghệ thông tin",
        "Trí tuệ nhân tạo"
    ],
    "Quản trị kinh doanh": [
        "Kinh doanh quốc tế",
        "Marketing",
        "Quản trị nhân lực",
        "Quản trị kinh doanh",
        "Logistics và Quản lý chuỗi cung ứng"
    ],
    "Tài chính - Ngân hàng": [
        "Tài chính – Ngân hàng",
        "Công nghệ tài chính",
        "Bảo hiểm"
    ],
    "Ngôn ngữ": [
        "Ngôn ngữ Anh",
        "Ngôn ngữ Trung Quốc",
        "Ngôn ngữ Nhật",
        "Ngôn ngữ Hàn Quốc"
    ],
    "Công nghệ sinh học": [
        "Công nghệ sinh học",
        "Công nghệ thực phẩm"
    ]
};

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
    const [formErrors, setFormErrors] = useState({});

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
        const { name, value } = e.target;

        if (name === "role") {
            setForm({
                ...form,
                role: value,
                khoa: "",
                khoaHoc: "",
                nganh: ""
            });
        } else if (name === "khoa") {
            setForm({
                ...form,
                khoa: value,
                nganh: ""  // reset nganh khi đổi khoa
            });
        } else {
            setForm({ ...form, [name]: value });
        }
    };

    const validateForm = () => {
        const errors = {};

        if (!form.fullname.trim()) errors.fullname = "Họ và tên là bắt buộc";
        if (!form.username.trim()) errors.username = "Tên đăng nhập là bắt buộc";
        if (!form.password.trim()) errors.password = "Mật khẩu là bắt buộc";

        if (["ROLE_GIAOVU", "ROLE_GIANGVIEN", "ROLE_SINHVIEN"].includes(form.role)) {
            if (!form.khoa) errors.khoa = "Khoa là bắt buộc";
        }

        if (form.role === "ROLE_SINHVIEN" && !form.khoaHoc.trim()) {
            errors.khoaHoc = "Khóa học là bắt buộc";
        }

        if (form.role === "ROLE_SINHVIEN" && !form.nganh) {
            errors.nganh = "Ngành học là bắt buộc";
        }

        if (!avatar.current?.files?.length) {
            errors.avatar = "Ảnh đại diện là bắt buộc";
        }

        setFormErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) return;

        const formData = new FormData();
        for (let key in form) {
            if (form[key]) formData.append(key, form[key]);
        }

        if (avatar.current?.files?.length) {
            formData.append('avatar', avatar.current.files[0]);
        }

        try {
            await Apis.post(endpoints['add-user'], formData, {
                headers: { 'Content-Type': 'multipart/form-data' }
            });
            setMessage("Thêm người dùng thành công!");
            setMessageType("success");
            loadUsers();
            setForm({
                fullname: "",
                username: "",
                password: "",
                email: "",
                role: "ROLE_ADMIN",
                khoa: "",
                khoaHoc: "",
                nganh: "",
            });
            avatar.current.value = null;
            setFormErrors({});
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

            <Form onSubmit={handleSubmit} noValidate>
                <Form.Group className="mb-2">
                    <Form.Label>Họ và tên</Form.Label>
                    <Form.Control
                        name="fullname"
                        onChange={handleChange}
                        value={form.fullname}
                        isInvalid={!!formErrors.fullname}
                        required
                    />
                    <Form.Control.Feedback type="invalid">{formErrors.fullname}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-2">
                    <Form.Label>Tên đăng nhập</Form.Label>
                    <Form.Control
                        name="username"
                        onChange={handleChange}
                        value={form.username}
                        isInvalid={!!formErrors.username}
                        required
                    />
                    <Form.Control.Feedback type="invalid">{formErrors.username}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-2">
                    <Form.Label>Mật khẩu</Form.Label>
                    <Form.Control
                        type="password"
                        name="password"
                        onChange={handleChange}
                        value={form.password}
                        isInvalid={!!formErrors.password}
                        required
                    />
                    <Form.Control.Feedback type="invalid">{formErrors.password}</Form.Control.Feedback>
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
                            isInvalid={!!formErrors.khoa}
                            required
                        >
                            <option value="">-- Chọn khoa --</option>
                            {KHOA_LIST.map((k, idx) => (
                                <option key={idx} value={k}>{k}</option>
                            ))}
                        </Form.Select>
                        <Form.Control.Feedback type="invalid">{formErrors.khoa}</Form.Control.Feedback>
                    </Form.Group>
                )}

                {form.role === "ROLE_SINHVIEN" && form.khoa && (
                    <Form.Group className="mb-2">
                        <Form.Label>Ngành học</Form.Label>
                        <Form.Select
                            name="nganh"
                            onChange={handleChange}
                            value={form.nganh}
                            isInvalid={!!formErrors.nganh}
                            required
                        >
                            <option value="">-- Chọn ngành --</option>
                            {NGANH_THEO_KHOA[form.khoa]?.map((n, idx) => (
                                <option key={idx} value={n}>{n}</option>
                            ))}
                        </Form.Select>
                        <Form.Control.Feedback type="invalid">{formErrors.nganh}</Form.Control.Feedback>
                    </Form.Group>
                )}

                {form.role === "ROLE_SINHVIEN" && (
                    <Form.Group className="mb-2">
                        <Form.Label>Khóa học</Form.Label>
                        <Form.Control
                            name="khoaHoc"
                            onChange={handleChange}
                            value={form.khoaHoc}
                            isInvalid={!!formErrors.khoaHoc}
                            required
                        />
                        <Form.Control.Feedback type="invalid">{formErrors.khoaHoc}</Form.Control.Feedback>
                    </Form.Group>
                )}

                <Form.Group className="mb-2">
                    <Form.Label>Avatar</Form.Label>
                    <Form.Control
                        ref={avatar}
                        type="file"
                        isInvalid={!!formErrors.avatar}
                        required
                    />
                    <Form.Control.Feedback type="invalid">{formErrors.avatar}</Form.Control.Feedback>
                </Form.Group>

                <Button type="submit">Thêm</Button>
            </Form>
        </div>
    );
};

export default AddUser;
