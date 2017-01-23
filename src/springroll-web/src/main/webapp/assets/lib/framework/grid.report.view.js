var Marionette = require('backbone.marionette');
var Application =require('Application');
var moment = require('moment');

var GridData = Backbone.Model.extend({
    url: function () {
        var gridName = this.get('reportName');
        this.unset('reportName');
        return "api/sr/getGridData/" + gridName;
    }
});
var ReportParams = Backbone.Model.extend({
    url: function () {
        var gridName = this.get('gridName');
        this.unset('gridName');
        return "api/sr/gridParams/" + gridName;
    }
});

Application.GridView = Marionette.View.extend({
    tagName: 'div',
    template: _.template("<div class='grid-title-panel'><div class='pull-left'><h4 class='pull-left text-info'><%-title%></h4></div><div id='filter' title='<%-filter%>' style='padding-left: 100px' class='pull-right glyphicon glyphicon-filter'></div></div><div id='grid'/> "),
    regions: {
        paramsRegion: '#params',
        gridRegion: '#grid',
    },
    initialize: function(options) {
        this.gridName = options.gridName;
        this.fixedParameters = options.parameters;
        this.model = new Backbone.Model({title:this.gridName, filter : Localize('Filter')});
        this.datamassager = options.datamassager;
    },

    events : {
        'click #filter' : "showParameters"
    },

    ui : {
        filter:  '#filter'
    },

    onRender: function() {
        var reportParams  = new ReportParams({gridName: this.gridName});
        var that = this;
        reportParams.save(null, {
           success: function(model, parameters){
               if(parameters.length == 0){
                   that.ui.filter.hide();
                   that.onParametersChanged({'reportName' : that.gridName});
               }
               else {
                   that.reportParamView = new Application.ReportParamsView({"parameters":parameters, "reportName":that.gridName, "myParent":that});
                   Application.showModal("", that.reportParamView, this, true, true);
               }
           }
        });
    },
    showParameters : function(){
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
                that.data = that.datamassager == undefined ? data.data : that.datamassager(data);
                data.myParent = that;
                that.gridView = new Application.SpringrollTable({'columnDefinitions' : data.columns, 'data' : data.data, 'dom' : 'flrtip'});
                that.showChildView('gridRegion', that.gridView);
            }
        });
    },
    /* Use this method to add/update rows in the grid */
    updateData : function(updatedData, indexOfIdCol){
        this.gridView.onDataChanged(updatedData, indexOfIdCol);
    },

    /* Use this method to delete rows in the grid */
    deleteData : function(deletedIds, indexOfIdCol){
        this.gridView.onDataDelete(deletedIds, indexOfIdCol);
    }

});