var Marionette = require('backbone.marionette');
var Application = require('Application');
var moment = require('moment');

Application.ReportParamsView  = Marionette.View.extend({

    template: function(parameters){
        var template = [];
        template.push('<div class="panel panel-default">');
        //template.push('<div class="panel-heading">' + Localize('Parameters') + '</div>' );
        template.push('<div class="panel-body">');
        template.push('<form class="form-inline"> ');

        _.each(parameters, function (parameter) {
            if (parameter.javaType == "java.time.LocalDate" || (parameter.javaType == "java.time.LocalDateTime" && (parameter.setTime === 'START_OF_DAY' || parameter.setTime === 'END_OF_DAY'))){
                var now = moment().format("DD/MM/YYYY"); //FIXME - format hardcoded
                template.push('<div class="form-group rep-param">');
                template.push('<div>' + Localize(parameter.name) + '</div>');
                Application.Utils.addDatePickerToTemplate(template, parameter, now, 'datepicker ' + parameter.name);
                template.push('</div>');
            } else if (parameter.javaType == "java.time.LocalDateTime"){
                var now = moment().format("DD/MM/YYYY HH:mm"); //FIXME - format hardcoded
                template.push('<div class="form-group rep-param">');
                template.push('<div>' + Localize(parameter.name) + '</div>');
                Application.Utils.addDatePickerToTemplate(template, parameter, now, 'datetimepicker ' + parameter.name);
                template.push('</div>');
            }else if(parameter.lovList != null) {
                template.push('<div class="form-group rep-param">');
                template.push('<div>' + Localize(parameter.name) + '</div>');
                var selected = (parameter.multiSelect === true) ? [parameter.lovList[0].value] : parameter.lovList[0].value;
                Application.Utils.addLovToTemplate(template, parameter, selected);
                template.push('</div>');
            } else {
                //FIXME - handle string, int and float separately
                template.push('<div class="form-group rep-param">');
                template.push('<div>' + Localize(parameter.name) + '</div>');
                template.push('<input class="form-control " type="text" id="'+ parameter.name + '"> ');
                template.push('</div>');
            }
        });

        template.push('<div class="clearfix " style="clear: both;">');
        template.push('<div  class="formSubmit">');
        template.push('<button id="submit">Submit</button>');
        template.push('</div>');
        template.push('</div>');

        template.push('</form>');
        template.push('</div>');
        template.push('</div>');
        return template.join("");
    },
    onRender: function() {
        this.ui.selectpicker.selectpicker({selectOnTab: true });
        this.ui.selectpickerSearchable.selectpicker({liveSearch:true, liveSearchNormalize : true, selectOnTab: true });
        this.ui.datepicker.datetimepicker({format : 'DD/MM/YYYY'});
        this.ui.datetimepicker.datetimepicker({format : 'DD/MM/YYYY HH:mm'});
    },

    ui: {
        selectpicker:  '.selectpicker',
        selectpickerSearchable:  '.selectpickerSearchable',
        datepicker:  '.datepicker',
        datetimepicker:  '.datetimepicker',
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

            if(parameter.javaType != "java.time.LocalDate" && parameter.javaType != "java.time.LocalDateTime") {
                events['change #' + parameter.name] = function (evt) {
                    var value = this.$el.find('#' + parameter.name).val();
                    that.params[parameter.name] =  value;
                };
            } else {
                events['dp.change .' + parameter.name] = function(evt){
                    if(evt.oldDate == null)return;
                    var value = (parameter.javaType == "java.time.LocalDate") ? evt.date.format('DD/MM/YYYY') : evt.date.format('DD/MM/YYYY HH:mm');//FIXME - hardcoded format
                    that.params[parameter.name] =  value;
                };
            }
            /* Set the default values for each of the parameters */
            if(parameter.javaType == "java.lang.Boolean") {
                parameter.lovList = [{name: 'true', value: true}, {name: 'false', value: false}];
                that.params[parameter.name] = "true";
            } else if (parameter.lovList != null){
                that.params[parameter.name] = (parameter.multiSelect === true) ? [parameter.lovList[0].value] : parameter.lovList[0].value;
            } else if (parameter.javaType == "java.time.LocalDateTime"){
                var now = parameter.setTime === null ? moment() : parameter.setTime == 'START_OF_DAY' ? moment().startOf('day') :moment().endOf('day');
                that.params[parameter.name] = now.format("DD/MM/YYYY HH:mm");
            }else if (parameter.javaType == "java.time.LocalDate"){
                var now = moment().format("DD/MM/YYYY"); //FIXME - format hardcoded
                that.params[parameter.name] = now;
            }
        });
        this.delegateEvents(events);
    },
    submit : function(){
        this.params["reportName"] = this.reportName;
        this.myParent.triggerMethod("parameters:changed", this.params);
    }
});