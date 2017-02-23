import React from 'react'
import Application from 'App.js';
import Modal from 'react-bootstrap/lib/Modal';
import Button from 'react-bootstrap/lib/Button';

class SrModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {showModal: true};
        this.close = this.close.bind(this);
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
            </Modal>
        );
    }
}
export default SrModal;
