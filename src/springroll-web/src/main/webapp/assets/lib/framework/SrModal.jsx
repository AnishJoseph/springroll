import React from 'react'
import Application from 'App.js';
import Modal from 'react-bootstrap/lib/Modal';
import Button from 'react-bootstrap/lib/Button';

//var ReactDOM = require('react-dom');

class SrModal extends React.Component {
    constructor(props){
        super(props);
        this.state = {showModal: true};
        this.close = this.close.bind(this);
    }
    close() {
        this.setState({ showModal: false });
        this.props.hideModal();
    }
    render() {
        return (
            <Modal show={this.state.showModal} onHide={this.close}>
                <Modal.Header closeButton>
                    <Modal.Title>Modal heading</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {this.props.children}
                </Modal.Body>
            </Modal>
        );
    }
}
export default SrModal;
