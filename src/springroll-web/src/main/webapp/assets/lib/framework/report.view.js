define(['Application', 'marionette'], function (Application, Marionette) {

    var ReportParams = Backbone.Model.extend({
        url: function () {
            return "/api/sr/gridParams/TestGrid1";
        }
    });

    var ParamsView  = Marionette.View.extend({
        serializeData: function(){
            return {data: this.data, view : this};
        },

        makeDate : function(template, parameter){
            template.push('<div class="col-md-3">');
            template.push('<div>' + parameter.name + '</div>');
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
            template.push('<div>' + parameter.name + '</div>');
            template.push('<select id="' + parameter.name + '" class="selectpicker" multiple>');
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
        events : {
            'click #submit' : 'submit',
            'change #date' : 'changeHandler',
            'change #select' : 'changeHandler',
        },
        ui: {
            selectpicker:  '.selectpicker',
            datepicker:  '.datepicker',
            date:  '#date',
        },
        initialize: function(options) {
            this.data = options.data;
        },
        submit : function(){
            console.log("sub");
        },
        changeHandler : function(evt){
            console.log("sub");
            this.model.set(evt.currentTarget.id, evt.currentTarget.value);
        }
    });

    var GridView  = Marionette.View.extend({
        template: _.template("<div id='params'>world</div>"),
    });

    Application.ReportView = Marionette.View.extend({
        tagName: 'div',
        template: _.template("<div id='params'/><div id='grid'/> "),
        regions: {
            paramsRegion: '#params',
            gridRegion: '#grid',
        },
        initialize: function(options) {
            this.gridName = options.gridName;
            this.parameters = options.parameters;
        },
        onRender: function() {
            var reportParams  = new ReportParams();
            var that = this;
            reportParams.save(null, {
               success: function(model, data){
                   that.showChildView('paramsRegion', new ParamsView({model:new Backbone.Model(), "data":data}));
               }
            });

        },
        showBody : function(data){
            this.showChildView('reportRegion', new GridView());
        }
    });
});