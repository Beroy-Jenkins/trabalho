	function request(func, url, params, onSuccessFunction, onFailFunction) {

		if (params instanceof Function) {
			$s.erro = 'params instanceof Function';
			return;
		}
		if (onSuccessFunction && !(onSuccessFunction instanceof Function)) {
			$s.erro = '!(onSuccessFunction instanceof Function)';
			return;
		}
		if (onFailFunction && !(onFailFunction instanceof Function)) {
			$s.erro = '!(onFailFunction instanceof Function)';
			return;
		}

		var rp = {
			headers : {
				'Content-Type' : 'application/json'
			}
		};
		if (!params)
			params = [];
		if (params.responseType) {
			rp.responseType = 'arraybuffer';
		}
		func(url, params, rp).then(function(response) {
			var data;
			if (response.data){
				data = response.data;
			} else {
				data = response;
			}
			
			if (data.erro) {
				if (onFailFunction) {
					onFailFunction(data);
				}
			} else {
				if (onSuccessFunction) {
					onSuccessFunction(data);
				}
			}
		}, function(data) {
			if (onFailFunction) {
				onFailFunction(data);
			}
		});
	}
	;

	var post = function(url, params, onSuccessFunction, onFailFunction) {
		request($s.http.post, url, params, onSuccessFunction, onFailFunction);
	}
	var get = function(url, params, onSuccessFunction, onFailFunction) {
		request($s.http.get, url, params, onSuccessFunction, onFailFunction);
	}
