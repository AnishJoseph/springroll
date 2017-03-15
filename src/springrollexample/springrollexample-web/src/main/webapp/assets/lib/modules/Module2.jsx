import React from 'react';
import Application from 'App.js';

const Module2 = ({ children }) => {
    return (
        <div>Hello World </div>
    );
};

module.exports = Module2;

Application.addMenu({
    title: 'ui.menu.second',
    index: 2,
    type: "menuitem",
    route : 'M2',
    component : Module2
});



