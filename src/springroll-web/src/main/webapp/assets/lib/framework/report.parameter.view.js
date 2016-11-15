var Marionette = require('backbone.marionette');
var Application =require('Application');
var moment = require('moment');


var makeDate = function(template, parameter){
    var now = moment().format("DD/MM/YYYY"); //FIXME - format hardcoded
    template.push('<div class="col-md-3">');
    template.push('<div>' + Localize(parameter.name) + '</div>');
    template.push('<div class="input-group date datepicker" data-provide="datepicker">');
    template.push('<input id="' + parameter.name + '" type="text" class="form-control" value="' + now + '">');
    template.push('<div class="input-group-addon">');
    template.push('<span class="glyphicon glyphicon-th"></span>');
    template.push('</div>');
    template.push('</div>');
    template.push('</div>');
};
var makeLovList = function(template, parameter){
    template.push('<div class="col-md-3">');
    template.push('<div>' + Localize(parameter.name) + '</div>');
    template.push('<select id="' + parameter.name + '" class="selectpicker"');
    if(parameter.multiSelect) template.push('multiple');
    template.push(">");
    _.each(parameter.lovList, function(lov, index){
        var selected = index == 0 ? "selected" : "";
        //template.push('<option value="' + lov.value + '">' + lov.name + '</option>');
        template.push('<option value="' + lov.value + '"' + selected + '>' + Localize(lov.name) + '</option>');
    });
    template.push('</select>');
    template.push('</div>');
};

Application.ReportParamsView  = Marionette.View.extend({

    template: function(parameters){
        var template = [];
        template.push('<div class="panel panel-default">');
        template.push('<div class="panel-heading">' + Localize('Parameters') + '</div>' );
        template.push('<div class="panel-body">');
        template.push('<div class="container">');

        _.each(parameters, function (parameter) {
            if (parameter.javaType == "java.time.LocalDateTime"){
                template.push(makeDate(template, parameter));
            }else if(parameter.javaType == "java.lang.Boolean") {
                parameter.lovList = [{name: 'Y', value: true}, {name: 'N', value: false}];
                template.push(makeLovList(template, parameter));
            }else if(parameter.lovList != null) {
                template.push(makeLovList(template, parameter));
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
                that.params[parameter.name] = "true";
            } else if (parameter.lovList != null){
                that.params[parameter.name] = parameter.lovList[0].value;
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