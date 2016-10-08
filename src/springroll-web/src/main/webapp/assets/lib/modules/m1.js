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

    return M1;

});