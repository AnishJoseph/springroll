var Marionette = require('backbone.marionette');
var Application = require('Application');
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
                    template.push('<div class="bg-danger err' + colDefs[index].name + '"></div>');
                    template.push('</td>');
                } else if (colDefs[index].type === 'date') {
                    template.push('<td style="vertical-align: middle">');
                    Application.Utils.addDatePickerToTemplate(template, colDefs[index], data.model[colName], colDefs[index].name + ' datepicker');
                    template.push('<div class="bg-danger err' + colDefs[index].name + '"></div>');
                    template.push('</td>');
                } else if (colDefs[index].type === 'datetime') {
                    template.push('<td style="vertical-align: middle">');
                    Application.Utils.addDatePickerToTemplate(template, colDefs[index], data.model[colName], 'datetimepicker');
                    template.push('<div class="bg-danger err' + colDefs[index].name + '"></div>');
                    template.push('</td>');
                } else if (colDefs[index].type === 'text') {
                    template.push('<td style="vertical-align: middle"><input type="text" data-attrname="' + colDefs[index].name + '" class="form-control " id ="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('>');
                    template.push('<div class="bg-danger err' + colDefs[index].name + '"></div>');
                    template.push('</td>');
                } else if (colDefs[index].type === 'num') {
                    template.push('<td style="vertical-align: middle"><input type="number" data-attrname="' + colDefs[index].name + '"  class="form-control " id ="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('>');
                    template.push('<div class="bg-danger err' + colDefs[index].name + '"></div>');
                    template.push('</td>');
                } else if (colDefs[index].type === 'int') {
                    template.push('<td style="vertical-align: middle"><input type="number" data-attrname="' + colDefs[index].name + '"  class="form-control " id ="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('>');
                    template.push('<div class="bg-danger err' + colDefs[index].name + '"></div>');
                    template.push('</td>');
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

    showError : function(errorFld, message){
        this.$el.find('.err' + errorFld).text(message);
    },

    initialize : function(options){
        this.masterGridData = options.masterGridData;
        var events = {};
        _.each(this.masterGridData.colDefs, function(colDef){
            if(colDef.type != 'datetime' && colDef.type != 'date') {
                events['change #' + colDef.name] = function (evt) {
                    var value = this.$el.find('#' + colDef.name).val();
                    if (colDef.type === 'boolean') value = value === 'true';
                    this.model.set(colDef.name, value);
                    this.$el.find('.err' + colDef.name).text('');/* Clear any error if present */
                };
            } else {
                events['dp.change .' + colDef.name] = function(evt){
                    if(evt.oldDate == null)return;
                    var value = (colDef.type == 'date') ? evt.date.format('DD/MM/YYYY') : evt.date.format('DD/MM/YYYY HH:mm');//FIXME - hardcoded format
                    this.model.set(colDef.name, value);
                }
            }
        });
        this.delegateEvents(events);
        this.model.on('searchBasedShow', this.searchBasedShow, this);
        this.model.on('searchBasedHide', this.searchBasedHide, this);
        this.model.on('disableNewRecords', this.disableNewRecords, this);
        this.model.on('showError', this.showError, this);

    },
    ui: {
        selectpicker:  '.selectpicker',
        selectpickerSearchable:  '.selectpickerSearchable',
        datepicker:  '.datepicker',
        datetimepicker:  '.datetimepicker',
    },

    onRender : function(){
        if($.inArray(this.model.id, this.masterGridData.recIdsUnderReview) > -1){
            this.disableNewRecords();
        }
        this.ui.selectpicker.selectpicker({selectOnTab: true });
        this.ui.selectpickerSearchable.selectpicker({liveSearch:true, liveSearchNormalize : true, selectOnTab: true });
        this.ui.datepicker.datetimepicker({format : 'DD/MM/YYYY'});
        this.ui.datetimepicker.datetimepicker({format : 'DD/MM/YYYY HH:mm'});
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
        template.push('</tr></thead> <tbody></tbody>');
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
        this.errorRecords = [];
        var that = this;

        this.collections.on('add', function(model){
            var newRecord = model.attributes;
            newRecord['cid'] = model['cid'];
            that.newRecords.push(newRecord);
        });
        this.collections.on('change', function(model){
            if(model.id == null){
                return;
            }
            that.enableSaveButton();
            var attrThatChanged = Object.keys(model.changed)[0];
            if(attrThatChanged == 'sr.error') return;
            var newValue = model.changed[Object.keys(model.changed)[0]];

            var changesForThisId = that.changes[model.id] || {};
            that.changes[model.id] = changesForThisId;

            var objectHoldingAttrValuesOldAndNew = changesForThisId[attrThatChanged] || {};
            changesForThisId[attrThatChanged] = objectHoldingAttrValuesOldAndNew;
            var prevVal = objectHoldingAttrValuesOldAndNew['prevVal'] || model.previousAttributes()[Object.keys(model.changed)[0]];
            objectHoldingAttrValuesOldAndNew['val'] = newValue;
            objectHoldingAttrValuesOldAndNew['prevVal'] = prevVal;
            changesForThisId['cid'] = {'val' : model['cid']};
        });
    },

    onRender : function(){
        var that = this;
        this.ui.search.keyup(_.debounce(function(){ that.search(); }, 50, this));//Search only after user input has stabilized
    },

    ui : {
        save : '.save',
        search : '.search',
        toggleErrors : '.toggleErrors'
    },

    events : {
        'click .save' : 'saveClicked',
        'click .add' : 'addRow',
        'click .toggleErrors' : 'toggleErrors'
    },
    enableSaveButton : function(){
        $(this.ui.save).removeClass('hidden');
    },
    disableSaveButton : function(){
        $(this.ui.save).addClass('hidden');
    },
    enableShowErrorsButton : function(){
        $(this.ui.toggleErrors).removeClass('hidden');
    },
    disableShowErrorsButton : function(){
        $(this.ui.toggleErrors).addClass('hidden');
    },

    toggleErrors : function () {
        if($(this.ui.toggleErrors).hasClass('allRecs')){
            this.showErrorRecords();
            $(this.ui.toggleErrors).addClass('errRecs').removeClass('allRecs');
            return;
        }
        if($(this.ui.toggleErrors).hasClass('errRecs')){
            this.showAllRecords();
            $(this.ui.toggleErrors).addClass('allRecs').removeClass('errRecs');
            return;
        }
    },

    showAllRecords : function(){
        _.each(this.collections.models, function(model){
            model.trigger('searchBasedShow');
        });

    },
    showErrorRecords : function(){
        _.each(this.collections.models, function(model){
            model.trigger('searchBasedHide');
        });
        _.each(this.errorRecords, function(model){
            model.trigger('searchBasedShow');
        });
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
        this.disableShowErrorsButton();
        this.showAllRecords();
        /* This is to handle multiple clicks on the save button */
        if(this.newRecords.length == 0 && _.isEmpty(this.changes))return;
        this.disableSaveButton();
        var copyOfNewRecords = this.newRecords.slice();
        var copyOfChanges = JSON.parse(JSON.stringify(this.changes));

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
                /* Clear out any New or Changes submitted */
                that.newRecords = [];
                that.changes = {};
                that.errorRecords = [];
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

            },
            error : function(model, response){
                if(response.status != 409)return; /* Handle only business validation errors here */
                /* Now that the save has failed - enable the save button */
                that.enableSaveButton();
                response['errorHandled'] = true;
                _.each(response.responseJSON, function (violation) {
                    /* The server returns and error in a 'BusinessValidationResult' object. Where cookie carries the cid,
                       the field is the name of the field with an error and the message is the localized error message
                     */
                    var errModel = _.find(that.collections.models, function(model) {
                        return model.cid == violation.cookie;
                    });
                    if(errModel === undefined){
                        console.log("Unable to find field and id from " + violation.field);
                        return;
                    }
                    errModel.trigger('showError', violation.field, violation.message);
                    errModel.trigger('searchBasedShow');
                    that.errorRecords.push(errModel);
                    that.showErrorRecords();
                    that.enableShowErrorsButton();
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
