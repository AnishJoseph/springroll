import { connect } from 'react-redux';
import Application from 'App.js';
import Navigation from 'Navigation.jsx';
import { bindActionCreators } from 'redux';
import { logout, switchUser } from 'SpringrollActionTypes';

const mapStateToProps = (state) => {
    return {
        delegators : Application.getUser().delegators,
        username   : Application.getUser().displayName
    }
};
const mapDispatchToProps = (dispatch) => bindActionCreators({
    onSwitchUser : switchUser,
    onLogout : logout
}, dispatch);

const NavigationContainer = connect(mapStateToProps, mapDispatchToProps )(Navigation);
export default NavigationContainer;