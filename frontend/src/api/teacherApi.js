import axiosInstance from "./axiosInstance";

export const fetchTeachersCount = async () => {
    const response = await axiosInstance.get("/teachers/count");
    return response.data;
}