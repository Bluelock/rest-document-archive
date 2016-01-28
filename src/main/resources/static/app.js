var urlLocation = 'bluelock';
var app = angular.module(urlLocation, []);
var baseUrl = 'web-chart-01.z01.devmgmt.blk';
var port = '8082';
var url = "http://" + baseUrl + ":" + port + "/" + urlLocation;

app.directive('fileModel', [ '$parse', function($parse) {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			var model = $parse(attrs.fileModel);
			var modelSetter = model.assign;

			element.bind('change', function() {
				scope.$apply(function() {
					modelSetter(scope, element[0].files[0]);
				});
			});
		}
	};
} ]);

app.service('ArchiveService', [ '$http', '$rootScope', function($http, $rootScope) {

}]);

app.service('fileUpload', ['$http','ArchiveService', function($http, ArchiveService) {
	this.uploadFileToUrl = function(uploadUrl, file) {
		var fd = new FormData();
		fd.append('file', file);
		$http.post(uploadUrl, fd, {
			transformRequest : angular.identity,
			headers : {
				'Content-Type' : undefined
			}
		}).success(function(data) {
			alert("File Uploaded! " + data);
		}).error(function() {
			alert("Unable to upload file!");
		});
	}
} ]);

app.controller('UploadCtrl', [ '$scope', 'fileUpload',
		function($scope, fileUpload) {
			$scope.uploadFile = function() {
				var clientId = $scope.clientId;
				var file = $scope.myFile;
				console.log('file is ' + JSON.stringify(file));
				var uploadUrl = url +"/upload/"+clientId;
				fileUpload.uploadFileToUrl(uploadUrl, file);
			};
		} ]);

app.controller('RetrieveCtrl', function($scope, $http) {
	$scope.search = function(clientId, fileName) {
		$http.get(url + "/document/"+clientId + "/"+fileName).success(function(response) {
			alert("FILE BYTE ARRAY:   " +JSON.stringify(response));
		}).error(function() {
			alert("Unable to download file!");
		});
	};
});
