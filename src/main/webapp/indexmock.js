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
