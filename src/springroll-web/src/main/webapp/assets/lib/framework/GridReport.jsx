import React from 'react';
import Application from 'App';
import ReportParams from 'ReportParams';
import ReviewModal from 'ReviewModal.jsx';
import Grid from 'grid/Grid.jsx';

class GridReport extends React.Component {
    constructor(props){
        super(props);
        this.handleModalClosed = this.handleModalClosed.bind(this);
        this.onFilterClick = this.onFilterClick.bind(this);
        this.paramsSelected = this.paramsSelected.bind(this);
        this.state = {showFilter : this.props.parameterFirst, paramValues : this.props.params || {}};
    }
    handleModalClosed(){
        this.setState({showFilter : false});
    }

    onFilterClick() {
        this.setState({showFilter : !this.state.showFilter});
    }
    paramsSelected(paramValues){
        console.log(JSON.stringify(paramValues));
        this.setState({showFilter : false, paramValues: paramValues});
        this.props.onGridDataRequest(this.props.gridName, paramValues);
    }
    render() {
        let message = "Report Params";
        let formatters = this.props.formatters || {};
        return (
            <span>
                <div className='grid-title-panel'>
                    <span className='pull-left' style={{width : "50%"}}>
                        <h4 className=' text-info'>{Application.Localize(this.props.gridName)}</h4>
                    </span>
                    {
                        (this.props.gridParams !== undefined && this.props.gridParams.length > 0) &&
                        <span onClick={this.onFilterClick} title={Application.Localize('ui.report.parameters')}
                              style={{ paddingLeft: 100 +'px'}} className='springroll-icon pull-left glyphicon glyphicon-tasks'></span>
                    }
                </div>
                <Grid gridData={this.props.gridData} formatters={formatters}/>
                {
                    this.state.showFilter &&
                    <ReviewModal onModalClosed={this.handleModalClosed} title={message}>
                        <ReportParams params={this.props.gridParams} onParamsSelected={this.paramsSelected} paramValues={this.state.paramValues}/>
                    </ReviewModal>
                }
            </span>
        );
    }
    componentDidMount(){
        /* Go fetch the paramaters for this grid, if any.
           While requesting the parameters for the grid, send in any preset parameters
         */
        this.props.onGridParamRequest(this.props.gridName, this.props.params || {});
        if(this.props.parameterFirst){
            return;
        }
        this.props.onGridDataRequest(this.props.gridName, this.props.params || {});
    }
}

export default GridReport;




