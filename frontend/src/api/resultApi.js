import axiosInstance from "./axiosInstance.js";

export const fetchStudentsGradeByCoursId = async (courseId) => {
    const response = await axiosInstance.get(`/results/course/${courseId}/students-grades`);
    return response.data;
}

export const applyBatchGrades = async (results) => {
    const response = await axiosInstance.post(`/results/batch`, results);
    return response.data;
}