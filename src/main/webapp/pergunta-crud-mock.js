
var httpMock = function(){
var
	perguntas = 
[
  {
    "opcoes": [
      {
        "correta": 'true',
        "text": "1 nostrud magna deserunt velit ea",
        "id": 0
      },
      {
        "correta": 'false',
        "text": "2 laborum nulla veniam qui aliquip",
        "id": 1
      },
      {
        "correta": 'false',
        "text": "3 nulla labore ullamco laboris esse",
        "id": 2
      }
    ],
    "text": "1  est voluptate laborum Lorem voluptate",
    "id": 0
  },
  {
    "opcoes": [
      {
        "correta": 'false',
        "text": "velit cillum enim voluptate exercitation",
        "id": 0
      },
      {
        "correta": 'true',
        "text": "nulla quis duis aliqua eu",
        "id": 1
      },
      {
        "correta": 'false',
        "text": "sit mollit pariatur consectetur in",
        "id": 2
      }
    ],
    "text": "ex duis elit ullamco ad",
    "id": 1
  },
  {
    "opcoes": [
      {
        "correta": 'false',
        "text": "eiusmod do id labore aliquip",
        "id": 0
      },
      {
        "correta": 'false',
        "text": "pariatur aute aute ipsum consequat",
        "id": 1
      },
      {
        "correta": 'true',
        "text": "ullamco enim incididunt aute irure",
        "id": 2
      }
    ],
    "text": "aute et veniam ad occaecat",
    "id": 2
  },
  {
    "opcoes": [
      {
        "correta": 'true',
        "text": "nisi quis ex exercitation do",
        "id": 0
      },
      {
        "correta": 'false',
        "text": "proident proident laboris cupidatat dolore",
        "id": 1
      },
      {
        "correta": 'false',
        "text": "Lorem officia et nostrud tempor",
        "id": 2
      }
    ],
    "text": "labore amet anim exercitation laborum",
    "id": 3
  },
  {
    "opcoes": [
      {
        "correta": 'false',
        "text": "non cupidatat anim qui mollit",
        "id": 0
      },
      {
        "correta": 'true',
        "text": "aliquip culpa consectetur officia mollit",
        "id": 1
      },
      {
        "correta": 'false',
        "text": "officia sit nisi occaecat anim",
        "id": 2
      }
    ],
    "text": "do tempor ex eiusmod ad",
    "id": 4
  }
];
	
	
function clone(obj) {
    var copy;

    // Handle the 3 simple types, and null or undefined
    if (null == obj || "object" != typeof obj) return obj;

    // Handle Date
    if (obj instanceof Date) {
        copy = new Date();
        copy.setTime(obj.getTime());
        return copy;
    }

    // Handle Array
    if (obj instanceof Array) {
        copy = [];
        for (var i = 0, len = obj.length; i < len; i++) {
            copy[i] = clone(obj[i]);
        }
        return copy;
    }

    // Handle Object
    if (obj instanceof Object) {
        copy = {};
        for (var attr in obj) {
            if (obj.hasOwnProperty(attr)) copy[attr] = clone(obj[attr]);
        }
        return copy;
    }

    throw new Error("Unable to copy obj! Its type isn't supported.");
}	
	
	var data;
	
	var result = {
		then: function(ok, fail) {
			if (ok) ok(data);
		}
	}

	var post = function(url, params) {
		
		data = {};
						
		if (url == url_remover) {
			var index = -1;	

			for (var i = 0; i < perguntas.length; i++) {
				if ( perguntas[i].id === params.id ) {
					index = i;
					break;				
				}
			}
			if( index === -1 ) {
				data.erro = 'Registro não encontrado';
				return result;
			} 
			
			perguntas.splice( index, 1 );	
			return result;
		}	

		if (url == url_getById) {
			var index = -1;	

			for (var i = 0; i < perguntas.length; i++) {
				if ( perguntas[i].id === params.id ) {
					index = i;
					data.o = clone(perguntas[i]);
					break;				
				}
			}
			if( index === -1 ) {
				data.erro = 'Registro não encontrado';
				return result;
			} 
			
			return result;
		}	
		
		if (url == url_salvar) {
			var
				o = clone(params.o)
			;
		
		
		
			//remove as opções que foram deletadas
			for (var i= o.opcoes.length -1 ; i > 0; i--) {
				if (o.opcoes[i].status === 'deletado') {
					o.opcoes.splice(i, 1);
				}
			}
			
			if ( (o.id != undefined ) &&  (o.id > -1)) {
				
				for (var i = 0; i < perguntas.length; i++) {
					if ( perguntas[i].id ==  o.id ) {
						perguntas[i] = o;
						break;
					}
				}	
			} else {
				var maxId = -1;	

				for (var i = 0; i < perguntas.length; i++) {
					if ( perguntas[i].id > maxId ) {
						maxId = perguntas[i].id;				
					}
				}	
				o.id = 	maxId + 1;
				perguntas.push(o);
			}
			
			data.o = o;
			return result;
		}
		
		if (url == url_pesquisar) {
			data = {list : []};
			/*
			function addPergunta(pergunta,opcao1,opcao2,opcao3,opcao4,opcaoCorreta){
				var o = {};
				o.pergunta = pergunta;
				o.opcao1 = opcao1;
				o.opcao2 = opcao2;
				o.opcao3 = opcao3;
				o.opcao4 = opcao4;
				o.opcaoCorreta = opcaoCorreta;
				data.list.push(o);
			}

			addPergunta('Quanto é 3*3','4','93','12','9',3);
			addPergunta('Azul+Amarelo é','Verde','Vermelho','Rosa','Laranja',0);
			addPergunta('Quem descobriu o Brasil','Maria','João','Pedro','José',2);
			addPergunta('Quantos livros tem a Bíblia?','100','93','45','66',3);
			addPergunta('Onde fica Brasilia?','Canadá','Brasil','Bolívia','Chile',1);
			*/
			if ((params.text == undefined) || (params.text == '')) {
				data.list = perguntas.slice();
			} else {
				for (var i = 0; i < perguntas.length; i++) {
					var o = perguntas[i];
					if (o.text.indexOf(params.text) > -1 ) {
						data.list.push(o);
					}

				}
			}
			return result;
			
		}
				
		$s.erro = 'url nao implementada: ' + url;
		return result;
		
	};
		
	return {post:post, get:post};
	
}();
