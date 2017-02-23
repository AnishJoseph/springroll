import React from 'react';
import Application from 'App.js';

class DelegatorMenu extends React.Component {
    render() {
        if(Application.getUser().delegators == null || Application.getUser().delegators.length == 0)
            return ( <p className="navbar-text navbar-right ">{Application.getUser().displayName}</p>);
        return (
            <ul className="nav navbar-nav nav-pills navbar-right">
                <li role="presentation" className="dropdown">
                    <a className="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">{Application.getUser().displayName}<span className="caret"></span></a>
                    <ul className="dropdown-menu">
                        {Application.getUser().delegators.map((delegator, index) =>  (<li key={index} role="presentation"><a>{delegator}</a></li>))}
                    </ul>
                </li>
            </ul>
        )
    }
}

export default DelegatorMenu;