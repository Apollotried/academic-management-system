import React from 'react';
import './header.css';
import '../styles.css';
import { Link } from "react-router-dom";

const Header = () => {
    return (
        <header>
            <img src="/logo.png" alt="Logo" />
            <nav className="vert">
                <ul>
                    <Link to="/admin/landing"><li><i className="fas fa-home"></i> Accueil</li></Link>
                    <Link to="/admin/students"><li><i className="fas fa-user-graduate"></i> Gestion des Étudiants</li></Link>
                    <Link to="/admin/courses"><li><i className="fas fa-book"></i> Gestion des Cours</li></Link>

                    <Link to="/admin/grades">
                        <li><i className="fas fa-book-open"></i> Gestion des Notes</li>
                    </Link>

                    <Link to="/admin/results">
                        <li><i className="fas fa-graduation-cap"></i> Gestion des Résultats</li>
                    </Link>

                    <Link to="/admin/send-notification"><li><i className="fas fa-envelope"></i> Envoyer un Email</li></Link>
                    <Link to="/admin/notifications"><li><i className="fas fa-bell"></i> Liste des Emails</li></Link>
                </ul>
            </nav>
        </header>
    );
}

export default Header;
