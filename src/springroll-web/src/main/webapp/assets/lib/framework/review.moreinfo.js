var Marionette = require('backbone.marionette');
var Backbone = require('backbone');
var Application = require('Application');

var RowView = Marionette.View.extend({
    tagName: 'tr',
    template: _.template('<td><%- violatedRule %></td> <td><%- message %></td>')
});

var TableBody = Marionette.CollectionView.extend({
    tagName: 'tbody',
    childView: RowView
});


Application.ReviewMoreInfoTableView = Marionette.View.extend({
    tagName: 'table',
    className: 'table table-hover',
    template: _.template('<thead> <tr> <th>Rule Name</th> <th>Violation Message</th> </tr> </thead> <tbody></tbody>'),

    regions: {
        body: {
            el: 'tbody',
            replaceElement: true
        }
    },

    onRender: function() {
        this.showChildView('body', new TableBody({
            collection: this.collection
        }));
    }
});


