var exec = require('cordova/exec');

var airplaner = {


    airplane: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'airplaner', 'toggleAirplaneMode');
    },
    
    clearcache: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'airplaner', 'clearcache');
    }



};

module.exports = airplaner;