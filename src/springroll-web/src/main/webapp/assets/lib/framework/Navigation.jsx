require('bootstrap-loader');
import React from 'react';
import ReactDOM from 'react-dom';
import Application from 'App.js';
import MenuItem from 'MenuItem.jsx';
import DelegatorMenu from 'DelegatorMenu.jsx';
import AlertSummaryContainer from 'AlertSummaryContainer.jsx';

class Navigation extends React.Component {
    handleLogout() {
        $.ajax({
            type: "POST",
            url: "/logout",
        });
        window.location.href = '/';
    }

    render() {
        return (
            <div id='main-menu' className='root-menu'>
                <nav className="navbar navbar-default spaRootElement">
                    <div className="container-fluid">
                        <div id="navbar" className="navbar-collapse collapse">
                            <ul className="nav navbar-nav nav-pills">
                                { Application.getMenuDefns().map((menusDefn, index) => ( <MenuItem key={index} menuDefn={menusDefn}/>)) }
                            </ul>
                            <p onClick={() => this.handleLogout()} data-toggle="tooltip" title={Application.Localize('ui.logout')} className="navbar-text navbar-right glyphicon glyphicon-eject" aria-hidden="true" id="logout"/>
                            <DelegatorMenu/>
                            <AlertSummaryContainer />
                        </div>
                    </div>
                </nav>
            </div>

        );
    }
}

export default Navigation;