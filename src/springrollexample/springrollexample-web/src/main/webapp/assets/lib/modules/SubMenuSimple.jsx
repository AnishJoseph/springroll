import React from 'react';
import Application from 'App';

const SubMenuSimpleOne = ({ children }) => {
    return (
        <div>Simple Submenu One</div>
    );
};
const SubMenuSimpleTwo = ({ children }) => {
    return (
        <div>Simple Submenu Two</div>
    );
};

/* This is an example of submenus with each submenu points to a different component
 - its a top level menu (will be shown second, as the parent index is set to 2
 - it has 2 submenus - order in which they appear depends on the value of index
 - a form is rendered when clicked - what gets rendered is specified by 'component'
 - the route is hardcoded /SubMenuSimple/SM1 or SM2 - i.e when clicked the url changes
 - the name of the parent menu is defined by parentTitle
 - title and parentTitle is localized (i.e the values are looked up in the properties file
*/
Application.addMenu({
    title: 'ui.submenu.simple.first',
    index: 3,
    type: "menuitem",
    route: 'SubMenuSimple/SM1',
    component : SubMenuSimpleOne,
    parentTitle: 'ui.submenu.simple',
    parentIndex : 2
});
Application.addMenu({
    title: 'ui.submenu.simple.second',
    index: 3,
    type: "menuitem",
    route: 'SubMenuSimple/SM2',
    component : SubMenuSimpleTwo,
    parentTitle: 'ui.submenu.simple',
    parentIndex : 2
});