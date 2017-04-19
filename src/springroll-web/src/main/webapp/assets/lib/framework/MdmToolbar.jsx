import React from 'react';
import Application from 'App';
import DebounceInput from 'react-debounce-input';

const MdmToolbar = React.createClass({
    onAddRow() {
        if (this.props.onAddRow !== null && this.props.onAddRow instanceof Function) {
            this.props.onAddRow({newRowIndex: this.props.numberOfRows});
        }
    },

    renderSaveButton() {
        if (this.props.needsSave ) {
            return (<span data-toggle="tooltip" title={Application.Localize('ui.mdm.Save')} onClick={this.props.onSaveClicked} className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-floppy-disk"></span>);
        }
    },

    renderAddRowButton() {
        if (this.props.onMdmMasterAddRow ) {
            return (<span data-toggle="tooltip" title={Application.Localize('ui.mdm.New')} onClick={this.props.onMdmMasterAddRow} className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-plus"></span>);
        }
    },


    renderToggleFilterButton() {
        if (this.props.enableFilter) {
            return (<span data-toggle="tooltip" title={Application.Localize('ui.mdm.changeToggle')} onClick={this.props.onShowModified} className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-filter"></span>);
        }
    },

    render() {
        return (
            <div>
                <div className="control-panel">
                    <div className="row">
                        <span className="text-info toolbar-title">{this.props.title}</span>
                        {this.renderSaveButton()}
                        {this.renderAddRowButton()}
                        {this.renderToggleFilterButton()}
                        <DebounceInput className="pull-right" minLength={2} debounceTimeout={300} onChange={this.props.onSearch} placeholder={Application.Localize('ui.search')}/>
                    </div>
                    {this.props.children}
                </div>
            </div>
        );
    }
});

module.exports = MdmToolbar;