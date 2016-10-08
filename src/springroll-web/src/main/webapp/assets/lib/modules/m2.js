define(['Application'], function (Application) {
    console.log("TOP OF M2.JS  --------")
    var M2 =  {
        f1: function(){
            console.log("INIT m2");
        }
    }
    Application.M2 = M2;
    Application.subscribe('/core/review', function(message){
        console.log("Recevived message on REVIEW CHANNEL - user 2");
    });

    return M2;

});