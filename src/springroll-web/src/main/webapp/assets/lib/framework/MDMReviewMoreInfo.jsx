import React from 'react'
import Application from 'App.js';
import {DateTimeFormatter, DateFormatter, BooleanFormatter, TextFormatter, ArrayFormatter} from 'Formatters';
import SpringrollTable from 'SpringrollTable';

function RecordFormatter(value, colDef, isNewRecord) {
    if(value == null)return null;
    let Formatter =  TextFormatter;
    if(colDef.type === 'date') Formatter =  DateFormatter;
    if(colDef.type === 'datetime') Formatter =  DateTimeFormatter;
    if(colDef.type === 'boolean') Formatter =  BooleanFormatter;
    if(colDef.multiSelect === true) Formatter =  ArrayFormatter;
    let valueToShow = isNewRecord? value : value.val;
    return (
        <div>
            <div><Formatter value={valueToShow}/></div>
            {!isNewRecord && value.changed && <div className="mdm-prev-value"><Formatter value={value.prevVal}/></div>}
        </div>
    )
}
function ExistingRecordFormatter({value, colDef}) {
    return RecordFormatter(value, colDef, false);
}
function NewRecordFormatter({value, colDef}) {
    return RecordFormatter(value, colDef, true);
}

class MDMReviewMoreInfo extends React.Component {
    constructor(props){
        super(props);
        this.changedRecs = this.props.alert.mdmChangesForReview.changedRecords.map(rec => rec.mdmChangedColumns);
        this.newRecords = this.props.alert.mdmChangesForReview.newRecords;
    }
    render() {
        let heightOfChangeTable = (this.props.alert.mdmChangesForReview.changedRecords.length + 1) * 36 >  350 ? 350 : (this.props.alert.mdmChangesForReview.changedRecords.length+1) * 36;
        let heightOfNewTable    = (this.props.alert.mdmChangesForReview.newRecords.length + 1)     * 36 >  350 ? 350 : (this.props.alert.mdmChangesForReview.newRecords.length+1) * 36;
        let formattersForExistingRecords = {}, formattersForNewRecords = {};
        let colDefs = this.props.alert.mdmChangesForReview.colDefs.map((colDef, index) => {
            formattersForExistingRecords[colDef.name] = ExistingRecordFormatter;
            formattersForNewRecords[colDef.name] = NewRecordFormatter;
            return {type : colDef.type, hidden : colDef.name == 'id', title : colDef.name, name : colDef.name, width : colDef.width};
        });
        let optionsForExistingRecords = {}, optionsForNewRecords = {};
        optionsForExistingRecords['formatters'] = formattersForExistingRecords;
        optionsForNewRecords['formatters'] = formattersForNewRecords;
        return (
            <div className="springroll-table">
                { this.props.alert.mdmChangesForReview.changedRecords.length > 0 &&
                    <SpringrollTable height={heightOfChangeTable} options={optionsForExistingRecords} data={this.changedRecs} columnDefinitions={colDefs} editable={false} keyName='id' title={Application.Localize('ui.mdmChangedRecs', this.changedRecs.length)}/>
                }
                <p/>
                <p/>
                <p/>
                { this.newRecords.length > 0 &&
                    <SpringrollTable height={heightOfNewTable} options={optionsForNewRecords} data={this.newRecords} columnDefinitions={colDefs} editable={false} keyName='id' title={Application.Localize('ui.mdmNewRecs', this.newRecords.length)}/>
                }
            </div>
        );
    }
}
export default MDMReviewMoreInfo;
