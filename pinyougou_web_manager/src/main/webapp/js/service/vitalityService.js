app.service("vitalityService",function($http){

    this.findAll = function(){
        return $http.get("../vitality/findAll.do");
    }

});