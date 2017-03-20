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
        this.rowGetterForNewRows = this.rowGetterForNewRows.bind(this);
        this.rowGetterForChangedRows = this.rowGetterForChangedRows.bind(this);
        this._columns = _.chain(this.props.alert.mdmChangesForReview.colDefs).filter(colDef => colDef.name !== 'id').map(function(colDef){
            if(colDef.type == 'date')
                return ({key : colDef.name, name : colDef.name, formatter : DateFormatter, resizable : true});
            if(colDef.type == 'datetime')
                return ({key : colDef.name, name : colDef.name, formatter : DateTimeFormatter, resizable : true});
            return ({key : colDef.name, name : colDef.name, formatter : ValueFormatter, resizable : true, width : 200});
        }).value();

        this._ncolumns = _.chain(this.props.alert.mdmChangesForReview.colDefs).filter(colDef => colDef.name !== 'id').map(function(colDef){
            if(colDef.type == 'date')
                return ({key : colDef.name, name : colDef.name, formatter : DateFormatter, resizable : true});
            if(colDef.type == 'datetime')
                return ({key : colDef.name, name : colDef.name, formatter : DateTimeFormatter, resizable : true});
            return ({key : colDef.name, name : colDef.name, resizable : true});
        }).value();

        this._colDefs = this.props.alert.colDefs;
        this._changedRows = this.props.alert.mdmChangesForReview.changedRecords;
        this._newRows = this.props.alert.mdmChangesForReview.newRecords;
    }
    rowGetterForChangedRows(i) {
        return this._changedRows[i].mdmChangedColumns;
    }
    rowGetterForNewRows(i) {
        return this._newRows[i];
    }
    render() {
        return (
            <div>
                { this.props.alert.mdmChangesForReview.changedRecords.length > 0     && <div><h4 className="text-info mdm-changed-header">{Application.Localize('ui.mdmChangedRecs', this._changedRows.length)}</h4><ReactDataGrid minHeight={100} columns={this._columns}  rowGetter={this.rowGetterForChangedRows} rowsCount={this._changedRows.length}/> </div> }
                { this.props.alert.mdmChangesForReview.newRecords.length > 0         && <div><h4 className="text-info mdm-changed-header">{Application.Localize('ui.mdmNewRecs',     this._newRows.length)}    </h4><ReactDataGrid minHeight={100} columns={this._ncolumns} rowGetter={this.rowGetterForNewRows}     rowsCount={this._newRows.length}/>     </div> }
            </div>
        );
    }
}
export default MDMReviewMoreInfo;
