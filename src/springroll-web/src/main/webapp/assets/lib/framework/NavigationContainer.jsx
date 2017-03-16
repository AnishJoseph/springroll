import { connect } from 'react-redux';
import Application from 'App.js';
import Navigation from 'Navigation.jsx';
import { bindActionCreators } from 'redux';
import { logout, switchUser } from 'SpringrollActionTypes';

const mapStateToProps = (state) => {
    return {
        delegators : state.user.delegators,
        username   : state.user.displayName,
        realLoggedInUser   : state.user.realLoggedInUser
    }
};
const mapDispatchToProps = (dispatch) => bindActionCreators({
    onSwitchUser : switchUser,
    onLogout : logout
}, dispatch);

const NavigationContainer = connect(mapStateToProps, mapDispatchToProps )(Navigation);
export default NavigationContainer;