import React from 'react'
import Application from 'App.js';
import ReviewModal from 'ReviewModal.jsx';
const ReactDataGrid = require('react-data-grid');
import DateTimeFormatter from 'DateTimeFormatter';
import DateFormatter from 'DateFormatter';

const ValueFormatter = React.createClass({
    render() {
        return (
            <div>
                <div>{this.props.value.val}</div>
                {this.props.value.changed && <div className="text-muted mdm-prev-value">{this.props.value.prevVal}</div>}
            </div>
        )
    }
});


class MDMReviewMoreInfo extends React.Component {
    constructor(props){
        super(props);
        this.onSubmit = this.onSubmit.bind(this);
        this.rowGetter = this.rowGetter.bind(this);
        this._columns = _.map(this.props.alert.mdmChangesForReview.colDefs, function(colDef){
            if(colDef.type == 'date')
                return ({key : colDef.name, name : colDef.name, formatter : DateFormatter, resizable : true});
            if(colDef.type == 'datetime')
                return ({key : colDef.name, name : colDef.name, formatter : DateTimeFormatter, resizable : true});
            return ({key : colDef.name, name : colDef.name, formatter : ValueFormatter, resizable : true});
        });
        this._colDefs = this.props.alert.colDefs;
        this._rows = this.props.alert.mdmChangesForReview.changedRecords;
    }
    rowGetter(i) {
        let x = this._rows[i].mdmChangedColumns;
        //let row = _.map(Object.keys(this._rows[i].mdmChangedColumns), function(colName, index){
        //    return { }
        //
        //});
        return this._rows[i].mdmChangedColumns;
    }

    onSubmit(action, comment){
    }

    render() {
        return (
            <ReactDataGrid columns={this._columns} rowGetter={this.rowGetter} rowsCount={this._rows.length}/>
        );
    }
}
export default MDMReviewMoreInfo;