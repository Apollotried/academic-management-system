import axiosInstance from "./axiosInstance";


export const getCoursesByStudentId = async (studentId) => {
    const response = await axiosInstance.get(`/inscriptions/CoursesBySpecificStudent`);
    return response.data;
}