import axiosInstance from "./axiosInstance";

export const fetchCourses = async () => {
    const response = await axiosInstance.get("/COURS-SERVICE/api/courses");
    return response.data;
};

export const fetchSyllabus = async () => {
    const response = await axiosInstance.get("/COURS-SERVICE/api/courses/syllabus");
    return response.data;
};

export const fetchSyllabusById = async (id) => {
    const response = await axiosInstance.get(`/COURS-SERVICE/api/courses/syllabus/${id}`);

    return response.data;
}

export const fetchCourseById = async (id) => {
    const response = await axiosInstance.get(`/COURS-SERVICE/api/courses/${id}`);

    return response.data;
}



