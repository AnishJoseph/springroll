import React from 'react';
import Application from 'App';
import Module3_2 from 'Module3.2.jsx';

const Module3_1 = ({ children }) => {
    return (
        <div>Third First</div>
    );
};


Application.addMenu({
    title: 'ui.menu.third.first',
    index: 3,
    type: "menuitem",
    route: 'M3/SM1',
    component : Module3_1,
    parentTitle: 'module3_name',
    parentIndex : 3
});
Application.addMenu({
    title: 'ui.menu.third.second',
    index: 3,
    type: "menuitem",
    route: 'M3/SM2',
    component : Module3_2,
    parentTitle: 'module3_name',
    parentIndex : 3
});

/*
 THIRD LEVEL MENUS
Application.addMenu({
    title: 'ui.menu.third.submenu',
    index: 3,
    type: "submenu",
    parentTitle: 'module3_name',
    parentIndex : 3
});
Application.addMenu({
    title: 'ui.menu.third.submenu.one',
    index: 1,
    type: "menuitem",
    route: 'M3/SM3/SM1',
    component : Module3_2,
    parentTitle: 'ui.menu.third.submenu',
    parentIndex : 3
});
Application.addMenu({
    title: 'ui.menu.third.submenu.two',
    index: 2,
    type: "menuitem",
    route: 'M3/SM3/SM2',
    component : Module3_2,
    parentTitle: 'ui.menu.third.submenu',
    parentIndex : 3
});
*/

module.exports = Module3_1;





