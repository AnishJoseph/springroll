import React from 'react';
import Application from 'App.js';

/**
 * Example of 2 (or more) menu items pointing to the same component - the route definition will be the same for all
 * menu items, while the routeOnClick value depends on the menu item
 */
const Module3_3 = (props) => {
    return (
        <div>Module 3-3 {props.params.submodule}</div>
    );
};

module.exports = Module3_3;

Application.addMenu({
    title: 'ui.menu.third.third',
    index: 3,
    type: "menuitem",
    route: 'M4/:submodule',
    component : Module3_3,
    parentTitle: 'module4_name',
    parentIndex : 3,
    routeOnClick: 'M4/one'
});

Application.addMenu({
    title: 'ui.menu.third.fourth',
    index: 3,
    type: "menuitem",
    route: 'M4/:submodule',
    component : Module3_3,
    parentTitle: 'module4_name',
    parentIndex : 3,
    routeOnClick: 'M4/two'
});




