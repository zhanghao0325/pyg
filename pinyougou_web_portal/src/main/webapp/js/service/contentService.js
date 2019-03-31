app.service("contentService",function($http){
	this.findByCategoryId = function(categoryId){
			return $http.get("content/findByCategoryId.do?categoryId="+categoryId);
	}
	this.add=function (entity) {
		return $http.post("content/add.do",entity);

    }
    this.update=function (entity) {
		return $http.post("content/update.do",entity);

    }
    this.dele=function (ids) {
		return $http.get("content/dele.do?ids="+ids);

    }
    this.findOne=function (id) {
		return $http.get("content/findOne.do?id="+id);
    }
});