import React from 'react';
import Application from 'App.js';
import { Link } from 'react-router'

class MenuItem extends React.Component {
    render() {
        if(this.props.menuDefn.type == 'submenu')
        {
            return (
                <li role="presentation" className="dropdown">
                    <a className="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">  {Application.Localize(this.props.menuDefn.title)} <span className="caret"></span></a>
                    <ul className="dropdown-menu">
                        {
                            this.props.menuDefn.subMenuItems.map((menuDefn, index) => ( <MenuItem key={index} menuDefn={menuDefn}/>))
                        }
                    </ul>
                </li>
            );
        }
        return (
            <li role="presentation"><Link activeClassName="active" to={(this.props.menuDefn.routeOnClick || this.props.menuDefn.route)}>{Application.Localize(this.props.menuDefn.title)}</Link></li>
        );
    }
}

export default MenuItem;