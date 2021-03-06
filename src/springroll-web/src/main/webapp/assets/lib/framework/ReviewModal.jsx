import React from 'react'
import Application from 'App.js';
import Modal from 'react-bootstrap/lib/Modal';

class ReviewModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {showModal: true, comment : ''};
        this.close = this.close.bind(this);
        this.handleCommentsChange = this.handleCommentsChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }
    handleCommentsChange(e){
        this.setState({comment : e.target.value});
    }
    handleSubmit(action){
        this.props.onSubmit(action, this.state.comment);
        event.preventDefault();
    }

    close() {
        this.setState({ showModal: false });
        this.props.onModalClosed();
    }
    render() {
        let footerNeeded = this.props.onSubmit !== undefined || this.props.onDismissAlert !== undefined;
        let classesForBody = this.props.classesForBody || 'scrollable';
        return (
            <Modal show={this.state.showModal} onHide={this.close} bsSize="large">
                <Modal.Header closeButton>
                    <Modal.Title>{this.props.title}</Modal.Title>
                </Modal.Header>
                <Modal.Body className={classesForBody}>
                    {this.props.children}
                </Modal.Body>
                {
                    footerNeeded && <Modal.Footer>
                        { this.props.onSubmit != undefined &&
                        <input onChange={this.handleCommentsChange} type="text" className="form-control"/> }
                        { this.props.onSubmit != undefined && this.state.comment.length > 0 &&
                        <span style={{fontSize:14 +'px'}} onClick={() => this.handleSubmit(true)} data-toggle="tooltip"
                              title={Application.Localize('ui.accept')}
                              className="alertActionsPanelItem glyphicon glyphicon-ok"></span> }
                        { this.props.onSubmit != undefined && this.state.comment.length > 0 &&
                        <span style={{fontSize:14 +'px'}} onClick={() => this.handleSubmit(false)} data-toggle="tooltip"
                              title={Application.Localize('ui.reject')}
                              className="alertActionsPanelItem glyphicon glyphicon-remove"></span> }
                        { this.props.onDismissAlert != undefined &&
                        <span style={{fontSize:14 +'px'}} onClick={this.props.onDismissAlert} data-toggle="tooltip"
                              title={Application.Localize('ui.dismiss')}
                              className="alertActionsPanelItem glyphicon glyphicon-trash"></span>}
                    </Modal.Footer>
                }
            </Modal>
        );
    }
}
export default ReviewModal;
