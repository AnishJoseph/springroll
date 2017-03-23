import React from 'react'
import Application from 'App.js';
import ReviewModal from 'ReviewModal.jsx';
import GenericAlertItem from 'GenericAlertItem.jsx';
import ExceptionMoreInfo from 'ExceptionMoreInfo.jsx';

class ExceptionAlertItem extends React.Component {
    constructor(props){
        super(props);
        this.handleInfoClicked = this.handleInfoClicked.bind(this);
        this.handleModalClosed = this.handleModalClosed.bind(this);
        this.state = {showMoreInfo : false}
    }
    handleInfoClicked(){
        this.setState({showMoreInfo: true});
    }
    handleModalClosed(){
        this.setState({showMoreInfo: false});
    }
    render() {
        let message = Application.Localize(this.props.alert.messageKey, this.props.alert.args);
        return (
            <span>
                <GenericAlertItem alert={this.props.alert} onMoreInfoClicked={this.handleInfoClicked} onDismissClicked={this.props.onDismissAlert}/>
                {
                    this.state.showMoreInfo &&
                    <ReviewModal onDismissAlert={this.props.onDismissAlert} onModalClosed={this.handleModalClosed} title={message}>
                        <ExceptionMoreInfo alert={this.props.alert}/>
                    </ReviewModal>
                }
            </span>
        );
    }
}
export default ExceptionAlertItem;
