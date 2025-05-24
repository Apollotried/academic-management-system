import axiosInstance from "./axiosInstance.js";

export const fetchResultDetailsByStudentId = async (studentId) => {
    const response = await axiosInstance.get(`/results/details/${studentId}`);
    return response.data;
}

export const fetchResultsByStudent = async () => {
    const response = await axiosInstance.get(`/results/resultsForSpecificStudent`);
    return response.data;
}

export const fetchCourseInfo = async (id) => {
    const response = await axiosInstance.get(`/results/course-info/${id}`);
    return response.data;
}



export const fetchStudentsGradeByCoursId = async (courseId) => {
    const response = await axiosInstance.get(`/results/course/${courseId}/students-grades`);
    return response.data;
}

