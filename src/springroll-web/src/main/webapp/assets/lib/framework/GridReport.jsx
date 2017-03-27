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
        this.paramsChosen = this.paramsChosen.bind(this);
        this.state = {showFilter : false}
    }
    handleModalClosed(){
        this.setState({showFilter : false});
    }

    onFilterClick() {
        this.setState({showFilter : !this.state.showFilter});
    }
    paramsChosen(params){
        console.log(JSON.stringify(params));
        this.setState({showFilter : false});
        this.props.onGridDataRequest(this.props.gridName, params);
    }
    render() {
        let message = "Report Params";
        return (
            <span>
                <div className='grid-title-panel'>
                    <span className='pull-left' style={{width : "100%"}}>
                        <h4 className=' text-info'>{Application.Localize(this.props.gridName)}</h4>
                    </span>
                    <span onClick={this.onFilterClick} title={Application.Localize('filter')} style={{ paddingLeft: 100 +'px'}} className='pull-left glyphicon glyphicon-filter'></span>
                </div>
                <div>
                    {
                        this.props.gridData && <Grid gridData={this.props.gridData} formatters={this.props.formatters}/>
                    }
                </div>
                {
                    (this.state.showFilter) &&
                        <ReviewModal onModalClosed={this.handleModalClosed} title={message}>
                            <ReportParams params={this.props.gridParams} paramsChosen={this.paramsChosen}/>
                        </ReviewModal>
                }
            </span>
        );
    }
    componentDidMount(){
        if(this.props.parameterFirst){
            this.props.onGridParamRequest(this.props.gridName, {});
        }
        this.props.onGridDataRequest(this.props.gridName, this.props.params || {});

    }
}

export default GridReport;




