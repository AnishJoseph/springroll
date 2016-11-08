var Marionette = require('backbone.marionette');
var Application =require('Application');

var makeLovList = function(template, colDef, selectedValue){
    if(colDef.multiSelect === true)var selectedValueAsArray = makeSelectedValue(selectedValue, colDef);
    template.push('<td>');
    template.push('<select class="selectpicker ' + colDef.name + '" data-attrname="' + colDef.name + '"');
    if(colDef.multiSelect === true) template.push(' multiple');
    template.push(">");
    _.each(colDef.lovList, function(lov){
        var selected = (lov.value == selectedValue)? " selected" : "";
        if(colDef.multiSelect === true) selected = ($.inArray(lov.value, selectedValueAsArray) > -1)? " selected" : "";
        template.push('<option value="' + lov.value + '"' + selected + '>' + Localize(lov.name) + '</option>');
    });
    template.push('</select></td>');
}
var makeSelectedValue = function(value){
    if(value.constructor === Array) return value;
    if(value.constructor === Array) return value;
    var res = value.split(",");
    return res;
}

var makeDate = function(template, colDef, value){
    template.push('<td><div class="input-group date datepicker" data-provide="datepicker">');
    template.push('<input type="text" class="form-control ' + colDef.name + '" data-attrname="' + colDef.name + '"');
    if(value !== undefined)template.push(' value="' + value + ' "');
    template.push('>');
    template.push('<div class="input-group-addon">');
    template.push('<span class="glyphicon glyphicon-th"></span>');
    template.push('</div></td>');
}

var getDisplayValue = function(colDef, value){
    if(colDef.lovList === null)return value;
    var selectedLov  = _.findWhere(colDef.lovList, {value: value});
    if(selectedLov == undefined)return "";
    return Localize(selectedLov.name);
}

var MasterRowView = Marionette.View.extend({
    tagName: 'tr',
    template : function(data){
        var colDefs = data.colDefs;
        var template = [];
        _.each(Object.keys(data.model), function (colName, index){
            if(colName === 'id')return;
            if(colDefs[index].writeable == true || data.model['id'] == undefined) {
                if (colDefs[index].lovList !== null) {
                    makeLovList(template, colDefs[index], data.model[colName]);
                } else if (colDefs[index].type === 'date') {
                    makeDate(template, colDefs[index], data.model[colName]);
                } else if (colDefs[index].type === 'text') {
                    template.push('<td><input type="text" data-attrname="' + colDefs[index].name + '" class="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('></td>');
                } else if (colDefs[index].type === 'num') {
                    template.push('<td><input type="number" data-attrname="' + colDefs[index].name + '"  class="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('></td>');
                } else if (colDefs[index].type === 'int') {
                    template.push('<td><input type="number" data-attrname="' + colDefs[index].name + '"  class="' + colDefs[index].name + '" ');
                    if(data.model[colName] !== undefined && data.model[colName] !== null) template.push(' value="' + data.model[colName] + '"');
                    template.push('></td>');
                } else {
                    console.error("Dont know what to do here with coldef type " + colDefs[index].type);
                }
            } else {
                template.push('<td>' + getDisplayValue(colDefs[index], data.model[colName]) + '</td>');
            }
        });
        return template.join("");
    },
    serializeData: function(){
        return {model: this.model.attributes, colDefs : this.masterGridData.colDefs};
    },
    initialize : function(options){
        this.masterGridData = options.masterGridData;
        var events = {};
        _.each(this.masterGridData.colDefs, function(colDef){
            events['change .' + colDef.name] = 'changeHandler';
        });
        this.delegateEvents(events);
    },

    changeHandler: function(evt) {
        var attrName = evt.target.getAttribute('data-attrname');
        if(attrName === undefined){
            console.log("data-attrname not set - dont know which attribute in the the model to update");
            return;
        }
        var classes = [];
        _.each(evt.target.classList, function(classname){
            classes.push(classname);
        });
        var fullClassSelector = '.' + classes.join(".");
        var value = this.$el.find(fullClassSelector).val();
        this.model.set(attrName, value );
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
        this.$(".selectpicker").selectpicker();
        this.$(".datepicker").datepicker({ autoclose: true, todayHighlight: true});
    },

    onAddChild : function(){
        this.$(".selectpicker").selectpicker();
        this.$(".datepicker").datepicker({ autoclose: true, todayHighlight: true});
    }
});

var MasterTable = Marionette.View.extend({
    tagName: 'table',
    className: 'table table-hover table-striped',
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
        var  tableheader = template.join("");
        return tableheader;
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
        this.masterTableBody = new MasterTableBody({ collection: this.collections , masterGridData :  this.masterGridData})
        this.showChildView('body', this.masterTableBody);
    }
});
var Control = Marionette.View.extend({
    template : function(data){
        var template = [];
        template.push('<h3 style="float: left; margin-right: 50px">Master for ' + Localize(data.master) + '</h3><span id="addRow" class="glyphicon glyphicon-plus" aria-hidden="true"></span> <button class="btn btn-default btn-warning" type="submit">');
        template.push(Localize('Save'));
        template.push('</button>');
        return template.join("");
    },
    serializeData: function(){
        return {master: this.master};
    },
    initialize : function(options){
        this.masterGridData = options.masterGridData;
        this.url = options.url;
        this.collections = options.collections;
        this.master = options.master;
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
    events : {
        'click .btn' : 'saveClicked',
        'click #addRow' : 'addRow'
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
    },

    saveClicked : function(){
        var changedRecords = [];
        var that = this;
        _.each(Object.keys(this.changes), function(id){
            var mdmChangedColumns = [];
            changedRecords.push({'id' : id, 'mdmChangedColumns' : mdmChangedColumns});
            _.each(Object.keys(that.changes[id]), function(columnName){
                mdmChangedColumns.push({'colName' : columnName, 'prevVal' : that.changes[id][columnName].prevVal, 'val' : that.changes[id][columnName].val});
            });
        });
        var Collection =  Backbone.Model.extend({ url: this.url});
        var collection = new Collection({'master' : this.master, 'changedRecords':changedRecords, 'newRecords' : this.newRecords});
        collection.save(null, {
            success: function(){
                console.log("e");
            }
        });
    }

});
Application.MasterView = Marionette.View.extend({
    template : _.template("<div id='mcontrol' style='margin-bottom: 20px;margin-top: 10px'/><div id='mtable'/>"),
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
        var collections = new Backbone.Collection(data);
        this.showChildView('tableRegion', new MasterTable({masterGridData : this.masterGridData, collections : collections}));
        this.showChildView('controlRegion', new Control({masterGridData : this.masterGridData, url: this.url, collections: collections, master : this.master}));
    }

});
