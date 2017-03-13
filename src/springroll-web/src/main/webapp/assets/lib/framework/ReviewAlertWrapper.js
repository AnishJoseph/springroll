import Application        from 'App.js';
import ReviewAlertItem    from 'ReviewAlertItem.jsx';
import ReviewFYIAlertItem from 'ReviewFYIAlertItem.jsx';
import FYIAlertItem       from 'FYIAlertItem.jsx';

Application.addSubscriberToAlerts('/alerts/core/review',    ReviewAlertItem);
Application.addSubscriberToAlerts('/alerts/core/reviewfyi', ReviewFYIAlertItem);
Application.addSubscriberToAlerts('/alerts/core/fyi',       FYIAlertItem);
