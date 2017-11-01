var $s;
const url_add_user = 'rest/usuario/cadastrar';
const url_checar_user = 'rest/usuario/checaruser';
const url_checar_senha = 'rest/usuario/checarsenha';
const url_carregar_perguntas = 'rest/pergunta';

var httpMock = function() {

	var usuarios = [];

	function addUser(login, senha) {
		var o = {
			login : login,
			senha : senha
		};
		o.id = usuarios.length + 1;
		usuarios.push(o);
		return o;
	}
	function getUser(login) {
		return usuarios.filter(function(o) {
			return o.login == login;
		})[0];
	}

	addUser('adm', '123');

	var data;

	var result = {
		then : function(ok, fail) {
			if (ok)
				ok(data);
		}
	}

	var post = function(url, params) {

		data = {};

		if (url == url_add_user) {
			var o = addUser(params.login, params.senha);
			data.usuario = o;
			return result;
		}

		if (url == url_checar_user) {
			if (!getUser(params.login)) {
				data.erro = 'Usuario não encontrado';
			}
			return result;
		}

		if (url == url_checar_senha) {
			var o = getUser(params.login);
			if (o.senha == params.senha) {
				data.usuario = o;
			}
			return result;
		}

		if (url == url_carregar_perguntas) {
			data = {};
			data.list = [];

			function addPergunta(pergunta, opcao1, opcao2, opcao3, opcao4,
					opcaoCorreta) {
				var o = {};
				o.pergunta = pergunta;
				o.opcao1 = opcao1;
				o.opcao2 = opcao2;
				o.opcao3 = opcao3;
				o.opcao4 = opcao4;
				o.opcaoCorreta = opcaoCorreta;
				data.list.push(o);
			}

			addPergunta('Quanto é 3*3', '4', '93', '12', '9', 3);
			addPergunta('Azul+Amarelo é', 'Verde', 'Vermelho', 'Rosa',
					'Laranja', 0);
			addPergunta('Quem descobriu o Brasil', 'Maria', 'João', 'Pedro',
					'José', 2);
			addPergunta('Quantos livros tem a Bíblia?', '100', '93', '45',
					'66', 3);
			addPergunta('Onde fica Brasilia?', 'Canadá', 'Brasil', 'Bolívia',
					'Chile', 1);
			return result;

		}

		$s.erro = 'url nao implementada: ' + url;
		return result;

	};

	return {
		post : post,
		get : post
	};

}();

var app = angular.module("app", []);

app.controller("ctrl", function($scope, $http, $timeout) {

	$s = $scope;
	// cliente se comunica com o server
	 $s.http = $http;
	
	// cliente sem server, para testes
	//$s.http = httpMock;

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
			var data = response.data;
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

	$s.iniciarProva = function() {

		$s.usuario.perguntas = [];

		get(url_carregar_perguntas, {
			usuario : $s.usuario.id
		}, function(data) {
			for (var o in data.list) {
				o = data.list[o];
				$s.usuario.perguntas.push(o);	
			}
			$s.usuario.pergunta = $s.usuario.perguntas[0];
			$s.usuario.acertos = 0;
			$s.usuario.erros = 0;
			$s.form = 'logado';
		});

	}

	$s.form = 'login';
	$('body').show();

	$s.$watch('form', function() {
		$timeout(function() {
			$('.focus').focus();
			$s.erro = undefined;
		});
	});

	$s.$watch('erro', function() {
		if ($s.erro) {
			$timeout(function() {
				$s.erro = undefined;
			}, 5000);
		}
	});

	function usuarioExiste(login, seEncontrar, seNaoEncontrar) {
		post(url_checar_user, {
			login : login
		}, seEncontrar, seNaoEncontrar);
	}

	$s.verificarLogin = function() {
		if (!$s.login)
			return;
		usuarioExiste(
			$s.login
			, function(data) {
				if (data && data.erro) {
					$s.erro = data.erro;
				} else {
					$s.form = 'senha';
				}				
				
			}
			, function(data) {
				if (data && data.erro) {
					$s.erro = data.erro;
				} else {
					$s.erro = 'Usuário não encontrado!';
				}
			}
		);
	};

	$s.verificarSenha = function() {
		if (!$s.senha)
			return;

		post(url_checar_senha, {
			login : $s.login,
			senha : $s.senha
		}, function(data) {
			if (data.usuario) {
				$s.usuario = data.usuario;
				if ($s.usuario.acertos) {
					$s.form = 'resultado';
				} else {
					if (!$s.usuario.provaIniciada) {
						$s.iniciarProva();
					} else {
						$s.form = 'logado';
					}
				}
			} else {
				$s.erro = 'Senha inválida!';
			}
		}, function(data) {
			$s.erro = 'Senha inválida!';
		});
	};

	$s.logout = function() {
		$s.usuario = undefined;
		$s.form = 'login';
		$s.senha = undefined;
	};

	$s.keydown = function(e) {
		if (e.shiftKey || e.ctrlKey || e.altKey) {
			return;
		}
		var code = e.keyCode || event.which;
		if (code === 13) {// enter
			if ($s.form == 'login') {
				e.preventDefault();
				$s.verificarLogin();
				return;
			}
			if ($s.form == 'senha') {
				e.preventDefault();
				$s.verificarSenha();
				return;
			}
			if ($s.form == 'cadastro') {
				e.preventDefault();
				$s.prosseguirCadastro();
				return;
			}
		}
	};

	$s.selecionaOpcao = function(o) {
		for ( var x in $s.usuario.pergunta.opcoes) {
			x = $s.usuario.pergunta.opcoes[x];
			x.selecionada = false;
		}
		o.selecionada = true;
		$s.usuario.pergunta.respondida = true;
	};

	$s.voltarQuestao = function() {
		$s.usuario.pergunta = $s.usuario.perguntas[$s.usuario.pergunta.id - 2];
	};

	$s.proximaQuestao = function() {
		$s.usuario.provaIniciada = true;
		$s.usuario.pergunta = $s.usuario.perguntas[$s.usuario.pergunta.id];
	};
	$s.concluir = function() {
		$s.form = 'resultado';
		for ( var pergunta in $s.usuario.perguntas) {
			pergunta = $s.usuario.perguntas[pergunta];
			for ( var x in pergunta.opcoes) {
				x = pergunta.opcoes[x];
				if (x.correta) {
					if (x.selecionada) {
						$s.usuario.acertos++;
					} else {
						$s.usuario.erros++;
					}
				}
			}
		}
	};

	$s.cadastrarSe = function() {
		$s.form = 'cadastro';
		$s.login = undefined;
		$s.senha = undefined;
		$s.senha2 = undefined;
	};

	$s.prosseguirCadastro = function() {
		if (!$s.login) {
			$s.erro = 'Login inválido!';
			return;
		}
		if (!$s.senha) {
			$s.erro = 'Senha inválida!';
			return;
		}
		if ($s.senha != $s.senha2) {
			$s.erro = 'Senhas não conferem!';
			return;
		}
		usuarioExiste($s.login, function() {
			$s.erro = 'Usuário já cadastrado!';
		}, function() {
			post(url_add_user, {
				login : $s.login,
				senha : $s.senha
			}, function(data) {
				if (data.erro) {
					$s.erro = data.erro;
				} else if (!data.usuario) {
					$s.erro = 'data deveria ter retornado usuario';
				} else {
					$s.usuario = data.usuario;
					$s.iniciarProva();
				}
			});
		});
	};
});