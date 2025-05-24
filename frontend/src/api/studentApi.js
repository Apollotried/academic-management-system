import axiosInstance from "./axiosInstance";

export const fetchStudents = async () => {
    const response = await axiosInstance.get("/students");
    return response.data;
};

export const fetchStudentCount = async () => {
    const response = await axiosInstance.get("students/count");
    return response.data;
}


export const fetchStudentById = async (id) => {
    const response = await axiosInstance.get(`students/${id}`);

    return response.data;
}

export const addStudent = async (student) => {
    const response = await axiosInstance.post("students", student);
    return response.data;
};


export const deleteStudent = async (id) => {
    const response = await axiosInstance.delete(`students/${id}`);
    return response.data;
};


export const updateStudent = async (id, student) => {
    const response = await axiosInstance.put(`students/${id}`, student);
    return response.data;
};