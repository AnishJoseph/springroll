var Marionette  = require('backbone.marionette');
var Application = require('Application');
var moment = require('moment');

Application.SpringrollTable  = Marionette.View.extend({

    template: _.template("<table class='table table-striped table-bordered table-hover' id='springrollTable' width='100%'/>"),
    ui : {
        springrollTable : '#springrollTable'
    },

    initialize : function(options){
        this.data = options.data;
        this.columnDefinitions = options.columnDefinitions;
        this.dom = options.dom;
        this.height = options.height;
        this.language = {};
        this.language['thousands'] = Application.Localize('groupingSeparator');
        this.language['decimal'] = Application.Localize('decimalSeparator');
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

    onRender : function() {
        var ht = (window.innerHeight - 220) + "px"; //FIXME - we need a better soln
        if (this.height != undefined){
            //If the table is in a modal use the specified height
            ht = this.height;
        }
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
        this.springrollTable = this.ui.springrollTable.DataTable( {
            dom: this.dom,
            data: this.data,
            paging:   false,
            scrollY:  ht,
            scrollCollapse: true,
            scrollX: true,
            search : false,
            columns: this.columnDefinitions,
            language : this.language
        } );
    },

    onAttach : function(){
        /* Since the initial calculations of the datatable are done BEFORE the view is attached to
         the DOM, datatables makes all sorts of errors when calculating col widths - as a
         workaround we ask datatables to adjust the col width post render
         */

        this.springrollTable.columns.adjust().draw();
    },

    refreshDataTable : function(){
        this.springrollTable.columns.adjust().draw();
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
        this.springrollTable.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
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
                that.springrollTable.row.add(updatedData);
            }
        });
        /* Redraw ONLY after all rows have either been updated/added */
        this.springrollTable.draw();
    },

    onDataDelete : function(deletedIds, indexOfIdCol){
        /* The default for the index of the ID col is 0 */
        if(indexOfIdCol == undefined) indexOfIdCol = 0;

        this.springrollTable.rows().every( function ( rowIdx, tableLoop, rowLoop ) {
            var index = _.indexOf(deletedIds, this.data()[indexOfIdCol]);
            if(index != -1){
                /* We have found the row that has been deleted */
                this.remove();
            }
        });
        /* Redraw ONLY after all rows have either been updated/added */
        this.springrollTable.draw();
    }
});