import React, {useEffect, useState} from 'react';
import Header from "./components/Header/header.jsx";
import ResultHome from "./components/ResultsManagement/resultHome.jsx";

const ResultManagement = ()=>{
    return (
        <div className="container">
            <Header />
            <ResultHome />
        </div>
    )
}

export default ResultManagement;