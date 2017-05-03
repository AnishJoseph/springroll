import React from 'react';
import Application from 'App';
import ReportParams from 'ReportParameterForm';
import ReviewModal from 'ReviewModal.jsx';
import { reduxForm } from 'redux-form';
import SpringrollTable from 'SpringrollTable';


class GridReport extends React.Component {
    constructor(props){
        super(props);
        this.handleModalClosed = this.handleModalClosed.bind(this);
        this.onFilterClick = this.onFilterClick.bind(this);
        this.paramsSelected = this.paramsSelected.bind(this);
        this.toggleParamPanel = this.toggleParamPanel.bind(this);
        this.state = {showFilter : this.props.parameterFirst};
        this.ParameterForm = reduxForm({
            form: 'grid-params:' + this.props.gridName,
            destroyOnUnmount : false,
            initialValues : this.props.params
        })(ReportParams);
    }
    toggleParamPanel(){
        this.setState({showFilter : !this.state.showFilter});
    }
    handleModalClosed(){
        this.setState({showFilter : false});
    }

    onFilterClick() {
        this.setState({showFilter : !this.state.showFilter});
    }
    paramsSelected(paramValues){
        console.log(JSON.stringify(paramValues));
        this.setState({showFilter : false});
        this.props.onGridDataRequest(this.props.gridName, paramValues);
    }
    render() {
        let message = Application.Localize("ui.ReportParameters");
        let customButtons = [{ className : "springroll-icon pull-right alertActionsPanelItem glyphicon glyphicon-tasks",
            onClick : this.toggleParamPanel,
            title : Application.Localize('ui.report.parameters'),
            visible : this.props.gridParams !== undefined && this.props.gridParams.length > 0
        }];
        let gridData = this.props.gridData || {data : undefined, columns : undefined, key : undefined};
        return (
            <span>
                <SpringrollTable customButtons={customButtons} data={gridData.data} columnDefinitions={gridData.columns} options={this.props.options} editable={false} keyName={gridData.key} title={Application.Localize(this.props.gridName)}/>
                {
                    /*  Show the parameters in a MODAL ONLY if the filter is to be shown AND this grid has parameters
                        We set classesForBody as 'noscroll' -  it can be anything - if this is set to null or empty
                        the Modal becomes scrollable - for params we dont want it scrollable - so we set it to something
                    * */
                    (this.state.showFilter &&  this.props.gridParams !== undefined && this.props.gridParams.length > 0) &&
                    <ReviewModal onModalClosed={this.handleModalClosed} title={message} classesForBody="noscroll">
                        <this.ParameterForm params={this.props.gridParams} onSubmit={this.paramsSelected} />
                    </ReviewModal>
                }
            </span>
        );
    }
    componentDidMount(){
        /* Go fetch the parameters for this grid, if any.
           While requesting the parameters for the grid, send in any preset parameters
         */
        this.props.onGridParamRequest(this.props.gridName, this.props.params || {});
        if(this.props.parameterFirst){
            return;
        }
        /* if caching has been requested and we have sometime in the past retrieved data for this grid
           then just return - the assumption is that if the data in this grid changes then the changes are
           pushed to the client and the existing data is updated. i.e somewhere in the callers code
           there is a subscribeToPushTopic. If this has not been done, the the data shown will never change
         */
        if(this.props.options && this.props.options.cache && this.props.gridData)
            return;
        this.props.onGridDataRequest(this.props.gridName, this.props.params || {});
    }
}

export default GridReport;




