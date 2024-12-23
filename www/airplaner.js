var exec = require('cordova/exec');

var airplaner = {


    airplane: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'airplaner', 'toggleAirplaneMode');
    }



};

module.exports = airplaner;