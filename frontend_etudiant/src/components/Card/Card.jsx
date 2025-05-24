// Card.js
import React from 'react';

const Card = ({ count, label, description }) => (
    <div className="card">
        <h1>{count}</h1>
        <h3>{label}</h3>
        <p>{description}</p>
    </div>
);

export default Card;
