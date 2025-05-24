import React, { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './LoginPage.module.css';
import { login } from "../../api/loginApi.js";
import { toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import {jwtDecode} from 'jwt-decode';
import {AuthContext} from "../contexe/AuthContext.jsx";
import Cookies from 'js-cookie';


const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { setIsAuthenticated, setUserProfile } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const loginRequestDTO = {email, password};
            const response = await login(loginRequestDTO);

            if (response && response.token) {
                localStorage.setItem('token', response.token);
                const decodedToken = jwtDecode(response.token);
                const email = decodedToken.sub;
                const role = decodedToken.scope;
                setUserProfile({ email, role });
                setIsAuthenticated(true);

                if(role === "USER"){
                    toast.success('Login successful!');

                    navigate('/user/landing');
                }else{
                    toast.success('Vous n\'êtes pas dans l\'interface administrateur.');
                }

            } else {
                throw new Error('Login failed. No token received.');
            }
        } catch (error) {
            console.error('Login failed', error);
            toast.error('Login failed. Please check your credentials and try again.');
        }
    };

    return (
        <div className={styles.body}>
        <div className={styles.loginContainer}>
            <ToastContainer />
            <section className={styles.left}>
                <div className={styles.wrap}>
                    <form onSubmit={handleLogin}>
                        <h1>Login</h1>

                        <div className={styles.box}>
                            <input
                                type="email"
                                name="email"
                                placeholder="Email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                required
                            />
                            <i className="bx bxs-user"></i>
                        </div>

                        <div className={styles.box}>
                            <input
                                type="password"
                                name="pwd"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                required
                            />
                            <i className="bx bxs-lock-alt"></i>
                        </div>

                        <button type="submit" className={styles.btn}>Login</button>
                    </form>
                </div>
            </section>
        </div>
        </div>
    );
};

export default LoginPage;
