/* CSS Files*/
import 'react-date-picker/index.css';
require("main.scss");
require("react-bootstrap-table/dist/react-bootstrap-table-all.min.css");

/* Core JS files - Import all the JS files that are required commonly for all users and roles */
import Application        from 'App.js';
import ReviewAlertItem    from 'ReviewAlertItem.jsx';
import ReviewFYIAlertItem from 'ReviewFYIAlertItem.jsx';
import FYIAlertItem       from 'FYIAlertItem.jsx';
import MDMAlertItem       from 'MDMAlertItem.jsx';
import ExceptionAlertItem from 'ExceptionAlertItem.jsx';

/* Subscribe to framework alerts */
Application.addSubscriberToAlerts('/alerts/core/review',    ReviewAlertItem);
Application.addSubscriberToAlerts('/alerts/core/reviewfyi', ReviewFYIAlertItem);
Application.addSubscriberToAlerts('/alerts/core/fyi',       FYIAlertItem);
Application.addSubscriberToAlerts('/alerts/core/mdmreview',       MDMAlertItem);
Application.addSubscriberToAlerts('/alerts/core/springrollexception',       ExceptionAlertItem);
