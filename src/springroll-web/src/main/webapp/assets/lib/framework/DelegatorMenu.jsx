import React from 'react';

const DelegatorMenu = ({delegators, username, onSwitchUser}) => {
    if(delegators == null || delegators.length == 0)
        return ( <p className="navbar-text navbar-right ">{username}</p>);
    return (
        <ul className="nav navbar-nav nav-pills navbar-right">
            <li role="presentation" className="dropdown">
                <a className="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">{username}<span className="caret"></span></a>
                <ul className="dropdown-menu">
                    {delegators.map((delegator, index) =>
                        (<li onClick={() => {onSwitchUser(delegator)}} key={index} role="presentation"><a>{delegator}</a></li>))}
                </ul>
            </li>
        </ul>
    );
};
export default DelegatorMenu;