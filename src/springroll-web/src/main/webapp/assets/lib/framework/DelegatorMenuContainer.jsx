import { connect } from 'react-redux';
import Application from 'App.js';
import DelegatorMenu from 'DelegatorMenu.jsx';

const switchUser = function(delegator) {
    if (Application.user.delegator != null && Application.user.delegator == delegator) {
        window.location.href = 'logout/impersonate';
    } else {
        window.location.href = 'login/impersonate?username=' + delegator;
    }
}


const mapStateToProps = (state) => {
    return {
        delegators : Application.getUser().delegators,
        username   : Application.getUser().displayName
    }
};

const mapDispatchToProps = (dispatch) => {
    return {
        onSwitchUser: (delegator) => {
            switchUser(delegator);
        }
    }
};
const DelegatorMenuContainer = connect(mapStateToProps, mapDispatchToProps )(DelegatorMenu);
export default DelegatorMenuContainer;