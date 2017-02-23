import React from 'react'
import Application from 'App.js';
import Modal from 'react-bootstrap/lib/Modal';
import Button from 'react-bootstrap/lib/Button';

class SrModal extends React.Component {
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
        return (
            <Modal show={this.state.showModal} onHide={this.close}>
                <Modal.Header closeButton>
                    <Modal.Title>{this.props.title}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {this.props.children}
                </Modal.Body>
                <Modal.Footer>
                    { this.props.onSubmit != undefined && <input onChange={this.handleCommentsChange} type="text"  className="form-control"/> }
                    { this.props.onSubmit != undefined && this.state.comment.length > 0 && <Button onClick={() => this.handleSubmit(true)}>{Application.Localize('ui.accept')}</Button> }
                    { this.props.onSubmit != undefined && this.state.comment.length > 0 && <Button onClick={() => this.handleSubmit(false)}>{Application.Localize('ui.reject')}</Button> }
                </Modal.Footer>
            </Modal>
        );
    }
}
export default SrModal;
