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

    template: _.template("<table class='table table-striped table-bordered table-hover' id='gridtable'/>"),
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
        var ht = (window.innerHeight - 300) + "px"; //FIXME - we need a better soln
        this.gridtable = this.ui.griddiv.DataTable( {
            "dom": 'Bflrtip',
            data: this.gridata,
            paging:   false,
            scrollY:  ht,
            scrollCollapse: true,
            search : true,
            columns: this.columnDefinitions,
            buttons: [
                {
                    text: 'Parameters',
                    action: function ( e, dt, node, config ) {
                        that.myParent.triggerMethod("show:parameters");
                    }
                }
            ]
        } );
    },

    onAttach : function(){
        /* Since the initil calculations of the datatable are done BEFORE the view is attached to
           the DOM, datatables makes all sorts of errors when calculating col widths - as a
           workaround we ask datatables to adjust the col width post render
         */

        this.gridtable.columns.adjust().draw();
    }
});

Application.GridView = Marionette.View.extend({
    tagName: 'div',
    template: _.template("<h4 class='text-info'><%-title%></h4><div id='grid'/> "),
    regions: {
        paramsRegion: '#params',
        gridRegion: '#grid',
    },
    initialize: function(options) {
        this.gridName = options.gridName;
        this.fixedParameters = options.parameters;
        this.model = new Backbone.Model({title:this.gridName});
    },

    onRender: function() {
        var reportParams  = new ReportParams({gridName: this.gridName});
        var that = this;
        reportParams.save(null, {
           success: function(model, parameters){
               if(parameters.length == 0){
                   that.onParametersChanged({'reportName' : that.gridName});
               }
               else {
                   that.reportParamView = new Application.ReportParamsView({"parameters":parameters, "reportName":that.gridName, "myParent":that});
                   Application.showModal("", that.reportParamView, this, true, true);
               }
           }
        });
    },
    onShowParameters : function(){
        if(this.reportParamView != undefined)Application.showModal("", this.reportParamView, this, true, true);
    },

    onDestroy : function(){
        if(this.reportParamView != undefined)this.reportParamView.destroy();
    },
    onParametersChanged : function(userChosenParameters){
        if(this.reportParamView != undefined) Application.hideModal();
        var gridData = new GridData(userChosenParameters);
        var that = this;
        gridData.save(null, {
            success : function(model, data){
                data.myParent = that;
                that.showChildView('gridRegion', new GridView(data));
            }
        });
    },
});