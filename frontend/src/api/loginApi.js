import axios from 'axios';

export const login = async (loginRequestDTO) => {
    const response = await axios.post('http://localhost:8888/auth/login', loginRequestDTO, {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    });
    return response.data;
};
