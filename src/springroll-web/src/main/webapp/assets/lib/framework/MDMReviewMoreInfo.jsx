import React from 'react'
import Application from 'App.js';
import ArrayFormatter from 'ArrayFormatter';
import {DateTimeFormatter, DateFormatter, BooleanFormatter, TextFormatter} from 'Formatters';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';

export function WrapperForFormatter(value, colDef, row, isNewRecord) {
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

class MDMReviewMoreInfo extends React.Component {
    constructor(props){
        super(props);
        this.changedRecs = this.props.alert.mdmChangesForReview.changedRecords.map(rec => rec.mdmChangedColumns);
        this.newRecords = this.props.alert.mdmChangesForReview.newRecords;
    }
    render() {
        let heightOfChangeTable = (this.props.alert.mdmChangesForReview.changedRecords.length + 1) * 36 >  350 ? 350 : (this.props.alert.mdmChangesForReview.changedRecords.length+1) * 36;
        let heightOfNewTable    = (this.props.alert.mdmChangesForReview.newRecords.length + 1)     * 36 >  350 ? 350 : (this.props.alert.mdmChangesForReview.newRecords.length+1) * 36;
        return (
            <div className="springroll-table">
                { this.props.alert.mdmChangesForReview.changedRecords.length > 0 &&
                    <div>
                        <h4 className="text-info mdm-changed-header">{Application.Localize('ui.mdmChangedRecs', this.changedRecs.length)}</h4>
                        <BootstrapTable ref="table" data={this.changedRecs} striped hover search={false} keyField={'id'} height={heightOfChangeTable} scrollTop={'Top'}>
                        {
                                this.props.alert.mdmChangesForReview.colDefs.map((colDef, index) => {
                                    let dataFormatter = (cellValue, row) => WrapperForFormatter(cellValue, colDef, row, false);
                                    return (
                                        <TableHeaderColumn width={colDef.width} hidden={colDef.name === 'id'} key={index} dataFormat={dataFormatter} dataField={colDef.name} editable={ false }>
                                            {Application.Localize(colDef.name)}
                                        </TableHeaderColumn>
                                    )
                                })
                            }
                        </BootstrapTable>
                    </div>
                }
                { this.newRecords.length > 0 &&
                    <div>
                        <h4 className="text-info mdm-changed-header">{Application.Localize('ui.mdmNewRecs', this.newRecords.length)}</h4>
                        <BootstrapTable ref="table" data={this.newRecords} striped hover search={false} keyField={'id'} height={heightOfNewTable} scrollTop={'Top'}>
                        {
                                this.props.alert.mdmChangesForReview.colDefs.map((colDef, index) => {
                                    let dataFormatter = (cellValue, row) => WrapperForFormatter(cellValue, colDef, row, true);
                                    return (
                                        <TableHeaderColumn width={colDef.width} hidden={colDef.name === 'id'} key={index} dataFormat={dataFormatter} dataField={colDef.name} editable={ false }>
                                            {Application.Localize(colDef.name)}
                                        </TableHeaderColumn>
                                    )
                                })
                            }
                        </BootstrapTable>
                    </div>
                }
            </div>
        );
    }
}
export default MDMReviewMoreInfo;
