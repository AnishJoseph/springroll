var Marionette  = require('backbone.marionette');
var Application = require('Application');
var moment = require('moment');

Application.SpringrollTable  = Marionette.View.extend({

    template: _.template("<table class='table table-striped table-bordered table-hover' id='springrollTable'/>"),
    ui : {
        springrollTable : '#springrollTable'
    },

    initialize : function(options){
        this.data = options.data;
        this.columnDefinitions = options.columnDefinitions;
        this.dom = options.dom;
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
        var ht = (window.innerHeight - 520) + "px"; //FIXME - we need a better soln
        ht = '100px';
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
    }
});