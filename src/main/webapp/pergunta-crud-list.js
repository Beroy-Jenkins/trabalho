
app.controller("list", function ($scope, $http, $timeout) {
		
	$s.btnPesquisarClick = function(){
		$s.pesquisaExecutar();
	}

	$s.btnPesquisaLimparClick = function(){
		//limpa o form de pesquisa
		$s.pes = {text:""};

		//limpa a lista de consulta, nesse momento a table fica vazia e é oculta pela diretiva ng-show 
		$s.pesquisa.items = [];	
	}

	$s.btnNovoRegistro = function(){
		//limpa o form de edicao
		$s.formEdicao.o = {opcoes : [{}]};

		//set a aba do cadastro
		$s.aba = 'cadastro';
	}
	
	$s.btnRowEditClick = function(id) {
		//recupera o registro pelo id passado da grid
		//recupra uma pergunta e coloca no objeto de edição e isso faz com que o form de ediçao seja exibido
		post(url_getById, {id:id}, function(data){
			
			//coloco o objeto model
			$s.formEdicao.o = data.o;	
			//set a aba do cadastro
			$s.aba = 'cadastro';
		});		
	}

	$s.btnRemoveClick = function(id){
		$s.remover(id, "Confirma a Exclusão", function(data) {
			// se a remoção ocorrer sem problemas, limpa o item da lista que mostra na tela
			var index = -1;	
			for (var i = 0; i < $s.pesquisa.items.length; i++) {
				if ( $s.pesquisa.items[i].id === id ) {
					index = i;
					break;				
				}
			}
			if( index !== -1 ) {
				$s.pesquisa.items.splice( index, 1 );	
			}
			//força a limpeza do form de edicao;
			$s.formEdicao.o = {opcoes : [{}]};
		});
	}

	$s.remover = function(id, message, callBack){
		$.confirm({
			title: 'Confirma!',
			content: message,
			buttons: {
				 confirm: {
						text: 'Sim',
						btnClass: 'btn-blue',
						action: function () {
							
							post(url_remover, {id:id}, function(data){
								if (data.erro) {
									$s.erro = data.erro;
								} else {
									callBack(data);
								}

							});
							$s.$apply();
							//return false; // prevent dialog from closing.
						}					 
				},
				cancel: {
						text: 'Não',
						btnClass: 'btn-blue',
						action: function () {
						}					 
				}
			}
		});		

	}
	
	//recupra uma pergunta e coloca no objeto de edição e isso faz com que o form de ediçao seja exibido
/*
	$s.getById = function(id){ 
		post(url_getById, {id:id}, function(data){
			$s.formEdicao.o = data.o;	
		});
	}
*/
	$s.pesquisaExecutar = function(){
		//limpa a lista de consulta, nesse momento a table fica vazia e é oculta pela diretiva ng-show 
		$s.pesquisa.items = [];	
		// caso o campo de pesquisa esteja preenchido, usar texto para pesquisa, caso contrário usar branco
		let text = $s.pes  ? $s.pes.text : '';
		
		post(url_pesquisar, {text:text}, function(data){
			// a lista de consulta recebe o retorno.
			$s.pesquisa.items = data.list;	
		});
	}

});

