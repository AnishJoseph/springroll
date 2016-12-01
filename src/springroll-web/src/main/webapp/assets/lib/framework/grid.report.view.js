var Marionette = require('backbone.marionette');
var Application =require('Application');

var GridData = Backbone.Model.extend({
    url: function () {
        var gridName = this.get('reportName');
        this.unset('reportName');
        return "/api/sr/getGridData/" + gridName;
    }
});
var ReportParams = Backbone.Model.extend({
    url: function () {
        var gridName = this.get('gridName');
        this.unset('gridName');
        return "/api/sr/gridParams/" + gridName;
    }
});

var GridView  = Marionette.View.extend({

    template: _.template("<table class='table-striped table-bordered table-hover' id='gridtable' style='width: 100%'/>"),
    ui : {
        griddiv : '#gridtable'
    },

    initialize : function(options){
        this.gridata = options.data;
        this.columnDefinitions = options.columns;
        this.myParent = options.myParent;
    },

    onRender : function(){
        var that = this;
        this.ui.griddiv.DataTable( {
            "dom": 'Bflrtip',
            data: this.gridata,
            paging:   false,
        //    scrollY:        '45vh',
        //    scrollCollapse: true,
            search : true,
            columns: this.columnDefinitions,
            buttons: [
                {
                    text: 'Parameters',
                    action: function ( e, dt, node, config ) {
                        that.myParent.triggerMethod("param:panel:toggled");
                    }
                }
            ]
        } );
    }
});

Application.GridView = Marionette.View.extend({
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

    ui : {
        paramPanel : '#params'
    },

    onRender: function() {
        var reportParams  = new ReportParams({gridName: this.gridName});
        var that = this;
        reportParams.save(null, {
           success: function(model, parameters){
               that.showChildView('paramsRegion', new Application.ReportParamsView({"parameters":parameters, "reportName":that.gridName, "myParent":that}));
           }
        });
    },
    onParamPanelToggled : function(){
        $(this.ui.paramPanel).toggle();
    },

    onParametersChanged : function(userChosenParameters){
        var gridData = new GridData(userChosenParameters);
        var that = this;
        gridData.save(null, {
            success : function(model, data){
                data.myParent = that;
                that.showChildView('gridRegion', new GridView(data));
                $(that.ui.paramPanel).hide();
            }
        });
    },
});