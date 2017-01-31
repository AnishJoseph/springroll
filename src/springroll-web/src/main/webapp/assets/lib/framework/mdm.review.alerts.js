var Marionette = require('backbone.marionette');
var Application =require('Application');
var Radio = require('backbone.radio');
var modalChannel = Radio.channel('ModalChannel');
Application.requiresTemplate('#mdm.moreinfo');


var MdmMoreInfoView = Marionette.View.extend({
    template: '#mdm.moreinfo',

    initialize : function(){
        var that = this;
        modalChannel.on('modal:attached', function() {
            if(that.isDestroyed())return;
            if(that.changedRecordstable != undefined) that.changedRecordstable.refreshDataTable();
            if(that.newRecordstable != undefined) that.newRecordstable.refreshDataTable();
        });
    },
    ui: {
        mdmChangedHeader:  '.mdm-changed-header',
        mdmNewHeader:  '.mdm-new-header'
    },

    regions : {
        changedRegion: '#changedRecords',
        newRegion: '#newRecords',
        controlRegion: '#control'
    },

    onRender : function(){
        var that = this;
        var changedRecords = this.model.get('mdmChangesForReview')['changedRecords'];
        var newRecords = this.model.get('mdmChangesForReview')['newRecords'];

        var columnDefinitions = [];
        _.each(this.model.get('mdmChangesForReview').colDefs, function(colDef){
            if(colDef.name == 'id')return;
            columnDefinitions.push({title : Localize('ui.'+colDef.name), type : 'html', visible : true});
        });

        var heightOfNewRecordsTable = '400px';
        var heightOfChangedRecordsTable = '400px';

        if( changedRecords !== null && changedRecords.length > 0){
            if( newRecords !== null && newRecords.length > 0) heightOfChangedRecordsTable = '200px';
            this.ui.mdmChangedHeader.removeClass('hidden');
            var data = [];
            _.each(changedRecords, function(changedRecord){
                var mdmChangedColumns = changedRecord.mdmChangedColumns;
                var dataForRow = [];
                _.each(that.model.get('mdmChangesForReview').colDefs, function(colDef){
                    if(colDef.name == 'id')return;
                    var val = mdmChangedColumns[colDef.name].val;
                    var prevVal = mdmChangedColumns[colDef.name].prevVal;
                    if(prevVal == undefined || prevVal == null) prevVal = "";
                    var changed = mdmChangedColumns[colDef.name].changed;
                    if(!changed){
                        dataForRow.push(val);
                    } else {
                        dataForRow.push('<div class="alertActionsPanelBorder">' + val + '</div><div class="text-muted">' + prevVal + '</div>');
                    }
                });
                data.push(dataForRow);
            });

            this.changedRecordstable = new Application.SpringrollTable({'columnDefinitions' : columnDefinitions, 'data' : data, 'dom' : 't', height : heightOfChangedRecordsTable});
            this.showChildView('changedRegion', this.changedRecordstable);
        }
        if( newRecords !== null && newRecords.length > 0){
            if( changedRecords !== null && changedRecords.length > 0) heightOfNewRecordsTable = '200px';
            this.ui.mdmNewHeader.removeClass('hidden');
            var data = [];
            _.each(newRecords, function(newRecord){
                var dataForRow = [];
                _.each(that.model.get('mdmChangesForReview').colDefs, function(colDef){
                    if(colDef.name == 'id')return;
                    dataForRow.push(newRecord[colDef.name]);
                });
                data.push(dataForRow);
            });
            this.newRecordstable = new Application.SpringrollTable({'columnDefinitions' : columnDefinitions, 'data' : data, 'dom' : 't', height : heightOfNewRecordsTable});
            this.showChildView('newRegion', this.newRecordstable);
        }
    }
});


var ReviewItem = Backbone.Model.extend({urlRoot:'api/sr/reviewaction'});

var AlertsView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<%-localMessage%>'),

    initialize : function(){
        this.model.set('localMessage', Localize(this.model.get("messageKey"), this.model.get("args")));
    },
    infoClicked : function(){
        var view = new MdmMoreInfoView({ 'model': this.model});
        Application.showModal(this.model.get('localMessage'), view, this);
    },
    acceptClicked : function(commentText){
        var reviewItem = new ReviewItem({reviewStepId: this.model.get('reviewStepId'), approved : true, 'reviewComment':commentText});
        var that = this;
        reviewItem.save({},{
            success : function () {
                that.model.trigger('destroy', that.model);
            },
            error : function(){
                //FIXME - handle success and failure
            }
        });
    },
    rejectClicked : function(commentText){
        var reviewItem = new ReviewItem({reviewStepId: this.model.get('reviewStepId'), approved : false, 'reviewComment':commentText});
        var that = this;
        reviewItem.save({},{
            success : function () {
                that.model.trigger('destroy', that.model);
            },
            error : function(){
                //FIXME - handle sucees and failure
            }
        });
    },
});

Application.subscribeForAlert('/core/mdmreview', {
    view: AlertsView,
    showDismiss: false,
    showInfo: true,
    showAccept: false,
    showReject: false,
});
