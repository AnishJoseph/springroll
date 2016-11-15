var Marionette = require('backbone.marionette');
var Application =require('Application');
Application.requiresTemplate('#mdm.control');

var MdmRecord = Backbone.Model.extend({
    validate: function(attrs, options) {
    }
});

var MdmRecords = Backbone.Collection.extend({
    model: MdmRecord
});

var getDisplayValue = function(colDef, value){
    if(colDef.lovList === null)return value;
    var selectedLov  = _.findWhere(colDef.lovList, {value: value});
    if(selectedLov == undefined)return "";
    return Localize(selectedLov.name);
};

var MasterRowView = Marionette.View.extend({
    tagName: 'tr',
    template : function(data){
        var colDefs = data.colDefs;
        var template = [];
        _.each(Object.keys(data.model), function (colName, index){
            if(colName === 'id')return;
            if(colDefs[index].writeable == true || data.model['id'] == undefined) {
                if (colDefs[index].lovList !== null) {
                    template.push('<td style="vertical-align: middle">');
                    Application.Utils.addLovToTemplate(template, colDefs[index], data.model[colName]);
                    template.push('</td>');
                } else if (colDefs[index].type === 'date') {
                    template.push('<td style="vertical-align: middle">');
                    Application.Utils.addDatePickerToTemplate(template, colDefs[index], data.model[colName]);
                    template.push('</td>');
                } else if (colDefs[index].type === 'text') {
                    template.push('<td ><input type="text" data-attrname="' + colDefs[index].name + '" class="form-control " id ="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('></td>');
                } else if (colDefs[index].type === 'num') {
                    template.push('<td ><input type="number" data-attrname="' + colDefs[index].name + '"  class="form-control " id ="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('></td>');
                } else if (colDefs[index].type === 'int') {
                    template.push('<td ><input type="number" data-attrname="' + colDefs[index].name + '"  class="form-control " id ="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('></td>');
                } else {
                    console.error("Dont know what to do here with coldef type " + colDefs[index].type);
                }
            } else {
                template.push('<td><input disabled class="form-control" value="' + getDisplayValue(colDefs[index], data.model[colName]) + '"/></td>');
            }
        });
        return template.join("");
    },
    serializeData: function(){
        return {model: this.model.attributes, colDefs : this.masterGridData.colDefs};
    },
    searchBasedHide : function(){
        this.$el.hide();
    },
    searchBasedShow : function(){
        this.$el.show();
    },
    disableNewRecords : function(){
        this.$el.find("input,button,textarea,select").attr("disabled", "disabled");
    },

    initialize : function(options){
        this.masterGridData = options.masterGridData;
        var events = {};
        _.each(this.masterGridData.colDefs, function(colDef){
            events['change #' + colDef.name] = function(evt){
                var value = this.$el.find('#' + colDef.name).val();
                if(colDef.type === 'boolean') value = value === 'true';
                this.model.set(colDef.name, value );
            };
        });
        this.delegateEvents(events);
        this.model.on('searchBasedShow', this.searchBasedShow, this);
        this.model.on('searchBasedHide', this.searchBasedHide, this);
        this.model.on('disableNewRecords', this.disableNewRecords, this);
    },

    onRender : function(){
        if($.inArray(this.model.id, this.masterGridData.recIdsUnderReview) > -1){
            this.disableNewRecords();
        }
    }
});

var MasterTableBody = Marionette.CollectionView.extend({
    tagName: 'tbody',
    childView: MasterRowView,
    initialize : function(options){
      this.masterGridData = options.masterGridData;
    },
    childViewOptions: function(model, index) {
        return {
            masterGridData: this.masterGridData,
            childIndex: index
        }
    },

    onRender : function(){
        //FIXME - this and below - enable live search always ??
        this.$(".selectpicker").selectpicker({liveSearch:true, liveSearchNormalize : true, selectOnTab: true });
        this.$(".datepicker").datepicker({ autoclose: true, todayHighlight: true});
    },

    onAddChild : function(){
        this.$(".selectpicker").selectpicker({liveSearch:true, liveSearchNormalize : true, selectOnTab: true });
        this.$(".datepicker").datepicker({ autoclose: true, todayHighlight: true});
    }
});

var MasterTable = Marionette.View.extend({
    tagName: 'table',
    className: 'table table-hover table-striped nowrap',
    serializeData: function(){
        return {colDefs: this.masterGridData.colDefs};
    },
    template : function(data){
        var template = [];
        template.push('<thead> <tr>');
        _.each(data.colDefs, function(colDef, index){
            if(index == 0)return;
            template.push('<th>' + Localize(colDef.name) + '</th>');
        });
        template.push('</tr><thead> <tbody></tbody>');
        return template.join("");
    },

    initialize : function(options){
        this.masterGridData = options.masterGridData;
        this.collections = options.collections;

    },
    regions: {
        body: {
            el: 'tbody',
            replaceElement: true
        }
    },
    onRender: function() {
        this.masterTableBody = new MasterTableBody({ collection: this.collections , masterGridData :  this.masterGridData});
        this.showChildView('body', this.masterTableBody);
    }
});
var Control = Marionette.View.extend({
    template: "#mdm.control",

    initialize : function(options){
        this.masterGridData = options.masterGridData;
        this.url = options.url;
        this.collections = options.collections;
        this.changes = {};
        this.newRecords = [];
        var that = this;
        this.collections.on('add', function(model){
            that.newRecords.push(model.attributes);
        });
        this.collections.on('change', function(model){
            if(model.id == null){
                return;
            }
            that.enableSaveButton();
            var attrThatChanged = Object.keys(model.changed)[0];
            var newValue = model.changed[Object.keys(model.changed)[0]];

            var changesForThisId = that.changes[model.id] || {};
            that.changes[model.id] = changesForThisId;

            var objectHoldingAttrValuesOldAndNew = changesForThisId[attrThatChanged] || {};
            changesForThisId[attrThatChanged] = objectHoldingAttrValuesOldAndNew;
            var prevVal = objectHoldingAttrValuesOldAndNew['prevVal'] || model.previousAttributes()[Object.keys(model.changed)[0]];
            objectHoldingAttrValuesOldAndNew['val'] = newValue;
            objectHoldingAttrValuesOldAndNew['prevVal'] = prevVal;
        });
    },

    onRender : function(){
        var that = this;
        this.ui.search.keyup(_.debounce(function(){ that.search(); }, 50, this));//Search only after user input has stabilized
    },

    ui : {
        save : '.save',
        search : '.search'
    },

    events : {
        'click .save' : 'saveClicked',
        'click .add' : 'addRow'
    },
    enableSaveButton : function(){
        $(this.ui.save).removeClass('disabled');
    },
    disableSaveButton : function(){
        $(this.ui.save).addClass('disabled');
    },

    search : function(){
        var searchStr = this.ui.search.val();
        var re = new RegExp(searchStr, 'i');

        _.each(this.collections.models, function(model){
            var found = false;
            _.each(model.attributes, function(value){
                if(value === null || value === undefined)value = '';
                if (typeof value !== 'string') value = value + '';
                if(value.match(re)){
                    /* Some column contains this search string - Dont hide, trigger show */
                    //FIXME - show only if currently hidden to be efficient
                    found = true;
                    model.trigger('searchBasedShow');
                    return;
                }
            });
            if(found == false){
                /* NO column contains this search string -  Hide the row */
                //FIXME - hide only if currently shown to be efficient
                model.trigger('searchBasedHide');
            }
        });
    },
    addRow : function(){
        var newRecord = {};
        _.each(this.masterGridData.colDefs, function(colDef){
            newRecord[colDef.name] = colDef.defaultValue;
            if(colDef.defaultValue == null && colDef.lovList != null){
                newRecord[colDef.name] = colDef.lovList[0].value;
            }
        });
        this.collections.add(newRecord);
        this.enableSaveButton();
    },

    saveClicked : function(){
        _.each(this.collections.models, function(model) {
            model.isValid();
        });

        /* This is to handle multiple clicks on the save button */
        if(this.newRecords.length == 0 && _.isEmpty(this.changes))return;
        this.disableSaveButton();
        var copyOfNewRecords = this.newRecords;
        this.newRecords = [];
        var copyOfChanges = JSON.parse(JSON.stringify(this.changes));
        this.changes = {};

        var changedRecords = [];
        var that = this;
        /* Convert this to a MdmDTO */
        _.each(Object.keys(copyOfChanges), function(id){
            var mdmChangedColumns = {};
            changedRecords.push({'id' : id, 'mdmChangedColumns' : mdmChangedColumns});
            _.each(Object.keys(copyOfChanges[id]), function(columnName){
                mdmChangedColumns[columnName] = copyOfChanges[id][columnName];
            });
        });
        var Collection =  Backbone.Model.extend({ url: this.url});
        var collection = new Collection({'master' : this.model.get('master'), 'changedRecords':changedRecords, 'newRecords' : copyOfNewRecords});
        collection.save(null, {
            success: function(savedModels){
                Application.Indicator.showSuccessMessage({message:Localize('ui.mdm.submit.success')});
                var copyOfChanges = that.copyOfChanges;
                var changedIds = _.pluck(savedModels.get('changedRecords'), 'id');
                _.each(that.collections.models, function(model){
                    if(model.id != null){
                        if($.inArray(model.id+'', changedIds) > -1){
                            model.trigger('disableNewRecords');
                        }
                        return;
                    }
                    model.trigger('disableNewRecords');
                });

            }
        });
    }

});
Application.MasterView = Marionette.View.extend({
    template : _.template("<div id='mcontrol' style='margin-bottom: 20px;margin-top: 10px'/>" +
        "<div style='position: relative;clear: both; height: 100%;width: 100%'>" +
        "<div style='height: calc(100vh - 150px);overflow: auto' id='mtable'/>" +
        "</div>"),
    regions : {
        'tableRegion' : '#mtable',
        'controlRegion' : '#mcontrol'
    },
    initialize : function(options){
        this.masterGridData = options.masterGridData;
        this.url = options.url;
        this.master = options.master;

    },
    onRender : function(){
        var data = [];
        var that = this;
        _.each(this.masterGridData.data, function(row){
            var rowData = {};
            _.each(row, function(col, index){
                if(index == 0)
                    rowData['id'] = col;
                else
                    rowData[that.masterGridData.colDefs[index].name] = col;
            });
            data.push(rowData);
        });
        var mdmRecords = new MdmRecords(data);
        this.showChildView('tableRegion', new MasterTable({masterGridData : this.masterGridData, collections : mdmRecords}));
        this.showChildView('controlRegion',
            new Control({masterGridData : this.masterGridData, url: this.url, collections: mdmRecords, 'model' : new Backbone.Model({'master':this.master})}));
    }

});
