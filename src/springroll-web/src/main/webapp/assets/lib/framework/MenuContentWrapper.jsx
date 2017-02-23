import React from 'react';

class MenuContentWrapper extends React.Component {
    render() {
        // make sure this var starts with a capital letter
        var MenuContent = this.props.menuContent;
        return (
            <div id='main-body' className='main-body'>
                <div id='content' className='main-body-content'>
                    <MenuContent/>
                </div>
            </div>
        );
    }
}

export default MenuContentWrapper;