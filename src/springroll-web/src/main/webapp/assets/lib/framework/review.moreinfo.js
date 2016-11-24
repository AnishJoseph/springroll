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
    template: _.template('<thead> <tr> <th><%-Localize("breachedRuleName")%></th> <th><%-Localize("breachedRuleText")%></th> </tr> </thead> <tbody></tbody>'),

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
    template: _.template('<h4 id="previousReviewers"><%-Localize("previousReviewers")%></h4><div id="prevreviews"></div><div class="border-line-bottom"/> <h4><%-Localize("breachedRules")%></h4><div id="breaches"></div>'),

    regions: {
        prevreviews : '#prevreviews',
        breaches    : '#breaches'

    },

    events : {
        'click #previousReviewers' : 'toggleReviewerTable'
    },

    initialize : function(options){
        this.violations = options.violations
    },

    toggleReviewerTable : function(){
        if(this.$el.find('.prt').hasClass('hidden')){
            this.$el.find('.prt').removeClass('hidden');
        } else {
            this.$el.find('.prt').addClass('hidden');
        }
    },

    onRender: function() {
        this.showChildView('breaches', new MoreInfoTableView({ collection: this.violations}));
        var reviewLog = this.model.get('reviewLog');
        if(reviewLog == null || reviewLog.length == 0)return;
        _.each(reviewLog, function(log){
            var str=log.time.dayOfMonth + ":" + log.time.monthValue + ":" + log.time.year + ":" + log.time.hour + ":" + log.time.minute;
            log['dateAsString'] = moment(str, 'D:M:YYYY:H:m').format("DD MMM HH:mm"); //FIXME - format should be externalized;
        });
        this.showChildView('prevreviews', new PreviousReviewers({ collection: new Backbone.Collection(reviewLog)}));
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

var PreviousReviewers = Marionette.View.extend({
    tagName: 'table',
    className: 'prt table table-hover table-striped hidden',
    template: _.template('<thead> <tr> <th><%-Localize("reviewer")%></th> <th><%-Localize("comments")%></th><th><%-Localize("time")%></th> </tr> </thead> <tbody></tbody>'),

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


