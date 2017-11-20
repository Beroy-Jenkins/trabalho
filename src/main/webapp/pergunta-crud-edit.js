app.controller("edit", function ($scope, $http, $timeout) {
		
	$s.opcaoUpdate = function(index) {
        for (var i=0;i< $s.formEdicao.o.opcoes.length; i++) {
            if (index != i) {
                $s.formEdicao.o.opcoes[i].correta = 'false';
            }
        }
	}
	$s.btnVoltaAbaConsulta = function(){
		$s.aba = 'pesquisa';
	}
	
	$s.btnSalvar = function(){
		
		var 
			o = $s.formEdicao.o
			, novoRegistro = ((o.id == undefined ) ||  (o.id == -1)) 
		;
		
//		if (data.erro) {
//			$s.erro = data.erro;
//		} else {
//			//console.log(data.o);
//			$s.formEdicao.o = data.o;
//			if (novoRegistro) {
//				$s.pesquisa.items.push(data.o);
//			}
//		}
		$s.erro = null;
		post(url_salvar, {o: o}
			, function(data){
				//console.log(data.o);
				$s.formEdicao.o = data.o;
				if (novoRegistro) {
					$s.pesquisa.items.push(data.o);
				}
			}
			, function(data){
					$s.erro = data.erro;
			}
	
		);
	}
	

	$s.opcoesRemover = function(index){
		//$s.formEdicao.o.opcoes.splice(index, 1);]
		if ($s.formEdicao.o.opcoes[index].status == 'deletado' ) {
			$s.formEdicao.o.opcoes[index].status = '';
		} else {
			$s.formEdicao.o.opcoes[index].status = 'deletado';
		}
	}
	$s.opcoesAdicionar = function(){
		$s.formEdicao.o.opcoes.push({id: null, text:'', correta: false});
	}	
	

});
