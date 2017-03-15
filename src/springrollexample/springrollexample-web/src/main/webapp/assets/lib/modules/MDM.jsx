import React from 'react';
import Application from 'App';
import { Router, Route } from 'react-router'

const MDM = (props) => {
    return (
        <div>Hello World - MDM </div>
    );
};
const routes = (<Route key="mdm" path="/mdm" component={MDM}>
                    <Route key="mm" path="/mdm/:master" component={MDM}/>
                </Route>
                );


Application.addMenu({
    title: 'ui.menu.mdm',
    index: 4,
    type: "menuitem",
    route : routes,
    component : MDM,
    routeOnClick: 'mdm'
});
module.exports = MDM;





