define(['Application'], function (Application) {
    var M2 =  {
    }

    Application.M2 = M2;

    Application.subscribe('/core/review', function(message){
        console.log("Recevived message on REVIEW CHANNEL - user 2");
    });

    return M2;

});