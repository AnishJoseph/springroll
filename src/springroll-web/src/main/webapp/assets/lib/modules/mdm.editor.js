var Marionette = require('backbone.marionette');
var Application =require('Application');

var makeLovList = function(template, colDef, selectedValue){
    template.push('<td class="modelattr">');
    template.push('<select class="selectpicker " data-attrname="' + colDef.name + '"');
    if(colDef.multiSelect === true) template.push(' multiple');
    template.push(">");
    _.each(colDef.lovList, function(lov){
        var selected = (lov.value == selectedValue)? " selected" : "";
        template.push('<option value="' + lov.value + '"' + selected + '>' + Localize(lov.name) + '</option>');
    });
    template.push('</select></td>');
}
var makeDate = function(template, parameter, value){
    template.push('<td><div class="input-group date datepicker" data-provide="datepicker">');
    template.push('<input type="text" class="form-control modelattr" data-attrname="' + parameter.name + '"');
    if(value !== undefined)template.push(' value="' + value + ' "');
    template.push('>');
    template.push('<div class="input-group-addon">');
    template.push('<span class="glyphicon glyphicon-th"></span>');
    template.push('</div></td>');
}

var getDisplayValue = function(colDef, value){
    if(colDef.lovList === undefined)return value;
    var selectedLov  = _.findWhere(colDef.lovList, {value: value});
    if(selectedLov == undefined)return "";
    return Localize(selectedLov.name);
}

var MasterRowView = Marionette.View.extend({
    tagName: 'tr',
    template : function(data){
        var colDefs = data.colDefs;
        var template = [];
        _.each(Object.keys(data.model.attributes), function (val, index){
            if(val === 'id')return; //FIXME - assumtion that each model will have all the cols defined - it should ateast be undefined
            if(colDefs[index].writeable == true || data.model.get('id') == undefined) {
                if (colDefs[index].lovList !== null) {
                    makeLovList(template, colDefs[index], data.model.get(val));
                } else if (colDefs[index].type === 'date') {
                    makeDate(template, colDefs[index], data.model.get(val));
                } else if (colDefs[index].type === 'text') {
                    template.push('<td class="modelattr"><input type="text" data-attrname="' + colDefs[index].name + '" ');
                    if(data.model.get(val) !== undefined && data.model.get(val) !== null) template.push(' value="' + data.model.get(val) + '"');
                    template.push('></td>');
                } else if (colDefs[index].type === 'num') {
                    template.push('<td class="modelattr"><input type="number" data-attrname="' + colDefs[index].name + '" ');
                    if(data.model.get(val) !== undefined && data.model.get(val) !== null) template.push(' value="' + data.model.get(val) + '"');
                    template.push('></td>');
                } else {
                    template.push('<td>' + getDisplayValue(colDefs[index], data.model.get(val)) + '</td>');
                }
            } else {
                template.push('<td>' + getDisplayValue(colDefs[index], data.model.get(val)) + '</td>');
            }
        });
        return template.join("");
    },
    serializeData: function(){
        return {model: this.model, colDefs : this.colDefs};
    },
    initialize : function(options){
        this.colDefs = options.colDefs;
    },

    events: {
        'change .modelattr' : 'changeHandler'
    },
    changeHandler: function(evt) {
        var attrName = evt.target.getAttribute('data-attrname');
        if(attrName === undefined){
            console.log("data-attrname not set - dont know which attribute in the the model to update");
            return;
        }
        this.model.set(attrName, evt.target.value );
    }
});

var MasterTableBody = Marionette.CollectionView.extend({
    tagName: 'tbody',
    childView: MasterRowView,
    initialize : function(options){
      this.colDefs = options.colDefs;
    },
    childViewOptions: function(model, index) {
        return {
            colDefs: this.colDefs,
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
        this.masterTableBody = new MasterTableBody({ collection: this.collections , colDefs :  this.masterGridData.colDefs})
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

    },
    events : {
        'click .btn' : 'saveClicked',
        'click #addRow' : 'addRow'
    },
    addRow : function(){
        var newRecord = {};
        _.each(this.masterGridData.colDefs, function(colDef){
            newRecord[colDef.name] = colDef.defaultValue;
        });
        this.collections.add(newRecord);
    },

    saveClicked : function(){
        var changedModels = [];
        _.each(this.collections.models, function(model){
            if(model.hasChanged()){
                var ca = model.changedAttributes();
                ca['id'] = model.get('id');
                changedModels.push(ca);
            } else if (model.isNew()){
                changedModels.push(model.attributes);
            }
        });
        var Collection =  Backbone.Model.extend({ url: this.url});
        var collection = new Collection(changedModels);
        collection.save(null, {
            success: function(){
                console.log("e");
            }
        })
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
