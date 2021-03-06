import React from 'react'
import Application from 'App.js';
import ReviewModal from 'ReviewModal.jsx';
import ReviewMoreInfo from 'ReviewMoreInfo.jsx';
import GenericAlertItem from 'GenericAlertItem.jsx';

class ReviewFYIAlertItem extends React.Component {
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
                        <ReviewMoreInfo alert={this.props.alert}/>
                    </ReviewModal>
                }
            </span>
        );
    }
}
export default ReviewFYIAlertItem;
