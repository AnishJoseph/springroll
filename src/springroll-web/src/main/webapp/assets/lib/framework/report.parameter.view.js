define(['Application', 'marionette'], function (Application, Marionette) {

    Application.ReportParamsView  = Marionette.View.extend({
        serializeData: function(){
            return {data: this.data, view : this};
        },

        makeDate : function(template, parameter){
            template.push('<div class="col-md-3">');
            template.push('<div>' + parameter.displayName + '</div>');
            template.push('<div class="input-group date datepicker" data-provide="datepicker">');
            template.push('<input id="' + parameter.name + '" type="text" class="form-control">');
            template.push('<div class="input-group-addon">');
            template.push('<span class="glyphicon glyphicon-th"></span>');
            template.push('</div>');
            template.push('</div>');
            template.push('</div>');
        },

        makeLovList : function(template, parameter){
            template.push('<div class="col-md-3">');
            template.push('<div>' + parameter.displayName + '</div>');
            template.push('<select id="' + parameter.name + '" class="selectpicker"');
            if(parameter.multiSelect) template.push('multiple');
            template.push(">");
            _.each(parameter.lovList, function(lov){
                template.push('<option title="' + lov.name + '">' + lov.value + '</option>');
            });
            template.push('</select>');
            template.push('</div>');
        },
        template: function(data){
            var template = [];
            template.push('<div class="panel panel-default">');
            template.push('<div class="panel-heading">' + Localize('Parameters') + '</div>' );
            template.push('<div class="panel-body">');
            template.push('<div class="container">');

            _.each(data.data, function (parameter) {
                if (parameter.javaType == "java.time.LocalDateTime"){
                    template.push(data.view.makeDate(template, parameter));
                }else if(parameter.javaType == "java.lang.Boolean") {
                    parameter.lovList = [{name: 'Y', value: true}, {name: 'N', value: false}];
                    template.push(data.view.makeLovList(template, parameter));
                }else if(parameter.lovList != null) {
                    template.push(data.view.makeLovList(template, parameter));
                }
            });

            template.push('<div class="clearfix " style="clear: both; margin-top: 10px">');
            template.push('<div style="margin:20px">');
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
            this.ui.datepicker.datepicker();
        },

        ui: {
            selectpicker:  '.selectpicker',
            datepicker:  '.datepicker',
        },

        initialize: function(options) {
            this.data = options.data;
            this.gridName = options.gridName;
            this.myparent = options.myparent;
            this.params = {};
            var events = {};
            events['click #submit'] =  'submit';
            _.each(this.data, function(parameter){
                events['change #' + parameter.name] = 'changeHandler';
            });
            this.delegateEvents(events);
        },
        submit : function(){
            this.params["gridName"] = this.gridName;
            this.myparent.triggerMethod("grid:data:changed", this.params);
        },
        changeHandler : function(evt){
            this.params[evt.currentTarget.id] = $(evt.target).val();
        }
    });
});