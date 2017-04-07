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

/* This is an example of submenus with each submenu pointing to the <b> SAME</b> component
 - its a top level menu (will be shown third, as the parent index is set to 3
 - it has 2 submenus - order in which they appear depends on the value of index
 - the route is not hardcoded - see route. Which route is shown depends on the routeOnClick
 - what gets rendered is specified by component. In this case the component is the same - the component can
   determine which menu was clicked from the props.params.submodule
 - the name of the parent menu is defined by parentTitle
 - title and parentTitle is localized (i.e the values are looked up in the properties file

 The route definition will be the same for all menu items, while the routeOnClick value depends on the menu item
 This kind of menu can be used when the number of submenus is known a priori - i.e we know at dev time the number
 of submenus needed. For dynamic submenus look at MDM
 */

Application.addMenu({
    title: 'ui.menu.submenuCommon.one',
    index: 3,
    type: "menuitem",
    route: 'SMC/:submodule',
    component : SubmenuCommon,
    parentTitle: 'ui.menu.submenuCommon',
    parentIndex : 3,
    routeOnClick: 'SMC/one'
});

Application.addMenu({
    title: 'ui.menu.submenuCommon.two',
    index: 3,
    type: "menuitem",
    route: 'SMC/:submodule',
    component : SubmenuCommon,
    parentTitle: 'ui.menu.submenuCommon',
    parentIndex : 3,
    routeOnClick: 'SMC/two'
});




