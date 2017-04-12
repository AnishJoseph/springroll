import React from 'react';
import Application from 'App';
import ReportParams from 'ReportParameterForm';
import ReviewModal from 'ReviewModal.jsx';
import Grid from 'grid/BootstrapGrid.jsx';
import { reduxForm } from 'redux-form';


class GridReport extends React.Component {
    constructor(props){
        super(props);
        this.handleModalClosed = this.handleModalClosed.bind(this);
        this.onFilterClick = this.onFilterClick.bind(this);
        this.paramsSelected = this.paramsSelected.bind(this);
        this.state = {showFilter : this.props.parameterFirst};
        this.ParameterForm = reduxForm({
            form: 'grid-params:' + this.props.gridName,
            destroyOnUnmount : false,
            initialValues : this.props.params
        })(ReportParams);
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
        let message = Application.Localize("Report Params");
        return (
            <span>
                <Grid gridData={this.props.gridData} options={this.props.options || {}} title={Application.Localize(this.props.gridName)} gridParams={this.props.gridParams} onFilterClick={this.onFilterClick}/>
                {
                    /* Show the parameters in a MODAL ONLY if the filter is to be shown AND this grid has parameters */
                    (this.state.showFilter &&  this.props.gridParams !== undefined && this.props.gridParams.length > 0) &&
                    <ReviewModal onModalClosed={this.handleModalClosed} title={message}>
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




