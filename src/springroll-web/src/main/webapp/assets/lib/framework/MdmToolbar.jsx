import React from 'react';
import Application from 'App';

const MdmToolbar = React.createClass({
    propTypes: {
        onAddRow: React.PropTypes.func,
        onToggleFilter: React.PropTypes.func,
        onSaveClicked: React.PropTypes.func,
        enableFilter: React.PropTypes.bool,
        needsSave: React.PropTypes.bool,
        numberOfRows: React.PropTypes.number,
        addRowButtonText: React.PropTypes.string,
        filterRowsButtonText: React.PropTypes.string,
        children: React.PropTypes.any
    },

    onAddRow() {
        if (this.props.onAddRow !== null && this.props.onAddRow instanceof Function) {
            this.props.onAddRow({newRowIndex: this.props.numberOfRows});
        }
    },

    getDefaultProps() {
        return {
            enableAddRow: true,
            addRowButtonText: 'Add Row',
            filterRowsButtonText: 'Filter Rows'
        };
    },

    renderSaveButton() {
        if (this.props.needsSave ) {
            return (<span data-toggle="tooltip" title={Application.Localize('ui.mdm.Save')} onClick={this.props.onSaveClicked} className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-floppy-disk"></span>);
        }
    },

    renderAddRowButton() {
        if (this.props.onAddRow ) {
            return (<span data-toggle="tooltip" title={Application.Localize('ui.mdm.New')} onClick={this.onAddRow} className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-plus"></span>);
        }
    },


    renderToggleFilterButton() {
        if (this.props.enableFilter) {
            return (<span data-toggle="tooltip" title={Application.Localize('ui.filter')} onClick={this.props.onToggleFilter} className="springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-filter"></span>);
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
                    </div>
                    {this.props.children}
                </div>
            </div>);
    }
});

module.exports = MdmToolbar;