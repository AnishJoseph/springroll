import React from 'react';
import Application from 'App.js';

/**
 * Example of 2 (or more) menu items pointing to the same component - the route definition will be the same for all
 * menu items, while the routeOnClick value depends on the menu item
 */
const SubmenuCommon = (props) => {
    /*  props.params.submodule indicates which route (submenu was clicked)
        see route and routeOnClick below in the menu definition
     */
    return (
        <div>Common Component - submenu clicked is  {props.params.submodule}</div>
    );
};

module.exports = Module3_3;

/* This is an example of submenus with each submenu pointing to the <b> SAME</b> component
 - its a top level menu (will be shown third, as the parent index is set to 3
 - it has 2 submenus - order in which they appear depends on the value of index
 - the route is not hardcoded - see route. Which route is shown depends on the routeOnClick
 - what gets rendered is specified by component. In this case the component is the same - the component can
   determine which menu was clicked from the props.params.submodule
 - the name of the parent menu is defined by parentTitle
 - title and parentTitle is localized (i.e the values are looked up in the properties file
 */

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




