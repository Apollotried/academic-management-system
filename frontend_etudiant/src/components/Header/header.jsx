import React from 'react';
import './header.css';
import '../styles.css';
import { Link } from "react-router-dom";


const Header = () => {
    return (
        <header>
            
                {/* Afficher les notifications en haut à droite */}
               
            <img src="/logo.png" alt="Logo" />
            <nav className="vert">
                <ul>
                    <Link to="/user/landing"><li><i className="fas fa-home"></i> Accueil</li></Link>
                    <Link to="/user/StudentDetails"><li><i className="far fa-address-card"></i> Student Details</li></Link>

                    <Link to="/user/CourseListe"><li><i className="fa fa-list-alt"></i> Liste des cours</li></Link>
                    <Link to="/user/StudentResults"><li><i className="fa fa-list-alt"></i> liste de resultats</li></Link>



                </ul>
            </nav>
        </header>
    );
}

export default Header;
