import Application        from 'App.js';
import ReviewAlertItem    from 'ReviewAlertItem.jsx';
import ReviewFYIAlertItem from 'ReviewFYIAlertItem.jsx';
import FYIAlertItem       from 'FYIAlertItem.jsx';
import MDMAlertItem       from 'MDMAlertItem.jsx';
import ExceptionAlertItem from 'ExceptionAlertItem.jsx';

Application.addSubscriberToAlerts('/alerts/core/review',    ReviewAlertItem);
Application.addSubscriberToAlerts('/alerts/core/reviewfyi', ReviewFYIAlertItem);
Application.addSubscriberToAlerts('/alerts/core/fyi',       FYIAlertItem);
Application.addSubscriberToAlerts('/alerts/core/mdmreview',       MDMAlertItem);
Application.addSubscriberToAlerts('/alerts/core/springrollexception',       ExceptionAlertItem);
