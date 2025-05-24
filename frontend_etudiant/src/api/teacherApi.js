import axiosInstance from "./axiosInstance";

export const fetchTeachers = async () => {
    const response = await axiosInstance.get("/PROF-SERVICE/api/teachers");
    return response.data;
};

export const fetchTeachersCount = async () => {
    const response = await axiosInstance.get("/PROF-SERVICE/api/teachers/count");
    return response.data;
}