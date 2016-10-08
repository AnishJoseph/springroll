define(['Application', 'jquery','jquery.cometd'], function (Application) {
    var CometD = $.cometd;
    var cometURL = location.protocol + "//" + location.host + "/cometd";
    function _connectionEstablished() {
        console.log('CometD Connection Established');
    }

    function _connectionBroken() {
        console.log('CometD Connection Broken');
    }

    function _connectionClosed() {
        console.log('CometD Connection Closed');
    }

    // Function that manages the connection status with the Bayeux server
    var _connected = false;

    function _metaConnect(message) {
        if (CometD.isDisconnected()) {
            _connected = false;
            _connectionClosed();
            return;
        }

        var wasConnected = _connected;
        _connected = message.successful === true;
        if (!wasConnected && _connected) {
            _connectionEstablished();
        }
        else if (wasConnected && !_connected) {
            _connectionBroken();
        }
    }

    // Function invoked when first contacting the server and
    // when the server has lost the state of this client
    function _metaHandshake(handshake) {
        console.log("HANDSHAKE SUCCESS!!!!!!!!!")
        if (handshake.successful === true) {
            CometD.batch(function () {
                var listeners = Application.getListeners();
                Object.keys(listeners).forEach(function(key,index) {
                    for(var i = 0; i < listeners[key].length; i++)
                        CometD.subscribe(key, listeners[key][i]);
                });
            });
        }
    }

    // Disconnect when the page unloads
    //$(window).unload(function()
    //{
    //    CometD.disconnect(true);
    //});

    CometD.configure({
        url: cometURL,
        logLevel: 'error'
    });

    CometD.addListener('/meta/handshake', _metaHandshake);
    CometD.addListener('/meta/connect', _metaConnect);
    CometD.unregisterTransport('websocket');



    var CometDGlue = {

        init : function(){
            CometD.handshake();
        }

    };
    Application.CometD = CometDGlue;
});