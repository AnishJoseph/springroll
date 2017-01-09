var Marionette = require('backbone.marionette');
var Backbone = require('backbone');
var Application = require('Application');
var moment = require('moment');

var RowView = Marionette.View.extend({
    tagName: 'tr',
    template: _.template('<td style="min-width: 100px"><%- violatedRule %></td> <td><%- message %></td>')
});

var TableBody = Marionette.CollectionView.extend({
    tagName: 'tbody',
    childView: RowView
});


var MoreInfoTableView = Marionette.View.extend({
    tagName: 'table',
    className: 'table table-hover table-striped',
    template: _.template('<thead> <tr> <th><%-Localize("review.breachedRuleName")%></th> <th><%-Localize("review.breachedRuleText")%></th> </tr> </thead> <tbody></tbody>'),

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

Application.ReviewMoreInfoTableView = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<div id="prevreviews"></div><h4><%-Localize("review.breachedRules")%></h4><div id="breaches"></div>'),

    regions: {
        prevreviews : '#prevreviews',
        breaches    : '#breaches'

    },

    initialize : function(options){
        this.violations = options.violations
    },

    onRender: function() {
        this.showChildView('breaches', new MoreInfoTableView({ collection: this.violations}));
        var reviewLog = this.model.get('reviewLog');
        if(reviewLog == null || reviewLog.length == 0)return;
        _.each(reviewLog, function(log){
            log['dateAsString'] = moment(log.time).format(Application.getMomentFormatForDateTime());
        });
        this.showChildView('prevreviews', new PreviousReviewersSection({ collection: new Backbone.Collection(reviewLog)}));
    }
});

var PreviousReviewersRowView = Marionette.View.extend({
    tagName: 'tr',
    template: _.template('<td><%- reviewer %></td> <td><%- reviewComment %></td><td><%- dateAsString %></td>')
});

var PreviousReviewersTableBody = Marionette.CollectionView.extend({
    tagName: 'tbody',
    childView: PreviousReviewersRowView
});

var PreviousReviewersHeader = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<h4 id="previousReviewers"><%-Localize("review.previousReviewers")%></h4>'),

    initialize : function(options){
      this.tableView = options.tableView
    },
    events : {
        'click #previousReviewers' : 'toggleReviewerTable'
    },
    toggleReviewerTable : function(){
    }


});

var PreviousReviewersSection = Marionette.View.extend({
    tagName: 'div',
    template: _.template('<div id="previousReviewersHeader"></div><div id="previousReviewersTable"></div>'),

    regions: {
        previousReviewersHeader : '#previousReviewersHeader',
        previousReviewersTable : '#previousReviewersTable'
    },

    onRender: function() {
        var previousReviewers = new PreviousReviewers({ collection: this.collection});
        this.showChildView('previousReviewersTable', previousReviewers);
        this.showChildView('previousReviewersHeader', new PreviousReviewersHeader({ tableView: previousReviewers}));
    }
});

var PreviousReviewers = Marionette.View.extend({
    tagName: 'table',
    className: 'table table-hover table-striped ',
    template: _.template('<thead> <tr> <th><%-Localize("review.reviewer")%></th> <th><%-Localize("review.comments")%></th><th><%-Localize("review.time")%></th> </tr> </thead> <tbody></tbody>'),

    regions: {
        body: {
            el: 'tbody',
            replaceElement: true
        }
    },

    onRender: function() {
        this.showChildView('body', new PreviousReviewersTableBody({ collection: this.collection}));
    }
});


