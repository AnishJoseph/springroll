import React from 'react';
import Application from 'App';
import NavigationContainer from 'NavigationContainer.jsx';
import AlertPanelContainer from 'AlertPanelContainer.jsx';
var NotificationSystem = require('react-notification-system');

var style = {
    Containers : {
        DefaultStyle : {
            width : 900
        }
    }
}

class Root extends React.Component {
    constructor(props){
        super(props);
        this._notificationSystem = null;
    }

    componentDidMount () {
        this._notificationSystem = this.refs.notificationSystem;
        Application.setNotificationSystem(this._notificationSystem);
    }

    render() {
        return (
            <div>
                <NavigationContainer />
                <div id='main-body' className='main-body'>
                    <div id='content' className='main-body-content'>
                        {this.props.children}
                    </div>
                </div>
                <AlertPanelContainer/>
                <NotificationSystem ref="notificationSystem" style={style} allowHTML={true}/>
            </div>
        );
    }
}
export default Root;
