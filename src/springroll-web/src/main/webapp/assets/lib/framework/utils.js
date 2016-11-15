var Marionette = require('backbone.marionette');
var Application = require('Application');

Application.Utils = {
    addDatePickerToTemplate : function(template, p, value){
        template.push('<div class="input-group date datepicker" data-provide="datepicker">');
        template.push('<input id="' + p.name + '" type="text" class="form-control" data-attrname="' + p.name + '"');
        if(value !== undefined)template.push(' value="' + value + ' "');
        template.push('>');
        template.push('<div class="input-group-addon">');
        template.push('<span class="glyphicon glyphicon-th"></span>');
        template.push('</div>');
    },

    addLovToTemplate : function(template, p, selectedValue){
        template.push('<select class="selectpicker" id="' + p.name + '" data-attrname="' + p.name + '"');
        if(p.multiSelect === true) template.push(' multiple');
        template.push(">");
        _.each(p.lovList, function(lov){
            var selected = lov.value == selectedValue ? " selected" : "";
            if(p.multiSelect === true) selected = ($.inArray(lov.value, selectedValue) > -1)? " selected" : "";
            template.push('<option value="' + lov.value + '"' + selected + '>' + Localize(lov.name) + '</option>');
        });
        template.push('</select>');
    }
};
