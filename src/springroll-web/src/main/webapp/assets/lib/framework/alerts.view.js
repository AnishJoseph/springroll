define(['Application', 'marionette'], function (Application, Marionette) {
    //var xx = '<ul class="nav nav-pills"> <li role="presentation"><a href="#" id="menuIdItem1">Item1</a></li> <li role="presentation"><a href="#" id="menuIdItem2">Item2</a></li> <li role="presentation" class="dropdown"> <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">Item3<span class="caret"></span></a> <ul class="dropdown-menu"> <li role="presentation"><a href="#" id="menuIdsubItem1">subItem1</a></li> <li role="presentation"><a href="#" id="menuIdsubItem2">subItem2</a></li> </ul> </li> </ul>';
    Application.AlertsView = Marionette.View.extend({
        tagName: 'div',
        template: '#alerts.view',
        events: {
            "click #alerts-handle": "toggleAlertContainer",

        },

        ui: {
            alertsContainer: "#alerts-container"
        },

        toggleAlertContainer : function(){
            console.log("toggleAlertContainer");
            this.ui.alertsContainer.toggle();
        }
    });


    Application.requiresTemplate('#alerts.view');
});



