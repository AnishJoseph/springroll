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

    dateRenderer : function(data, type, full, meta) {
        if(type == 'display' || type == 'filter'){
            if(data == undefined || data == null) return '';
            return moment(data).format(Application.getMomentFormatForDate());
        }
        return data;
    },
    datetimeRenderer : function(data, type, full, meta) {
        if(type == 'display' || type == 'filter'){
            if(data == undefined || data == null) return '';
            return moment(data).format(Application.getMomentFormatForDateTime());
        }
        return data;
    },

    onRender : function(){
        var ht = (window.innerHeight - 220) + "px"; //FIXME - we need a better soln
        var that = this;
        _.each(this.columnDefinitions, function(columnDefinition){
            if(columnDefinition.type == 'datetime'){
                columnDefinition['render'] = that.datetimeRenderer;
            } else if(columnDefinition.type == 'date'){
                columnDefinition['render'] = that.dateRenderer;
            }
        });
        _.each(this.columnDefinitions, function(columnDefinition){
            columnDefinition.title = Localize(columnDefinition.title);
        });
        this.gridtable = this.ui.griddiv.DataTable( {
            "dom": 'flrtip',
            data: this.gridata,
            paging:   false,
            scrollY:  ht,
            scrollCollapse: true,
            search : true,
            columns: this.columnDefinitions,
        } );
    },

    onAttach : function(){
        /* Since the initial calculations of the datatable are done BEFORE the view is attached to
           the DOM, datatables makes all sorts of errors when calculating col widths - as a
           workaround we ask datatables to adjust the col width post render
         */

        this.gridtable.columns.adjust().draw();
    },

    onDataChanged : function(updatedDataArray, indexOfIdCol){
        var that = this;
        /* The default for the index of the ID col is 0 */
        if(indexOfIdCol == undefined) indexOfIdCol = 0;
        var existingRows = [];  /* An array to store ids of rows that are already displayed in the grid */
        var updatedIds = [];
        /* Go thru all the update and get the ids of the rows */
        _.each(updatedDataArray, function(updatedData){
            updatedIds.push(updatedData[indexOfIdCol]);
        });

        /* FIXME - this is very inefficient. For example if only one row has a status change
           we still go thru all the rows in the grid. Ideally we should stop the moment all
           rows that have changed are identified
         */
        this.gridtable.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
            var index = _.indexOf(updatedIds, this.data()[indexOfIdCol]);
            if(index != -1){
                /* We have found the row that has changed */
                this.data(updatedDataArray[index]);
                existingRows.push(this.data()[indexOfIdCol]); /* Store the value of the index col */
            }
        });
        /* Now determine if there are any new rows */
        _.each(updatedDataArray, function(updatedData){
            var index = _.indexOf(existingRows, updatedData[indexOfIdCol]);
            if(index == -1){
                /* The index for this updatedData does not exist in the grid - it MUST be a new row */
                that.gridtable.row.add(updatedData);
            }
        });
        /* Redraw ONLY after all rows have either been updated/added */
        this.gridtable.draw();
    },

    onDataDelete : function(deletedIds, indexOfIdCol){
        /* The default for the index of the ID col is 0 */
        if(indexOfIdCol == undefined) indexOfIdCol = 0;

        this.gridtable.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
            var index = _.indexOf(deletedIds, this.data()[indexOfIdCol]);
            if(index != -1){
                /* We have found the row that has been deleted */
                this.remove();
            }
        });
        /* Redraw ONLY after all rows have either been updated/added */
        this.gridtable.draw();
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
                that.data = data.data;
                data.myParent = that;
                that.gridView = new GridView(data);
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