import Application from 'App';
import React from 'react';
import ReactDOM from 'react-dom';
import { Router, Route, hashHistory,IndexRoute } from 'react-router'
import MenuDefinitions from 'MenuDefinitions';
import ReviewAlertWrapper from 'ReviewAlertWrapper';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import springrollReducers from 'SpringrollReducers.jsx';
import { connect } from 'react-redux';
import RootContainer from 'RootContainer.jsx';
import { addAlerts, AlertActions} from 'SpringrollActionTypes';
var moment = require('moment');


$(function() {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var headers = {};
    headers[header] = token;
    $.ajaxSetup({
        headers: headers,
        cache: false,
        statusCode : {
            406:function(message){   //NOT_ACCEPTABLE
                // Check if this is already handled in the business logic
                if(message.errorHandled == undefined) {
                    Application.Indicator.showErrorMessage({message:message.responseText});
                }
            },
            409:function(message){   //CONFLICT BUSINESS VIOLATIONS
                // Check if this is already handled in the business logic
                if(message.errorHandled == undefined) {
                    _.each(message.responseJSON, function (violation) {
                        Application.Indicator.showErrorMessage({message:violation.field + ': ' + violation.message});
                    });
                }
            },
            400:function(message){   //BAD_MESSAGE CONSTRAINT VIOLATIONS
                // Check if this is already handled in the business logic
                if(message.errorHandled == undefined) {
                    _.each(message.responseJSON, function (violation) {
                        _.each(Object.keys(violation), function(field) {
                            /* First localize the field name */
                            var localizedFieldName = Localize(field);
                            if (violation[field].includes('{0}')){
                                /* if the error message is a custom message which includes  the field name  i.e message string contains '{0}'
                                 then dont show the field name separately - just create the message as per the template
                                 */
                                Application.Indicator.showErrorMessage({message: Localize(violation[field], [localizedFieldName])});
                            } else {
                                Application.Indicator.showErrorMessage({message: localizedFieldName +  " : " + Localize(violation[field])});
                            }
                        });
                    });
                }
            },
        }
    });

    Application.loadUser();
    Application.loadProperties();
    Application.loadLocaleMessages();
    var modules = ["Module1", "Module2"];
    var currentModules = [];

    /* Build the menu based on the authorities given to this user */
    _.each(Object.keys(MenuDefinitions.getxMenuDefns()), function(menu){
        if (_.contains(modules, menu)) {
            Application.addMenu(MenuDefinitions.getMenuDefns()[menu]);
            currentModules.push(menu);
        }
    });
    if (_.contains(modules, "Module1")){
        let deferred = $.Deferred();
        Application.addPromise(deferred.promise());
        require.ensure([], function (require) {
            Application.setModule("Module1", require("Module1"));
            deferred.resolve();
        });
    }
    if (_.contains(modules, "Module2")){
        let deferred = $.Deferred();
        Application.addPromise(deferred.promise());
        require.ensure([], function (require) {
            Application.setModule("Module2", require("Module2"));
            deferred.resolve();
        });
    }
    //let store = createStore(springrollReducers);

    const store = createStore(
        springrollReducers,{}
        +  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
    );

    _.each(Object.keys(Application.getSubscribersToAlerts()), function(channel){
        Application.subscribe(channel, (response) => {
            _.each(response.data, alert => (alert['creationTimeMoment'] = moment(alert.creationTime).format(Application.getMomentFormatForDateTime())));
            if (response.data[0].alertType == 'ACTION') {
                store.dispatch(addAlerts(AlertActions.ACTION_ALERTS, response.data));
            } else if (response.data[0].alertType == 'ERROR') {
                store.dispatch(addAlerts(AlertActions.ERROR_ALERTS, response.data));
            } else if (response.data[0].alertType == 'INFO') {
                store.dispatch(addAlerts(AlertActions.INFO_ALERTS, response.data));
            }
        });
    });

    $.when.apply($, Application.getPromises()).then(function () {
        ReactDOM.render(
            <Provider store={store}>
                <Router history={hashHistory}>
                    <Route path="/" component={RootContainer}>
                        <IndexRoute component={Application.getModuleMap()[Object.keys(Application.getModuleMap())[0]]}/>
                        {currentModules.map((module, index) => ( <Route key={index} component={Application.getModuleMap()[module]} path={"/" + module} />))}
                    </Route>
                </Router>
            </Provider>,
            document.getElementById('app')
        );
        console.log("GOING TO START APP");

        Application.start();
    });
});
