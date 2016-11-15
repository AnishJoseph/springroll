var Marionette = require('backbone.marionette');
var Application = require('Application');
var moment = require('moment');

Application.ReportParamsView  = Marionette.View.extend({

    template: function(parameters){
        var template = [];
        template.push('<div class="panel panel-default">');
        template.push('<div class="panel-heading">' + Localize('Parameters') + '</div>' );
        template.push('<div class="panel-body">');
        template.push('<div class="container">');

        _.each(parameters, function (parameter) {
            if (parameter.javaType == "java.time.LocalDateTime"){
                var now = moment().format("DD/MM/YYYY"); //FIXME - format hardcoded
                template.push('<div class="col-md-3">');
                template.push('<div>' + Localize(parameter.name) + '</div>');
                Application.Utils.addDatePickerToTemplate(template, parameter, now);
                template.push('</div>');
            }else if(parameter.lovList != null) {
                template.push('<div class="col-md-3">');
                template.push('<div>' + Localize(parameter.name) + '</div>');
                var selected = (parameter.multiSelect === true) ? [parameter.lovList[0].value] : parameter.lovList[0].value;
                Application.Utils.addLovToTemplate(template, parameter, selected);
                template.push('</div>');
            }
        });

        template.push('<div class="clearfix " style="clear: both;">');
        template.push('<div  class="formSubmit">');
        template.push('<button id="submit">Submit</button>');
        template.push('</div>');
        template.push('</div>');

        template.push('</div>');
        template.push('</div>');
        template.push('</div>');
        return template.join("");
    },
    onRender: function() {
        this.ui.selectpicker.selectpicker();
        this.ui.datepicker.datepicker({
            autoclose: true,
            todayHighlight: true
        });
    },

    ui: {
        selectpicker:  '.selectpicker',
        datepicker:  '.datepicker',
    },

    initialize: function(options) {
        this.parameters = options.parameters;
        this.reportName = options.reportName;
        this.myParent = options.myParent;
        this.params = {};
        this.model = new Backbone.Model(this.parameters);
        var that = this;
        var events = {};
        events['click #submit'] =  'submit';
        /* For each parameter setup a changeHandler and also set the default value for that parameter */
        _.each(this.parameters, function(parameter){
            events['change #' + parameter.name] = 'changeHandler';
            if(parameter.javaType == "java.lang.Boolean") {
                parameter.lovList = [{name: 'true', value: true}, {name: 'false', value: false}];
                that.params[parameter.name] = "true";
            } else if (parameter.lovList != null){
                that.params[parameter.name] = (parameter.multiSelect === true) ? [parameter.lovList[0].value] : parameter.lovList[0].value;
            } else if (parameter.javaType == "java.time.LocalDateTime"){
                var now = moment().format("DD/MM/YYYY"); //FIXME - format hardcoded
                that.params[parameter.name] = now;
            }
        });
        this.delegateEvents(events);
    },
    submit : function(){
        this.params["reportName"] = this.reportName;
        this.myParent.triggerMethod("parameters:changed", this.params);
    },
    changeHandler : function(evt){
        this.params[evt.currentTarget.id] = $(evt.target).val();
    }
});