import React from 'react';
import Application from 'App';
import SpringrollTable from 'SpringrollTable';

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

var data = [
    {id : 1, date : 100, product : 'P1'},
    {id : 2, date : 200000000, product : 'P2'},
];

/*
 type
 lovList
 multiSelect
 width
 hidden
 name
 title
 format (for numbers - numeral.js
 */
var coldefs = [
    { type : 'int',  hidden : true, name : 'id', title : 'id'},
    { type : 'date', hidden : false, name : 'date', title : 'ui.dateFld'},
    { type : 'text', hidden : false, name : 'product', title : 'ui.productFld'},
];
const SpringrollTableTest = () => {
    return (
        <SpringrollTable data={data} columnDefinitions={coldefs} options={{}} editable={false} keyName='id' title="test"/>
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
    component : SpringrollTableTest,
    parentTitle: 'ui.submenu.simple',
    parentIndex : 2
});