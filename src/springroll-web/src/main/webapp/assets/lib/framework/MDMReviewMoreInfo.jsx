import React from 'react'
import Application from 'App.js';
const ReactDataGrid = require('react-data-grid');
import ArrayFormatter from 'ArrayFormatter';
import {DateTimeFormatter, DateFormatter, BooleanFormatter, BooleanToString, DateTimeToString, DateToString} from 'Formatters';

const BasicFormatter = ({value}) => {
    return <div title={value}>{value}</div>;
};

const ValueFormatter = React.createClass({
    render() {
        let Formatter =  BasicFormatter;
        if(this.props.colDef.type === 'date') Formatter =  DateFormatter;
        if(this.props.colDef.type === 'datetime') Formatter =  DateTimeFormatter;
        if(this.props.colDef.type === 'boolean') Formatter =  BooleanFormatter;
        if(this.props.colDef.multiSelect === true) Formatter =  ArrayFormatter;
        return (
            <div>
                <div><Formatter value={this.props.value.val}/></div>
                {this.props.value.changed && <div className="mdm-prev-value"><Formatter value={this.props.value.prevVal}/></div>}
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
            return {key : colDef.name, name : colDef.title == undefined ? colDef.name : Application.Localize(colDef.title), resizable : true, formatter : <ValueFormatter colDef={colDef}/>};
        }).value();

        this._ncolumns = _.chain(this.props.alert.mdmChangesForReview.colDefs).filter(colDef => colDef.name !== 'id').map(function(colDef){
            var defn = {key : colDef.name, name : colDef.name, resizable : true};
            if(colDef.type === 'date') defn['formatter'] =  DateFormatter;
            if(colDef.type === 'datetime') defn['formatter'] =  DateTimeFormatter;
            if(colDef.type === 'boolean') defn['formatter'] =  BooleanFormatter;
            if(colDef.multiSelect === true) defn['formatter'] =  ArrayFormatter;
            return defn;
        }).value();

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
        let heightOfChangeTable = (this._changedRows.length + 1) * 36 >  350 ? 350 : (this._changedRows.length+1) * 36;
        let heightOfNewTable    = (this._newRows.length + 1)     * 36 >  350 ? 350 : (this._newRows.length+1) * 36;
        return (
            <div className="springroll-table">
                { this.props.alert.mdmChangesForReview.changedRecords.length > 0     && <div><h4 className="text-info mdm-changed-header">{Application.Localize('ui.mdmChangedRecs', this._changedRows.length)}</h4><ReactDataGrid minHeight={heightOfChangeTable} columns={this._columns}  rowGetter={this.rowGetterForChangedRows} rowsCount={this._changedRows.length}/> </div> }
                { this.props.alert.mdmChangesForReview.newRecords.length > 0         && <div><h4 className="text-info mdm-changed-header">{Application.Localize('ui.mdmNewRecs',     this._newRows.length)}    </h4><ReactDataGrid minHeight={heightOfNewTable} columns={this._ncolumns} rowGetter={this.rowGetterForNewRows}     rowsCount={this._newRows.length}/>     </div> }
            </div>
        );
    }
}
export default MDMReviewMoreInfo;
