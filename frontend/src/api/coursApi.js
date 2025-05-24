import axiosInstance from "./axiosInstance";

export const fetchCourses = async () => {
    const response = await axiosInstance.get("/courses");
    return response.data;
};

export const fetchSyllabus = async () => {
    const response = await axiosInstance.get("/courses/syllabus");
    return response.data;
};

export const fetchSyllabusById = async (id) => {
    const response = await axiosInstance.get(`/courses/syllabus/${id}`);

    return response.data;
}

export const fetchCourseById = async (id) => {
    const response = await axiosInstance.get(`/courses/${id}`);

    return response.data;
}


export const fetchCourseCount = async () => {
    const response = await axiosInstance.get("/courses/count");
    return response.data;
}



export const addCourse = async (Course) => {
    const response = await axiosInstance.post("/courses", Course);
    return response.data;
}

export const deleteCourse = async (id) => {
    const response = await axiosInstance.delete(`/courses/${id}`);
    return response.data;
};


export const updateCourse = async (id, course) => {
    const response = await axiosInstance.put(`/courses/${id}`, course);
    return response.data;
};

export const updateSyllabus = async (id, syllabus) => {
    const response = await axiosInstance.put(`/courses/syllabus/${id}`, syllabus);
    return response.data;
};




export const addSyllabus = async (syllabus) => {
    const response = await axiosInstance.post("/courses/syllabus", syllabus);
    return response.data;
}

export const assignSyllabusToCourse = async (courseId, syllabusId) => {
    const response = await axiosInstance.post(`/courses/assignSyllabus/${courseId}/${syllabusId}`);
    return response.data;
};


