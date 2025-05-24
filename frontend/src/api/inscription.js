import axiosInstance from "./axiosInstance";

export const AssignCoursesToStudent = async (AssignCoursesReqestDTO) => {
    const response = await axiosInstance.post("/inscriptions/assign-courses", AssignCoursesReqestDTO);
    return response.data;
}

export const getCoursesByStudentId = async (studentId) => {
    const response = await axiosInstance.get(`/inscriptions/CoursesByStudentId/${studentId}`);
    return response.data;
}

export const fetchStudentsByCourseId = async (courseId) => {
    const response = await axiosInstance.get(`/inscriptions/course/${courseId}/students`);
    return response.data;
}