import axiosInstance from "./axiosInstance";

export const fetchStudentById = async () => {
    const response = await axiosInstance.get('/specificStudent');

    return response.data;
}
