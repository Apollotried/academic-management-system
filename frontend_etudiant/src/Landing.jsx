import React from 'react';
import Header from "./components/Header/header.jsx";
import Main from "./components/Main/Main.jsx";
import WebSocketComponent from "./components/NotificationComponent.jsx";

const Landing = ()=>{
    return (
        <div className="container">
            <Header/>
            <Main/>

        </div>
    );
};

export default Landing;