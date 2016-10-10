define(['Application'], function (Application) {
    console.log("TOP OF M1.JS  --------")
    var M1 =  {
        f1: function(){
            console.log("INIT m1");
        }
    }
    Application.M1 = M1;
    Application.subscribe('/core/review', function(message){
        console.log("Recevived message on REVIEW CHANNEL - user 1");
    });

    Application.addMenuItem({
        name: 'Item1',
        cb : function(){
            console.log("Item 1 CLICKED");
        }

    });
    Application.addMenuItem({
        name: 'Item2',
        cb : function(){
            console.log("Item 2 CLICKED");
        }

    });
    return M1;

});